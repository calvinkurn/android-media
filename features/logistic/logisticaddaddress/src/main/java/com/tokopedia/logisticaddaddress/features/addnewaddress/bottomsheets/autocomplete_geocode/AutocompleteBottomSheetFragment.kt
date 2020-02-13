package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info.LocationInfoBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-13.
 */
class AutocompleteBottomSheetFragment : BottomSheets(), AutocompleteBottomSheetListener, AutocompleteBottomSheetAdapter.ActionListener {
    private var bottomSheetView: View? = null
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var currentSearch: String = ""
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
    private lateinit var actionListener: ActionListener
    private lateinit var icCloseBtn: ImageView

    @Inject
    lateinit var presenter: AutocompleteBottomSheetPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentLat = it.getDouble(CURRENT_LAT, defaultLat)
            currentLong = it.getDouble(CURRENT_LONG, defaultLong)
            currentSearch = it.getString(CURRENT_SEARCH, "")
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_autocomplete
    }

    override fun title(): String {
        return getString(R.string.title_bottomsheet_search_location)
    }

    override fun state(): BottomSheets.BottomSheetsState {
        return BottomSheets.BottomSheetsState.FULL
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
                AddNewAddressAnalytics.eventClickFieldCariLokasi()
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                               after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                           count: Int) {
                    if (s.isNotEmpty()) {
                        icCloseBtn.visibility = View.VISIBLE
                        val input = "$s"
                        setListenerClearBtn()
                        handler.postDelayed({
                            loadAutocomplete(input)
                        }, 500)
                    } else {
                        icCloseBtn.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            })

            isFocusableInTouchMode = true
            isFocusable = true
            requestFocus()
            AddNewAddressUtils.showKeyboard(context)
        }

        rlCurrentLocation.setOnClickListener {
            actionListener.useCurrentLocation()
            dismiss()
        }
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
        showLoadingList()

        presenter.clearCacheAutocomplete()
        presenter.getAutocomplete(input)
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

    override fun onSuccessGetAutocomplete(dataUiModel: AutocompleteDataUiModel) {
        llLoading.visibility = View.GONE
        llSubtitle.visibility = View.GONE
        rvPoiList.visibility = View.VISIBLE
        mDisabledGps.visibility = View.GONE
        if (dataUiModel.listPredictions.isNotEmpty()) {
            llPoi.visibility = View.VISIBLE
            adapter.isAutocompleteGeocode = false
            adapter.dataAutocomplete = dataUiModel.listPredictions.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        KeyboardHandler.hideSoftKeyboard(activity)
        super.onPause()
    }

    override fun onPoiListClicked(placeId: String) {
        placeId.run {
            actionListener.onGetPlaceId(placeId)
            dismiss()
        }
        AddNewAddressAnalytics.eventClickAddressSuggestionFromSuggestionList()
    }

    private fun showLocationInfoBottomSheet() {
        val locationInfoBottomSheetFragment = LocationInfoBottomSheetFragment.newInstance()
        fragmentManager?.run {
            locationInfoBottomSheetFragment.show(this, "")
        }
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

        fun newInstance(currentLat: Double, currentLong: Double, currentSearch: String): AutocompleteBottomSheetFragment {
            return AutocompleteBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putDouble(CURRENT_LAT, currentLat)
                    putDouble(CURRENT_LONG, currentLong)
                    putString(CURRENT_SEARCH, currentSearch)
                }
            }
        }

        fun newInstance(): AutocompleteBottomSheetFragment {
            return AutocompleteBottomSheetFragment()
        }
    }
}