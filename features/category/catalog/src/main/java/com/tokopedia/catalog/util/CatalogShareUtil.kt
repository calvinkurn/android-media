package com.tokopedia.catalog.util

import androidx.fragment.app.Fragment
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.model.ShareProperties
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics
import com.tokopedia.oldcatalog.analytics.CatalogUniversalShareAnalytics
import com.tokopedia.oldcatalog.model.util.CatalogConstant
import com.tokopedia.oldcatalog.model.util.CatalogUtil
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel

class CatalogShareUtil(
    private val fragment: Fragment,
    private val shareProperties: ShareProperties
): ShareBottomsheetListener {
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenshotDetector: ScreenshotDetector? = null
    private var userId: String = ""

    fun showNativeShare(linkerShareData: LinkerShareData) {
        CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_SHARE,
            "${shareProperties.title} - ${shareProperties.catalogId}",
            userId,
            shareProperties.catalogId
        )
        CatalogUtil.shareData(
            fragment.activity,
            linkerShareData.linkerData.description,
            linkerShareData.linkerData.uri
        )
    }

    fun showUniversalShareBottomSheet(userId: String) {
        this.userId = userId
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@CatalogShareUtil)
            setUtmCampaignData(
                CatalogConstant.CATALOG,
                userId,
                shareProperties.catalogId,
                CatalogConstant.CATALOG_SHARE
            )
            setMetaData(
                "${CatalogConstant.KATALOG} ${shareProperties.title}",
                shareProperties.images.firstOrNull().orEmpty(),
                "",
                ArrayList(shareProperties.images)
            )
            setOgImageUrl(shareProperties.images.firstOrNull().orEmpty())
        }
        universalShareBottomSheet?.show(
            fragment.childFragmentManager,
            fragment,
            screenshotDetector
        )
        if (universalShareBottomSheet?.getShareBottomSheetType() == UniversalShareBottomSheet.CUSTOM_SHARE_SHEET) {
            CatalogUniversalShareAnalytics.shareBottomSheetAppearGTM(shareProperties.catalogId, userId)
        }
    }

    private fun linkerDataMapper(catalogId: String): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = catalogId
        linkerData.type = LinkerData.CATALOG_TYPE
        linkerData.name = fragment.getString(R.string.catalog_share_link_name, shareProperties.title)
        linkerData.uri = CatalogUtil.getShareURI(shareProperties.catalogUrl)
        linkerData.description = fragment.getString(R.string.catalog_share_link_description)
        linkerData.isThrowOnError = true
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val linkerShareData = linkerDataMapper(shareProperties.catalogId)
        linkerShareData.linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            isThrowOnError = false
            if (shareModel.ogImgUrl?.isNotEmpty() == true) {
                ogImageUrl = shareModel.ogImgUrl
            }
        }
        CatalogUniversalShareAnalytics.sharingChannelSelectedGTM(
            shareModel.channel.orEmpty(),
            shareProperties.catalogId,
            userId
        )
        executeShare(linkerShareData, shareModel)
    }

    private fun executeShare(linkerShareData: LinkerShareData, shareModel: ShareModel) {
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                Int.ZERO,
                linkerShareData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult?) {
                        fragment.getString(
                            R.string.catalog_share_string,
                            shareProperties.title,
                            linkerShareData?.url
                        ).let { shareString ->
                            SharingUtil.executeShareIntent(
                                shareModel,
                                linkerShareData,
                                fragment.activity,
                                fragment.view,
                                shareString
                            )
                            universalShareBottomSheet?.dismiss()
                        }
                    }

                    override fun onError(linkerError: LinkerError?) {
                        universalShareBottomSheet?.dismiss()
                        showNativeShare(linkerShareData)
                    }
                }
            )
        )
    }

    override fun onCloseOptionClicked() {
        // no-op
    }
}
