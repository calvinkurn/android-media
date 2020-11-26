package com.tokopedia.play.broadcaster.pusher

import com.alivc.live.pusher.AlivcLivePushCameraTypeEnum
import com.alivc.live.pusher.AlivcLivePushConfig


/**
 * Created by mzennis on 10/08/20.
 */
class PlayPusherConfig : AlivcLivePushConfig() {

    fun getCameraTypeEnum(): AlivcLivePushCameraTypeEnum {
        return if (cameraType == 0) AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK else
            AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT
    }
}