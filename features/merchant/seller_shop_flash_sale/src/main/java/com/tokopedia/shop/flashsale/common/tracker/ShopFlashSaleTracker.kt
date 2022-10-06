package com.tokopedia.shop.flashsale.common.tracker

import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.extension.removeTimeZone
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import java.util.TimeZone
import javax.inject.Inject

class ShopFlashSaleTracker @Inject constructor(private val userSession : UserSessionInterface) {

    companion object {
        private const val BUSINESS_UNIT = "physical goods"
        private const val CURRENT_SITE = "tokopediaseller"
        private const val EVENT = "clickPG"
    }

    //region Campaign List Page Tracker
    fun sendClickCreateCampaignEvent() {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - campaign aktif")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click buat campaign")
            .setEventLabel("")
            .build()
            .send()
    }
    //endregion

    //region Campaign Info Page Tracker
    fun sendClickButtonProceedOnCampaignInfoPageEvent(campaignId: Long, vpsPackageId: Long) {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - creation")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click lanjut")
            .setEventLabel("$campaignId - $vpsPackageId")
            .build()
            .send()
    }
    //endregion

    //region Manage Product Page Tracker
    fun sendClickButtonProceedOnManageProductPage() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click lanjut")
            .setEventCategory("fs toko - kelola produk")
            .setEventLabel("")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .build()
            .send()
    }
    //endregion

    //region Manage Highlighted Product Page Tracker
    fun sendClickButtonProceedOnManageHighlightPageEvent() {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - kelola produk highlight")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click lanjut")
            .setEventLabel("")
            .build()
            .send()
    }
    //endregion

    //region Share Component
    fun sendClickShareEvent(selectedChannelName: String) {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - campaign aktif")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click share")
            .setEventLabel(selectedChannelName)
            .build()
            .send()
    }
    //endregion

    //region Aturan Campaign Page
    fun sendClickButtonCreateCampaign(campaignId: Long, vpsPackageId: Long) {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - aturan campaign")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click buat campaign")
            .setEventLabel("$campaignId - $vpsPackageId")
            .build()
            .send()
    }
    //endregion

    //region Campaign Details Page
    fun sendClickTextEditCampaign(
        campaignId: Long,
        campaignName: String,
        campaignStatus: CampaignStatus,
    ) {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - campaign detail")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click edit campaign")
            .setEventLabel("$campaignId - $campaignName - ${campaignStatus.id}")
            .build()
            .send()
    }

    fun sendClickButtonShareCampaign(
        campaignId: Long,
        campaignName: String,
        campaignStatus: CampaignStatus,
    ) {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT)
            .setEventCategory("fs toko - campaign detail")
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .setEvent(EVENT)
            .setEventAction("click share campaign")
            .setEventLabel("$campaignId - $campaignName - ${campaignStatus.id}")
            .build()
            .send()
    }

    fun sendClickStopCampaignEvent(campaign: CampaignUiModel) {
        val campaignPeriod = buildCampaignPeriod(campaign)
        val eventLabel = "${campaign.campaignId} - ${campaign.campaignName} - $campaignPeriod"

        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click yes - hentikan campaign - pop up")
            .setEventCategory("fs toko - campaign detail")
            .setEventLabel(eventLabel)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .build()
            .send()
    }


    fun sendClickCancelCampaignEvent(campaign: CampaignUiModel) {
        val campaignPeriod = buildCampaignPeriod(campaign)
        val eventLabel = "${campaign.campaignId} - ${campaign.campaignName} - $campaignPeriod"

        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click yes - batalkan campaign - pop up")
            .setEventCategory("fs toko - campaign detail")
            .setEventLabel(eventLabel)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .build()
            .send()
    }
    //endregion

    //region campaign list bottom sheet
    fun sendClickShareCampaignPopupEvent(campaign: CampaignUiModel) {
        val eventLabel = "${campaign.campaignId} - ${campaign.campaignName} - ${campaign.status.id}"
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click share campaign - popup")
            .setEventCategory("fs toko - campaign aktif")
            .setEventLabel(eventLabel)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .build()
            .send()
    }

    fun sendClickCancelPopupEvent(campaign: CampaignUiModel) {
        val eventLabel = "${campaign.campaignId} - ${campaign.campaignName} - ${campaign.status.id}"
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click batalkan - popup")
            .setEventCategory("fs toko - campaign aktif")
            .setEventLabel(eventLabel)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .build()
            .send()
    }

    fun sendClickStopPopupEvent(campaign: CampaignUiModel) {
        val eventLabel = "${campaign.campaignId} - ${campaign.campaignName} - ${campaign.status.id}"
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click hentikan - popup")
            .setEventCategory("fs toko - campaign aktif")
            .setEventLabel(eventLabel)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .build()
            .send()
    }

    fun sendClickEditPopupEvent(campaign: CampaignUiModel) {
        val eventLabel = "${campaign.campaignId} - ${campaign.campaignName} - ${campaign.status.id}"
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click edit - popup")
            .setEventCategory("fs toko - campaign aktif")
            .setEventLabel(eventLabel)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .build()
            .send()
    }


    fun sendClickViewCampaignDetailPopUpEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click lihat detail - pop up")
            .setEventCategory("fs toko - campaign aktif")
            .setEventLabel("")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId.toString())
            .build()
            .send()
    }

    private fun buildCampaignPeriod(campaign: CampaignUiModel): String {
        return campaign.startDate
            .removeTimeZone()
            .formatTo(DateConstant.DATE_TIME_TRACKER_FORMAT, timeZone = TimeZone.getTimeZone("UTC"))
    }
    //endregion
}
