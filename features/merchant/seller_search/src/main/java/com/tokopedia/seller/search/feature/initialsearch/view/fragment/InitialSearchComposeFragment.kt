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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
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

class InitialSearchComposeFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: InitialSearchComposeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InitialSearchComposeViewModel::class.java)
    }

    private var historyViewUpdateComposeListener: HistoryViewUpdateComposeListener? = null

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
                val isMonitoringStarted = remember { mutableStateOf(false) }
                val isMonitoringFinished = remember { mutableStateOf(false) }

                val getActivity = LocalContext.current as? InitialSellerSearchComposeActivity
                val sellerSearchResult by viewModel.uiState.collectAsState(initial = null)

                LaunchedEffect(Unit) {
                    if (!isMonitoringStarted.value) {
                        (getActivity as? GlobalSearchSellerPerformanceMonitoringListener)?.startRenderPerformanceMonitoring()
                        isMonitoringStarted.value = true
                    }

                    sellerSearchResult?.let { result ->
                        if (!isMonitoringFinished.value) {
                            (getActivity as? GlobalSearchSellerPerformanceMonitoringListener)?.finishMonitoring()
                            isMonitoringFinished.value = true
                        }
                    }
                }

                InitialSearchFragmentScreen(
                    sellerSearchResult,
                    ::onUiEvent
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            }

            is InitialSearchUiEvent.OnItemRecommendationClicked -> {
            }
        }
    }

    override fun onDestroy() {
        viewModel.insertSuccessSearch.removeObservers(this)
        super.onDestroy()
    }

    fun historySearch(searchKeyword: String) {
        (activity as? GlobalSearchSellerPerformanceMonitoringListener)?.startNetworkPerformanceMonitoring()
        viewModel.fetchSellerSearch(keyword = searchKeyword, shopId = userSession.shopId)
    }
}
