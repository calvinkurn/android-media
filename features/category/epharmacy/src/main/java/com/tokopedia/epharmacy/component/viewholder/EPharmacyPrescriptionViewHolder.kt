package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyGalleryAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyImagesDecoration
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.utils.MAX_MEDIA_ITEM
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class EPharmacyPrescriptionViewHolder(private val view: View,
                                      private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyPrescriptionDataModel>(view) {

    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    companion object {
        val LAYOUT = R.layout.epharmacy_prescription_view_item
    }

    override fun bind(element: EPharmacyPrescriptionDataModel) {
        render(element)
    }

    private fun render(dataModel: EPharmacyPrescriptionDataModel) {
        val galleryRV = view.findViewById<RecyclerView>(R.id.gallery_rv)
        galleryRV.layoutManager = layoutManager
        val photoResepTitle = view.findViewById<Typography>(R.id.foto_resep_title)
        dataModel.prescriptions?.let { safePrescriptionArray ->
            if(safePrescriptionArray.isNullOrEmpty()){
                galleryRV.hide()
                photoResepTitle.hide()
            }else {
                galleryRV.show()
                photoResepTitle.show()
                if(galleryRV.itemDecorationCount == 0)
                    galleryRV.addItemDecoration(EPharmacyImagesDecoration())
                galleryRV.adapter =  EPharmacyGalleryAdapter(safePrescriptionArray,
                    ePharmacyListener,dataModel.isReUpload ,hasCameraButton(dataModel.isReUpload,
                        (safePrescriptionArray.size < (MAX_MEDIA_ITEM))))
            }
        }
    }

    private fun hasCameraButton(showCamera: Boolean , safeSizeBoolean : Boolean) : Boolean{
        return if(!showCamera){
            false
        }else {
            safeSizeBoolean
        }
    }
}