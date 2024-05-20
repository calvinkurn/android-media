package com.tkpd.atcvariant.view.viewmodel

import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.aggregator.SimpleBasicInfo
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

interface GetVariantDataMediator {
    fun getVariantData(): ProductVariant?
    fun getSelectedOptionIds(): MutableMap<String, String>?
    fun getActivityResultData(): ProductVariantResult
    fun getBasicInfo(): SimpleBasicInfo?
}
