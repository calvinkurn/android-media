package com.tokopedia.play.view.viewcomponent

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
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
    private var imgSaveFilePath = ""

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

    private var screenshotDetector: ScreenshotDetector? = null

    init {
        ivShareLink.setOnClickListener {
            listener.onShareIconClick(this)
        }
    }

    fun setIsShareable(isShow: Boolean) {
        if (isShow) ivShareLink.show() else ivShareLink.hide()
    }

    private fun deleteTemporaryImage() {
        if(isTemporaryImageAvailable())
            File(imgSaveFilePath).delete()
    }

    private fun isTemporaryImageAvailable(): Boolean {
        if(imgSaveFilePath.isNotEmpty()) {
            return File(imgSaveFilePath).exists()
        }

        return false
    }

    fun saveTemporaryImage(imageUrl: String) {
        try {
            if(isTemporaryImageAvailable()) {
                listener.onShareOpenBottomSheet(this)
                return
            }

            deleteTemporaryImage()
            loadImageWithEmptyTarget(context, imageUrl, {
                fitCenter()
            }, MediaBitmapEmptyTarget(
                onReady = {
                    val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                        it, Bitmap.CompressFormat.PNG
                    )

                    if(savedFile != null) {
                        imgSaveFilePath = savedFile.absolutePath
                        listener.onShareOpenBottomSheet(this)
                    }
                    else {
                        listener.onHandleShareFallback(this)
                    }
                }
            ))
        }
        catch (e: Exception) {
            listener.onHandleShareFallback(this)
        }
    }

    fun showSharingOptions(title: String, coverUrl: String, userId: String, channelId: String) {
        universalShareBottomSheet.apply {
            setMetaData(tnTitle = title, tnImage = coverUrl)
            setUtmCampaignData("Play", userId, channelId, "share")
            setOgImageUrl(coverUrl)
            imageSaved(imgSaveFilePath)
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
        screenshotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
            context, object: ScreenShotListener {
                override fun screenShotTaken() {
                    listener.onScreenshotTaken(this@ShareExperienceViewComponent)
                }
            }, fragment, addFragmentLifecycleObserver = false, permissionListener = object: PermissionListener {
                override fun permissionAction(action: String, label: String) {
                    listener.onSharePermissionAction(this@ShareExperienceViewComponent, label)
                }
            }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        UniversalShareBottomSheet.clearState(screenshotDetector)
        screenshotDetector = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        deleteTemporaryImage()
    }

    interface Listener {
        fun onShareIconClick(view: ShareExperienceViewComponent)
        fun onShareOpenBottomSheet(view: ShareExperienceViewComponent)
        fun onShareOptionClick(view: ShareExperienceViewComponent, shareModel: ShareModel)
        fun onShareOptionClosed(view: ShareExperienceViewComponent)
        fun onScreenshotTaken(view: ShareExperienceViewComponent)
        fun onSharePermissionAction(view: ShareExperienceViewComponent, label: String)
        fun onHandleShareFallback(view: ShareExperienceViewComponent)
    }
}