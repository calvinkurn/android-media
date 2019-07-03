package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info.LocationInfoBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import javax.inject.Inject
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils

/**
 * Created by fwidjaja on 2019-05-13.
 */
class AutocompleteBottomSheetFragment : BottomSheets(), AutocompleteBottomSheetListener, AutocompleteBottomSheetAdapter.ActionListener {
    private var bottomSheetView: View? = null
    private var currentLat: Double? = 0.0
    private var currentLong: Double? = 0.0
    private var currentSearch: String? = ""
    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }
    private lateinit var rlCurrentLocation: RelativeLayout
    private lateinit var rvPoiList: RecyclerView
    private lateinit var llPoi: LinearLayout
    private lateinit var llLoading: LinearLayout
    private lateinit var llSubtitle: LinearLayout
    private lateinit var etSearch: EditText
    private lateinit var adapter: AutocompleteBottomSheetAdapter
    private lateinit var actionListener: ActionListener
    val handler = Handler()

    @Inject
    lateinit var presenter: AutocompleteBottomSheetPresenter

    interface ActionListener {
        fun onGetPlaceId(placeId: String)
        fun useCurrentLocation(lat: Double?, long: Double?)
    }

    companion object {
        private const val CURRENT_LAT = "CURRENT_LAT"
        private const val CURRENT_LONG = "CURRENT_LONG"
        private const val CURRENT_SEARCH = "CURRENT_SEARCH"

        @JvmStatic
        fun newInstance(currentLat: Double?, currentLong: Double?, currentSearch: String?): AutocompleteBottomSheetFragment {
            return AutocompleteBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    currentLat?.let { putDouble(CURRENT_LAT, it) }
                    currentLong?.let { putDouble(CURRENT_LONG, it) }
                    currentSearch?.let { putString(CURRENT_SEARCH, it) }
                }
            }
        }

        @JvmStatic
        fun newInstance(): AutocompleteBottomSheetFragment {
            return AutocompleteBottomSheetFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            currentLat = arguments?.getDouble("CURRENT_LAT")
            currentLong = arguments?.getDouble("CURRENT_LONG")
            currentSearch = arguments?.getString("CURRENT_SEARCH")
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_autocomplete
    }

    override fun title(): String {
        return getString(R.string.title_bottomsheet_search_location)
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FULL
    }

    override fun initView(view: View) {
        prepareLayout(view)
        if (activity != null) {
            initInjector()
        }
        setViewListener()
    }

    private fun prepareLayout(view: View) {
        bottomSheetView = view
        rlCurrentLocation = view.findViewById(R.id.rl_current_location)
        rvPoiList = view.findViewById(R.id.rv_poi_list)
        llPoi = view.findViewById(R.id.ll_poi)
        llLoading = view.findViewById(R.id.ll_loading)
        llSubtitle = view.findViewById(R.id.ll_subtitle_poi)
        etSearch = view.findViewById(R.id.et_search)

        adapter = AutocompleteBottomSheetAdapter(this)
        hideListPointOfInterest()

        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        rvPoiList.layoutManager = linearLayoutManager
        rvPoiList.adapter = adapter
    }

    private fun setViewListener() {
        if (currentLat != 0.0 && currentLong != 0.0) {
            doLoadAutocompleteGeocode()
        } else {
            context?.let {
                if (AddNewAddressUtils.isLocationEnabled(it)) {
                    if (currentLat == 0.0 && currentLong == 0.0) {
                        currentLat = defaultLat
                        currentLong = defaultLong
                    }
                    doLoadAutocompleteGeocode()
                } else {
                    rlCurrentLocation.setOnClickListener {
                        showLocationInfoBottomSheet()
                    }
                }
            }
        }

        if (currentSearch?.isNotEmpty()!!) {
            etSearch.run {
                setText(currentSearch.toString())
                isFocusable = true
                setSelection(etSearch.text.length)
                setSelectAllOnFocus(true)
            }
            loadAutocomplete(currentSearch!!)
        }

        etSearch.setOnClickListener {
            AddNewAddressAnalytics.eventClickFieldCariLokasi()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (s.isNotEmpty()) {
                    val input = "$s"
                    handler.postDelayed({
                        loadAutocomplete(input)
                    }, 500)
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun doLoadAutocompleteGeocode() {
        // show loading list, hide result list
        showLoadingList()

        presenter.clearCacheAutocompleteGeocode()
        presenter.getAutocompleteGeocode(currentLat, currentLong)
        rlCurrentLocation.setOnClickListener {
            actionListener.useCurrentLocation(currentLat, currentLong)
            dismiss()
        }
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(R.id.btn_close)?.setOnClickListener {
            AddNewAddressAnalytics.eventClickBackArrowOnInputAddress()
            onCloseButtonClick()
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
        // show loading list, hide result list
        showLoadingList()

        presenter.clearCacheAutocomplete()
        presenter.getAutocomplete(input)
    }

    private fun showLoadingList() {
        llPoi.visibility = View.GONE
        llLoading.visibility = View.VISIBLE
    }

    override fun onSuccessGetAutocompleteGeocode(responseAutocompleteGeocodeDataUiModel: AutocompleteGeocodeDataUiModel) {
        // hide loading list, show subtitle & result list
        llLoading.visibility = View.GONE
        if (responseAutocompleteGeocodeDataUiModel.results.isNotEmpty()) {
            llSubtitle.visibility = View.VISIBLE
            llPoi.visibility = View.VISIBLE
            adapter.isAutocompleteGeocode = true
            adapter.dataAutocompleteGeocode = responseAutocompleteGeocodeDataUiModel.results.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onSuccessGetAutocomplete(dataUiModel: AutocompleteDataUiModel) {
        // hide loading list, show result list
        // hide subtitle_poi
        llLoading.visibility = View.GONE
        llSubtitle.visibility = View.GONE
        if (dataUiModel.listPredictions.isNotEmpty()) {
            llPoi.visibility = View.VISIBLE
            adapter.isAutocompleteGeocode = false
            adapter.dataAutocomplete = dataUiModel.listPredictions.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onPoiListClicked(placeId: String) {
        context?.let {
            placeId.run {
                actionListener.onGetPlaceId(placeId)
                dismiss()
            }
        }
        AddNewAddressAnalytics.eventClickAddressSuggestionFromSuggestionList()
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE);
        (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.windowToken, 0);
    }

    private fun showLocationInfoBottomSheet() {
        val locationInfoBottomSheetFragment = LocationInfoBottomSheetFragment.newInstance()
        locationInfoBottomSheetFragment.show(fragmentManager, "")
        dismiss()
    }
}