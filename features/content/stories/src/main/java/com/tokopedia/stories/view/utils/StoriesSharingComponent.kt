package com.tokopedia.stories.view.utils

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * @author by astidhiyaa on 15/08/23
 */
class StoriesSharingComponent (rootView: View) {
    private val sharingSheet = UniversalShareBottomSheet.createInstance(rootView).apply {
        init(object : ShareBottomsheetListener {
            override fun onShareOptionClicked(shareModel: ShareModel) {
            }

            override fun onCloseOptionClicked() {
                dismiss()
            }
        })
        enableDefaultShareIntent()
    }

    fun show (fg: FragmentManager, metadata: StoriesSharingMetadata) {
        if (sharingSheet.isAdded) return
        sharingSheet.setMetaData(
            tnImage = metadata.image,
            tnTitle = metadata.title
        )
        sharingSheet.setLinkProperties(LinkProperties())
        sharingSheet.show(fg, TAG)
    }

    companion object {
        private const val TAG = "StoriesSharingBottomSheet"
    }

    @Deprecated("replace with universal's LinkProperties")
    data class StoriesSharingMetadata(
        val title: String,
        val image: String,
    )
}
