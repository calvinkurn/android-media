package com.tokopedia.affiliate.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.viewholder.AffiliateSharedProductCardsItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSharedProductCardsModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionHistoryViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class AffiliatePromotionHistoryFragment :
    BaseViewModelFragment<AffiliatePromotionHistoryViewModel>(), ProductClickInterface {

    private var totalDataItemsCount: Int = 0
    private var isSwipeRefresh = false
    private var listSize = 0

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private lateinit var affiliatePromotionViewModel: AffiliatePromotionHistoryViewModel
    lateinit var adapter: AffiliateAdapter
    private var isUserBlackListed = false

    companion object {
        fun getFragmentInstance(isBlackListed: Boolean): Fragment {
            return AffiliatePromotionHistoryFragment().apply {
                isUserBlackListed = isBlackListed
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        setObservers()
    }

    private fun initAdapter() {
        adapter = AffiliateAdapter(AffiliateAdapterFactory(productClickInterface = this))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.affiliate_promotion_history_fragment_layout,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val productsRV = view?.findViewById<RecyclerView>(R.id.products_rv)
        adapter.setVisitables(ArrayList())
        productsRV?.layoutManager = layoutManager
        view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.setOnRefreshListener {
            isSwipeRefresh = true
            resetItems()
        }
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
        productsRV?.adapter = adapter
        loadMoreTriggerListener?.let { productsRV?.addOnScrollListener(it) }
        affiliatePromotionViewModel.getAffiliatePerformance(PAGE_ZERO)
    }

    private fun resetItems() {
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        affiliatePromotionViewModel.getAffiliatePerformance(PAGE_ZERO)
    }

    private fun showNoAffiliate() {
        view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.hide()
        view?.findViewById<DeferredImageView>(R.id.affiliate_no_product_iv)?.show()
        view?.findViewById<GlobalError>(R.id.home_global_error)?.run {
            show()
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_choose_product)
            errorDescription.text = getString(R.string.affiliate_choose_product_description)
            errorAction.text = getString(R.string.affiliate_promote_affiliatw)
            errorSecondaryAction.gone()
            setActionClickListener {
                activity?.setResult(Activity.RESULT_OK, Intent())
                activity?.finish()
            }
        }
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (totalItemsCount < totalDataItemsCount) {
                    sendImpressionEvent()
                    affiliatePromotionViewModel.getAffiliatePerformance(page - 1)
                }
            }
        }
    }

    private fun sendImpressionEvent() {
        lastItem?.let { item ->
            val itemID = item.product.itemID
            var itemName = ""
            item.product.itemTitle?.let {
                itemName = it
            }
            AffiliateAnalytics.trackEventImpression(
                AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,
                AffiliateAnalytics.ActionKeys.IMPRESSION_DAFTAR_LINK_PRODUK,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE_GENERATED_LINK_HIST,
                userSessionInterface.userId,
                itemID,
                listSize,
                itemName,
                itemID
            )
        }
    }

    private fun setObservers() {
        affiliatePromotionViewModel.getShimmerVisibility().observe(this) { visibility ->
            if (visibility != null) {
                if (visibility) {
                    adapter.addShimmer()
                } else {
                    adapter.removeShimmer(listSize)
                }
            }
        }
        affiliatePromotionViewModel.getAffiliateDataItems().observe(this) { dataList ->
            adapter.removeShimmer(listSize)
            if (isSwipeRefresh) {
                view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.isRefreshing = false
                isSwipeRefresh = !isSwipeRefresh
            }
            if (dataList.isNotEmpty()) {
                setLastDataForEvent(dataList)
                listSize += dataList.size
                adapter.addMoreData(dataList)
                loadMoreTriggerListener?.updateStateAfterGetData()
            } else if (dataList.isNullOrEmpty() && listSize == 0) {
                showNoAffiliate()
            }
        }
        affiliatePromotionViewModel.getErrorMessage().observe(this) { error ->
            view?.findViewById<GlobalError>(R.id.home_global_error)?.run {
                when (error) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        setType(GlobalError.NO_CONNECTION)
                    }
                    is IllegalStateException -> {
                        setType(GlobalError.PAGE_FULL)
                    }
                    else -> {
                        setType(GlobalError.SERVER_ERROR)
                    }
                }
                show()
                setActionClickListener {
                    hide()
                    resetItems()
                }
            }
        }
        affiliatePromotionViewModel.getAffiliateItemCount().observe(this) { itemCount ->
            if (itemCount != 0) {
                view?.findViewById<Typography>(R.id.affiliate_products_count)?.text =
                    getString(R.string.affiliate_product_count, itemCount.toString())
                totalDataItemsCount = itemCount
            }
        }
    }

    private var lastItem: AffiliateSharedProductCardsModel? = null
    private fun setLastDataForEvent(dataList: ArrayList<Visitable<AffiliateAdapterTypeFactory>>) {
        dataList[dataList.lastIndex].let {
            if (it is AffiliateSharedProductCardsModel) {
                lastItem = it
            }
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectPromotionHistoryFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getViewModelType(): Class<AffiliatePromotionHistoryViewModel> {
        return AffiliatePromotionHistoryViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliatePromotionViewModel = viewModel as AffiliatePromotionHistoryViewModel
    }

    override fun onProductClick(
        productId: String,
        productName: String,
        productImage: String,
        productUrl: String,
        productIdentifier: String,
        status: Int?,
        type: String?,
        ssaInfo: AffiliatePromotionBottomSheetParams.SSAInfo?
    ) {
        if (status == AffiliateSharedProductCardsItemVH.PRODUCT_ACTIVE) {
            AffiliatePromotionBottomSheet.newInstance(
                AffiliatePromotionBottomSheetParams(
                    null,
                    productId,
                    productName,
                    productImage,
                    productUrl,
                    productIdentifier,
                    AffiliatePromotionBottomSheet.ORIGIN_HOME_GENERATED,
                    !isUserBlackListed,
                    type = type,
                    ssaInfo = ssaInfo
                ),
                AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                null
            ).show(childFragmentManager, "")
        } else {
            AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_PRODUCT_INACTIVE)
                .show(childFragmentManager, "")
        }
    }
}
