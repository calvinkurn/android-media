package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutoCompleteGeocodeDataUiModel
import kotlinx.android.synthetic.main.bottomsheet_autocomplete_geolocation.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-13.
 */
class AutoCompleteGeocodeBottomSheetFragment: BottomSheets(), AutoCompleteGeocodeBottomSheetView, AutoCompleteGeocodeBottomSheetAdapter.AutoCompleteGeolocationListener {
    private var bottomSheetView: View? = null
    private var currentLat: Double? = 0.0
    private var currentLong: Double? = 0.0
    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }
    private lateinit var rvPoiList: RecyclerView

    private lateinit var adapter: AutoCompleteGeocodeBottomSheetAdapter

    @Inject
    lateinit var presenter: AutoCompleteGeocodeBottomSheetPresenter

    companion object {
        private const val CURRENT_LAT = "CURRENT_LAT"
        private const val CURRENT_LONG = "CURRENT_LONG"

        /*@JvmStatic
        fun newInstance(currentLat: Double, currentLong: Double): AutoCompleteGeocodeBottomSheetFragment {
            return AutoCompleteGeocodeBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putDouble(CURRENT_LAT, currentLat)
                    putDouble(CURRENT_LONG, currentLong)
                }
            }
        }*/

        @JvmStatic
        fun newInstance(): AutoCompleteGeocodeBottomSheetFragment {
            return AutoCompleteGeocodeBottomSheetFragment()
        }
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            currentLat = arguments?.getDouble("CURRENT_LAT")
            currentLong = arguments?.getDouble("CURRENT_LONG")
        }
    }*/

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_autocomplete_geolocation
    }

    override fun title(): String {
        return getString(R.string.title_bottomsheet_search_location)
    }

    override fun initView(view: View) {
        bottomSheetView = view
        rvPoiList = view.findViewById(R.id.rv_poi_list)
        adapter = AutoCompleteGeocodeBottomSheetAdapter(this)
        // adapter.setActionListener(this)
        if (activity != null) {
            initInjector()
        }
        loadPOI()
    }

    fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@AutoCompleteGeocodeBottomSheetFragment)
            presenter.attachView(this@AutoCompleteGeocodeBottomSheetFragment)
        }
    }

    override fun hideListPointOfInterest() {
        rv_poi_list.visibility = View.GONE
    }

    private fun loadPOI() {
        presenter.clearCache()
        presenter.getPointOfInterest(defaultLat, defaultLong)
    }

    override fun onSuccessGetListPointOfInterest(responseAutocompleteGeocodeDataUiModel: AutoCompleteGeocodeDataUiModel) {
        if (responseAutocompleteGeocodeDataUiModel.results.isNotEmpty()){
            adapter.data = responseAutocompleteGeocodeDataUiModel.results.toMutableList()
            val linearLayoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL, false)
            rvPoiList.layoutManager = linearLayoutManager
            rvPoiList.adapter = adapter
            updateHeight()
        }
    }

    override fun onPoiListClicked(placeId: String) {
        // dismiss this then show bottomsheet detail
        context?.let {
            placeId.run {
                println("## dismiss this then show bottomsheet detail! - placeID = $placeId")
            }
        }
    }
}