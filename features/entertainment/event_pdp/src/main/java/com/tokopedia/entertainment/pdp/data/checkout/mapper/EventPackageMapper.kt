package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.Package
import com.tokopedia.entertainment.pdp.data.Schedule

object EventPackageMapper {

    fun getPackage(scheduleId: String, groupID: String, packageID: String,
                   productDetailData: ProductDetailData): Package {

        var packet: Package = Package()
        for (i in 0..productDetailData.schedules.size-1) {
            val groups = productDetailData.schedules[i].groups

            for (j in 0..groups.size-1) {

                if (groups[j].id.equals(groupID)) {
                    val packages = groups[j].packages

                    for (k in 0..packages.size-1) {
                        if(packages[k].id.equals(packageID)){
                            packet = packages[k]
                        }
                    }
                }
            }
        }

        return packet
    }

    fun getSchedule(scheduleId: String, productDetailData: ProductDetailData): Schedule {

        var schedule = Schedule()
        for (i in 0..productDetailData.schedules.size-1) {
            val scheduleTemp = productDetailData.schedules[i].schedule
            if(scheduleTemp.id.equals(scheduleId)){
                schedule =  productDetailData.schedules[i].schedule
            }
        }

        return schedule
    }


}