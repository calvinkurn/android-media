package com.tokopedia.inbox.universalinbox.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.inbox.databinding.UniversalInboxFragmentBinding
import com.tokopedia.inbox.universalinbox.di.UniversalInboxComponent
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxAdapter
import com.tokopedia.inbox.universalinbox.view.adapter.decorator.UniversalInboxRecommendationDecoration
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxEndlessScrollListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationLoaderUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.viewmodel.UniversalInboxViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.viewmodel.TopAdsHeadlineViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class UniversalInboxFragment:
    BaseDaggerFragment(),
    UniversalInboxEndlessScrollListener.Listener,
    TdnBannerResponseListener,
    TopAdsImageViewClickListener,
    RecommendationListener
{
    private var binding: UniversalInboxFragmentBinding? by autoClearedNullable()
    private var adapter = UniversalInboxAdapter(
        this, this, this)
    private var endlessRecyclerViewScrollListener: UniversalInboxEndlessScrollListener? = null

    @Inject
    lateinit var viewModel: UniversalInboxViewModel

//    @Inject
//    lateinit var topAdsViewModel: TopAdsHeadlineViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UniversalInboxFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        return binding?.root
    }

    override fun initInjector() {
        getComponent(UniversalInboxComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view, savedInstanceState)
    }

    private fun initViews(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupRecyclerViewLoadMore()
        setupObservers()
        adapter.addItems(viewModel.dummy())
        loadTopAdsAndRecommendation()
    }

    private fun setupRecyclerView() {
        binding?.inboxRv?.layoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL)
        binding?.inboxRv?.setHasFixedSize(true)
        binding?.inboxRv?.itemAnimator = null
        binding?.inboxRv?.adapter = adapter
        binding?.inboxRv?.addItemDecoration(UniversalInboxRecommendationDecoration())
    }

    private fun setupRecyclerViewLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            binding?.inboxRv?.layoutManager?.let {
                endlessRecyclerViewScrollListener = UniversalInboxEndlessScrollListener(
                    layoutManager = it as StaggeredGridLayoutManager,
                    this@UniversalInboxFragment
                )
            }
        }
        endlessRecyclerViewScrollListener?.let {
            binding?.inboxRv?.addOnScrollListener(it)
        }
    }

    private fun setupObservers() {
        viewModel.firstPageRecommendation.observe(viewLifecycleOwner) {
            removeLoadMoreLoading()
            when (it) {
                is Success -> {
                    onSuccessGetFirstRecommendationData(it.data.recommendationWidget, it.data.tdnBanner)
                }
                is Fail -> {}
            }
        }

        viewModel.morePageRecommendation.observe(viewLifecycleOwner) {
            removeLoadMoreLoading()
            when (it) {
                is Success -> {
                    addRecommendationItem(it.data)
                }
                is Fail -> {}
            }
        }
    }

    private fun onSuccessGetFirstRecommendationData(
        recommendation: RecommendationWidget,
        topAds: List<TopAdsImageViewModel>
    ) {
        // TopAds
        addTopAdsItem(topAds)

        // Recommendation
        adapter.addItem(UniversalInboxRecommendationTitleUiModel(recommendation.title))
        addRecommendationItem(recommendation.recommendationItemList)
    }

    private fun addRecommendationItem(list: List<RecommendationItem>) {
        val itemCountBefore = adapter.itemCount
        adapter.addItems(list)
        adapter.notifyItemRangeInserted(itemCountBefore, itemCountBefore + list.size)
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun addTopAdsItem(list: List<TopAdsImageViewModel>) {
        adapter.addItem(UniversalInboxTopAdsBannerUiModel(list))
        adapter.notifyItemInserted(adapter.itemCount)
    }

    private fun loadTopAdsAndRecommendation() {
        showLoadMoreLoading()
        viewModel.loadTopAdsAndFirstPageRecommendation()
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        showLoadMoreLoading()
        viewModel.loadMoreRecommendation(page)
    }

    private fun showLoadMoreLoading() {
        adapter.addItem(adapter.getItems().size, UniversalInboxRecommendationLoaderUiModel())
        adapter.notifyItemInserted(adapter.itemCount)
    }

    private fun removeLoadMoreLoading() {
        if (adapter.getItems().isNotEmpty() &&
            adapter.getItems()[adapter.getItems().lastIndex]::class ==
            UniversalInboxRecommendationLoaderUiModel::class
        ) {
            adapter.removeItemAt(adapter.getItems().lastIndex)
            adapter.notifyItemRemoved(adapter.getItems().size)
        }
    }

    override fun onTdnBannerResponse(categoriesList: MutableList<List<TopAdsImageViewModel>>) {
        //TODO
    }

    override fun onError(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onTopAdsImageViewClicked(applink: String?) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "UniversalInboxFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): UniversalInboxFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UniversalInboxFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UniversalInboxFragment::class.java.name
            ).apply {
                arguments = bundle
            } as UniversalInboxFragment
        }
    }

    override fun onProductClick(
        item: RecommendationItem,
        layoutType: String?,
        vararg position: Int
    ) {
        if (item.isTopAds) {
//            onClickTopAds(item)
        } else {
//            onClickOrganic(item)
        }
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            item.productId.toString()
        )
//        if (position.size >= 1) intent.putExtra(
//            com.tokopedia.navigation.presentation.fragment.InboxFragment.PDP_EXTRA_UPDATED_POSITION,
//            position[0]
//        )
//        startActivityForResult(
//            intent,
//            com.tokopedia.navigation.presentation.fragment.InboxFragment.REQUEST_FROM_PDP
//        )
    }

    override fun onProductImpression(item: RecommendationItem) {
        if (item.isTopAds) {
//            onImpressionTopAds(item)
        } else {
//            onImpressionOrganic(item)
        }
    }

    override fun onWishlistV2Click(item: RecommendationItem, isAddWishlist: Boolean) {
        //TODO
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        if (position.isEmpty()) return

//        showProductCardOptions(this, createProductCardOptionsModel(item, position[0]))
    }
}
