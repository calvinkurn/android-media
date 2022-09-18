package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isOdd
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class ManageProductNonVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main){

    fun dummy(){
        println("asdas")
    }

    fun validateInput(
        criteria: ProductCriteria,
        discountSetup: DiscountSetup
    ): ValidationResult {
        return ValidationResult(
            Random().nextInt().isOdd(),
            Random().nextInt().isOdd(),
            Random().nextInt().isOdd(),
        )
    }
}