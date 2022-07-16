package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.UploadPrescriptionUiModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class UploadPrescriptionViewHolder(val view: View, private val actionListener: ShipmentAdapterActionListener): RecyclerView.ViewHolder(view) {

    private val uploadPrescriptionLayout: LinearLayout = view.findViewById(R.id.upload_prescription_layout)
    private val uploadPrescriptionText: Typography = view.findViewById(R.id.upload_prescription_text)
    private val uploadDescriptionText: Typography = view.findViewById(R.id.upload_description_text)
    private val uploadPrescriptionIcon: ImageUnify = view.findViewById(R.id.upload_icon)

    companion object {
        @SuppressLint("ResourcePackage")
        @JvmStatic
        val ITEM_VIEW_UPLOAD = R.layout.item_upload_prescription
        const val EPharmacyAppLink = "tokopedia://epharmacy/"
        const val EPharmacyCountImageUrl = "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_uploaded_images_count.png"
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

        if(uploadPrescriptionUiModel.prescriptionIds?.isEmpty() == true){
            actionListener.fetchPrescriptionIds(uploadPrescriptionUiModel.checkoutId)
        }
    }
}