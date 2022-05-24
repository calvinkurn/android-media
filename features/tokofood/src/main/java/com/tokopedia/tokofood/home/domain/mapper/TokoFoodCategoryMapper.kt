package com.tokopedia.tokofood.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeStaticLayoutId
import com.tokopedia.tokofood.home.domain.data.Merchant
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodMerchantListUiModel

object TokoFoodCategoryMapper {

    fun MutableList<Visitable<*>>.addLoadingCategoryIntoList() {
        val loadingLayout = TokoFoodCategoryLoadingStateUiModel(id = TokoFoodHomeStaticLayoutId.LOADING_STATE)
        add(loadingLayout)
    }

    fun MutableList<Visitable<*>>.mapCategoryLayoutList(
        responses: List<Merchant>
    ){
        responses.forEach {
            val merchant = TokoFoodMerchantListUiModel(it.id, it)
            add(merchant)
        }
    }
}