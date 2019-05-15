package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutoCompleteGeocodeBottomSheetFragment

/**
 * Created by fwidjaja on 2019-05-14.
 */
class MapLoadingBottomSheetFragment: BottomSheets() {
    private var bottomSheetViewLoading: View? = null
    val handler = Handler()

    companion object {
        @JvmStatic
        fun newInstance(): MapLoadingBottomSheetFragment {
            return MapLoadingBottomSheetFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler.postDelayed({
            // Do something after 1s = 1000ms
            showMapSearchLocationBottomSheet()
            dismiss()
        }, 1000)
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_map_loading
    }

    override fun initView(view: View?) {
        bottomSheetViewLoading = view
    }

    override fun title(): String {
        return ""
    }

    private fun showMapSearchLocationBottomSheet() {
        val mapSearchLocationBottomSheetFragment =
                AutoCompleteGeocodeBottomSheetFragment.newInstance()
        mapSearchLocationBottomSheetFragment.show(fragmentManager, "")
    }
}