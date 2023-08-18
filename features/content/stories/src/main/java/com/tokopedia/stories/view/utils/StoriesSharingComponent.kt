package com.tokopedia.stories.view.utils

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * @author by astidhiyaa on 15/08/23
 */
class StoriesSharingComponent (rootView: View) {
    private var mListener : Listener? = null
    private val sharingSheet = UniversalShareBottomSheet.createInstance(rootView).apply {
        init(object : ShareBottomsheetListener {
            override fun onShareOptionClicked(shareModel: ShareModel) {}

            override fun onCloseOptionClicked() {
                mListener?.onDismissEvent(this@StoriesSharingComponent)
            }
        })
        enableDefaultShareIntent()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun show (fg: FragmentManager, data: StoriesDetailUiModel.Sharing) {
        if (sharingSheet.isAdded) return
        sharingSheet.setMetaData(
            tnImage = data.metadata.ogImageUrl,
            tnTitle = data.metadata.ogTitle,
        )
        sharingSheet.setLinkProperties(data.metadata)
        sharingSheet.show(fg, TAG)
    }

    companion object {
        private const val TAG = "StoriesSharingBottomSheet"
    }

    interface Listener {
        fun onDismissEvent(view: StoriesSharingComponent)
    }
}
