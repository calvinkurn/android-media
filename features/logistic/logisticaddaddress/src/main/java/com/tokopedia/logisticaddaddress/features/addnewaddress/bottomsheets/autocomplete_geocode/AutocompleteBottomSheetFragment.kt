package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
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
    private lateinit var rvPoiList: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var adapter: AutocompleteBottomSheetAdapter
    private lateinit var actionListener: ActionListener
    val handler = Handler()

    @Inject
    lateinit var presenter: AutocompleteBottomSheetPresenter

    interface ActionListener {
        fun onGetPlaceId(placeId: String)
    }

    companion object {
        private const val CURRENT_LAT = "CURRENT_LAT"
        private const val CURRENT_LONG = "CURRENT_LONG"

        @JvmStatic
        fun newInstance(currentLat: Double, currentLong: Double): AutocompleteBottomSheetFragment {
            return AutocompleteBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putDouble(CURRENT_LAT, currentLat)
                    putDouble(CURRENT_LONG, currentLong)
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
        rvPoiList = view.findViewById(R.id.rv_poi_list)
        etSearch = view.findViewById(R.id.et_search)
        adapter = AutocompleteBottomSheetAdapter(this)
        if (activity != null) {
            initInjector()
        }

        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        rvPoiList.layoutManager = linearLayoutManager
        rvPoiList.adapter = adapter

        loadAutocompleteGeocode()
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
        rv_poi_list.visibility = View.GONE
    }

    private fun loadAutocompleteGeocode() {
        presenter.clearCacheAutocompleteGeocode()
        presenter.getAutocompleteGeocode(currentLat, currentLong)
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
                /*handler.postDelayed({
                    println("## afterTextChanged - s = $s")
                }, 1000)*/
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
}