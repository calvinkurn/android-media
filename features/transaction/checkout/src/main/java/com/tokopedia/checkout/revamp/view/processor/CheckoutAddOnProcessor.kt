package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCaseCoroutine
import javax.inject.Inject

class CheckoutAddOnProcessor @Inject constructor(
    private val prescriptionIdsUseCase: GetPrescriptionIdsUseCaseCoroutine,
    private val epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase,
    private val saveAddOnProductUseCase: SaveAddOnStateUseCase
)
