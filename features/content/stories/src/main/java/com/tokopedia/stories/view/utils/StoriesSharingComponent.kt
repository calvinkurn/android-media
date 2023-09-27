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

    private val sharingSheet =
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
        sharingSheet.setMetaData(
            tnImage = data.metadata.ogImageUrl,
            tnTitle = data.metadata.ogTitle,
        )
        sharingSheet.setLinkProperties(data.metadata)
        sharingSheet.setUtmCampaignData(
            pageName = "Story",
            userId = userId,
            pageId = storyId,
            feature = "share",
        )
        sharingSheet.setOnDismissListener { mListener?.onDismissEvent(false, this@StoriesSharingComponent) }
        sharingSheet.setShowListener { mListener?.onShowSharing(this@StoriesSharingComponent) }

        if (sharingSheet.isAdded) return
        sharingSheet.show(fg, TAG)
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
