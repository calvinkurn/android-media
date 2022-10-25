package com.tokopedia.purchase_platform.common.feature.ethicaldrug

import android.animation.Animator
import android.annotation.SuppressLint
import android.view.View
import android.view.animation.CycleInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class UploadPrescriptionViewHolder(
    val view: View,
    val listener: UploadPrescriptionListener
) : RecyclerView.ViewHolder(view) {

    private val uploadPrescriptionLayout: FrameLayout =
        view.findViewById(R.id.upload_prescription_layout)
    private val uploadPrescriptionText: Typography =
        view.findViewById(R.id.upload_prescription_text)
    private val uploadDescriptionText: Typography = view.findViewById(R.id.upload_description_text)
    private val uploadPrescriptionIcon: ImageUnify = view.findViewById(R.id.upload_icon)
    private val containerUploadPrescription: ConstraintLayout =
        view.findViewById(R.id.container_upload_prescription)

    companion object {
        @SuppressLint("ResourcePackage")
        @JvmStatic
        val ITEM_VIEW_UPLOAD = R.layout.item_upload_prescription
        const val EPharmacyAppLink = "tokopedia://epharmacy/"
        const val EPharmacyCountImageUrl =
            "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_sucess_image_uploaded_count.png"
        private const val VIBRATION_ANIMATION_DURATION = 1250
        private const val VIBRATION_ANIMATION_TRANSLATION_X = -10
        private const val VIBRATION_ANIMATION_CYCLE = 4f
    }

    fun bindViewHolder(uploadPrescriptionUiModel: UploadPrescriptionUiModel) {
        if (uploadPrescriptionUiModel.uploadedImageCount == null || uploadPrescriptionUiModel.uploadedImageCount == 0) {
            if (uploadPrescriptionUiModel.hasInvalidPrescription) {
                uploadPrescriptionText.text =
                    itemView.resources.getString(R.string.pp_epharmacy_upload_invalid_title_text)
                uploadPrescriptionIcon.loadImage(uploadPrescriptionUiModel.leftIconUrl ?: "")
                uploadDescriptionText.text =
                    itemView.resources.getString(R.string.pp_epharmacy_upload_invalid_description_text)
                uploadDescriptionText.show()
            } else {
                uploadPrescriptionText.text = uploadPrescriptionUiModel.uploadImageText
                uploadPrescriptionIcon.loadImage(uploadPrescriptionUiModel.leftIconUrl ?: "")
                uploadDescriptionText.hide()
            }
        } else {
            uploadPrescriptionText.text =
                itemView.resources.getString(R.string.pp_epharmacy_upload_success_title_text)
            uploadPrescriptionIcon.loadImage(EPharmacyCountImageUrl)
            uploadDescriptionText.text = itemView.resources.getString(
                R.string.pp_epharmacy_upload_count_text,
                uploadPrescriptionUiModel.uploadedImageCount
            )
            uploadDescriptionText.show()
        }
        uploadPrescriptionLayout.setOnClickListener {
            listener.uploadPrescriptionAction(uploadPrescriptionUiModel)
        }

        if (uploadPrescriptionUiModel.isError) {
            containerUploadPrescription.setBackgroundResource(R.drawable.pp_bg_rounded_red)
            containerUploadPrescription.animate()
                .translationX(VIBRATION_ANIMATION_TRANSLATION_X.toFloat())
                .setDuration(VIBRATION_ANIMATION_DURATION.toLong())
                .setInterpolator(CycleInterpolator(VIBRATION_ANIMATION_CYCLE))
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {
                        /* no-op */
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        /* no-op */
                    }

                    override fun onAnimationCancel(animator: Animator) {
                        /* no-op */
                    }

                    override fun onAnimationRepeat(animator: Animator) {
                        /* no-op */
                    }
                })
                .start()
        } else {
            containerUploadPrescription.setBackgroundResource(R.drawable.pp_bg_rounded_grey)
        }
    }
}
