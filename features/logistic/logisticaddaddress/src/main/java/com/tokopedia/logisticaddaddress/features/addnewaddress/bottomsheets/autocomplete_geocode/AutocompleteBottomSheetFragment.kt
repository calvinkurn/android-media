package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

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
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import kotlinx.android.synthetic.main.bottomsheet_autocomplete.*
import javax.inject.Inject


/**
 * Created by fwidjaja on 2019-05-13.
 */
class AutocompleteBottomSheetFragment: BottomSheets(), AutocompleteBottomSheetListener, AutocompleteBottomSheetAdapter.ActionListener{
    private var bottomSheetView: View? = null
    private var currentLat: Double? = 0.0
    private var currentLong: Double? = 0.0
    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }
    private lateinit var rlCurrentLocation: RelativeLayout
    private lateinit var rvPoiList: RecyclerView
    private lateinit var llPoi: LinearLayout
    private lateinit var etSearch: EditText
    private lateinit var adapter: AutocompleteBottomSheetAdapter
    private lateinit var actionListener: ActionListener
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
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

        @JvmStatic
        fun newInstance(currentLat: Double?, currentLong: Double?): AutocompleteBottomSheetFragment {
            return AutocompleteBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    currentLat?.let { putDouble(CURRENT_LAT, it) }
                    currentLong?.let { putDouble(CURRENT_LONG, it) }
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
        permissionCheckerHelper = PermissionCheckerHelper()
        if (arguments != null) {
            currentLat = arguments?.getDouble("CURRENT_LAT")
            currentLong = arguments?.getDouble("CURRENT_LONG")
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_autocomplete
    }

    override fun title(): String {
        return getString(R.string.title_bottomsheet_search_location)
    }

    override fun initView(view: View) {
        bottomSheetView = view
        rlCurrentLocation = view.findViewById(R.id.rl_current_location)
        rvPoiList = view.findViewById(R.id.rv_poi_list)
        llPoi = view.findViewById(R.id.ll_poi)
        etSearch = view.findViewById(R.id.et_search)
        adapter = AutocompleteBottomSheetAdapter(this)

        // set presenter
        if (activity != null) {
            initInjector()
        }
        presenter.setPermissionChecker(permissionCheckerHelper)
        activity?.let { presenter.requestLocation(it) }

        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        rvPoiList.layoutManager = linearLayoutManager
        rvPoiList.adapter = adapter

        rlCurrentLocation.setOnClickListener {
            actionListener.useCurrentLocation(currentLat, currentLong)
            dismiss()
        }

        /*if (currentLat != 0.0 && currentLong != 0.0) {
            presenter.clearCacheAutocompleteGeocode()
            presenter.getAutocompleteGeocode(currentLat, currentLong)
            rlCurrentLocation.setOnClickListener {
                actionListener.useCurrentLocation(currentLat, currentLong)
                dismiss()
            }
        } else {
            hideListPointOfInterest()
            rlCurrentLocation.setOnClickListener {
                // show undetected bottomsheet
            }
        }*/

        onAddressTyped()
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(R.id.btn_close)?.setOnClickListener{ onCloseButtonClick() }
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
        // rvPoiList.visibility = View.GONE
    }

    private fun loadAutocomplete(input: String) {
        presenter.clearCacheAutocomplete()
        presenter.getAutocomplete(input)
    }

    override fun onSuccessGetAutocompleteGeocode(responseAutocompleteGeocodeDataUiModel: AutocompleteGeocodeDataUiModel) {
        if (responseAutocompleteGeocodeDataUiModel.results.isNotEmpty()){
            adapter.isAutocompleteGeocode = true
            adapter.dataAutocompleteGeocode = responseAutocompleteGeocodeDataUiModel.results.toMutableList()
            adapter.notifyDataSetChanged()
            updateHeight()
        }
    }

    override fun onSuccessGetAutocomplete(dataUiModel: AutocompleteDataUiModel) {
        if (dataUiModel.listPredictions.isNotEmpty()){
            llPoi.visibility = View.VISIBLE
            // rvPoiList.visibility = View.VISIBLE
            adapter.isAutocompleteGeocode = false
            adapter.dataAutocomplete = dataUiModel.listPredictions.toMutableList()
            adapter.notifyDataSetChanged()
            updateHeight()
        }
    }

    override fun onPoiListClicked(placeId: String) {
        // dismiss this then show bottomsheet detail
        context?.let {
            placeId.run {
                /*val getDistrictBottomSheetFragment =
                        GetDistrictBottomSheetFragment.newInstance(placeId, "", "")
                getDistrictBottomSheetFragment.isCancelable = false
                getDistrictBottomSheetFragment.show(fragmentManager, "")*/
                actionListener.onGetPlaceId(placeId)
                dismiss()
            }
        }
    }

    private fun onAddressTyped() {
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

    /*override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        println("## onInterceptTouchEvent")
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        println("## onRequestDisallowInterceptTouchEvent")
    }*/

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE);
        (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.windowToken, 0);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                permissionCheckerHelper.onRequestPermissionsResult(it,
                        requestCode, permissions,
                        grantResults)
            }
        }
    }

    override fun setCurrentLatLong(lat: Double, long: Double) {
        currentLat = lat
        currentLong = long
    }
}