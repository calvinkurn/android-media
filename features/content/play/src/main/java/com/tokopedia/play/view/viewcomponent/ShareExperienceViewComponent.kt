package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created By : Jonathan Darwin on November 01, 2021
 */
class ShareExperienceViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val fragmentManager: FragmentManager,
    private val fragment: Fragment,
    private val listener: Listener
) : ViewComponent(container, idRes) {

    private val ivShareLink = findViewById<IconUnify>(R.id.ic_play_share_experience)

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    init {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(object: ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    listener.onShareOptionClick(this@ShareExperienceViewComponent, shareModel)
                }

                override fun onCloseOptionClicked() {
                    listener.onShareOptionClosed(this@ShareExperienceViewComponent)
                }
            })
        }

        ivShareLink.setOnClickListener {
            listener.onShareIconClick(this)
        }
    }

    fun setIsShareable(isShow: Boolean) {
        if (isShow) ivShareLink.show() else ivShareLink.hide()
    }

    fun showSharingOptions(title: String, coverUrl: String) {
        universalShareBottomSheet?.setMetaData(
            tnTitle = title,
            tnImage = coverUrl,
        )
        universalShareBottomSheet?.show(fragmentManager, fragment)
    }

    interface Listener {
        fun onShareIconClick(view: ShareExperienceViewComponent)
        fun onShareOptionClick(view: ShareExperienceViewComponent, shareModel: ShareModel)
        fun onShareOptionClosed(view: ShareExperienceViewComponent)
    }
}