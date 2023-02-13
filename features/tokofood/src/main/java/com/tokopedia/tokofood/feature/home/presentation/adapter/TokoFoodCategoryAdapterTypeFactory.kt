package com.tokopedia.tokofood.feature.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.common.presentation.adapter.TokoFoodCategoryTypeFactory
import com.tokopedia.tokofood.common.presentation.adapter.TokoFoodCommonTypeFactory
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodCategoryEmptyStateViewHolder
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodCategoryLoadingViewHolder
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodMerchantListViewHolder
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodProgressBarViewHolder
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryEmptyStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel

class TokoFoodCategoryAdapterTypeFactory(
    private val merchantListListener: TokoFoodMerchantListViewHolder.TokoFoodMerchantListListener? = null,
    private val errorStateListener: TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener? = null,
    private val tokofoodScrollChangedListener: TokofoodScrollChangedListener
):  BaseAdapterTypeFactory(),
    TokoFoodCategoryTypeFactory,
    TokoFoodMerchantListTypeFactory,
    TokoFoodCommonTypeFactory {

    override fun type(uiModel: TokoFoodCategoryLoadingStateUiModel): Int = TokoFoodCategoryLoadingViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodCategoryEmptyStateUiModel): Int = TokoFoodCategoryEmptyStateViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodMerchantListUiModel): Int = TokoFoodMerchantListViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodProgressBarUiModel): Int = TokoFoodProgressBarViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodErrorStateUiModel): Int = TokoFoodErrorStateViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            TokoFoodCategoryLoadingViewHolder.LAYOUT -> TokoFoodCategoryLoadingViewHolder(view)
            TokoFoodMerchantListViewHolder.LAYOUT -> TokoFoodMerchantListViewHolder(view, merchantListListener, tokofoodScrollChangedListener)
            TokoFoodProgressBarViewHolder.LAYOUT -> TokoFoodProgressBarViewHolder(view)
            TokoFoodErrorStateViewHolder.LAYOUT -> TokoFoodErrorStateViewHolder(view, errorStateListener)
            TokoFoodCategoryLoadingViewHolder.LAYOUT -> TokoFoodCategoryLoadingViewHolder(view)
            TokoFoodCategoryEmptyStateViewHolder.LAYOUT -> TokoFoodCategoryEmptyStateViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
