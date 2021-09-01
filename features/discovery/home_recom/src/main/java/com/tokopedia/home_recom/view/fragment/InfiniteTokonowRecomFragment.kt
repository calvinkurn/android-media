package com.tokopedia.home_recom.view.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater
import com.tokopedia.home_recom.RecomPageChooseAddressWidgetCallback
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.listener.RecomPageListener
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorListener
import com.tokopedia.home_recom.util.RecomPageConstant.SAVED_PRODUCT_ID
import com.tokopedia.home_recom.util.RecomPageConstant.SAVED_QUERY_PARAM
import com.tokopedia.home_recom.util.RecomPageConstant.SAVED_REF
import com.tokopedia.home_recom.util.RecomPageUiUpdater
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.view.adapter.RecomPageAdapter
import com.tokopedia.home_recom.view.diffutil.RecomPageDiffUtil
import com.tokopedia.home_recom.viewmodel.InfiniteRecomViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * Created by yfsx on 30/08/21.
 */
class InfiniteTokonowRecomFragment : BaseRecomPageFragment<HomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl>(), RecomPageListener, RecommendationListener, RecommendationErrorListener {

    companion object {
        private const val className = "com.tokopedia.home_recom.view.fragment.InfiniteTokonowRecomFragment"
        private const val REQUEST_FROM_PDP = 394
        private const val REQUEST_CODE_LOGIN = 283
        private const val SPACING_30 = 30

        fun newInstance(productId: String = "", queryParam: String = "", ref: String = "null", internalRef: String = "", @FragmentInflater fragmentInflater: String = FragmentInflater.DEFAULT) = InfiniteTokonowRecomFragment().apply {
            this.productId = productId
            this.queryParam = queryParam
            this.ref = ref
            this.internalRef = internalRef
            this.fragmentInflater = fragmentInflater
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var trackingQueue: TrackingQueue? = null
    private var productId: String = ""
    private var queryParam: String = ""
    private var ref: String = ""
    private var internalRef: String = ""
    private lateinit var viewModelProvider: ViewModelProvider
    private val adapterFactory by lazy { HomeRecommendationTypeFactoryImpl(this, null, this, null) }
    private lateinit var viewModel: InfiniteRecomViewModel
    private var pageName = ""
    private var recomPageUiUpdater: RecomPageUiUpdater = RecomPageUiUpdater(mutableListOf())


    private val adapter by lazy {
        val asyncDifferConfig: AsyncDifferConfig<HomeRecommendationDataModel> = AsyncDifferConfig.Builder(RecomPageDiffUtil())
                .build()
        RecomPageAdapter(asyncDifferConfig, this, adapterFactory)
    }

    override fun initInjector() {
        homeRecommendationComponent = getComponent(HomeRecommendationComponent::class.java)
        homeRecommendationComponent?.inject(this)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateExtended(savedInstanceState: Bundle?) {
        activity?.let {
            viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(InfiniteRecomViewModel::class.java)
            trackingQueue = TrackingQueue(it)
        }
        savedInstanceState?.let {
            productId = it.getString(SAVED_PRODUCT_ID) ?: ""
            queryParam = it.getString(SAVED_QUERY_PARAM) ?: ""
            ref = it.getString(SAVED_REF) ?: ""
        }
        observeLiveData()
        getMiniCartData()
        loadInitData(false)
    }

    override fun createAdapterInstance(): RecomPageAdapter = adapter

    override fun loadInitData(forceRefresh: Boolean) {
        viewModel.getRecommendationFirstPage(pageName, productId, queryParam)
    }

    override fun loadMoreData(pageNumber: Int) {
        viewModel.getRecommendationNextPage(pageName, productId, pageNumber, queryParam)
    }

    override fun observeData() {
        observeLiveData()
    }

    override fun onRefreshRecommendation() {
    }

    override fun onCloseRecommendation() {
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
    }

    override fun onProductImpression(item: RecommendationItem) {
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
    }

    override fun onChooseAddressUpdated() {
        getMiniCartData()
    }

    override fun onChooseAddressServerDown() {
        chooseAddressWidget?.gone()
    }

    override fun onChooseAddressImplemented(): ChooseAddressWidget.ChooseAddressWidgetListener {
        return RecomPageChooseAddressWidgetCallback(context = requireContext(), listener = this, fragment = this)
    }

    override fun onDestroy() {
        viewModel.recommendationWidgetData.removeObservers(this)
        viewModel.recommendationFirstLiveData.removeObservers(this)
        viewModel.miniCartData.removeObservers(this)
        viewModel.recommendationNextLiveData.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    private fun observeLiveData() {
        viewModel.recommendationWidgetData.observe(viewLifecycleOwner, Observer {
            it?.let {
                navToolbar?.setToolbarTitle(it.title)
            }
        })
        viewModel.recommendationFirstLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { response ->
                recomPageUiUpdater.appendFirstData(response.toList())
                submitInitialList(recomPageUiUpdater.dataList.toMutableList())
            }
        })
        viewModel.miniCartData.observe(viewLifecycleOwner, Observer {
            it?.let {
//                val newList = RecomPageTokonowDataUpdater.updateRecomWithMinicartData(it, adapter.data)
//                adapter.
            }
        })
        viewModel.recommendationNextLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { response ->
                recomPageUiUpdater.appendNextData(response.toList())
                submitList(recomPageUiUpdater.dataList.toMutableList())
            }
        })
        viewModel.loadMoreData.observe(viewLifecycleOwner, Observer {
            disableLoadMore()
        })

    }

    private fun getMiniCartData() {
        val localAddress = ChooseAddressUtils.getLocalizingAddressData(requireContext())
        viewModel.getMiniCart(localAddress?.shop_id ?: "")
    }
}