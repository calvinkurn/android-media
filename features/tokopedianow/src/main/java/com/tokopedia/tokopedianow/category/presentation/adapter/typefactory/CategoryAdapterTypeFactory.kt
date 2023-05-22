package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryHeaderSpaceViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryNavigationViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryNavigationViewHolder.CategoryNavigationListener
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder.CategoryShowcaseItemListener
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryTitleViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryTitleViewHolder.CategoryTitleListener
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProgressBarTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProgressBarViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener

class CategoryAdapterTypeFactory(
    private val categoryTitleListener: CategoryTitleListener? = null,
    private val categoryNavigationListener: CategoryNavigationListener? = null,
    private val categoryShowcaseItemListener: CategoryShowcaseItemListener? = null,
    private val categoryShowcaseHeaderListener: TokoNowDynamicHeaderListener? = null,
    private val tokoNowView: TokoNowView? = null,
    private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetListener? = null,
    private val tokoNowCategoryMenuListener: TokoNowCategoryMenuListener? = null,
    private val tokoNowProductRecommendationListener: TokoNowProductRecommendationListener? = null
): BaseAdapterTypeFactory(),
    CategoryTypeFactory,
    TokoNowChooseAddressWidgetTypeFactory,
    TokoNowCategoryMenuTypeFactory,
    TokoNowProductRecommendationTypeFactory,
    TokoNowProgressBarTypeFactory
{
    /* Category Component Ui Model */
    override fun type(uiModel: CategoryHeaderSpaceUiModel): Int = CategoryHeaderSpaceViewHolder.LAYOUT
    override fun type(uiModel: CategoryTitleUiModel): Int = CategoryTitleViewHolder.LAYOUT
    override fun type(uiModel: CategoryNavigationUiModel): Int = CategoryNavigationViewHolder.LAYOUT
    override fun type(uiModel: CategoryShowcaseUiModel): Int = CategoryShowcaseViewHolder.LAYOUT

    /* Common Component Ui Model */
    override fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int = TokoNowChooseAddressWidgetViewHolder.LAYOUT
    override fun type(uiModel: TokoNowCategoryMenuUiModel): Int = TokoNowCategoryMenuViewHolder.LAYOUT
    override fun type(uiModel: TokoNowProductRecommendationUiModel): Int = TokoNowProductRecommendationViewHolder.LAYOUT
    override fun type(uiModel: TokoNowProgressBarUiModel): Int = TokoNowProgressBarViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            /* Category Component View Holder */
            CategoryHeaderSpaceViewHolder.LAYOUT -> CategoryHeaderSpaceViewHolder(
                itemView = view
            )
            CategoryTitleViewHolder.LAYOUT -> CategoryTitleViewHolder(
                itemView = view,
                listener = categoryTitleListener
            )
            CategoryNavigationViewHolder.LAYOUT -> CategoryNavigationViewHolder(
                itemView = view,
                listener = categoryNavigationListener
            )
            CategoryShowcaseViewHolder.LAYOUT -> CategoryShowcaseViewHolder(
                itemView = view,
                categoryShowcaseItemListener = categoryShowcaseItemListener,
                categoryShowcaseHeaderListener = categoryShowcaseHeaderListener
            )

            /* Common Component View Holder */
            TokoNowChooseAddressWidgetViewHolder.LAYOUT -> TokoNowChooseAddressWidgetViewHolder(
                itemView = view,
                tokoNowView = tokoNowView,
                tokoNowChooseAddressWidgetListener = tokoNowChooseAddressWidgetListener
            )
            TokoNowCategoryMenuViewHolder.LAYOUT -> TokoNowCategoryMenuViewHolder(
                itemView = view,
                listener = tokoNowCategoryMenuListener
            )
            TokoNowProductRecommendationViewHolder.LAYOUT -> TokoNowProductRecommendationViewHolder(
                itemView = view,
                listener = tokoNowProductRecommendationListener
            )
            TokoNowProgressBarViewHolder.LAYOUT -> TokoNowProgressBarViewHolder(
                itemView = view
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
