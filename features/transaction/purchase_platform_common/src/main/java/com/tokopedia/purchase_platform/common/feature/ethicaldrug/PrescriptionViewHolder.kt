package com.tokopedia.purchase_platform.common.feature.ethicaldrug

import android.annotation.SuppressLint
import android.view.animation.CycleInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.purchase_platform.common.databinding.ItemPrescriptionWidgetBinding

class PrescriptionViewHolder(
    private val binding: ItemPrescriptionWidgetBinding,
    private val listener: Listener
) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        @SuppressLint("ResourcePackage")
        @JvmStatic
        val ITEM_VIEW_UPLOAD = R.layout.item_prescription_widget
        const val EPharmacyAppLink = "tokopedia://epharmacy/"
        const val EPharmacyCountImageUrl =
            "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_sucess_image_uploaded_count.png"
        private const val VIBRATION_ANIMATION_DURATION = 1250
        private const val VIBRATION_ANIMATION_TRANSLATION_X = -10
        private const val VIBRATION_ANIMATION_CYCLE = 4f
    }

    fun bindViewHolder(uploadPrescriptionUiModel: UploadPrescriptionUiModel) {
        binding.apply {
            uploadPrescriptionText.text = uploadPrescriptionUiModel.uploadImageText
            prescriptionIcon.loadImage(uploadPrescriptionUiModel.leftIconUrl ?: "")
            if (uploadPrescriptionUiModel.uploadedImageCount == 0) {
                uploadDescriptionText.hide()
            } else {
                uploadDescriptionText.show()
                uploadDescriptionText.text = uploadPrescriptionUiModel.descriptionText
            }
            uploadPrescriptionLayout.setOnClickListener {
                listener.onClickPrescriptionWidget(uploadPrescriptionUiModel)
            }

            if (uploadPrescriptionUiModel.isError) {
                containerUploadPrescription.setBackgroundResource(R.drawable.pp_bg_rounded_red)
                containerUploadPrescription.animate()
                    .translationX(VIBRATION_ANIMATION_TRANSLATION_X.toFloat())
                    .setDuration(VIBRATION_ANIMATION_DURATION.toLong())
                    .setInterpolator(CycleInterpolator(VIBRATION_ANIMATION_CYCLE))
                    .start()
            } else {
                containerUploadPrescription.setBackgroundResource(R.drawable.pp_bg_rounded_grey)
            }
        }
    }

    interface Listener {
        fun onClickPrescriptionWidget(uploadPrescriptionUiModel: UploadPrescriptionUiModel)
    }
}
