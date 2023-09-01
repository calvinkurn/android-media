package com.tokopedia.checkout.revamp.view.viewholder

import android.animation.Animator
import android.graphics.drawable.Animatable
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutEpharmacyBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.unifyprinciples.Typography

class CheckoutEpharmacyViewHolder(
    private val binding: ItemCheckoutEpharmacyBinding,
    private val listener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_epharmacy
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
        if (uploadPrescriptionUiModel.uploadedImageCount <= 0) {
            binding.uploadIcon.loadImage(getIconUnifyDrawable(binding.root.context, IconUnify.DOCTOR_RECEIPT, MethodChecker.getColor(binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)))
            if (uploadPrescriptionUiModel.hasInvalidPrescription) {
                binding.uploadPrescriptionText.text =
                    itemView.resources.getString(com.tokopedia.purchase_platform.common.R.string.pp_epharmacy_upload_invalid_title_text)
                binding.uploadPrescriptionText.setWeight(Typography.BOLD)
                binding.uploadDescriptionText.text =
                    itemView.resources.getString(com.tokopedia.purchase_platform.common.R.string.pp_epharmacy_upload_invalid_description_text)
                binding.uploadDescriptionText.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                binding.uploadDescriptionText.show()
            } else {
                binding.uploadPrescriptionText.text = uploadPrescriptionUiModel.uploadImageText
                binding.uploadPrescriptionText.setWeight(Typography.REGULAR)
                binding.uploadDescriptionText.text = ""
                binding.uploadDescriptionText.hide()
            }
        } else {
            if (!uploadPrescriptionUiModel.hasShowAnimation) {
                binding.uploadIcon.loadImage(AnimatedVectorDrawableCompat.create(binding.root.context, R.drawable.checkout_module_epharmacy_icon_avd))
                (binding.uploadIcon.drawable as? Animatable)?.start()
                uploadPrescriptionUiModel.hasShowAnimation = true
            } else if ((binding.uploadIcon.drawable as? Animatable)?.isRunning == false) {
                binding.uploadIcon.loadImage(R.drawable.checkout_module_epharmacy_icon_checked)
            }
            binding.uploadPrescriptionText.text =
                itemView.resources.getString(com.tokopedia.purchase_platform.common.R.string.pp_epharmacy_upload_prescription_attached_title_text)
            binding.uploadPrescriptionText.setWeight(Typography.BOLD)
            binding.uploadDescriptionText.text = itemView.resources.getString(
                com.tokopedia.purchase_platform.common.R.string.pp_epharmacy_upload_prescription_count_text,
                uploadPrescriptionUiModel.uploadedImageCount
            )
            binding.uploadDescriptionText.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
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
            binding.uploadDescriptionText.text =
                itemView.resources.getString(com.tokopedia.purchase_platform.common.R.string.pp_epharmacy_message_error_prescription_or_consultation_not_found)
            binding.uploadDescriptionText.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_RN500)
            binding.uploadDescriptionText.show()
        }
    }

    fun getButtonText(): String {
        return binding.uploadPrescriptionText.text.toString()
    }

    fun getButtonNotes(): String {
        return binding.uploadDescriptionText.text.toString()
    }
}
