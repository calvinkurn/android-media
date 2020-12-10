package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.BottomSheets.BottomSheetsState.FULL
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.util.rxEditText
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info.LocationInfoBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-13.
 */
class AutocompleteBottomSheetFragment : BottomSheets(), AutocompleteBottomSheetListener, AutocompleteBottomSheetAdapter.ActionListener {
    private var bottomSheetView: View? = null
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var currentSearch: String = ""
    private var actionListener: ActionListener? = null
    private val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }
    private lateinit var rlCurrentLocation: RelativeLayout
    private lateinit var rvPoiList: RecyclerView
    private lateinit var llPoi: LinearLayout
    private lateinit var llLoading: LinearLayout
    private lateinit var llSubtitle: LinearLayout
    private lateinit var mDisabledGps: View
    private lateinit var etSearch: EditText
    private lateinit var adapter: AutocompleteBottomSheetAdapter
    private lateinit var icCloseBtn: ImageView
    private val compositeSubs: CompositeSubscription by lazy { CompositeSubscription() }
    private var isFullFlow: Boolean = true
    private var isLogisticLabel: Boolean = true
    private var token: Token? = null
    private var saveAddressDataModel: SaveAddressDataModel? = null

    @Inject
    lateinit var presenter: AutocompleteBottomSheetPresenter

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, factory).get(AutoCompleteBottomSheetViewModel::class.java)
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
            saveAddressDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObservers()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_autocomplete
    }

    override fun title(): String {
        return getString(R.string.title_bottomsheet_search_location)
    }

    override fun state(): BottomSheetsState {
        return FULL
    }

    override fun initView(view: View) {
        prepareLayout(view)
        setViewListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubs.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data != null && data.hasExtra(EXTRA_ADDRESS_NEW)) {
            val newAddress = data.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
            finishActivity(newAddress)
        }
    }

    private fun finishActivity(saveAddressDataModel: SaveAddressDataModel) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, saveAddressDataModel)
            })
            finish()
        }
    }

    private fun prepareLayout(view: View) {
        bottomSheetView = view
        rlCurrentLocation = view.findViewById(R.id.rl_current_location)
        rvPoiList = view.findViewById(R.id.rv_poi_list)
        llPoi = view.findViewById(R.id.ll_poi)
        llLoading = view.findViewById(R.id.ll_loading)
        llSubtitle = view.findViewById(R.id.ll_subtitle_poi)
        mDisabledGps = view.findViewById(R.id.layout_gps_disabled)
        etSearch = view.findViewById(R.id.et_search)
        icCloseBtn = view.findViewById(R.id.ic_close)

        adapter = AutocompleteBottomSheetAdapter(this)
        hideListPointOfInterest()

        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        rvPoiList.layoutManager = linearLayoutManager
        rvPoiList.adapter = adapter
    }

    private fun setViewListener() {
        if (currentSearch.isNotEmpty()) {
            etSearch.apply {
                setText(currentSearch)
                selectAll()
                requestFocus()
                setListenerClearBtn()
                setSelection(etSearch.text.length)
            }
            loadAutocomplete(currentSearch)
        } else {
            icCloseBtn.visibility = View.GONE
            context?.let {
                if (AddNewAddressUtils.isLocationEnabled(it)) {
                    doLoadAutocompleteGeocode()
                } else {
                    // When user does not enable location
                    showGpsDisabledNotification()
                    rlCurrentLocation.setOnClickListener {
                        showLocationInfoBottomSheet()
                    }
                }
            }

        }

        etSearch.run {
            setOnClickListener {
                AddNewAddressAnalytics.eventClickFieldCariLokasi(isFullFlow, isLogisticLabel)
            }
            rxEditText(this).subscribe(object : Subscriber<String>() {
                override fun onNext(t: String) {
                    if (t.isNotEmpty()) {
                        icCloseBtn.visibility = View.VISIBLE
                        setListenerClearBtn()
                        loadAutocomplete(t)
                    } else {
                        icCloseBtn.visibility = View.GONE
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

        rlCurrentLocation.setOnClickListener {
            actionListener?.useCurrentLocation()
            hideKeyboardAndDismiss()
        }
    }

    private fun setObservers() {
        viewModel.autoCompleteList.observe(this, Observer {
            when(it) {
                is Success -> {
                    if (it.data.errorCode  == CIRCUIT_BREAKER_ON_CODE) {
                        goToAddNewAddressNegative()
                    } else {
                        onSuccessGetAutocomplete(it.data)
                    }
                }
                is Fail -> {
                    Timber.d(it.throwable)
                    hideListPointOfInterest()
                }
            }
        })
    }

    private fun setListenerClearBtn() {
        icCloseBtn.setOnClickListener {
            etSearch.setText("")
            hideListPointOfInterest()
            icCloseBtn.visibility = View.GONE
        }
    }

    private fun doLoadAutocompleteGeocode() {
        showLoadingList()

        presenter.clearCacheAutocompleteGeocode()
        presenter.getAutocompleteGeocode(currentLat, currentLong)
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(com.tokopedia.purchase_platform.common.R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(com.tokopedia.purchase_platform.common.R.id.btn_close)?.setOnClickListener {
            AddNewAddressAnalytics.eventClickBackArrowOnInputAddress(isFullFlow, isLogisticLabel)
            hideKeyboardAndDismiss()
        }
    }

    fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@AutocompleteBottomSheetFragment)
            presenter.attachView(this@AutocompleteBottomSheetFragment)
        }
    }

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    override fun hideListPointOfInterest() {
        llPoi.visibility = View.GONE
        llLoading.visibility = View.GONE
    }

    private fun loadAutocomplete(input: String) {
        showLoadingList()
        viewModel.getAutoCompleteList(input)
//        presenter.getAutocomplete(input)
    }

    private fun showLoadingList() {
        llPoi.visibility = View.GONE
        llLoading.visibility = View.VISIBLE
    }

    override fun onSuccessGetAutocompleteGeocode(dataUiModel: AutocompleteGeocodeDataUiModel) {
        llLoading.visibility = View.GONE
        rvPoiList.visibility = View.VISIBLE
        mDisabledGps.visibility = View.GONE
        if (dataUiModel.results.isNotEmpty()) {
            llSubtitle.visibility = View.VISIBLE
            llPoi.visibility = View.VISIBLE
            adapter.isAutocompleteGeocode = true
            adapter.dataAutocompleteGeocode = dataUiModel.results.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onSuccessGetAutocomplete(suggestedPlaces: Place) {
        llLoading.visibility = View.GONE
        llSubtitle.visibility = View.GONE
        rvPoiList.visibility = View.VISIBLE
        mDisabledGps.visibility = View.GONE
        if (suggestedPlaces.data.isNotEmpty()) {
            llPoi.visibility = View.VISIBLE
            adapter.isAutocompleteGeocode = false
            adapter.addAutoComplete(suggestedPlaces.data)
        }
    }

    override fun goToAddNewAddressNegative() {
        val saveModel = getUnnamedRoadModelFormat(saveAddressDataModel)

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
            startActivityForResult(this, 1212)
        }
    }

    override fun onPoiListClicked(placeId: String) {
        placeId.run {
            actionListener?.onGetPlaceId(placeId)
            hideKeyboardAndDismiss()
        }
        AddNewAddressAnalytics.eventClickAddressSuggestionFromSuggestionList(isFullFlow, isLogisticLabel)
    }

    private fun getUnnamedRoadModelFormat(data: SaveAddressDataModel?): SaveAddressDataModel? {
        val fmt = data?.formattedAddress?.replaceAfter("Unnamed Road, ", "")
        return fmt?.let { data.copy(formattedAddress = it, selectedDistrict = fmt) }
    }

    private fun showLocationInfoBottomSheet() {
        val locationInfoBottomSheetFragment = LocationInfoBottomSheetFragment.newInstance(isFullFlow, isLogisticLabel)
        fragmentManager?.run {
            locationInfoBottomSheetFragment.show(this, "")
        }
        dismiss()
    }

    /**
     * Hiding keyboard is called before the dismiss in each of necessary funnel, calling it onDismiss
     * won't work probably due to some API changes from Google
     */
    private fun hideKeyboardAndDismiss() {
        AddNewAddressUtils.hideKeyboard(etSearch, context)
        dismiss()
    }

    private fun showGpsDisabledNotification() {
        mDisabledGps.visibility = View.VISIBLE
        rvPoiList.visibility = View.GONE
    }

    interface ActionListener {
        fun onGetPlaceId(placeId: String)
        fun useCurrentLocation()
    }

    companion object {
        private const val CURRENT_LAT = "CURRENT_LAT"
        private const val CURRENT_LONG = "CURRENT_LONG"
        private const val CURRENT_SEARCH = "CURRENT_SEARCH"

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