package com.tokopedia.epharmacy.adapters

import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.network.response.PrescriptionImage

interface EPharmacyListener {

    /**
     * Prescription Image Gallery
     */

    fun onPrescriptionCrossImageClick(adapterPosition: Int){}

    fun onPrescriptionReLoadButtonClick(adapterPosition: Int , image : PrescriptionImage){}

    fun onPrescriptionImageClick(adapterPosition: Int, image: PrescriptionImage){}

    fun onCameraClick(){}

    /**
     * Attachment Accordion
     */

    fun onInteractAccordion(adapterPosition: Int, isExpanded: Boolean, modelKey: String?){}

    fun onCTACClick(adapterPosition: Int, modelKey: String?){}
}
