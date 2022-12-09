package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

data class TitleDataView(
        val titleType: TitleType,
        val hasSeeAllCategoryButton: Boolean = false,
        val chooseAddressData: LocalCacheModel?
): Visitable<BaseSearchCategoryTypeFactory> {
    companion object {
        private const val NO_WAREHOUSE_ID = 0L
    }

    val serviceType: String
        get() = chooseAddressData?.service_type.orEmpty()

    val is15mAvailable: Boolean
        get() = chooseAddressData?.warehouses?.find { it.service_type == ServiceType.NOW_15M }?.warehouse_id.orZero() != NO_WAREHOUSE_ID

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this).orZero()
}

sealed class TitleType
data class CategoryTitle(val categoryName: String): TitleType()
object SearchTitle : TitleType()
object AllProductTitle : TitleType()
