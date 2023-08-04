package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.search.common.plt.GlobalSearchSellerPerformanceMonitoringListener
import com.tokopedia.seller.search.feature.analytics.SellerSearchTracking
import com.tokopedia.seller.search.feature.initialsearch.di.component.InitialSearchComponent
import com.tokopedia.seller.search.feature.initialsearch.view.activity.InitialSellerSearchComposeActivity
import com.tokopedia.seller.search.feature.initialsearch.view.compose.InitialSearchFragmentScreen
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistoryViewUpdateComposeListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchComposeViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class)
class InitialSearchComposeFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: InitialSearchComposeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InitialSearchComposeViewModel::class.java)
    }

    private var historyViewUpdateComposeListener: HistoryViewUpdateComposeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.let { ComposeView(it) }?.apply {
            setContent {
                val isMonitoringStarted = remember { mutableStateOf(false) }
                val isMonitoringFinished = remember { mutableStateOf(false) }

                val softwareKeyboardController = LocalSoftwareKeyboardController.current

                val getActivity = LocalContext.current as? InitialSellerSearchComposeActivity
                val sellerSearchResult = viewModel.uiState.collectAsState()

                LaunchedEffect(sellerSearchResult) {
                    if (!isMonitoringStarted.value) {
                        (getActivity as? GlobalSearchSellerPerformanceMonitoringListener)?.startRenderPerformanceMonitoring()
                        isMonitoringStarted.value = true
                    }

                    sellerSearchResult.let { result ->
                        if (!isMonitoringFinished.value) {
                            (getActivity as? GlobalSearchSellerPerformanceMonitoringListener)?.finishMonitoring()
                            isMonitoringFinished.value = true
                        }
                    }

                    if (sellerSearchResult.value.isDismissKeyboard) {
                        softwareKeyboardController?.hide()
                    }
                }

                InitialSearchFragmentScreen(
                    sellerSearchResult.value,
                    ::onUiEvent
                )
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(InitialSearchComponent::class.java)?.inject(this)
    }

    private fun onUiEvent(uiEvent: InitialSearchUiEvent) {
        when (uiEvent) {
            is InitialSearchUiEvent.OnClearAllHistory -> {
                viewModel.deleteSuggestionSearch(uiEvent.titleList, null)
                SellerSearchTracking.clickDeleteAllSearchEvent(userSession.userId)
            }

            is InitialSearchUiEvent.OnItemRemoveClicked -> {
                viewModel.deleteSuggestionSearch(listOf(uiEvent.title), uiEvent.position)
                SellerSearchTracking.clickDeleteSelectedSearch(userSession.userId)
            }

            is InitialSearchUiEvent.OnItemHistoryClicked -> {
                historyViewUpdateComposeListener?.setKeywordSearchBarView(uiEvent.searchBarKeyword)
                SellerSearchTracking.clickRecommendWordingEvent(userSession.userId)
            }

            is InitialSearchUiEvent.OnItemHighlightClicked -> {
                viewModel.insertSearchSeller(
                    uiEvent.item.title.orEmpty(),
                    uiEvent.item.id.orEmpty(),
                    uiEvent.item.title.orEmpty(),
                    uiEvent.position
                )
                startActivityFromAutoComplete(uiEvent.item.appUrl.orEmpty())
                SellerSearchTracking.clickOnItemSearchHighlights(userSession.userId)
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

    fun setHistoryViewUpdateListener(historyViewUpdateComposeListener: HistoryViewUpdateComposeListener) {
        this.historyViewUpdateComposeListener = historyViewUpdateComposeListener
    }

    fun fetchHistorySearch(searchKeyword: String) {
        (activity as? GlobalSearchSellerPerformanceMonitoringListener)?.startNetworkPerformanceMonitoring()
        viewModel.fetchSellerSearch(keyword = searchKeyword, shopId = userSession.shopId)
    }
}
