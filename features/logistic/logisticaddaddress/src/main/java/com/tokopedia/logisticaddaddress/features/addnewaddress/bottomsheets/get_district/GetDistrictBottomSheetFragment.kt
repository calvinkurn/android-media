package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.get_district

import android.os.Bundle
import android.view.View
import com.tokopedia.design.component.BottomSheets

/**
 * Created by fwidjaja on 2019-05-15.
 */
class GetDistrictBottomSheetFragment: BottomSheets() {
    private var placeId: String? = ""

    companion object {
        private const val PLACE_ID = "PLACE_ID"

        @JvmStatic
        fun newInstance(placeId: String): GetDistrictBottomSheetFragment {
            return GetDistrictBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(PLACE_ID, placeId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            placeId = arguments?.getString("PLACE_ID")
        }
    }

    override fun getLayoutResourceId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView(view: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}