package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import com.alivc.live.pusher.*
import java.io.File


/**
 * Created by mzennis on 15/03/21.
 */
class DefaultApsaraLivePusherConfig(context: Context) : AlivcLivePushConfig() {

    init {
        setCameraType(AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT)

        // set the screen orientation to portrait
        setPreviewOrientation(AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT)

        // indicates that the stream ingest SDK crops the video to fit the screen for a preview.
        // If the aspect ratio of the video is different from that of the preview screen, the video is cropped for the preview.
        // These preview modes do not affect stream ingest.
        previewDisplayMode = AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL

        // max resolution
        setResolution(AlivcResolutionEnum.RESOLUTION_720P)

        // max fps
        setFps(AlivcFpsEnum.FPS_30)

        // configure mirroring
        setPushMirror(this.getCameraTypeEnum() == AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT)

        //  the resolution-first mode in which the stream ingest SDK sets the bitrate to prioritize the resolution of video streams.
        qualityMode = AlivcQualityModeEnum.QM_RESOLUTION_FIRST
        isEnableBitrateControl = true
        isEnableAutoResolution = true
        pausePushImage = context.filesDir.path.toString() + File.separator + "sample_pause_push_image.png"
        networkPoorPushImage = context.filesDir.path.toString() + File.separator + "sample_pause_push_image.png"
    }

    fun getCameraTypeEnum(): AlivcLivePushCameraTypeEnum {
        return if (cameraType == 0) AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK else
            AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT
    }
}