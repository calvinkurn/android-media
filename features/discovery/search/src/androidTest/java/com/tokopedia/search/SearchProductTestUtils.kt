package com.tokopedia.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
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

internal const val QUERY_PARAMS_WITH_KEYWORD = "?q=samsung"

internal fun disableOnBoarding(context: Context) {
    LocalCacheHandler(context, SearchConstant.FreeOngkir.FREE_ONGKIR_LOCAL_CACHE_NAME).also {
        it.putBoolean(SearchConstant.FreeOngkir.FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN, true)
        it.applyEditor()
    }

    LocalCacheHandler(context, SearchConstant.OnBoarding.LOCAL_CACHE_NAME).also {
        it.putBoolean(SearchConstant.OnBoarding.FILTER_ONBOARDING_SHOWN, true)
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
        override fun onThreeDotsClick(item: ProductItemViewModel?, adapterPosition: Int) {}
        override fun onItemClicked(item: ProductItemViewModel?, adapterPosition: Int) {}
        override fun onProductImpressed(item: ProductItemViewModel?) {}
    }
}

internal fun createInspirationCardListener(): InspirationCardListener {
    return object: InspirationCardListener {
        override fun onInspirationCardOptionClicked(option: InspirationCardOptionViewModel) {}
    }
}

internal fun createInspirationCarouselListener(): InspirationCarouselListener {
    return object: InspirationCarouselListener {
        override fun onInspirationCarouselListProductClicked(product: InspirationCarouselViewModel.Option.Product) {}
        override fun onInspirationCarouselSeeAllClicked(inspirationCarouselViewModelOption: InspirationCarouselViewModel.Option) {}
        override fun onInspirationCarouselInfoProductClicked(product: InspirationCarouselViewModel.Option.Product) {}
    }
}

internal fun createBroadMatchListener(): BroadMatchListener {
    return object: BroadMatchListener {
        override fun onBroadMatchItemClicked(broadMatchItemViewModel: BroadMatchItemViewModel) {}
        override fun onBroadMatchSeeMoreClicked(broadMatchViewModel: BroadMatchViewModel) {}
        override fun onBroadMatchThreeDotsClicked(broadMatchItemViewModel: BroadMatchItemViewModel) {}
    }
}

internal fun createGlobalNavListener(): GlobalNavListener {
    return object: GlobalNavListener {
        override fun onGlobalNavWidgetClicked(item: GlobalNavViewModel.Item?, keyword: String?) {}
        override fun onGlobalNavWidgetClickSeeAll(globalNavViewModel: GlobalNavViewModel?) {}
    }
}

internal fun createBannerAdsListener(): BannerAdsListener {
    return object: BannerAdsListener {
        override fun onBannerAdsImpressionListener(position: Int, data: CpmData?) {}
        override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {}
    }
}

internal fun createSuggestionListener(): SuggestionListener {
    return SuggestionListener { }
}

internal fun createEmptyStateListener(): EmptyStateListener {
    return object: EmptyStateListener {
        override fun getUserId(): String { return "" }
        override fun getSelectedFilterAsOptionList(): MutableList<Option> { return mutableListOf() }
        override fun onSelectedFilterRemoved(uniqueId: String?) {}
        override fun getRegistrationId(): String { return "" }
        override fun onEmptyButtonClicked() {}
    }
}

internal fun createBannedProductsEmptySearch(): BannedProductsRedirectToBrowserListener {
    return object: BannedProductsRedirectToBrowserListener {
        override fun onGoToBrowserClicked(isEmptySearch: Boolean, liteUrl: String) {}
    }
}

internal fun createRecommendationListener(): RecommendationListener {
    return object: RecommendationListener {
        override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {}
        override fun onProductImpression(item: RecommendationItem) {}
        override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {}
    }
}