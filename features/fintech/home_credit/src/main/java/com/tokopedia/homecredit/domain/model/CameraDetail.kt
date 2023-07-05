package com.tokopedia.homecredit.domain.model

import android.content.Context
import androidx.annotation.DrawableRes
import com.otaliastudios.cameraview.controls.Facing
import com.tokopedia.applink.internal.ApplinkConstInternalFintech
import com.tokopedia.homecredit.R

data class CameraDetail(
    @DrawableRes
    val cameraCutout: Int,
    @DrawableRes
    val cameraCutoutReview: Int,
    val cameraFacing: Facing,
    val cameraTitle: String,
    val cameraTips: String,
    val cameraTipsReview: String,
    val type: String
) {

    companion object {
        fun create(context: Context?, isKtp: Boolean): CameraDetail {
            return if (isKtp) {
                CameraDetail(
                    R.drawable.ktp,
                    R.drawable.ktp_review,
                    Facing.BACK,
                    context?.getString(R.string.hc_camera_ktp_title) ?: "",
                    context?.getString(R.string.hc_camera_ktp_tips) ?: "",
                    context?.getString(R.string.hc_camera_tips_review) ?: "",
                    ApplinkConstInternalFintech.TYPE_KTP
                )
            } else {
                CameraDetail(
                    R.drawable.selfie_with_ktp,
                    R.drawable.selfie_with_ktp_review,
                    Facing.FRONT,
                    context?.getString(R.string.hc_camera_selfie_title) ?: "",
                    context?.getString(R.string.hc_camera_selfie_tips) ?: "",
                    context?.getString(R.string.hc_camera_tips_review) ?: "",
                    ApplinkConstInternalFintech.TYPE_SELFIE
                )
            }
        }
    }
}
