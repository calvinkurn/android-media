package com.tokopedia.tokofood.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodCategoryLoadingViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodMerchantListViewHolder
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodView

class TokoFoodCategoryAdapterTypeFactory(
    private val tokoFoodView: TokoFoodView? = null,
):  BaseAdapterTypeFactory(),
    TokoFoodCategoryTypeFactory,
    TokoFoodMerchantListTypeFactory {

    override fun type(uiModel: TokoFoodCategoryLoadingStateUiModel): Int = TokoFoodCategoryLoadingViewHolder.LAYOUT

    override fun type(uiModel: TokoFoodMerchantListUiModel): Int = TokoFoodMerchantListViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            TokoFoodCategoryLoadingViewHolder.LAYOUT -> TokoFoodCategoryLoadingViewHolder(view)
            TokoFoodMerchantListViewHolder.LAYOUT -> TokoFoodMerchantListViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}