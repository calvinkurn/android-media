package com.tokopedia.checkout.view.viewholder

import android.animation.Animator
import android.annotation.SuppressLint
import android.view.View
import android.view.animation.CycleInterpolator
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.UploadPrescriptionUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class UploadPrescriptionViewHolder(val view: View, private val actionListener: ShipmentAdapterActionListener): RecyclerView.ViewHolder(view) {

    private val uploadPrescriptionLayout: LinearLayout = view.findViewById(R.id.upload_prescription_layout)
    private val uploadPrescriptionText: Typography = view.findViewById(R.id.upload_prescription_text)
    private val uploadDescriptionText: Typography = view.findViewById(R.id.upload_description_text)
    private val uploadPrescriptionIcon: ImageUnify = view.findViewById(R.id.upload_icon)
    private val containerUploadPrescription: ConstraintLayout = view.findViewById(R.id.container_upload_prescription)

    companion object {
        @SuppressLint("ResourcePackage")
        @JvmStatic
        val ITEM_VIEW_UPLOAD = R.layout.item_upload_prescription
        const val EPharmacyAppLink = "tokopedia://epharmacy/"
        const val EPharmacyCountImageUrl = "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_sucess_image_uploaded_count.png"
        private const val VIBRATION_ANIMATION_DURATION = 1250
        private const val VIBRATION_ANIMATION_TRANSLATION_X = -10
        private const val VIBRATION_ANIMATION_CYCLE = 4f
    }

    fun bindViewHolder(uploadPrescriptionUiModel: UploadPrescriptionUiModel){
        uploadPrescriptionText.text = uploadPrescriptionUiModel.uploadImageText
        uploadPrescriptionIcon.loadImage(uploadPrescriptionUiModel.leftIconUrl ?: "")
        if(uploadPrescriptionUiModel.uploadedImageCount == 0){
            uploadDescriptionText.hide()
        }else {
            uploadDescriptionText.show()
            uploadDescriptionText.text = uploadPrescriptionUiModel.descriptionText
        }
        uploadPrescriptionLayout.setOnClickListener {
            actionListener.uploadPrescriptionAction(uploadPrescriptionUiModel)
        }

        if(uploadPrescriptionUiModel.isError){
            containerUploadPrescription.setBackgroundResource(R.drawable.checkout_module_bg_rounded_red)
            containerUploadPrescription.animate()
                .translationX(VIBRATION_ANIMATION_TRANSLATION_X.toFloat())
                .setDuration(VIBRATION_ANIMATION_DURATION.toLong())
                .setInterpolator(CycleInterpolator(VIBRATION_ANIMATION_CYCLE))
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {

                    }

                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                })
                .start()
        }else {
            containerUploadPrescription.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey)
        }
    }
}