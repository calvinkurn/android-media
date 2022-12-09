package com.tokopedia.tokopedianow.searchcategory.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryGridTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateNoResultTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRecommendationCarouselTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRepurchaseTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProgressBarDataView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRecommendationCarouselViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRepurchaseViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SwitcherWidgetDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BannerViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.CategoryFilterViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductCountViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProgressBarViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.QuickFilterViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.SwitcherWidgetViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TitleViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder


abstract class BaseSearchCategoryTypeFactoryImpl(
    protected val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener,
    protected val chooseAddressListener: ChooseAddressListener,
    protected val titleListener: TitleListener,
    protected val bannerListener: BannerComponentListener,
    protected val quickFilterListener: QuickFilterListener,
    protected val categoryFilterListener: CategoryFilterListener,
    protected val productItemListener: ProductItemListener,
    protected val switcherWidgetListener: SwitcherWidgetListener,
    protected val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    private val recommendationCarouselListener: TokoNowRecommendationCarouselViewHolder.TokoNowRecommendationCarouselListener,
    private val recommendationCarouselBindPageNameListener: TokoNowRecommendationCarouselViewHolder.TokonowRecomBindPageNameListener?,
    private val feedbackWidgetListener: TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener
):  BaseAdapterTypeFactory(),
    BaseSearchCategoryTypeFactory,
    TokoNowEmptyStateNoResultTypeFactory,
    TokoNowRecommendationCarouselTypeFactory,
    TokoNowCategoryGridTypeFactory,
    TokoNowRepurchaseTypeFactory,
    TokoNowEmptyStateOocTypeFactory,
    TokoNowFeedbackWidgetTypeFactory {

    override fun type(chooseAddressDataView: ChooseAddressDataView) = BaseChooseAddressViewHolder.LAYOUT

    override fun type(bannerDataView: BannerDataView) = BannerViewHolder.LAYOUT

    override fun type(titleDataView: TitleDataView) = TitleViewHolder.LAYOUT

    override fun type(categoryFilterDataView: CategoryFilterDataView) = CategoryFilterViewHolder.LAYOUT

    override fun type(quickFilterDataView: QuickFilterDataView) = QuickFilterViewHolder.LAYOUT

    override fun type(productCountDataView: ProductCountDataView) = ProductCountViewHolder.LAYOUT

    override fun type(productItemDataView: ProductItemDataView) = ProductItemViewHolder.LAYOUT

    override fun type(viewModel: LoadingMoreModel) = TokoNowLoadingMoreViewHolder.LAYOUT

    override fun type(uiModel: TokoNowEmptyStateNoResultUiModel) = TokoNowEmptyStateNoResultViewHolder.LAYOUT

    override fun type(uiModel: TokoNowRepurchaseUiModel): Int = TokoNowRepurchaseViewHolder.LAYOUT

    override fun type(uiModel: TokoNowCategoryGridUiModel): Int = TokoNowCategoryGridViewHolder.LAYOUT

    override fun type(uiModel: TokoNowRecommendationCarouselUiModel): Int = TokoNowRecommendationCarouselViewHolder.LAYOUT

    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT

    override fun type(progressBarDataView: ProgressBarDataView): Int = ProgressBarViewHolder.LAYOUT

    override fun type(switcherWidgetDataView: SwitcherWidgetDataView): Int = SwitcherWidgetViewHolder.LAYOUT

    override fun type(uiModel: TokoNowFeedbackWidgetUiModel): Int = TokoNowFeedbackWidgetViewHolder.LAYOUT

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
            TokoNowLoadingMoreViewHolder.LAYOUT -> TokoNowLoadingMoreViewHolder(
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
            SwitcherWidgetViewHolder.LAYOUT -> SwitcherWidgetViewHolder(
                itemView = view,
                listener = switcherWidgetListener
            )
            ProgressBarViewHolder.LAYOUT -> ProgressBarViewHolder(view)
            TokoNowFeedbackWidgetViewHolder.LAYOUT -> TokoNowFeedbackWidgetViewHolder(
                itemView = view,
                listener = feedbackWidgetListener
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
