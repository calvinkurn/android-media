package com.tokopedia.play.view.viewcomponent

import android.content.Context
import android.util.Log
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
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import java.lang.Exception

/**
 * Created By : Jonathan Darwin on November 01, 2021
 */
class ShareExperienceViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val fragmentManager: FragmentManager,
    private val fragment: Fragment,
    private val listener: Listener,
    private val context: Context,
) : ViewComponent(container, idRes) {

    private val ivShareLink = findViewById<IconUnify>(R.id.ic_play_share_experience)

    private val universalShareBottomSheet: UniversalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
        init(object: ShareBottomsheetListener {
            override fun onShareOptionClicked(shareModel: ShareModel) {
                listener.onShareOptionClick(this@ShareExperienceViewComponent, shareModel)
            }

            override fun onCloseOptionClicked() {
                listener.onShareOptionClosed(this@ShareExperienceViewComponent)
            }
        })
    }

    private val screenshotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
        context, object: ScreenShotListener {
            override fun screenShotTaken() {
                Log.d("<LOG>", "screenshottaken $fragment")
                listener.onScreenshotTaken(this@ShareExperienceViewComponent)
            }
        }, fragment, addFragmentLifecycleObserver = true, permissionListener = object: PermissionListener {
            override fun permissionAction(action: String, label: String) {
                Log.d("<LOG>", "onPermissionAction : $label")
                listener.onSharePermissionAction(this@ShareExperienceViewComponent, label)
            }
        }
    )

    init {
        ivShareLink.setOnClickListener {
            listener.onShareIconClick(this)
        }
    }

    fun setIsShareable(isShow: Boolean) {
        if (isShow) ivShareLink.show() else ivShareLink.hide()
    }

    fun showSharingOptions(title: String, coverUrl: String, userId: String, channelId: String) {
        universalShareBottomSheet.apply {
            setMetaData(tnTitle = title, tnImage = coverUrl)
            setUtmCampaignData("Play", userId, channelId, "share")
            show(this@ShareExperienceViewComponent.fragmentManager, fragment, screenshotDetector)
        }
    }

    fun dismiss() {
        universalShareBottomSheet.dismiss()
    }

    fun handleRequestPermissionResult(requestCode: Int, grantResults: IntArray) {
        screenshotDetector?.onRequestPermissionsResult(requestCode, grantResults, fragment)
    }

    interface Listener {
        fun onShareIconClick(view: ShareExperienceViewComponent)
        fun onShareOptionClick(view: ShareExperienceViewComponent, shareModel: ShareModel)
        fun onShareOptionClosed(view: ShareExperienceViewComponent)
        fun onScreenshotTaken(view: ShareExperienceViewComponent)
        fun onSharePermissionAction(view: ShareExperienceViewComponent, label: String)
    }
}