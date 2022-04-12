package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.affiliate.AFFILIATE_LIHAT_KATEGORI
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.adapter.AffiliateItemOffSetDecoration
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredPromotionCardModel
import com.tokopedia.affiliate.viewmodel.AffiliateRecommendedProductViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_recommended_product_fragment_layout.*
import java.util.*
import javax.inject.Inject

class AffiliateRecommendedProductFragment : BaseViewModelFragment<AffiliateRecommendedProductViewModel>(),
         PromotionClickInterface{

    private var currentPageNumber : Int = 0
    private var recommendationHasNextPage = true
    private var isSwipeRefresh = false
    private var listSize = 0

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private lateinit var affiliateRecommendedProductViewModel: AffiliateRecommendedProductViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(promotionClickInterface = this))
    private var affiliatePromoInterface : AffiliatePromoInterface? = null
    private var identifier = BOUGHT_IDENTIFIER

    companion object {
        private const val GRID_SPAN_COUNT: Int = 2
        const val BOUGHT_IDENTIFIER = "recent_purchase"
        const val LAST_VIEWED_IDENTIFIER = "recent_view"
        fun getFragmentInstance(recommendationType : String , promoInterface : AffiliatePromoInterface): Fragment {
            return AffiliateRecommendedProductFragment().apply {
                affiliatePromoInterface = promoInterface
                identifier = recommendationType
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_recommended_product_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        setUpRecyclerView()
        setUpEmptyState()
        sendScreenEvent()
        setupTickerView(getString(R.string.affiliate_black_list_title),
                getString(R.string.affiliate_black_list_description))
        affiliateRecommendedProductViewModel.isUserBlackListed = (activity as? AffiliateActivity)?.getBlackListedStatus() ?: false
        affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(identifier,PAGE_ZERO)
    }

    private fun setUpEmptyState() {
        recommended_global_error.run {
            errorIllustration.hide()
            errorSecondaryAction.gone()
            setButtonFull(true)
            if(identifier == BOUGHT_IDENTIFIER){
                errorTitle.text = getString(R.string.affiliate_no_product_bought_on_tokopedia_yet)
                errorDescription.text = getString(R.string.affiliate_no_product_bought_on_tokopedia_yet_content)
            }else {
                errorTitle.text = getString(R.string.affiliate_no_product_seen_on_tokopedia_yet)
                errorDescription.text = getString(R.string.affiliate_no_product_seen_on_tokopedia_yet_content)
            }
            errorAction.text = getString(R.string.affiliate_paste_link)
            errorAction.setOnClickListener {
               affiliatePromoInterface?.enterLinkButtonClicked()
            }

        }
    }

    private fun showEmptyState() {
        if(identifier == BOUGHT_IDENTIFIER) emptyStateRecentPurchase() else emptyStateLastSeen()
    }

    private fun emptyStateLastSeen() {
        affiliate_no_product_seen_iv.show()
        affiliate_no_product_bought_iv.hide()
    }

    private fun emptyStateRecentPurchase() {
        affiliate_no_product_seen_iv.hide()
        affiliate_no_product_bought_iv.show()
    }

    private fun setUpRecyclerView(){
        val layoutManager = StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        adapter.setVisitables(ArrayList())
        view?.findViewById<RecyclerView>(R.id.recommended_recycler_view)?.let { recyclerView ->
            recyclerView.addItemDecoration(AffiliateItemOffSetDecoration())
            recyclerView.layoutManager = layoutManager
            swipe_refresh_layout.setOnRefreshListener {
                isSwipeRefresh = true
                listSize = 0
                adapter.resetList()
                loadMoreTriggerListener?.resetState()
                affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(identifier,PAGE_ZERO)
            }
            loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
            recyclerView.adapter = adapter
            loadMoreTriggerListener?.let { recyclerView.addOnScrollListener(it) }
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(recommendationHasNextPage) {
                    sendImpressionEvent()
                    affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(
                        identifier,
                        currentPageNumber + 1
                    )
                }
            }
        }
    }

    private fun sendImpressionEvent() {
        lastItem?.let { item ->
            var itemID = ""
            item.product.productID?.let {
                itemID = it
            }
            var itemName = ""
            item.product.title?.let {
                itemName = it
            }
            var label = ""
            lastItem?.product?.commission?.amount?.let {
                label = "$itemID - {$it}"
            }
            val action = if(identifier == BOUGHT_IDENTIFIER) AffiliateAnalytics.ActionKeys.IMPRESSION_PRODUCT_PERNAH_DIBELI else AffiliateAnalytics.ActionKeys.IMPRESSION_PRODUCT_PERNAH_DILIHAT
            AffiliateAnalytics.trackEventImpression(AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,action,AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
                userSessionInterface.userId,itemID,listSize,itemName,label)
        }
    }

    private fun setObservers() {
        affiliateRecommendedProductViewModel.getShimmerVisibility().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.addShimmer(true)
                else {
                    adapter.removeShimmer(listSize)
                }
            }
        })

        affiliateRecommendedProductViewModel.getAffiliateDataItems().observe(this ,{ dataList ->
            adapter.removeShimmer(listSize)
            if(isSwipeRefresh){
                swipe_refresh_layout.isRefreshing = false
                isSwipeRefresh = !isSwipeRefresh
            }
            if (dataList.isNotEmpty()) {
                setLastDataForEvent(dataList)
                listSize += dataList.size
                hideErrorGroup()
                swipe_refresh_layout.show()
                adapter.addMoreData(dataList)
                loadMoreTriggerListener?.updateStateAfterGetData()
            } else {
                showErrorGroup()
                showEmptyState()
                swipe_refresh_layout.hide()
            }
        })

        affiliateRecommendedProductViewModel.getAffiliateItemCount().observe(this, { pageInfo ->
            currentPageNumber = pageInfo.currentPage ?: 0
            recommendationHasNextPage = pageInfo.hasNext ?: false
        })

        affiliateRecommendedProductViewModel.getErrorMessage().observe(this, { errorMessage ->
            swipe_refresh_layout.hide()
            showErrorGroup()
            showEmptyState()
        })
    }

    var lastItem : AffiliateStaggeredPromotionCardModel? = null
    private fun setLastDataForEvent(dataList: ArrayList<Visitable<AffiliateAdapterTypeFactory>>) {
        dataList[dataList.lastIndex].let {
            if(it is AffiliateStaggeredPromotionCardModel){
                lastItem = it
            }
        }
    }

    private fun showErrorGroup() {
        recommended_global_error.show()
        affiliate_no_product_bought_iv.show()
        affiliate_no_product_seen_iv.show()
    }

    private fun hideErrorGroup() {
        recommended_global_error.hide()
        affiliate_no_product_bought_iv.hide()
        affiliate_no_product_seen_iv.hide()
    }

    private fun setupTickerView(title: String?,desc :String?)
    {
        if((activity as AffiliateActivity).getBlackListedStatus()){
            affiliate_announcement_ticker_cv.show()
            affiliate_announcement_ticker.tickerTitle = title
            desc?.let {
                affiliate_announcement_ticker.setHtmlDescription(
                        it
                )
            }
            affiliate_announcement_ticker.tickerType = Ticker.TYPE_ERROR
            affiliate_announcement_ticker.setDescriptionClickEvent(object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    RouteManager.routeNoFallbackCheck(context, AFFILIATE_LIHAT_KATEGORI, AFFILIATE_LIHAT_KATEGORI)
                }
                override fun onDismiss() {}
            })
        }else {
            affiliate_announcement_ticker_cv.hide()
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectRecommendedProductFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getViewModelType(): Class<AffiliateRecommendedProductViewModel> {
        return AffiliateRecommendedProductViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateRecommendedProductViewModel = viewModel as AffiliateRecommendedProductViewModel
    }

    private fun sendScreenEvent() {

    }

    override fun onPromotionClick(productId: String, shopId : String, productName: String, productImage: String, productUrl: String, productIdentifier: String, position: Int, commison: String) {
        pushPromosikanEvent(productId,productName,position,commison)
        val origin = if(identifier == BOUGHT_IDENTIFIER) AffiliatePromotionBottomSheet.ORIGIN_PERNAH_DIBELI_PROMOSIKA else AffiliatePromotionBottomSheet.ORIGIN_TERAKHIR_DILIHAT
        AffiliatePromotionBottomSheet.newInstance(AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                null,null,
                productId, productName, productImage, productUrl,
                productIdentifier,origin,commission = commison).show(childFragmentManager, "")
    }

    private fun pushPromosikanEvent(
        productId: String,
        productName: String,
        position: Int,
        commison: String
    ) {
        var item = ""
        var eventAction = ""
        if(identifier == BOUGHT_IDENTIFIER) {
            item = AffiliateAnalytics.ItemKeys.AFFILIATE_PROMOSIKAN_PERNAH_DIBEL
            eventAction = AffiliateAnalytics.ActionKeys.PROMISIKAN_PERNAH_DIBELI
        }
        else {
            item = AffiliateAnalytics.ItemKeys.AFFILIATE_PROMOSIKAN_PERNAH_DILIHAT
            eventAction = AffiliateAnalytics.ActionKeys.PROMOSIKAN_PERNAH_DILIHAT
        }
        AffiliateAnalytics.trackEventImpression(AffiliateAnalytics.EventKeys.SELECT_CONTENT,eventAction,AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,userSessionInterface.userId,
            productId,position+1,productName,"$productId - $commison",item
        )
    }

    override fun onButtonClick(errorCta: AffiliateSearchData.SearchAffiliate.Data.Error.ErrorCta?) {

    }
}
