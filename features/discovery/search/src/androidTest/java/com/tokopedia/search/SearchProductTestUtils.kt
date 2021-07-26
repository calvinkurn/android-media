package com.tokopedia.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.Option
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter
import com.tokopedia.search.result.presentation.view.listener.*
import com.tokopedia.topads.sdk.domain.model.CpmData
import org.hamcrest.Matcher

internal const val QUERY_PARAMS_WITH_KEYWORD = "?q=samsung"

internal fun disableOnBoarding(context: Context) {
    LocalCacheHandler(context, SearchConstant.FreeOngkir.FREE_ONGKIR_LOCAL_CACHE_NAME).also {
        it.putBoolean(SearchConstant.FreeOngkir.FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN, true)
        it.applyEditor()
    }
}

internal fun createIntent(queryParams: String = QUERY_PARAMS_WITH_KEYWORD): Intent {
    return Intent(InstrumentationRegistry.getInstrumentation().targetContext, SearchActivity::class.java).also {
        it.data = Uri.parse(ApplinkConstInternalDiscovery.SEARCH_RESULT + queryParams)
    }
}

internal fun RecyclerView?.getProductListAdapter(): ProductListAdapter {
    val productListAdapter = this?.adapter as? ProductListAdapter

    if (productListAdapter == null) {
        val detailMessage = "Adapter is not ${ProductListAdapter::class.java.simpleName}"
        throw AssertionError(detailMessage)
    }

    return productListAdapter
}

internal fun createProductItemListener(): ProductListener {
    return object: ProductListener {
        override fun onThreeDotsClick(itemData: ProductItemDataView?, adapterPosition: Int) {}
        override fun onItemClicked(itemData: ProductItemDataView?, adapterPosition: Int) {}
        override fun onProductImpressed(itemData: ProductItemDataView?, adapterPosition: Int) {}
    }
}

internal fun createInspirationCardListener(): InspirationCardListener {
    return object: InspirationCardListener {
        override fun onInspirationCardOptionClicked(optionData: InspirationCardOptionDataView) {}
    }
}

internal fun createInspirationCarouselListener(): InspirationCarouselListener {
    return object: InspirationCarouselListener {
        override val carouselRecycledViewPool: RecyclerView.RecycledViewPool?
            get() = null
        override fun onInspirationCarouselListProductClicked(product: InspirationCarouselDataView.Option.Product) {}
        override fun onInspirationCarouselSeeAllClicked(inspirationCarouselDataViewOption: InspirationCarouselDataView.Option) {}
        override fun onInspirationCarouselInfoProductClicked(product: InspirationCarouselDataView.Option.Product) {}
        override fun onImpressedInspirationCarouselInfoProduct(product: InspirationCarouselDataView.Option.Product) {}
        override fun onImpressedInspirationCarouselListProduct(product: InspirationCarouselDataView.Option.Product) {}
        override fun onImpressedInspirationCarouselGridProduct(product: InspirationCarouselDataView.Option.Product) {}
        override fun onInspirationCarouselGridProductClicked(product: InspirationCarouselDataView.Option.Product) {}
        override fun onInspirationCarouselGridBannerClicked(product: InspirationCarouselDataView.Option) {}
        override fun onInspirationCarouselChipsProductClicked(product: InspirationCarouselDataView.Option.Product) {}
        override fun onImpressedInspirationCarouselChipsProduct(product: InspirationCarouselDataView.Option.Product) {}
        override fun onInspirationCarouselChipsSeeAllClicked(inspirationCarouselDataViewOption: InspirationCarouselDataView.Option) {}
        override fun onInspirationCarouselChipsClicked(inspirationCarouselAdapterPosition: Int, inspirationCarouselViewModel: InspirationCarouselDataView, inspirationCarouselOption: InspirationCarouselDataView.Option) {}

    }
}

internal fun createBroadMatchListener(): BroadMatchListener {
    return object: BroadMatchListener {
        override fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView) {}
        override fun onBroadMatchSeeMoreClicked(broadMatchDataView: BroadMatchDataView) {}
        override fun onBroadMatchThreeDotsClicked(broadMatchItemDataView: BroadMatchItemDataView) {}
        override fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView) {}
        override val carouselRecycledViewPool: RecyclerView.RecycledViewPool?
            get() = null
    }
}

internal fun createGlobalNavListener(): GlobalNavListener {
    return object: GlobalNavListener {
        override fun onGlobalNavWidgetClicked(item: GlobalNavDataView.Item?, keyword: String?) {}
        override fun onGlobalNavWidgetClickSeeAll(globalNavDataView: GlobalNavDataView?) {}
    }
}

internal fun createBannerAdsListener(): BannerAdsListener {
    return object: BannerAdsListener {
        override fun onBannerAdsImpressionListener(position: Int, data: CpmData?) {}
        override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {}
    }
}

internal fun createSuggestionListener(): SuggestionListener {
    return object : SuggestionListener {
        override fun onSuggestionClicked(suggestionDataView: SuggestionDataView?) {

        }
    }
}

internal fun createEmptyStateListener(): EmptyStateListener {
    return object: EmptyStateListener {
        override fun getUserId(): String { return "" }
        override fun getSelectedFilterAsOptionList(): MutableList<Option> { return mutableListOf() }
        override fun onSelectedFilterRemoved(uniqueId: String?) {}
        override fun onEmptySearchToGlobalSearchClicked(applink: String?) {}
        override fun getRegistrationId(): String { return "" }
        override fun onEmptyButtonClicked() {}
    }
}

internal fun createRecommendationListener(): RecommendationListener {
    return object: RecommendationListener {
        override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {}
        override fun onProductImpression(item: RecommendationItem) {}
        override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {}
    }
}

internal fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(uiController: UiController?, view: View) {
            val v: View = view.findViewById(id)
            v.performClick()
        }
    }
}

internal fun List<Visitable<*>>.getGlobalNavViewModelPosition(): Int {
    return indexOfFirst { it is GlobalNavDataView }
}

internal fun List<Visitable<*>>.getFirstTopAdsProductPosition(): Int {
    return indexOfFirst { it is ProductItemDataView && it.isTopAds }
}

internal fun List<Visitable<*>>.getFirstOrganicProductPosition(): Int {
    return indexOfFirst { it is ProductItemDataView && !it.isTopAds }
}

internal fun List<Visitable<*>>.getEmptySearchProductViewModelPosition(): Int {
    return indexOfFirst { it is EmptySearchProductDataView }
}

internal fun List<Visitable<*>>.getRecommendationTitleViewModelPosition(): Int {
    return indexOfFirst { it is RecommendationTitleDataView }
}

internal fun List<Visitable<*>>.getRecommendationItemViewModelPosition(): Int {
    return indexOfFirst { it is RecommendationItemDataView }
}

internal fun List<Visitable<*>>.getSuggestionViewModelPosition(): Int {
    return indexOfFirst { it is SuggestionDataView }
}

internal fun List<Visitable<*>>.getBroadMatchViewModelPosition(): Int {
    return indexOfFirst { it is BroadMatchDataView }
}