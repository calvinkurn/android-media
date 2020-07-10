package com.tokopedia.entertainment.pdp.listener

import com.tokopedia.entertainment.pdp.data.Facilities
import com.tokopedia.entertainment.pdp.data.Outlet
import com.tokopedia.entertainment.pdp.data.ValueBullet
import com.tokopedia.entertainment.pdp.data.pdp.OpenHour

/**
 * @author by firman on 28/10/19
 */
interface OnBindItemListener {
    fun seeAllOpenHour(openHour : List<OpenHour>, title:String)
    fun seeAllFacilities(facilities: List<Facilities>,title: String)
    fun seeAllHowtoGoThere(listBullet: List<ValueBullet>, title: String)
    fun seeAllAbout(value : String, title: String)
    fun seeLocationDetail(outlet: Outlet)

    fun performancePdp()
}