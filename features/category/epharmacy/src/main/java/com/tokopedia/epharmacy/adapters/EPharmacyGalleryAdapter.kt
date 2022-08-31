package com.tokopedia.epharmacy.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.viewholder.EPharmacyPrescriptionGalleryItemViewHolder
import com.tokopedia.epharmacy.network.response.PrescriptionImage

class EPharmacyGalleryAdapter (
    private val list : ArrayList<PrescriptionImage?>,
    private val ePharmacyListener: EPharmacyListener?,
    private val isReUpload : Boolean = false,
    private val hasCameraButton : Boolean = false)
    : RecyclerView.Adapter<EPharmacyPrescriptionGalleryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EPharmacyPrescriptionGalleryItemViewHolder {
        return EPharmacyPrescriptionGalleryItemViewHolder(View.inflate(parent.context, R.layout.epharmacy_prescription_gallery_view_item, null))
    }

    override fun getItemCount(): Int  = if(hasCameraButton) {
            (list.size + 1)
    }else { (list.size) }

    override fun onBindViewHolder(holderEPharmacyPrescription: EPharmacyPrescriptionGalleryItemViewHolder, position: Int) {
        if(hasCameraButton && position == (list.size)){
            holderEPharmacyPrescription.bind(null,ePharmacyListener)
        }else {
            list[position]?.isDeletable = isReUpload
            holderEPharmacyPrescription.bind(list[position],ePharmacyListener)
        }
    }
}