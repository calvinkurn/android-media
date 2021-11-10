package com.tokopedia.tokopedianow.searchcategory.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.*
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.common.viewholder.*
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BannerViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.CategoryFilterViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.model.*
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProgressBarDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.LoadingMoreViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductCountViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.QuickFilterViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TitleViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProgressBarViewHolder

abstract class BaseSearchCategoryTypeFactoryImpl(
    protected val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener,
    protected val chooseAddressListener: ChooseAddressListener,
    protected val titleListener: TitleListener,
    protected val bannerListener: BannerComponentListener,
    protected val quickFilterListener: QuickFilterListener,
    protected val categoryFilterListener: CategoryFilterListener,
    protected val productItemListener: ProductItemListener,
    protected val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    private val recommendationCarouselListener: TokoNowRecommendationCarouselViewHolder.TokoNowRecommendationCarouselListener,
    private val recommendationCarouselBindPageNameListener: TokoNowRecommendationCarouselViewHolder.TokonowRecomBindPageNameListener?
):  BaseAdapterTypeFactory(),
    BaseSearchCategoryTypeFactory,
    TokoNowEmptyStateNoResultTypeFactory,
    TokoNowRecommendationCarouselTypeFactory,
    TokoNowCategoryGridTypeFactory,
    TokoNowRepurchaseTypeFactory,
    TokoNowEmptyStateOocTypeFactory{

    override fun type(chooseAddressDataView: ChooseAddressDataView) = BaseChooseAddressViewHolder.LAYOUT

    override fun type(bannerDataView: BannerDataView) = BannerViewHolder.LAYOUT

    override fun type(titleDataView: TitleDataView) = TitleViewHolder.LAYOUT

    override fun type(categoryFilterDataView: CategoryFilterDataView) = CategoryFilterViewHolder.LAYOUT

    override fun type(quickFilterDataView: QuickFilterDataView) = QuickFilterViewHolder.LAYOUT

    override fun type(productCountDataView: ProductCountDataView) = ProductCountViewHolder.LAYOUT

    override fun type(productItemDataView: ProductItemDataView) = ProductItemViewHolder.LAYOUT

    override fun type(viewModel: LoadingMoreModel) = LoadingMoreViewHolder.LAYOUT

    override fun type(uiModel: TokoNowEmptyStateNoResultUiModel) = TokoNowEmptyStateNoResultViewHolder.LAYOUT

    override fun type(uiModel: TokoNowRepurchaseUiModel): Int = TokoNowRepurchaseViewHolder.LAYOUT

    override fun type(uiModel: TokoNowCategoryGridUiModel): Int = TokoNowCategoryGridViewHolder.LAYOUT

    override fun type(uiModel: TokoNowRecommendationCarouselUiModel): Int = TokoNowRecommendationCarouselViewHolder.LAYOUT

    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT

    override fun type(progressBarDataView: ProgressBarDataView): Int = ProgressBarViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(
                itemView = view,
                productItemListener = productItemListener
            )
            BannerViewHolder.LAYOUT -> BannerViewHolder(
                itemView = view,
                bannerListener = bannerListener
            )
            TitleViewHolder.LAYOUT -> TitleViewHolder(
                itemView = view,
                titleListener = titleListener
            )
            CategoryFilterViewHolder.LAYOUT -> CategoryFilterViewHolder(
                itemView = view,
                categoryFilterListener = categoryFilterListener
            )
            QuickFilterViewHolder.LAYOUT -> QuickFilterViewHolder(
                itemView = view,
                quickFilterListener = quickFilterListener
            )
            ProductCountViewHolder.LAYOUT -> ProductCountViewHolder(
                itemView = view
            )
            LoadingMoreViewHolder.LAYOUT -> LoadingMoreViewHolder(
                itemView = view
            )
            TokoNowEmptyStateNoResultViewHolder.LAYOUT -> TokoNowEmptyStateNoResultViewHolder(
                itemView = view,
                tokoNowEmptyStateNoResultListener = tokoNowEmptyStateNoResultListener
            )
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(
                itemView = view,
                listener = tokoNowEmptyStateOocListener
            )
            TokoNowRecommendationCarouselViewHolder.LAYOUT -> TokoNowRecommendationCarouselViewHolder(
                itemView = view,
                recommendationCarouselListener = recommendationCarouselListener,
                recommendationCarouselWidgetBindPageNameListener = recommendationCarouselBindPageNameListener
            )
            ProgressBarViewHolder.LAYOUT -> ProgressBarViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}