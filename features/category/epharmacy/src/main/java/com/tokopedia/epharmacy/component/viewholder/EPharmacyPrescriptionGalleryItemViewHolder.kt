package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.network.response.PrescriptionImage
import com.tokopedia.epharmacy.utils.GALLERY_IMAGE_VIEW_TYPE
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64


class EPharmacyPrescriptionGalleryItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private var cardView = v.findViewById<CardUnify2>(R.id.card_view)
    private var reloadIcon = v.findViewById<IconUnify>(R.id.retry_upload_button)
    private var cameraIcon = v.findViewById<IconUnify>(R.id.camera_button)
    private var cancelButton = v.findViewById<IconUnify>(R.id.cross_image_button)
    private var image = v.findViewById<ImageUnify>(R.id.image)

    fun bind(model: PrescriptionImage?, ePharmacyListener: EPharmacyListener?) {
        model?.let {
            renderData(model,ePharmacyListener)
        } ?: kotlin.run {
            cardView.cardType = CardUnify2.TYPE_BORDER
            cardView.setOnClickListener {
                ePharmacyListener?.onCameraClick()
            }
            cameraIcon.show()
            reloadIcon.hide()
            cancelButton.hide()
        }
    }

    private fun renderData(model: PrescriptionImage,ePharmacyListener: EPharmacyListener?) {
        cardView.cardType = CardUnify2.TYPE_CLEAR
        cardView.setOnClickListener(null)

        cameraIcon.hide()

        model.data?.let {
//            val decodedString: ByteArray = Base64.decode(it, Base64.DEFAULT)
//            val decodedByte: Bitmap =
//                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//            image.loadImage(decodedByte)
        }

        model.localPath.let {
            image.loadImage(it)
        }

        if(model.isUploadFailed){
            reloadIcon.show()
            reloadIcon.setOnClickListener {
                ePharmacyListener?.onPrescriptionReLoadButtonClick(adapterPosition,model)
            }
        }else {
            reloadIcon.hide()
        }

        cancelButton.show()
        cancelButton.setOnClickListener {
            ePharmacyListener?.onPrescriptionCrossImageClick(adapterPosition)
        }
    }
}