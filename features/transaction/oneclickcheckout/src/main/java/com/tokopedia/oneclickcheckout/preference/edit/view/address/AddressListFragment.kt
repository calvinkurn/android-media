package com.tokopedia.oneclickcheckout.preference.edit.view.address

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditParent
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.ShippingDurationFragment
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.KERO_TOKEN
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.empty_list_address.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AddressListFragment : BaseDaggerFragment(), SearchInputView.Listener, AddressListItemAdapter.onSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferenceListAnalytics: PreferenceListAnalytics

    private val viewModel: AddressListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AddressListViewModel::class.java]
    }

    private val adapter = AddressListItemAdapter(this)

    private var searchAddress: SearchInputView? = null
    private var addressListRv: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var searchInputView: SearchInputView? = null
    private var buttonSaveAddress: UnifyButton? = null
    private var bottomLayout: FrameLayout? = null

    private var emptyStateLayout: LinearLayout? = null

    private var textSearchError: Typography? = null
    private var globalErrorLayout: GlobalError? = null

    companion object {
        const val REQUEST_FIRST_CREATE = 1
        const val REQUEST_CREATE = 2

        const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"
        const val EXTRA_IS_LOGISTIC_LABEL = "EXTRA_IS_LOGISTIC_LABEL"

        private const val EMPTY_STATE_PICT_URL = "https://ecs7.tokopedia.net/android/others/pilih_alamat_pengiriman3x.png"
        private const val ARG_IS_EDIT = "is_edit"

        fun newInstance(isEdit: Boolean = false): AddressListFragment {
            val addressListFragment = AddressListFragment()
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_EDIT, isEdit)
            addressListFragment.arguments = bundle
            return addressListFragment
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_address, container, false)
        searchAddress = view.findViewById(R.id.search_input_view)
        return view
    }

    private fun initViewModel() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            if (parent.getAddressId() > 0) {
                viewModel.selectedId = parent.getAddressId().toString()
            }
        }

        viewModel.addressList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OccState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    globalErrorLayout?.gone()
                    setEmptyState(it.data.listAddress.isEmpty(), viewModel.savedQuery.isEmpty())
                    adapter.setData(it.data.listAddress)
                }

                is OccState.Failed -> {
                    swipeRefreshLayout?.isRefreshing = false
                    it.getFailure()?.let { failure ->
                        handleError(failure.throwable)
                    }
                }

                is OccState.Loading -> swipeRefreshLayout?.isRefreshing = true
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initView()
        initViewModel()
        initSearch()
        addressListRv?.adapter = adapter
        addressListRv?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initSearchView()
    }

    private fun setEmptyState(isEmpty: Boolean, isFirstLoad: Boolean) {
        if (!isEmpty) {
            buttonSaveAddress?.text = getString(R.string.label_button_input_address)
            buttonSaveAddress?.setOnClickListener {
                if (arguments?.getBoolean(ARG_IS_EDIT) == false) {
                    goToNextStep()
                } else {
                    goBack()
                }
            }
            emptyStateLayout?.gone()
            textSearchError?.gone()
            searchAddress?.visible()
            addressListRv?.visible()
            bottomLayout?.visible()
        } else if (isFirstLoad) {
            buttonSaveAddress?.text = getString(R.string.label_button_input_address_empty)
            buttonSaveAddress?.setOnClickListener {
                goToPickLocation(REQUEST_FIRST_CREATE)
            }
            addressListRv?.gone()
            searchAddress?.gone()
            textSearchError?.gone()
            emptyStateLayout?.visible()
            bottomLayout?.visible()
        } else {
            addressListRv?.gone()
            bottomLayout?.gone()
            emptyStateLayout?.gone()
            searchAddress?.visible()
            textSearchError?.visible()
        }
    }

    private fun initSearch() {
        val searchKey = viewModel.savedQuery
        searchInputView?.searchText = searchKey

        performSearch(searchKey)
    }

    private fun initView() {
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        addressListRv = view?.findViewById(R.id.address_list_rv)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
        searchInputView = view?.findViewById(R.id.search_input_view)
        buttonSaveAddress = view?.findViewById(R.id.btn_save_address)
        bottomLayout = view?.findViewById(R.id.bottom_layout_address)
        emptyStateLayout = view?.findViewById(R.id.empty_state_order_list)
        textSearchError = view?.findViewById(R.id.text_search_error)
        globalErrorLayout = view?.findViewById(R.id.global_error)

        ImageHandler.LoadImage(iv_empty_state, EMPTY_STATE_PICT_URL)
    }

    private fun goBack() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            val selectedId = viewModel.selectedId.toIntOrZero()
            if (selectedId > 0) {
                preferenceListAnalytics.eventClickSimpanAlamatInPilihAlamatPage()
                parent.setAddressId(selectedId)
                setShippingParam()
                parent.goBack()
            }
        }
    }

    private fun initHeader() {
        if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
            val parent = activity
            if (parent is PreferenceEditParent) {
                parent.hideStepper()
                parent.setHeaderTitle(getString(R.string.activity_title_choose_address))
                parent.showAddButton()
                parent.setAddButtonOnClickListener {
                    if (viewModel.token != null) {
                        goToPickLocation(REQUEST_CREATE)
                    } else {
                        view?.let {
                            Toaster.build(it, DEFAULT_LOCAL_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
        } else {
            val parent = activity
            if (parent is PreferenceEditParent) {
                parent.showAddButton()
                parent.setAddButtonOnClickListener {
                    goToPickLocation(REQUEST_CREATE)
                }
                parent.showStepper()
                parent.setStepperValue(25)
                parent.setHeaderTitle(getString(R.string.activity_title_choose_address))
                parent.setHeaderSubtitle(getString(R.string.activity_subtitle_choose_address))
            }
        }
    }

    /*OnActivityResult utk flow dari ana*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        preferenceListAnalytics.eventClickPilihDurasiInANAFlow()
        if (requestCode == REQUEST_FIRST_CREATE) {
            val saveAddressDataModel = data?.getParcelableExtra<SaveAddressDataModel>("EXTRA_ADDRESS_NEW")
            if (saveAddressDataModel != null) {
                viewModel.selectedId = saveAddressDataModel.id.toString()
                viewModel.destinationLongitude = saveAddressDataModel.longitude
                viewModel.destinationLatitude = saveAddressDataModel.latitude
                viewModel.destinationPostalCode = saveAddressDataModel.postalCode
                viewModel.destinationDistrict = saveAddressDataModel.districtId.toString()
                performSearch("")
                goToNextStep()
            }
        } else if (requestCode == REQUEST_CREATE) {
            performSearch(searchAddress?.searchTextView?.text?.toString() ?: "")
        }

    }

    private fun initSearchView() {
        searchAddress?.searchTextView?.setOnClickListener { view ->
            searchAddress?.searchTextView?.isCursorVisible = true
            openSoftKeyboard()
        }
        searchAddress?.setResetListener {
            performSearch("")
        }
        searchAddress?.setListener(this)
        searchAddress?.setSearchHint(getString(com.tokopedia.purchase_platform.common.R.string.label_hint_search_address))
    }

    override fun onSelect(addressId: String) {
        preferenceListAnalytics.eventClickAddressOptionInPilihAlamatPage()
        viewModel.setSelectedAddress(addressId)
    }

    override fun onSearchSubmitted(text: String) {
        performSearch(text)
    }

    override fun onSearchTextChanged(text: String?) {
        openSoftKeyboard()
    }

    private fun performSearch(query: String) {
        viewModel.searchAddress(query)
    }

    private fun openSoftKeyboard() {
        searchAddress?.searchTextView?.let {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun goToPickLocation(requestCode: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
        intent.putExtra(EXTRA_IS_FULL_FLOW, true)
        intent.putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
        intent.putExtra(KERO_TOKEN, viewModel.token)
        startActivityForResult(intent, requestCode)
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            val selectedId = viewModel.selectedId.toIntOrZero()
            if (selectedId > 0) {
                preferenceListAnalytics.eventClickSimpanAlamatInPilihAlamatPage()
                parent.setAddressId(selectedId)
                setShippingParam()
                parent.addFragment(ShippingDurationFragment())
            }
        }
    }

    private fun setShippingParam() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            val shippingParam = parent.getShippingParam()
            if (shippingParam != null) {
                shippingParam.destinationDistrictId = viewModel.destinationDistrict
                shippingParam.addressId = viewModel.selectedId.toInt()
                shippingParam.destinationLatitude = viewModel.destinationLatitude
                shippingParam.destinationLongitude = viewModel.destinationLongitude
                shippingParam.destinationPostalCode = viewModel.destinationPostalCode
                shippingParam.let { parent.setShippingParam(it) }
            }
        }
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(it, throwable?.message
                            ?: DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalErrorLayout?.setType(type)
        globalErrorLayout?.setActionClickListener {
            viewModel.searchAddress("")
        }
        searchAddress?.gone()
        textSearchError?.gone()
        addressListRv?.gone()
        bottomLayout?.gone()
        emptyStateLayout?.gone()
        globalErrorLayout?.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchAddress = null
        addressListRv = null
        swipeRefreshLayout = null
        searchInputView = null
        buttonSaveAddress = null
        bottomLayout = null
        emptyStateLayout = null
        textSearchError = null
        globalErrorLayout = null
    }
}