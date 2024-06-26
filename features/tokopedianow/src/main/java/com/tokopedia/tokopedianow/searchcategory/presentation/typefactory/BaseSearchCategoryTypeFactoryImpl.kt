package com.tokopedia.tokopedianow.searchcategory.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.tokopedianow.common.adapter.oldrepurchase.TokoNowRepurchaseTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowAdsCarouselTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateNoResultTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductItemTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTickerTypeFactory
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.model.oldrepurchase.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowAdsCarouselViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTickerViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowRepurchaseViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProgressBarDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BannerViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.CategoryFilterViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductCountViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProgressBarViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.QuickFilterViewHolder
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
    private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    private val tokoNowEmptyStateNoResultTrackerListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultTrackerListener? = null,
    private val feedbackWidgetListener: TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener,
    private val productCardCompactListener: ProductCardCompactView.ProductCardCompactListener,
    private val productCardCompactSimilarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener,
    private val productRecommendationBindOocListener: TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener,
    private val productRecommendationOocListener: TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener,
    private val productRecommendationListener: TokoNowProductRecommendationView.TokoNowProductRecommendationListener?,
    private val productAdsCarouselListener: ProductAdsCarouselListener? = null
) : BaseAdapterTypeFactory(),
    BaseSearchCategoryTypeFactory,
    TokoNowEmptyStateNoResultTypeFactory,
    TokoNowProductRecommendationOocTypeFactory,
    TokoNowCategoryMenuTypeFactory,
    TokoNowRepurchaseTypeFactory,
    TokoNowEmptyStateOocTypeFactory,
    TokoNowFeedbackWidgetTypeFactory,
    TokoNowProductRecommendationTypeFactory ,
    TokoNowTickerTypeFactory,
    TokoNowAdsCarouselTypeFactory,
    TokoNowProductItemTypeFactory
{

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

    override fun type(uiModel: TokoNowCategoryMenuUiModel): Int = TokoNowCategoryMenuViewHolder.LAYOUT

    override fun type(uiModel: TokoNowProductRecommendationOocUiModel): Int = TokoNowProductRecommendationOocViewHolder.LAYOUT

    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT

    override fun type(progressBarDataView: ProgressBarDataView): Int = ProgressBarViewHolder.LAYOUT

    override fun type(uiModel: TokoNowFeedbackWidgetUiModel): Int = TokoNowFeedbackWidgetViewHolder.LAYOUT

    override fun type(uiModel: TokoNowProductRecommendationUiModel): Int = TokoNowProductRecommendationViewHolder.LAYOUT

    override fun type(uiModel: TokoNowTickerUiModel): Int  = TokoNowTickerViewHolder.LAYOUT

    override fun type(uiModel: TokoNowAdsCarouselUiModel): Int = TokoNowAdsCarouselViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(
                itemView = view,
                listener = productItemListener,
                productCardCompactListener = productCardCompactListener,
                similarProductTrackerListener = productCardCompactSimilarProductTrackerListener
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
                tokoNowEmptyStateNoResultListener  = tokoNowEmptyStateNoResultListener,
                tokoNowEmptyStateNoResultTrackerListener = tokoNowEmptyStateNoResultTrackerListener
            )
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(
                itemView = view,
                listener = tokoNowEmptyStateOocListener
            )
            TokoNowProductRecommendationOocViewHolder.LAYOUT -> TokoNowProductRecommendationOocViewHolder(
                itemView = view,
                recommendationCarouselListener = productRecommendationOocListener,
                recommendationCarouselWidgetBindPageNameListener = productRecommendationBindOocListener
            )
            TokoNowProductRecommendationViewHolder.LAYOUT -> TokoNowProductRecommendationViewHolder(
                itemView = view,
                listener = productRecommendationListener
            )
            ProgressBarViewHolder.LAYOUT -> ProgressBarViewHolder(view)
            TokoNowFeedbackWidgetViewHolder.LAYOUT -> TokoNowFeedbackWidgetViewHolder(
                itemView = view,
                listener = feedbackWidgetListener
            )
            TokoNowTickerViewHolder.LAYOUT -> TokoNowTickerViewHolder(
                itemView = view
            )
            TokoNowAdsCarouselViewHolder.LAYOUT -> TokoNowAdsCarouselViewHolder(
                itemView = view,
                listener = productAdsCarouselListener
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
