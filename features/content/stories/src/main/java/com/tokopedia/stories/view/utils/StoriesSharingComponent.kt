package com.tokopedia.stories.view.utils

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * @author by astidhiyaa on 15/08/23
 */
class StoriesSharingComponent(rootView: View) {
    private var mListener: Listener? = null

    private val universalShareBottomSheet =
        UniversalShareBottomSheet.createInstance(rootView).apply {
            init(object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    mListener?.onShareChannel(shareModel)
                }

                override fun onCloseOptionClicked() {
                    mListener?.onDismissEvent(true, this@StoriesSharingComponent)
                }
            })
            enableDefaultShareIntent()
        }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun show(
        fg: FragmentManager,
        data: StoriesDetailItem.Sharing,
        userId: String,
        storyId: String
    ) {
        universalShareBottomSheet.apply {
            setMetaData(
                tnImage = data.metadata.ogImageUrl,
                tnTitle = data.metadata.ogTitle
            )
            setLinkProperties(data.metadata)
            setUtmCampaignData(
                pageName = "Story",
                userId = userId,
                pageId = storyId,
                feature = "share"
            )
            setShareText(getShareDescription(data.metadata.ogDescription))
            setOnDismissListener { mListener?.onDismissEvent(false, this@StoriesSharingComponent) }
            setShowListener { mListener?.onShowSharing(this@StoriesSharingComponent) }
        }.also {
            if (it.isAdded) return@also
            it.show(fg, TAG)
        }
    }

    private fun getShareDescription(str: String): String {
        return "$str %s"
    }

    companion object {
        private const val TAG = "StoriesSharingBottomSheet"
    }

    interface Listener {
        fun onDismissEvent(isFromClick: Boolean, view: StoriesSharingComponent)
        fun onShareChannel(shareModel: ShareModel)

        fun onShowSharing(view: StoriesSharingComponent)
    }
}
