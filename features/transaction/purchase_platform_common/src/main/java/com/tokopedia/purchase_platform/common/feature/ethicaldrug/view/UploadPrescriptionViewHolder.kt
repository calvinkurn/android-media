package com.tokopedia.purchase_platform.common.feature.ethicaldrug.view

import android.animation.Animator
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.purchase_platform.common.databinding.ItemUploadPrescriptionBinding
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel

class UploadPrescriptionViewHolder(
    val view: View,
    val listener: UploadPrescriptionListener
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemUploadPrescriptionBinding.bind(view)

    companion object {
        @SuppressLint("ResourcePackage")
        @JvmStatic
        val ITEM_VIEW_UPLOAD = R.layout.item_upload_prescription
        const val EPharmacyAppLink = "tokopedia://epharmacy/"
        const val EPharmacyCountImageUrl = TokopediaImageUrl.E_PHARMACY_COUNT_IMAGE_URL
        const val EPharmacyMiniConsultationAppLink = "tokopedia://epharmacy/attach-prescription/"
        private const val VIBRATION_ANIMATION_DURATION = 1250
        private const val VIBRATION_ANIMATION_TRANSLATION_X = -10
        private const val VIBRATION_ANIMATION_CYCLE = 4f
    }

    fun bindViewHolder(uploadPrescriptionUiModel: UploadPrescriptionUiModel) {
        if (!uploadPrescriptionUiModel.showImageUpload) {
            binding.root.gone()
            binding.root.layoutParams = RecyclerView.LayoutParams(0, 0)
            return
        } else {
            binding.root.visible()
            binding.root.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        binding.uploadIcon.loadImage(uploadPrescriptionUiModel.leftIconUrl)
        if (uploadPrescriptionUiModel.uploadedImageCount == 0) {
            if (uploadPrescriptionUiModel.hasInvalidPrescription) {
                binding.uploadPrescriptionText.text =
                    itemView.resources.getString(R.string.pp_epharmacy_upload_invalid_title_text)
                binding.uploadDescriptionText.text =
                    itemView.resources.getString(R.string.pp_epharmacy_upload_invalid_description_text)
                binding.uploadDescriptionText.show()
            } else {
                binding.uploadPrescriptionText.text = uploadPrescriptionUiModel.uploadImageText
                binding.uploadDescriptionText.text = ""
                binding.uploadDescriptionText.hide()
            }
        } else {
            binding.uploadPrescriptionText.text =
                itemView.resources.getString(R.string.pp_epharmacy_upload_prescription_attached_title_text)
            binding.uploadDescriptionText.text = itemView.resources.getString(
                R.string.pp_epharmacy_upload_prescription_count_text,
                uploadPrescriptionUiModel.uploadedImageCount
            )
            binding.uploadDescriptionText.show()
        }
        binding.uploadPrescriptionLayout.setOnClickListener {
            listener.uploadPrescriptionAction(
                uploadPrescriptionUiModel,
                getButtonText(),
                getButtonNotes()
            )
        }

        if (uploadPrescriptionUiModel.isError) {
            binding.containerUploadPrescription.setBackgroundResource(R.drawable.bg_pp_rounded_red)
            binding.containerUploadPrescription.animate()
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
            binding.containerUploadPrescription.setBackgroundResource(R.drawable.bg_pp_rounded_grey)
        }
        if (uploadPrescriptionUiModel.isOcc) {
            binding.occDivider.visible()
        } else {
            binding.occDivider.gone()
        }
    }

    fun getButtonText(): String {
        return binding.uploadPrescriptionText.text.toString()
    }

    fun getButtonNotes(): String {
        return binding.uploadDescriptionText.text.toString()
    }
}
