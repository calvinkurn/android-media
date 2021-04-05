package com.tokopedia.play.broadcaster.pusher.config

import com.alivc.live.pusher.AlivcLivePushCameraTypeEnum
import com.alivc.live.pusher.AlivcLivePushConfig


/**
 * Created by mzennis on 22/09/20.
 */
open class ApsaraLivePusherConfig : AlivcLivePushConfig() {

    fun getCameraTypeEnum(): AlivcLivePushCameraTypeEnum {
        return if (cameraType == 0) AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK else
            AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT
    }
}