package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.widget.ScrollView
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_add_edit_new_address.*

/**
 * Created by fwidjaja on 2019-06-22.
 */
object AddNewAddressUtils {

    @JvmStatic
    fun generateLatLng(latitude: Double?, longitude: Double?): LatLng {
        return latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }!!
    }

    @JvmStatic
    fun scrollUpLayout(scrollViewLayout: ScrollView) {
        scrollViewLayout.postDelayed(Runnable {
            val lastChild = scrollViewLayout.getChildAt(scrollViewLayout.childCount - 1)
            val bottom = lastChild.bottom + scrollViewLayout.paddingBottom
            val sy = scrollViewLayout.scrollY
            val sh = scrollViewLayout.height
            val delta = bottom - (sy + sh)
            scrollViewLayout.smoothScrollBy(0, delta)
        }, 200)
    }
}