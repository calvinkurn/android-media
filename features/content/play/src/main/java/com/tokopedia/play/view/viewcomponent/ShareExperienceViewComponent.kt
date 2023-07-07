package com.tokopedia.play.view.viewcomponent

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import kotlinx.coroutines.*

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
    source: Source = Source.PlayRoom
) : ViewComponent(container, idRes) {

    private val ivShareLink = rootView as IconUnify

    private val universalShareBottomSheet: UniversalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
        init(object : ShareBottomsheetListener {
            override fun onShareOptionClicked(shareModel: ShareModel) {
                listener.onShareOptionClick(this@ShareExperienceViewComponent, shareModel)
            }

            override fun onCloseOptionClicked() {
                listener.onShareOptionClosed(this@ShareExperienceViewComponent)
            }
        })
    }

    private var screenshotDetector: ScreenshotDetector? = null

    val isScreenshotBottomSheet: Boolean
        get() = universalShareBottomSheet.getShareBottomSheetType() == UniversalShareBottomSheet.SCREENSHOT_SHARE_SHEET

    init {
        ivShareLink.setOnClickListener {
            listener.onShareIconClick(this)
        }
        ivShareLink.setImage(newIconId = if (source == Source.Upcoming) IconUnify.SHARE_MOBILE else IconUnify.SHARE)
    }

    fun setIsShareable(isShow: Boolean) {
        if (isShow) {
            ivShareLink.show()
            listener.onShareIconImpressed(this)
        } else {
            ivShareLink.hide()
        }
    }

    fun showSharingOptions(title: String, coverUrl: String, userId: String, channelId: String) {
        universalShareBottomSheet.apply {
            setFeatureFlagRemoteConfigKey()
            setMetaData(tnTitle = title, tnImage = coverUrl)
            setUtmCampaignData("Play", userId, channelId, "share")
            setOgImageUrl(coverUrl)
            show(this@ShareExperienceViewComponent.fragmentManager, fragment, screenshotDetector)
        }
    }

    fun dismiss() {
        universalShareBottomSheet.dismiss()
    }

    fun handleRequestPermissionResult(requestCode: Int, grantResults: IntArray) {
        screenshotDetector?.onRequestPermissionsResult(requestCode, grantResults, fragment)
    }

    /**
     * Lifecycle
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        screenshotDetector = SharingUtil.createAndStartScreenShotDetector(
            context,
            object : ScreenShotListener {
                override fun screenShotTaken(path: String) {
                    listener.onScreenshotTaken(this@ShareExperienceViewComponent)
                }
            },
            fragment,
            addFragmentLifecycleObserver = false,
            permissionListener = object : PermissionListener {
                override fun permissionAction(action: String, label: String) {
                    listener.onSharePermissionAction(this@ShareExperienceViewComponent, label)
                }
            }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        SharingUtil.clearState(screenshotDetector)
        screenshotDetector = null
    }

    interface Listener {
        fun onShareIconClick(view: ShareExperienceViewComponent)
        fun onShareOptionClick(view: ShareExperienceViewComponent, shareModel: ShareModel)
        fun onShareOptionClosed(view: ShareExperienceViewComponent)
        fun onScreenshotTaken(view: ShareExperienceViewComponent)
        fun onSharePermissionAction(view: ShareExperienceViewComponent, label: String)
        fun onShareIconImpressed(view: ShareExperienceViewComponent)
    }

    enum class Source {
        Upcoming, PlayRoom;
    }
}
