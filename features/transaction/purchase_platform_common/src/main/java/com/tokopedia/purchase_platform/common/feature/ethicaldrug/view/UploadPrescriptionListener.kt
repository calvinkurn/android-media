package com.tokopedia.purchase_platform.common.feature.ethicaldrug.view

import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel

interface UploadPrescriptionListener {

    fun uploadPrescriptionAction(
        uploadPrescriptionUiModel: UploadPrescriptionUiModel,
        buttonText: String,
        buttonNotes: String
    )
}
