package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.get_district

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-15.
 */
class GetDistrictBottomSheetFragment: BottomSheets(), GetDistrictBottomSheetView {
    private lateinit var placeId: String
    private lateinit var lat: String
    private lateinit var long: String
    private var bottomSheetView: View? = null

    @Inject
    lateinit var presenter: GetDistrictBottomSheetPresenter

    companion object {
        private const val PLACE_ID = "PLACE_ID"
        private const val LAT = "LAT"
        private const val LONG = "LONG"

        @JvmStatic
        fun newInstance(placeId: String, lat: String, long: String): GetDistrictBottomSheetFragment {
            return GetDistrictBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(PLACE_ID, placeId)
                    putString(LAT, lat)
                    putString(LONG, long)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            placeId = arguments?.getString("PLACE_ID").toString()
            lat = arguments?.getString("LAT").toString()
            long = arguments?.getString("LONG").toString()
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_getdistrict
    }

    override fun title(): String {
        return ""
    }

    override fun initView(view: View?) {
        bottomSheetView = view

        if (activity != null) {
            initInjector()
        }
    }

    fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@GetDistrictBottomSheetFragment)
            presenter.attachView(this@GetDistrictBottomSheetFragment)
        }
    }

    override fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel) {
        title.text = getDistrictDataUiModel.title
        address.text = getDistrictDataUiModel.formattedAddress
    }
}