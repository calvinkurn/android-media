package com.tokopedia.epharmacy.adapters

import com.tokopedia.epharmacy.network.response.PrescriptionImage

interface EPharmacyListener {

    /**
     * Prescription Image Gallery
     */

    fun onPrescriptionCrossImageClick(adapterPosition: Int) {}

    fun onPrescriptionReLoadButtonClick(adapterPosition: Int, image: PrescriptionImage) {}

    fun onPrescriptionImageClick(adapterPosition: Int, image: PrescriptionImage) {}

    fun onCameraClick() {}

    /**
     * Attachment Accordion
     */

    fun onInteractAccordion(adapterPosition: Int, isExpanded: Boolean, modelKey: String?) {}

    fun onCTACClick(adapterPosition: Int, modelKey: String?) {}

    fun onError(adapterPosition: Int, modelKey: String?) {}

    fun onEndAnimation(adapterPosition: Int, modelKey: String?) {}

    fun onToast(toasterType: Int, message: String) {}
}
