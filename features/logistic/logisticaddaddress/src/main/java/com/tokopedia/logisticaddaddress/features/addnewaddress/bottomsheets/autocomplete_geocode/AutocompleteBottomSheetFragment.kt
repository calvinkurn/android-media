package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.util.rxEditText
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.databinding.BottomsheetAutocompleteBinding
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info.LocationInfoBottomSheetFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-13.
 */
class AutocompleteBottomSheetFragment : BottomSheetUnify(), AutocompleteBottomSheetAdapter.ActionListener {
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var currentSearch: String = ""
    private var actionListener: ActionListener? = null
    private val defaultLat: Double by lazy { DEFAULT_LAT }
    private val defaultLong: Double by lazy { DEFAULT_LONG }
    private lateinit var adapter: AutocompleteBottomSheetAdapter
    private val compositeSubs: CompositeSubscription by lazy { CompositeSubscription() }
    private var isFullFlow: Boolean = true
    private var isLogisticLabel: Boolean = true
    private var token: Token? = null
    private var saveAddressDataModel = SaveAddressDataModel()

    private var binding by autoCleared<BottomsheetAutocompleteBinding>()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, factory).get(AutoCompleteBottomSheetViewModel::class.java)
    }

    init {
        isFullpage = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null) {
            initInjector()
        }
        arguments?.let {
            currentLat = it.getDouble(CURRENT_LAT, defaultLat)
            currentLong = it.getDouble(CURRENT_LONG, defaultLong)
            currentSearch = it.getString(CURRENT_SEARCH, "")
            isFullFlow = it.getBoolean(EXTRA_IS_FULL_FLOW, true)
            isLogisticLabel = it.getBoolean(EXTRA_IS_LOGISTIC_LABEL, true)
            token = it.getParcelable(KERO_TOKEN)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


     fun initView() {
        binding = BottomsheetAutocompleteBinding.inflate(LayoutInflater.from(context), null, false)
         setChild(binding.root)
         setTitle(getString(R.string.title_bottomsheet_search_location))
         setCloseClickListener {
             AddNewAddressAnalytics.eventClickBackArrowOnInputAddress(isFullFlow, isLogisticLabel)
             hideKeyboardAndDismiss()
         }

        adapter = AutocompleteBottomSheetAdapter(this)
        hideListLocation()

        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        binding.rvPoiList.layoutManager = linearLayoutManager
        binding.rvPoiList.adapter = adapter
        setViewListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubs.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && data.hasExtra(EXTRA_ADDRESS_NEW)) {
            val newAddress = data.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
            finishActivity(newAddress)
        }
    }

    private fun finishActivity(saveAddressDataModel: SaveAddressDataModel?) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, saveAddressDataModel)
            })
            finish()
        }
    }

    private fun setViewListener() {
        if (currentSearch.isNotEmpty()) {
            binding.layoutSearch.apply {
                searchBarTextField.setText(currentSearch)
                searchBarTextField.selectAll()
                requestFocus()
                searchBarTextField.setSelection(searchBarTextField.text.length)
            }
            loadAutocomplete(currentSearch)
        } else {
            context?.let {
                if (!AddNewAddressUtils.isLocationEnabled(it)) {
                    // When user does not enable location
                    showGpsDisabledNotification()
                    binding.rlCurrentLocation.setOnClickListener {
                        showLocationInfoBottomSheet()
                    }
                }
            }
        }

        binding.layoutSearch.searchBarTextField.run {
            setOnClickListener {
                AddNewAddressAnalytics.eventClickFieldCariLokasi(isFullFlow, isLogisticLabel)
            }

            rxEditText(this).subscribe(object : Subscriber<String>() {
                override fun onNext(t: String) {
                    if (t.isNotEmpty()) {
                        loadAutocomplete(t)
                    }
                }

                override fun onCompleted() {
                    // no op
                }

                override fun onError(e: Throwable?) {
                    // no op
                }
            }).toCompositeSubs(compositeSubs)

            isFocusableInTouchMode = true
            isFocusable = true
            requestFocus()
            AddNewAddressUtils.showKeyboard(context)
        }

        binding.rlCurrentLocation.setOnClickListener {
            actionListener?.useCurrentLocation()
            hideKeyboardAndDismiss()
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setObservers() {
        viewModel.autoCompleteList.observe(this, Observer {
            when (it) {
                is Success -> {
                    if (it.data.errorCode == CIRCUIT_BREAKER_ON_CODE) {
                        goToAddNewAddressNegative()
                    } else {
                        onSuccessGetAutocomplete(it.data)
                    }
                }
                is Fail -> {
                    Timber.d(it.throwable)
                    hideListLocation()
                }
            }
        })
    }

    fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@AutocompleteBottomSheetFragment)
        }
    }

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    private fun hideListLocation() {
        binding.rvPoiList.visibility = View.GONE
        binding.llLoading.visibility = View.GONE
    }

    private fun loadAutocomplete(input: String) {
        showLoadingList()
        viewModel.getAutoCompleteList(input)
    }

    private fun showLoadingList() {
        binding.rvPoiList.visibility = View.GONE
        binding.llLoading.visibility = View.VISIBLE
    }

    private fun onSuccessGetAutocomplete(suggestedPlaces: Place) {
        binding.llLoading.visibility = View.GONE
        binding.rvPoiList.visibility = View.VISIBLE
        binding.layoutGpsDisabled.root.visibility = View.GONE
        if (suggestedPlaces.data.isNotEmpty()) {
            binding.rvPoiList.visibility = View.VISIBLE
            adapter.addAutoComplete(suggestedPlaces.data)
        }
    }

    private fun goToAddNewAddressNegative() {
        val saveModel = getUnnamedRoadModelFormat()

        Intent(context, AddEditAddressActivity::class.java).apply {
            putExtra(EXTRA_IS_MISMATCH, true)
            putExtra(KERO_TOKEN, token)
            putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveModel)
            putExtra(EXTRA_IS_MISMATCH_SOLVED, false)
            putExtra(EXTRA_IS_UNNAMED_ROAD, false)
            putExtra(EXTRA_IS_NULL_ZIPCODE, false)
            putExtra(EXTRA_IS_FULL_FLOW, isFullFlow)
            putExtra(EXTRA_IS_LOGISTIC_LABEL, isLogisticLabel)
            putExtra(EXTRA_IS_CIRCUIT_BREAKER, true)
            startActivityForResult(this, ADD_EDIT_ADDRESS_REQUEST)
        }
    }

    override fun onPoiListClicked(placeId: String) {
        placeId.run {
            actionListener?.onGetPlaceId(placeId)
            hideKeyboardAndDismiss()
        }
        AddNewAddressAnalytics.eventClickAddressSuggestionFromSuggestionList(isFullFlow, isLogisticLabel)
    }

    private fun getUnnamedRoadModelFormat(): SaveAddressDataModel? {
        val fmt = saveAddressDataModel.formattedAddress.replaceAfter("Unnamed Road, ", "")
        return saveAddressDataModel.copy(formattedAddress = fmt, selectedDistrict = fmt)
    }

    private fun showLocationInfoBottomSheet() {
        try {
            LocationInfoBottomSheetFragment
                    .newInstance(isFullFlow, isLogisticLabel)
                    .show(parentFragmentManager, "")
        } catch (e: Exception) {
            Timber.e(e)
        }
        dismiss()
    }

    /**
     * Hiding keyboard is called before the dismiss in each of necessary funnel, calling it onDismiss
     * won't work probably due to some API changes from Google
     */
    private fun hideKeyboardAndDismiss() {
        AddNewAddressUtils.hideKeyboard(binding.layoutSearch.searchBarTextField, context)
        dismiss()
    }

    private fun showGpsDisabledNotification() {
        binding.layoutGpsDisabled.root.visibility = View.VISIBLE
        binding.rvPoiList.visibility = View.GONE
    }

    interface ActionListener {
        fun onGetPlaceId(placeId: String)
        fun useCurrentLocation()
    }

    companion object {
        private const val CURRENT_LAT = "CURRENT_LAT"
        private const val CURRENT_LONG = "CURRENT_LONG"
        private const val CURRENT_SEARCH = "CURRENT_SEARCH"
        private const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"

        private const val DEFAULT_LAT = -6.175794
        private const val DEFAULT_LONG = 106.826457
        private const val ADD_EDIT_ADDRESS_REQUEST = 1212

        fun newInstance(currentLat: Double, currentLong: Double, currentSearch: String, isLogisticLabel: Boolean): AutocompleteBottomSheetFragment {
            return AutocompleteBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putDouble(CURRENT_LAT, currentLat)
                    putDouble(CURRENT_LONG, currentLong)
                    putString(CURRENT_SEARCH, currentSearch)
                    putBoolean(EXTRA_IS_LOGISTIC_LABEL, isLogisticLabel)
                }
            }
        }

        fun newInstance(): AutocompleteBottomSheetFragment {
            return AutocompleteBottomSheetFragment()
        }
    }
}