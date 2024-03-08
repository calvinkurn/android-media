package com.tokopedia.gamification.utils

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData.GamiGetScratchCardLandingPage.AppBar
import com.tokopedia.universal_sharing.util.UniversalShareConst
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * @author by astidhiyaa on 15/08/23
 */
class KetupatSharingComponent(rootView: View, val trackerLabel: String,
                              private val userID: String) {
    private var mListener: Listener? = null

    private val universalShareBottomSheet =
        UniversalShareBottomSheet.createInstance(rootView).apply {
            init(object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    mListener?.onShareChannel(shareModel)

                    shareTracker.trackClickShareChannel(
                        trackerLabel,
                        shareModel.channel ?: "",
                        UniversalShareConst.ImageType.KEY_IMAGE_DEFAULT,
                        "thr ketupat",
                        "49194",
                        "tokopediamarketplace",
                        getDataMap()
                    )
                }

                override fun onCloseOptionClicked() {
                    mListener?.onDismissEvent(true, this@KetupatSharingComponent)
                    shareTracker.trackCloseShare(
                        trackerLabel,
                        "thr ketupat",
                        "49193",
                        "tokopediamarketplace",
                        getDataMap()
                    )
                }
            })
            enableDefaultShareIntent()
        }

    private fun getDataMap(): HashMap<String, Any>{
        val dataMap = HashMap<String, Any>()
        dataMap["userId"] = userID
        dataMap["pagePath"] = "DirectRewardGame"
        return dataMap
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun show(
        fg: FragmentManager,
        data: AppBar.Shared?,
        userId: String,
    ) {
        universalShareBottomSheet.apply {
            setMetaData(
                tnImage = data?.ogImageURL!!,
                tnTitle = data.ogTitle!!
            )
            setUtmCampaignData(
                pageName = data.page!!,
                userId = userId,
                pageId = data.identifier!!,
                feature = "share"
            )
            setLinkProperties(
                LinkProperties(
                    linkerType = "KetupatLP",
                    id = TAG,
                    ogTitle = data.ogTitle,
                    ogDescription = data.message!!,
                    ogImageUrl = data.ogImageURL,
                    deeplink = "tokopedia://gamification/ketupat_rewards_landing_page",
                    desktopUrl = "https://www.tokopedia.com"
                )
            )
            setShareText(data.message!!)
            setOnDismissListener { mListener?.onDismissEvent(false, this@KetupatSharingComponent) }
            setShowListener { shareTracker.trackViewShare(
                trackerLabel,
                "thr ketupat",
                "49195",
                "tokopediamarketplace",
                getDataMap()
            ) }
        }.also {
            if (it.isAdded) return@also
            it.show(fg, TAG)
        }
    }

    companion object {
        private const val TAG = "KetupatSharingBottomSheet"
    }

    interface Listener {
        fun onDismissEvent(isFromClick: Boolean, view: KetupatSharingComponent)
        fun onShareChannel(shareModel: ShareModel)
        fun onShowSharing(view: KetupatSharingComponent)
    }
}
