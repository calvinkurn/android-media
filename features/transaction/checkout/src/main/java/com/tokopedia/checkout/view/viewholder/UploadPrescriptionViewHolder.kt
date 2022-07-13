package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.UploadPrescriptionUiModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class UploadPrescriptionViewHolder(val view: View, private val actionListener: ShipmentAdapterActionListener): RecyclerView.ViewHolder(view) {

    private val uploadPrescriptionLayout: LinearLayout = view.findViewById(R.id.upload_prescription_layout)
    private val uploadPrescriptionText: Typography = view.findViewById(R.id.upload_prescription_text)
    private val uploadPrescriptionIcon: ImageUnify = view.findViewById(R.id.upload_icon)
    private val uploadPrescriptionCardUnify: CardUnify2 = view.findViewById(R.id.upload_prescription_card)

    companion object {
        @SuppressLint("ResourcePackage")
        @JvmStatic
        val ITEM_VIEW_UPLOAD = R.layout.item_upload_prescription
    }

    fun bindViewHolder(uploadPrescriptionUiModel: UploadPrescriptionUiModel){
        uploadPrescriptionCardUnify.cardType = CardUnify2.TYPE_BORDER
        uploadPrescriptionText.text = uploadPrescriptionUiModel.uploadImageText
        uploadPrescriptionIcon.loadImage(uploadPrescriptionUiModel.leftIconUrl ?: "")
        uploadPrescriptionLayout.setOnClickListener {
            actionListener.uploadPrescriptionAction(uploadPrescriptionUiModel)
        }
    }
}