package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class UploadPrescriptionViewHolder(val view: View, private val actionListener: ShipmentAdapterActionListener): RecyclerView.ViewHolder(view) {

    private val uploadPrescButton: LinearLayout = view.findViewById(R.id.upload_button)
    private val uploadPrescText: Typography = view.findViewById(R.id.upload_prescription_text)
    private val uploadPrescIcon: ImageUnify = view.findViewById(R.id.upload_icon)

    companion object {
        @SuppressLint("ResourcePackage")
        @JvmStatic
        val ITEM_VIEW_UPLOAD = R.layout.upload_prescription
    }

    fun bindViewHolder(cartShipmentAddressFormData: CartShipmentAddressFormData){

        when{
            cartShipmentAddressFormData.uploadPrescText.isEmpty() -> {
                uploadPrescButton.gone()
            }
            !cartShipmentAddressFormData.showImageUpload -> {
                uploadPrescButton.gone()
            }
            else -> {
                uploadPrescButton.visible()
                uploadPrescText.text = cartShipmentAddressFormData.uploadPrescText
                uploadPrescIcon.loadImage(cartShipmentAddressFormData.leftIconUrl)
            }
        }
        uploadPrescButton.setOnClickListener {
            actionListener.uploadPrescriptionAction(cartShipmentAddressFormData)
        }
    }
}