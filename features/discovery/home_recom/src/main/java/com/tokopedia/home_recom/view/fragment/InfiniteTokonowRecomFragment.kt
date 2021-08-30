package com.tokopedia.home_recom.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorListener
import com.tokopedia.home_recom.model.datamodel.TitleListener
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.view.adapter.InfiniteRecomTypeFactory
import com.tokopedia.home_recom.viewmodel.InfiniteRecomViewModel
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * Created by yfsx on 30/08/21.
 */
class InfiniteTokonowRecomFragment : BaseListFragment<HomeRecommendationDataModel, InfiniteRecomTypeFactory>(), RecommendationListener, RecommendationErrorListener {

    companion object {
        private const val className = "com.tokopedia.home_recom.view.fragment.InfiniteTokonowRecomFragment"
        private const val SPAN_COUNT = 2
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val SAVED_REF = "saved_ref"
        private const val SAVED_QUERY_PARAM = "saved_query_param"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val RECOMMENDATION_APP_LINK = "https://tokopedia.com/rekomendasi/%s"
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
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
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val adapterFactory by lazy { InfiniteRecomTypeFactory(this, this) }
    private val viewModel by lazy { viewModelProvider.get(InfiniteRecomViewModel::class.java) }
    private var pageName = ""

    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_infinite_recom, container, false)
    }

    override fun onItemClicked(t: HomeRecommendationDataModel) {
    }

    override fun loadData(page: Int) {
        if (page == 1) {
            viewModel.getRecommendationFirstPage(pageName, productId)
        } else {
            viewModel.getRecommendationNextPage(pageName, productId, page)
        }
    }

    override fun getAdapterTypeFactory(): InfiniteRecomTypeFactory {
        return adapterFactory
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

    private fun observeLiveData() {
        viewModel.recommendationFirstLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { response ->
                clearAllData()
                renderList(response.toList())
                hideLoading()
            }
        })
    }

}