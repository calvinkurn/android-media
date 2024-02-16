package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryNavigationViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryNavigationViewHolder.CategoryNavigationListener
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder.CategoryShowcaseItemListener
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowThematicHeaderViewHolder.TokoNowHeaderListener
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProgressBarTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTickerTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowThematicHeaderTypeFactory
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowThematicHeaderViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProgressBarViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTickerViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener

class CategoryAdapterTypeFactory(
    private var categoryNavigationListener: CategoryNavigationListener? = null,
    private var categoryShowcaseItemListener: CategoryShowcaseItemListener? = null,
    private var categoryShowcaseHeaderListener: TokoNowDynamicHeaderListener? = null,
    private var tokoNowCategoryMenuListener: TokoNowCategoryMenuListener? = null,
    private var tokoNowProductRecommendationListener: TokoNowProductRecommendationListener? = null,
    private var tokoNowHeaderListener: TokoNowHeaderListener? = null,
    private var recycledViewPool: RecyclerView.RecycledViewPool? = null,
    private val lifecycleOwner: LifecycleOwner? = null,
    tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetListener,
    productAdsCarouselListener: ProductAdsCarouselListener,
    tokoNowView: TokoNowView
) : BaseCategoryAdapterTypeFactory(
    tokoNowChooseAddressWidgetListener,
    productAdsCarouselListener,
    tokoNowView
), CategoryTypeFactory,
    TokoNowThematicHeaderTypeFactory,
    TokoNowCategoryMenuTypeFactory,
    TokoNowProductRecommendationTypeFactory,
    TokoNowProgressBarTypeFactory,
    TokoNowTickerTypeFactory {

    /* Category Component Ui Model */
    override fun type(uiModel: CategoryNavigationUiModel): Int = CategoryNavigationViewHolder.LAYOUT
    override fun type(uiModel: CategoryShowcaseUiModel): Int = CategoryShowcaseViewHolder.LAYOUT

    /* Common Component Ui Model */
    override fun type(uiModel: TokoNowCategoryMenuUiModel): Int = TokoNowCategoryMenuViewHolder.LAYOUT
    override fun type(uiModel: TokoNowProductRecommendationUiModel): Int = TokoNowProductRecommendationViewHolder.LAYOUT
    override fun type(uiModel: TokoNowProgressBarUiModel): Int = TokoNowProgressBarViewHolder.LAYOUT
    override fun type(uiModel: TokoNowTickerUiModel): Int = TokoNowTickerViewHolder.LAYOUT
    override fun type(uiModel: TokoNowThematicHeaderUiModel): Int = TokoNowThematicHeaderViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            /* Category Component View Holder */
            CategoryNavigationViewHolder.LAYOUT -> CategoryNavigationViewHolder(
                itemView = view,
                listener = categoryNavigationListener
            )
            CategoryShowcaseViewHolder.LAYOUT -> CategoryShowcaseViewHolder(
                itemView = view,
                categoryShowcaseItemListener = categoryShowcaseItemListener,
                categoryShowcaseHeaderListener = categoryShowcaseHeaderListener,
                parentRecycledViewPool = recycledViewPool,
                lifecycleOwner = lifecycleOwner
            )

            /* Common Component View Holder */
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
            TokoNowTickerViewHolder.LAYOUT -> TokoNowTickerViewHolder(
                itemView = view
            )
            TokoNowThematicHeaderViewHolder.LAYOUT -> TokoNowThematicHeaderViewHolder(
                itemView = view,
                listener = tokoNowHeaderListener
            )
            else -> super.createViewHolder(view, type)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tokoNowHeaderListener = null
        categoryNavigationListener = null
        categoryShowcaseItemListener = null
        categoryShowcaseHeaderListener = null
        tokoNowCategoryMenuListener = null
        tokoNowProductRecommendationListener = null
    }
}
