package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageUiModel
import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class DownloadVoucherImageTracker @Inject constructor(private val userSession: UserSessionInterface) {


    fun sendClickDownloadButtonEvent(voucherId: Long, selectedImages: List<VoucherImageUiModel>) {
        val selectedImageRatios = selectedImages.map { image ->
            when (image.imageRatio) {
                ImageRatio.SQUARE -> "square"
                ImageRatio.HORIZONTAL -> "horizontal"
                ImageRatio.VERTICAL -> "vertikal"
            }
        }

        val imageRatios = selectedImageRatios.joinToString(separator = ", ")
        val eventLabel = "voucher id: $voucherId - ukuran kupon: $imageRatios"

        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click checkbox")
            .setEventCategory("kupon toko saya - detail kupon - download kupon")
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40646")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }


    fun sendBottomSheetVisibleImpression() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click download - pop up ukuran kupon")
            .setEventCategory("kupon toko saya - detail kupon - download kupon")
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40647")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }


    fun sendClickCancelEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click cancel")
            .setEventCategory("kupon toko saya - detail kupon - download kupon")
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "40648")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
