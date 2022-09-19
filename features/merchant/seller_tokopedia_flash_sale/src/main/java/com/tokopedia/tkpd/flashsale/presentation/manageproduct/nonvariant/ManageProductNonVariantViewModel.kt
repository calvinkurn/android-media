package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isOdd
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import java.util.*
import javax.inject.Inject

class ManageProductNonVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main){

    private val _isMultiloc: MutableLiveData<Boolean> = MutableLiveData()
    val isMultiloc: LiveData<Boolean> get() = _isMultiloc

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

    fun checkMultiloc(product: ReservedProduct.Product) {
        _isMultiloc.value = product.isMultiWarehouse
    }
}