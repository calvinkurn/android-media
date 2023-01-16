package com.tokopedia.shop.flashsale.presentation.list.quotamonitoring

import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.domain.entity.VpsPackageAvailability

object VpsPackageDummyDataHelper {
    fun generateDummyVpsPackagesWithDynamicEndTime(): List<VpsPackage> {
        return listOf(
            VpsPackage(
                5,
                5,
                false,
                10,
                1856769776,
                "packageId",
                "PackageName",
                0
            ),
            VpsPackage(
                5,
                5,
                false,
                10,
                DateHelper.getDayAfterTomorrowInUnix(),
                "packageId",
                "PackageName",
                0
            )
        )
    }

    fun generateDummyVpsPackages(): List<VpsPackage> {
        return listOf(
            VpsPackage(
                5,
                5,
                false,
                10,
                1856769776,
                "packageId",
                "PackageName",
                0
            ),
            VpsPackage(
                5,
                5,
                false,
                10,
                1856769776,
                "packageId",
                "PackageName",
                0
            )
        )
    }

    fun generateDummyVpsPackageAvailability(): VpsPackageAvailability {
        return VpsPackageAvailability(
            20,
            10,
            true,
            1
        )
    }
}
