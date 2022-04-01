package com.tokopedia.home_recom.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_recom.RecomPageChooseAddressWidgetCallback
import com.tokopedia.home_recom.analytics.InfiniteRecomTracker
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.listener.RecomPageListener
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationEmptyDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorListener
import com.tokopedia.home_recom.util.*
import com.tokopedia.home_recom.util.ReccomendationViewModelUtil.doSuccessOrFail
import com.tokopedia.home_recom.util.RecomPageConstant.PAGE_TITLE_RECOM_DEFAULT
import com.tokopedia.home_recom.util.RecomPageConstant.REQUEST_CODE_LOGIN
import com.tokopedia.home_recom.util.RecomPageConstant.SAVED_PRODUCT_ID
import com.tokopedia.home_recom.util.RecomPageConstant.SAVED_QUERY_PARAM
import com.tokopedia.home_recom.util.RecomPageConstant.SAVED_REF
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.view.adapter.RecomPageAdapter
import com.tokopedia.home_recom.view.diffutil.RecomPageDiffUtil
import com.tokopedia.home_recom.viewmodel.InfiniteRecomViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.listener.RecommendationTokonowListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by yfsx on 30/08/21.
 */
class InfiniteTokonowRecomFragment :
        BaseRecomPageFragment<HomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl>(),
        RecomPageListener,
        RecommendationListener,
        RecommendationErrorListener,
        RecommendationTokonowListener,
        MiniCartWidgetListener {

    companion object {
        private const val className = "com.tokopedia.home_recom.view.fragment.InfiniteTokonowRecomFragment"
        private const val REQUEST_FROM_PDP = 394
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
    private val adapterFactory by lazy {
        HomeRecommendationTypeFactoryImpl(
                this,
                null,
                this,
                null, this)
    }
    private lateinit var viewModel: InfiniteRecomViewModel
    private var pageName = ""
    private var recomPageUiUpdater: RecomPageUiUpdater = RecomPageUiUpdater(mutableListOf())
    private var firstRecomWidget: RecommendationWidget? = null


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

    override fun setDefaultPageTitle(): String {
        return PAGE_TITLE_RECOM_DEFAULT
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
            pageName = ref
        }
        observeLiveData()
        hideChooseAddressWidget()
        showMiniCartWidget()
        if (isWarehouseIdEmpty()) {
            showErrorFullPage(Throwable())
        } else {
            getMiniCartData()
            loadInitData(false)
        }
    }

    override fun createAdapterInstance(): RecomPageAdapter = adapter

    override fun loadInitData(forceRefresh: Boolean) {
        if (forceRefresh) {
            enableLoadMore()
            resetLoadMore()
        }
        getMiniCartData()
        viewModel.getRecommendationFirstPage(pageName, productId, queryParam, forceRefresh)
    }

    override fun loadMoreData(pageNumber: Int) {
        viewModel.getRecommendationNextPage(pageName, productId, pageNumber, queryParam)
    }

    override fun shouldPageImplementChooseAddress(): Boolean {
        return false
    }

    override fun shouldPageImplementMiniCartWidget(): Boolean {
        return true
    }

    override fun setShopId(): String {
        context?.let {
            val localAddress = ChooseAddressUtils.getLocalizingAddressData(it)
            return localAddress?.shop_id ?: ""
        }
        return ""
    }

    override fun setImplementingFragment(): Fragment {
        return this
    }

    override fun setMiniCartWidgetListener(): MiniCartWidgetListener {
        return this
    }

    override fun setMiniCartPageName(): MiniCartAnalytics.Page {
        return MiniCartAnalytics.Page.RECOMMENDATION_INFINITE
    }

    override fun onResume() {
        super.onResume()
        showMiniCartWidget()
        getMiniCartData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            getMiniCartData()
        }
    }

    override fun observeData() {
        observeLiveData()
    }

    override fun onRefreshRecommendation() {
        loadInitData(false)
    }

    override fun onCloseRecommendation() {
        activity?.finish()
    }

    override fun onShowSnackbarError(throwable: Throwable) {
        showErrorSnackbar(throwable)
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(InfiniteRecomTracker.eventRecomItemClick(firstRecomWidget, item, getUserSession().userId, productId) as HashMap<String, Any>)
        goToPDP(item.productId.toString(), item.position)
    }

    override fun onProductImpression(item: RecommendationItem) {
        getTrackingQueueObj()?.putEETracking(InfiniteRecomTracker.eventRecomItemImpression(firstRecomWidget, item, getUserSession().userId, productId) as HashMap<String, Any>)
    }

    override fun onProductTokonowNonVariantQuantityChanged(recomItem: RecommendationItem, adapterPosition: Int, quantity: Int) {
        recomPageUiUpdater?.updateCurrentQuantityRecomItem(recomItem)
        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)
    }

    override fun onProductTokonowVariantClicked(recomItem: RecommendationItem, adapterPosition: Int) {
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                    context = it,
                    productId = recomItem.productId.toString(),
                    pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                    isTokoNow = true,
                    shopId = recomItem.shopId.toString(),
                    startActivitResult = { data, _ ->
                        startActivity(data)
                    }
            )
        }
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
    }

    override fun onChooseAddressUpdated() {
        getMiniCartData()
    }

    override fun onChooseAddressServerDown() {
        chooseAddressWidget?.gone()
    }

    override fun onChooseAddressImplemented(): ChooseAddressWidget.ChooseAddressWidgetListener? {
        context?.let {
            return RecomPageChooseAddressWidgetCallback(context = it, listener = this, fragment = this)
        }
        return null
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        getMiniCartData()
    }

    override fun onDestroy() {
        viewModel.recommendationWidgetData.removeObservers(this)
        viewModel.recommendationFirstLiveData.removeObservers(this)
        viewModel.miniCartData.removeObservers(this)
        viewModel.recommendationNextLiveData.removeObservers(this)
        viewModel.errorGetRecomData.removeObservers(this)
        viewModel.atcRecomTokonow.removeObservers(this)
        viewModel.atcRecomTokonowSendTracker.removeObservers(this)
        viewModel.atcRecomTokonowResetCard.removeObservers(this)
        viewModel.atcRecomTokonowNonLogin.removeObservers(this)
        viewModel.refreshMiniCartDataTrigger.removeObservers(this)
        viewModel.minicartWidgetUpdater.removeObservers(this)
        viewModel.deleteCartRecomTokonowSendTracker.removeObservers(this)
        viewModel.minicartError.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    private fun observeLiveData() {
        viewModel.recommendationWidgetData.observe(viewLifecycleOwner, Observer {
            it?.let {
                navToolbar?.setToolbarTitle(it.title)
                firstRecomWidget = it
            }
        })
        viewModel.recommendationFirstLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { response ->
                removeFirstLoading()
                recomPageUiUpdater.appendFirstRecomData(response.toList())
                submitInitialList(recomPageUiUpdater.dataList.toMutableList())
            }
        })
        viewModel.miniCartData.observe(viewLifecycleOwner, Observer {
            it?.let {
                recomPageUiUpdater.updateRecomWithMinicartData(it)
                if (recomPageUiUpdater.dataList.size != 0) updateUi()
            }
        })
        viewModel.minicartWidgetUpdater.observe(viewLifecycleOwner, Observer {
            it?.let {
                updateMinicartWidgetVisibility(it)
            }
        })
        viewModel.recommendationNextLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { response ->
                removeLoadingForLoadMore()
                recomPageUiUpdater.appendNextRecomData(response.toList())
                updateUi()
            }
        })
        viewModel.errorGetRecomData.observe(viewLifecycleOwner, Observer {
            it?.let {
                disableLoadMore()
                hideSwipeLoading()
                when {
                    it.isForceRefreshAndError -> {
                        showErrorSnackbar(it.errorThrowable)
                    }
                    it.isEmptyFirstPage -> {
                        showEmptyPage()
                    }
                    it.isErrorFirstPage -> {
                        showErrorFullPage(it.errorThrowable)
                    }
                    it.isEmptyNextPage -> {
                        disableLoadMore()
                    }
                    else -> {
                        showErrorSnackbarWithRetryLoad(it.pageNumber, it.errorThrowable)
                    }
                }
            }
        })
        viewModel.atcRecomTokonow.observe(viewLifecycleOwner, Observer { data ->
            data.doSuccessOrFail({
                if (it.data.isNotEmpty()) {
                    showToastSuccess(it.data)
                }
            }, {
                showToastErrorWithPrompt(it)
            })
        })
        viewModel.atcRecomTokonowSendTracker.observe(viewLifecycleOwner, Observer { data ->
            data.doSuccessOrFail({
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(InfiniteRecomTracker.eventClickRecomAddToCart(firstRecomWidget, it.data, getUserSession().userId, it.data.minOrder, productId) as HashMap<String, Any>)
            }, {})
        })
        viewModel.deleteCartRecomTokonowSendTracker.observe(viewLifecycleOwner, Observer { data ->
            data.doSuccessOrFail({
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(InfiniteRecomTracker.eventClickRecomRemoveFromCart(firstRecomWidget, it.data, getUserSession().userId, it.data.minOrder, productId) as HashMap<String, Any>)
            }, {})
        })
        viewModel.atcRecomTokonowResetCard.observe(viewLifecycleOwner, Observer {
            recomPageUiUpdater.resetFailedRecomTokonowCard(it)
            updateUi()
        })
        viewModel.atcRecomTokonowNonLogin.observe(viewLifecycleOwner, Observer {
            goToLogin()
            recomPageUiUpdater.resetFailedRecomTokonowCard(it)
            updateUi()
        })
        viewModel.refreshMiniCartDataTrigger.observe(viewLifecycleOwner, Observer {
            getMiniCartData()
        })
        viewModel.minicartError.observe(viewLifecycleOwner, Observer {
            RecomServerLogger.logWarning(RecomServerLogger.TYPE_ERROR_GET_MINICART, it)
        })
    }

    private fun updateUi() {
        submitList(recomPageUiUpdater.dataList.toMutableList())
    }

    private fun getMiniCartData() {
        context?.let {
            if (getUserSession().isLoggedIn) {
                val localAddress = ChooseAddressUtils.getLocalizingAddressData(it)
                viewModel.getMiniCart(localAddress?.shop_id ?: "")
            }
        }
    }
}