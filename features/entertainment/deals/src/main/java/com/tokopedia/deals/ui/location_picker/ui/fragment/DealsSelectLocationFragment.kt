package com.tokopedia.deals.ui.location_picker.ui.fragment

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.CurrentLocationCallback
import com.tokopedia.deals.databinding.FragmentDealsSelectLocationBinding
import com.tokopedia.deals.databinding.LayoutDealsSearchLocationBottomsheetBinding
import com.tokopedia.deals.di.DealsComponentFactory
import com.tokopedia.deals.ui.location_picker.domain.viewmodel.DealsLocationViewModel
import com.tokopedia.deals.ui.location_picker.listener.DealsLocationListener
import com.tokopedia.deals.ui.location_picker.mapper.DealsLocationMapper
import com.tokopedia.deals.ui.location_picker.model.response.Location
import com.tokopedia.deals.ui.location_picker.ui.typefactory.DealsSelectLocationTypeFactory
import com.tokopedia.deals.ui.location_picker.ui.typefactory.DealsSelectLocationTypeFactoryImpl
import com.tokopedia.deals.utils.DealsLocationUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DealsSelectLocationFragment :
    BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
    DealsLocationListener,
    CoroutineScope {

    private var callback: CurrentLocationCallback? = null

    private var binding by autoCleared<FragmentDealsSelectLocationBinding>()
    private var binding2 by autoCleared<LayoutDealsSearchLocationBottomsheetBinding>()

    @Inject
    lateinit var dealsLocationUtils: DealsLocationUtils

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(DealsLocationViewModel::class.java) }

    private var selectedLocation: String? = ""
    private var location: Location? = null
    private var isLandmarkPage: Boolean = false

    private var permissionCheckerHelper = PermissionCheckerHelper()

    private var userTyped: Boolean = false
    private var searchNotFound: Boolean = false
    private var initialDataList: ArrayList<Visitable<DealsSelectLocationTypeFactory>> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDealsSelectLocationBinding.inflate(inflater, container, false)
        binding2 = LayoutDealsSearchLocationBottomsheetBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        selectedLocation = arguments?.getString(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.SELECTED_LOCATION)
        location = arguments?.getParcelable(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.LOCATION_OBJECT)
        isLandmarkPage = arguments?.getBoolean(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.IS_LANDMARK_PAGE_EXTRA, false) ?: false
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun setCallback(callback: CurrentLocationCallback) {
        this.callback = callback
    }

    private fun initViews() {
        initObservers()
        setListener()
    }

    private fun initObservers() {
        if (isLandmarkPage) {
            observerLandmarkLocation()
            observerLandmarkMoreLocation()
        } else {
            observerPopularCities()
            observerPopularLocation()
            observerLoadMorePopularLocation()
        }
        observerSearchLocation()
        observerSearchMoreLocation()
    }

    private fun observerPopularCities() {
        viewModel.dealsPopularCitiesResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (DealsLocationMapper.listPopularCities.isNotEmpty()) {
                            DealsLocationMapper.listPopularCities.clear()
                        }
                        DealsLocationMapper.listPopularCities.addAll(it.data.locations)
                    }
                    is Fail -> {
                        createToaster(getString(R.string.deals_location_popular_cities_error))
                    }
                }
                location?.coordinates?.let { coordinates -> viewModel.getInitialPopularLocation(coordinates) }
            }
        )
    }

    private fun observerPopularLocation() {
        viewModel.dealsPopularLocationResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (DealsLocationMapper.listPopularLocation.isNotEmpty()) {
                            DealsLocationMapper.listPopularLocation.clear()
                        }
                        DealsLocationMapper.listPopularLocation.addAll(it.data.locations)
                        initialDataList = DealsLocationMapper.displayInitialStateLocationBottomSheet()
                        renderList(initialDataList.toList(), it.data.page.nextPage.isNotEmpty())
                    }
                    is Fail -> {
                        createToaster(getString(R.string.deals_location_popular_location_error))
                    }
                }
            }
        )
    }

    private fun observerLoadMorePopularLocation() {
        viewModel.dealsLoadMorePopularLocationResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        loadMore(DealsLocationMapper.displayLoadMoreLocationBottomSheet(it.data.locations), it.data.page.nextPage.isNotEmpty())
                    }
                    is Fail -> {
                        createToaster(getString(R.string.deals_location_popular_location_more_error))
                    }
                }
            }
        )
    }

    private fun observerLandmarkLocation() {
        viewModel.dealsDataLandmarkLocationResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        initialDataList = DealsLocationMapper.displayInitialStateLandmarkLocationBottomSheet(it.data.locations)
                        renderList(initialDataList.toList(), it.data.page.nextPage.isNotEmpty())
                    }
                    is Fail -> {
                        createToaster(getString(R.string.deals_location_landmark_error))
                    }
                }
            }
        )
    }

    private fun observerLandmarkMoreLocation() {
        viewModel.dealsLoadMoreDataLandmarkLocationResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        loadMore(DealsLocationMapper.displayMoreLandmarkLocationBottomSheet(it.data.locations), it.data.page.nextPage.isNotEmpty())
                    }
                    is Fail -> {
                        createToaster(getString(R.string.deals_location_landmark_more_error))
                    }
                }
            }
        )
    }

    private fun observerSearchLocation() {
        viewModel.dealsSearchedLocationResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        clearAllData()
                        if (it.data.locations.isEmpty()) {
                            searchNotFound = true
                        }
                        renderList(DealsLocationMapper.displayInitialStateLandmarkLocationBottomSheet(it.data.locations).toList(), it.data.page.nextPage.isNotEmpty())
                    }
                    is Fail -> {
                        createToaster(getString(R.string.deals_search_location_error))
                    }
                }
            }
        )
    }

    private fun observerSearchMoreLocation() {
        viewModel.dealsLoadMoreSearchedLocationResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        loadMore(DealsLocationMapper.displayMoreLandmarkLocationBottomSheet(it.data.locations), it.data.page.nextPage.isNotEmpty())
                    }
                    is Fail -> {
                        createToaster(getString(R.string.deals_search_location_more_error))
                    }
                }
            }
        )
    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        adapter.clearAllElements()
        showLoading()
        if (isLandmarkPage) {
            location?.coordinates?.let { viewModel.getInitialDataLandmarkLocation(it) }
        } else {
            viewModel.getInitialPopularCities()
        }
    }

    override fun loadData(page: Int) {
        if (userTyped) {
            var keyword = getSearchKeyword()
            if (searchNotFound) {
                keyword = ""
            }
            viewModel.getLoadMoreSearchedLocation(keyword, page.toString())
        } else {
            if (isLandmarkPage) {
                location?.let { viewModel.getLoadMoreDataLandmarkLocation(it.coordinates, page.toString()) }
            } else {
                location?.let { viewModel.getLoadMoreDataLocation(it.coordinates, page.toString()) }
            }
        }
    }

    private fun loadMore(list: List<Visitable<DealsSelectLocationTypeFactory>>, hasNextPage: Boolean) {
        hideLoading()
        if (isLoadingInitialData) {
            clearAllData()
        } else {
            adapter.clearAllNonDataElement()
        }
        adapter.addMoreData(list)
        updateScrollListenerState(hasNextPage)

        if (isListEmpty) {
            showEmpty()
        } else {
            isLoadingInitialData = false
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(getRecyclerView(view)?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                loadData(page)
            }
        }
    }

    private fun setListener() {
        binding2.sbLocation.requestFocus()

        binding2.sbLocation?.searchBarTextField?.afterTextChangedDelayed {
            onSearchTextChanged(it)
        }

        binding2.sbLocation?.searchBarTextField?.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH || keyEvent.action == KeyEvent.KEYCODE_ENTER) {
                onSearchSubmitted(getSearchKeyword())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding2.detectCurrentLocation?.setOnClickListener {
            getCurrentLocation()
        }
    }

    private fun getSearchKeyword(): String {
        var query = ""
        if (binding2.sbLocation?.searchBarTextField?.text?.isNotEmpty() == true) {
            query = binding2.sbLocation?.searchBarTextField?.text.toString()
        }
        return query
    }

    private fun onSearchSubmitted(text: String) {
        KeyboardHandler.hideSoftKeyboard(activity)
        viewModel.getSearchedLocation(text)
    }

    private fun onSearchTextChanged(text: String) {
        viewModel.getSearchedLocation(text)
    }

    private fun TextView.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            private var searchFor = ""

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText == searchFor) {
                    return
                }

                searchFor = searchText

                launch {
                    delay(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.DELAY)
                    if (searchText != searchFor) {
                        return@launch
                    } else {
                        userTyped = searchText.isEmpty() != true
                        searchNotFound = false
                        if (!userTyped) {
                            clearAllData()
                            renderList(initialDataList.toList(), true)
                        } else {
                            afterTextChanged.invoke(searchText)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        })
    }

    private fun getCurrentLocation() {
        permissionCheckerHelper.checkPermission(
            activity as Activity,
            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION,
            object : PermissionCheckerHelper.PermissionCheckListener {
                override fun onNeverAskAgain(permissionText: String) {}
                override fun onPermissionDenied(permissionText: String) {}
                override fun onPermissionGranted() {
                    callback?.let { callback ->
                        dealsLocationUtils.detectAndSendLocation(activity as Activity, permissionCheckerHelper, callback)
                    }
                }
            }
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(
                context,
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    private fun createToaster(textBody: String) {
        val toast = Toast.makeText(requireContext(), textBody, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 100)
        toast.show()
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return DealsSelectLocationTypeFactoryImpl(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_location_result
    override fun getScreenName(): String = SCREEN_NAME
    override fun onItemClicked(p0: Visitable<*>?) {}

    override fun initInjector() {
        val dealsLocationComponent = activity?.application?.let { DealsComponentFactory.instance.getDealsLocationComponent(it, requireContext()) }
        dealsLocationComponent?.inject(this)
    }

    override fun onCityClicked(itemView: View, location: Location, position: Int) {
        itemView.setOnClickListener {
            callback?.let { callback ->
                dealsLocationUtils.updateLocationAndCallback(location, callback)
            }
        }
    }

    override fun onLocationClicked(itemView: View, location: Location, position: Int) {
        itemView.setOnClickListener {
            callback?.let { callback ->
                dealsLocationUtils.updateLocationAndCallback(location, callback)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    companion object {
        const val SCREEN_NAME = "deals select location"

        fun createInstance(
            selectedLocation: String?,
            location: Location?,
            isLandmarkPage: Boolean
        ): DealsSelectLocationFragment {
            val fragment = DealsSelectLocationFragment()
            val bundle = Bundle()
            bundle.putString(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.SELECTED_LOCATION, selectedLocation)
            bundle.putParcelable(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.LOCATION_OBJECT, location)
            bundle.putBoolean(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.IS_LANDMARK_PAGE_EXTRA, isLandmarkPage)
            fragment.arguments = bundle
            return fragment
        }
    }
}
