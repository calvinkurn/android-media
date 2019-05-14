package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets

import android.os.Bundle
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.R
import kotlinx.android.synthetic.main.bottomsheet_map_search_location.*

/**
 * Created by fwidjaja on 2019-05-13.
 */
class MapSearchLocationBottomSheetFragment: BottomSheets(), MapSearchLocationBottomSheetView {
    private var bottomSheetView: View? = null
    private var currentLat: Double? = 0.0
    private var currentLong: Double? = 0.0

    lateinit var actionListener: ActionListener

    interface ActionListener {
        fun onSelectedLocation(selectedLat: Double, selectedLong: Double)
    }

    companion object {
        private const val CURRENT_LAT = "CURRENT_LAT"
        private const val CURRENT_LONG = "CURRENT_LONG"

        /*@JvmStatic
        fun newInstance(currentLat: Double, currentLong: Double): MapSearchLocationBottomSheetFragment {
            return MapSearchLocationBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putDouble(CURRENT_LAT, currentLat)
                    putDouble(CURRENT_LONG, currentLong)
                }
            }
        }*/

        @JvmStatic
        fun newInstance(): MapSearchLocationBottomSheetFragment {
            return MapSearchLocationBottomSheetFragment()
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
        return R.layout.bottomsheet_map_search_location
    }

    override fun title(): String {
        return getString(R.string.title_bottomsheet_search_location)
    }

    override fun initView(view: View?) {
        bottomSheetView = view
    }

    override fun hideListPointOfInterest() {
        rvPOIList.visibility = View.GONE
    }

    override fun onSuccessGetListPointOfInterest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}