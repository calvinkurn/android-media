package com.tokopedia.product.addedit.variant.presentation.dialog

import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import java.math.BigInteger

interface MultipleVariantEditListener {
    fun onMultipleEditInputFinished(multipleVariantEditInputModel: MultipleVariantEditInputModel)
    fun onMultipleEditInputValidatePrice(price: BigInteger): String
    fun onMultipleEditInputValidateStock(stock: BigInteger): String
    fun onMultipleEditInputValidateWeight(weight: BigInteger): String
}