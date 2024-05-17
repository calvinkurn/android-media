package com.tokopedia.checkout.backup.view.uimodel

import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel

data class CheckoutEpharmacyModel(
    override val cartStringGroup: String = "",
    val epharmacy: UploadPrescriptionUiModel
) : CheckoutItem
