package com.tokopedia.epharmacy.component.viewholder

import android.util.Base64
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.network.response.PrescriptionImage
import com.tokopedia.epharmacy.utils.EPharmacyPrescriptionStatus
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify


class EPharmacyPrescriptionGalleryItemViewHolder(private val viewItem: View) : RecyclerView.ViewHolder(viewItem) {

    private var cardView = viewItem.findViewById<CardUnify2>(R.id.card_view)
    private var reloadIcon = viewItem.findViewById<IconUnify>(R.id.retry_upload_button)
    private var overlay = viewItem.findViewById<ImageUnify>(R.id.overlay)
    private var cameraIcon = viewItem.findViewById<ImageUnify>(R.id.camera_button)
    private var cancelButton = viewItem.findViewById<ImageUnify>(R.id.cross_image_button)
    private var image = viewItem.findViewById<ImageUnify>(R.id.prescription_image)
    private var loader = viewItem.findViewById<LoaderUnify>(R.id. prescription_loader)

    private var actionListener: EPharmacyListener? = null

    fun bind(model: PrescriptionImage?, ePharmacyListener: EPharmacyListener?) {
        actionListener = ePharmacyListener
        model?.let {
            renderData(model,ePharmacyListener)
        } ?: kotlin.run {
            cardView.cardType = CardUnify2.TYPE_BORDER
            cardView.setOnClickListener {
                actionListener?.onCameraClick()
            }
            Glide.with(itemView.context)
                .load(MethodChecker.getDrawable(itemView.context,R.drawable.epharmacy_rectangle))
                .into(image)
            cameraIcon.show()
            reloadIcon.hide()
            overlay.hide()
            cancelButton.hide()
        }
    }

    private fun renderData(model: PrescriptionImage, ePharmacyListener: EPharmacyListener?) {
        cardView.cardType = CardUnify2.TYPE_CLEAR
        cardView.setOnClickListener(null)
        cameraIcon.hide()

        renderImageUi(model)
        renderReloadUi(model)
        renderLoaderUi(model)
        renderCancelUi(model)

    }

    private fun renderImageUi(model: PrescriptionImage) {
        if(!model.prescriptionData?.value.isNullOrBlank()){
            val imgByteArray: ByteArray = Base64.decode(model.prescriptionData?.value, Base64.DEFAULT)
            Glide.with(viewItem.context)
                .asBitmap()
                .load(imgByteArray)
                .into(image)
        }else {
            model.localPath?.let {
                image.loadImage(it)
            }
        }
    }

    private fun renderCancelUi(model: PrescriptionImage) {
        if(model.status == EPharmacyPrescriptionStatus.APPROVED.status){
            cancelButton.hide()
        }else {
            cancelButton.show()
            cancelButton.setOnClickListener {
                actionListener?.onPrescriptionCrossImageClick(adapterPosition)
            }
        }
    }

    private fun renderLoaderUi(model: PrescriptionImage) {
        if(model.isUploading){
            loader.show()
        }else {
            loader.hide()
        }
    }

    private fun renderReloadUi(model: PrescriptionImage) {
        if(model.isUploadFailed){
            reloadIcon.show()
            overlay.show()
            reloadIcon.setOnClickListener {
                actionListener?.onPrescriptionReLoadButtonClick(adapterPosition,model)
            }
        }else {
            overlay.hide()
            reloadIcon.hide()
        }
    }
}