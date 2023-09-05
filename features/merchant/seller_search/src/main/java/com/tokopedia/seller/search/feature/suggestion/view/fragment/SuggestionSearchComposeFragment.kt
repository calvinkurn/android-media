package com.tokopedia.seller.search.feature.suggestion.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringListener
import com.tokopedia.seller.search.common.util.addWWWPrefix
import com.tokopedia.seller.search.feature.analytics.SellerSearchTracking
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.suggestion.view.compose.SuggestionSearchScreen
import com.tokopedia.seller.search.feature.suggestion.view.model.compose.SuggestionSearchUiEvent
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ArticleSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.FaqSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.OrderSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ProductSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHasMoreSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemHighlightSuggestionSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.viewmodel.SuggestionSearchComposeViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class)
class SuggestionSearchComposeFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SuggestionSearchComposeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SuggestionSearchComposeViewModel::class.java)
    }

    private var searchKeyword = ""
    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(InitialSearchComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.let { ComposeView(it) }?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                SuggestionSearchScreen(
                    viewModel,
                    ::onUiEvent,
                    ::startRenderPerformanceMonitoring,
                    ::finishMonitoring
                )
            }
        }
    }

    fun suggestionSearch(keyword: String) {
        this.searchKeyword = keyword
        viewModel.showLoading()
        viewModel.fetchSellerSearch(keyword = searchKeyword, shopId = userSession.shopId)
    }

    private fun onUiEvent(uiEvent: SuggestionSearchUiEvent) {
        when (uiEvent) {
            is SuggestionSearchUiEvent.OnSellerSearchNoResult -> {
                SellerSearchTracking.impressionEmptyResultEvent(userSession.userId)
            }

            is SuggestionSearchUiEvent.OnArticleItemClicked -> {
                onArticleItemClicked(uiEvent.articleSellerSearchUiModel, uiEvent.position)
            }

            is SuggestionSearchUiEvent.OnFaqItemClicked -> {
                onFaqItemClicked(uiEvent.faqSearchUiModel, uiEvent.position)
            }

            is SuggestionSearchUiEvent.OnHighlightItemClicked -> {
                onHighlightItemClicked(
                    uiEvent.itemHighlightSuggestionSearchUiModel,
                    uiEvent.position
                )
            }

            is SuggestionSearchUiEvent.OnNavigationItemClicked -> {
                onNavigationItemClicked(uiEvent.navigationSellerSearchUiModel, uiEvent.position)
            }

            is SuggestionSearchUiEvent.OnNavigationSellerSearchSubItemClicked -> {
                context?.let {
                    RouteManager.route(
                        it,
                        uiEvent.navigationSellerSearchSubItemUiModel.appLink
                    )
                }
            }

            is SuggestionSearchUiEvent.OnOrderItemClicked -> {
                onOrderItemClicked(uiEvent.orderSellerSearchUiModel, uiEvent.position)
            }

            is SuggestionSearchUiEvent.OnProductItemClicked -> {
                onProductItemClicked(uiEvent.productSellerSearchUiModel, uiEvent.position)
            }

            is SuggestionSearchUiEvent.OnOrderMoreClicked -> {
                onOrderMoreClicked(uiEvent.item)
            }

            is SuggestionSearchUiEvent.OnProductMoreClicked -> {
                onProductMoreClicked(uiEvent.item)
            }

            is SuggestionSearchUiEvent.OnFaqMoreClicked -> {
                onFaqMoreClicked(uiEvent.item)
            }

            is SuggestionSearchUiEvent.OnArticleMoreClicked -> {
                onArticleMoreClicked(uiEvent.item)
            }

            else -> {
                // no op
            }
        }
    }

    private fun startActivityFromAutoComplete(appLink: String) {
        activity?.let {
            RouteManager.route(it, appLink)
            it.finish()
        }
    }

    private fun onNavigationItemClicked(data: NavigationSellerSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(
            data.title.orEmpty(),
            data.id.orEmpty(),
            data.title.orEmpty(),
            position
        )
        SellerSearchTracking.clickOnSearchResult(
            userSession.userId,
            data.section.orEmpty(),
            searchKeyword
        )
        startActivityFromAutoComplete(data.appUrl.orEmpty())
    }

    private fun onOrderItemClicked(data: OrderSellerSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(
            data.title.orEmpty(),
            data.id.orEmpty(),
            data.title.orEmpty(),
            position
        )
        SellerSearchTracking.clickOnSearchResult(
            userSession.userId,
            data.section.orEmpty(),
            searchKeyword
        )
        startActivityFromAutoComplete(data.appUrl.orEmpty())
    }

    private fun onOrderMoreClicked(element: TitleHasMoreSellerSearchUiModel) {
        SellerSearchTracking.clickOtherResult(userSession.userId, element.title, searchKeyword)
        startActivityFromAutoComplete(element.appActionLink)
    }

    private fun onProductItemClicked(data: ProductSellerSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(
            data.title.orEmpty(),
            data.id.orEmpty(),
            data.title.orEmpty(),
            position
        )
        SellerSearchTracking.clickOnSearchResult(
            userSession.userId,
            data.section.orEmpty(),
            searchKeyword
        )
        startActivityFromAutoComplete(data.appUrl.orEmpty())
    }

    private fun onProductMoreClicked(element: TitleHasMoreSellerSearchUiModel) {
        SellerSearchTracking.clickOtherResult(userSession.userId, element.title, searchKeyword)
        startActivityFromAutoComplete(element.appActionLink)
    }

    private fun onFaqItemClicked(data: FaqSellerSearchUiModel, position: Int) {
        itemRedirectToWebView(
            data.title.orEmpty(),
            data.id.orEmpty(),
            data.section.orEmpty(),
            data.appUrl.orEmpty(),
            position
        )
    }

    private fun onFaqMoreClicked(element: TitleHasMoreSellerSearchUiModel) {
        moreRedirectToWebView(element.title, element.appActionLink)
    }

    private fun onArticleMoreClicked(element: TitleHasMoreSellerSearchUiModel) {
        moreRedirectToWebView(element.title, element.appActionLink)
    }

    private fun onHighlightItemClicked(data: ItemHighlightSuggestionSearchUiModel, position: Int) {
        viewModel.insertSearchSeller(
            data.title.orEmpty(),
            data.id.orEmpty(),
            data.title.orEmpty(),
            position
        )
        startActivityFromAutoComplete(data.appUrl.orEmpty())
        SellerSearchTracking.clickOnItemSearchHighlights(userSession.userId)
    }

    private fun onArticleItemClicked(data: ArticleSellerSearchUiModel, position: Int) {
        itemRedirectToWebView(
            data.title.orEmpty(),
            data.id.orEmpty(),
            data.section.orEmpty(),
            data.appUrl.orEmpty(),
            position
        )
    }

    private fun moreRedirectToWebView(
        title: String,
        appUrl: String
    ) {
        SellerSearchTracking.clickOtherResult(userSession.userId, title, searchKeyword)
        val appUrlFormatted = appUrl.addWWWPrefix
        RouteManager.route(activity, appUrlFormatted)
    }

    private fun itemRedirectToWebView(
        title: String,
        id: String,
        section: String,
        appUrl: String,
        position: Int
    ) {
        SellerSearchTracking.clickOnSearchResult(userSession.userId, section, searchKeyword)
        viewModel.insertSearchSeller(
            title,
            id,
            title,
            position
        )
        val appUrlFormatted = appUrl.addWWWPrefix
        RouteManager.route(activity, appUrlFormatted)
    }

    private fun startRenderPerformanceMonitoring() {
        (activity as? GlobalSearchSellerPerformanceMonitoringListener)?.startRenderPerformanceMonitoring()
    }

    private fun finishMonitoring() {
        (activity as? GlobalSearchSellerPerformanceMonitoringListener)?.finishMonitoring()
    }
}
