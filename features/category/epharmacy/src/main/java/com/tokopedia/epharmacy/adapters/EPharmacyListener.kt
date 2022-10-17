package com.tokopedia.epharmacy.adapters

import com.tokopedia.epharmacy.network.response.PrescriptionImage

interface EPharmacyListener {

    /**
     * Prescription Image Gallery
     */

    fun onPrescriptionCrossImageClick(adapterPosition: Int)

    fun onPrescriptionReLoadButtonClick(adapterPosition: Int , image : PrescriptionImage)

    fun onPrescriptionImageClick(adapterPosition: Int, image: PrescriptionImage)

    fun onCameraClick()

}