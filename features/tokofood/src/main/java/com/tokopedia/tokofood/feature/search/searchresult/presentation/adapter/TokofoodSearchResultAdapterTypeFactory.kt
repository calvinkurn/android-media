package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.common.presentation.adapter.TokoFoodCategoryTypeFactory
import com.tokopedia.tokofood.common.presentation.adapter.TokoFoodCommonTypeFactory
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodCategoryEmptyStateViewHolder
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodCategoryLoadingViewHolder
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodProgressBarViewHolder
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryEmptyStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchEmptyWithFilterViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchEmptyWithoutFilterViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchResultViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithoutFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchResultUiModel

class TokofoodSearchResultAdapterTypeFactory(
    private val merchantListListener: MerchantSearchResultViewHolder.TokoFoodMerchantSearchResultListener? = null,
    private val errorStateListener: TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener? = null,
    private val emptyStateWithFilterListener: MerchantSearchEmptyWithFilterViewHolder.Listener,
    private val emptyStateWithoutFilterListener: MerchantSearchEmptyWithoutFilterViewHolder.Listener
) : BaseAdapterTypeFactory(),
    TokoFoodCategoryTypeFactory,
    TokoFoodCommonTypeFactory,
    TokofoodSearchResultTypeFactory {

    override fun type(uiModel: TokoFoodCategoryLoadingStateUiModel): Int = TokoFoodCategoryLoadingViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodCategoryEmptyStateUiModel): Int = TokoFoodCategoryEmptyStateViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodProgressBarUiModel): Int = TokoFoodProgressBarViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodErrorStateUiModel): Int = TokoFoodErrorStateViewHolder.LAYOUT
    override fun type(uiModel: MerchantSearchResultUiModel): Int = MerchantSearchResultViewHolder.LAYOUT
    override fun type(uiModel: MerchantSearchEmptyWithFilterUiModel): Int = MerchantSearchEmptyWithFilterViewHolder.LAYOUT
    override fun type(uiModel: MerchantSearchEmptyWithoutFilterUiModel): Int = MerchantSearchEmptyWithoutFilterViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            MerchantSearchResultViewHolder.LAYOUT -> MerchantSearchResultViewHolder(view, merchantListListener)
            TokoFoodProgressBarViewHolder.LAYOUT -> TokoFoodProgressBarViewHolder(view)
            TokoFoodErrorStateViewHolder.LAYOUT -> TokoFoodErrorStateViewHolder(view, errorStateListener)
            TokoFoodCategoryLoadingViewHolder.LAYOUT -> TokoFoodCategoryLoadingViewHolder(view)
            TokoFoodCategoryEmptyStateViewHolder.LAYOUT -> TokoFoodCategoryEmptyStateViewHolder(view)
            MerchantSearchEmptyWithFilterViewHolder.LAYOUT -> MerchantSearchEmptyWithFilterViewHolder(view, emptyStateWithFilterListener)
            MerchantSearchEmptyWithoutFilterViewHolder.LAYOUT -> MerchantSearchEmptyWithoutFilterViewHolder(view, emptyStateWithoutFilterListener)
            else -> super.createViewHolder(view, type)
        }
    }
}