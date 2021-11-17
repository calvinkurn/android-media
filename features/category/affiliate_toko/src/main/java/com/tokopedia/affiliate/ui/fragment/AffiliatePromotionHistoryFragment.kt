package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.viewholder.AffiliateSharedProductCardsItemVH
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionHistoryViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_promotion_history_fragment_layout.*
import kotlinx.android.synthetic.main.affiliate_promotion_history_fragment_layout.affiliate_no_product_iv
import kotlinx.android.synthetic.main.affiliate_promotion_history_fragment_layout.home_global_error
import kotlinx.android.synthetic.main.affiliate_promotion_history_fragment_layout.products_rv
import kotlinx.android.synthetic.main.affiliate_promotion_history_fragment_layout.swipe_refresh_layout
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class AffiliatePromotionHistoryFragment : BaseViewModelFragment<AffiliatePromotionHistoryViewModel>(), ProductClickInterface{

    private var totalDataItemsCount: Int = 0
    private var isSwipeRefresh = false
    private var listSize = 0
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private lateinit var affiliatePromotionViewModel: AffiliatePromotionHistoryViewModel
    lateinit var adapter: AffiliateAdapter
    private var isUserBlackListed = false

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliatePromotionHistoryFragment()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_promotion_history_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        products_rv.layoutManager = layoutManager
        swipe_refresh_layout.setOnRefreshListener {
            isSwipeRefresh = true
            resetItems()
        }
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
        products_rv.adapter = adapter
        loadMoreTriggerListener?.let { products_rv.addOnScrollListener(it) }
        affiliatePromotionViewModel.getAffiliatePerformance(PAGE_ZERO)
    }

    private fun resetItems() {
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        affiliatePromotionViewModel.getAffiliatePerformance(PAGE_ZERO)
    }



    private fun showNoAffiliate() {
        swipe_refresh_layout.hide()
        affiliate_no_product_iv.show()
        home_global_error.run {
            show()
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_choose_product)
            errorDescription.text = getString(R.string.affiliate_choose_product_description)
            setButtonFull(true)
            errorAction.text = getString(R.string.affiliate_promote_affiliatw)
            errorSecondaryAction.gone()
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(totalItemsCount < totalDataItemsCount)
                    affiliatePromotionViewModel.getAffiliatePerformance(page - 1)
            }
        }
    }

    private fun setObservers() {
        affiliatePromotionViewModel.getShimmerVisibility().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.addShimmer()
                else
                    adapter.removeShimmer(listSize)
            }
        })
        affiliatePromotionViewModel.getAffiliateDataItems().observe(this ,{ dataList ->
            adapter.removeShimmer(listSize)
            if(isSwipeRefresh){
                swipe_refresh_layout.isRefreshing = false
                isSwipeRefresh = !isSwipeRefresh
            }
            if (dataList.isNotEmpty()) {
                listSize += dataList.size
                adapter.addMoreData(dataList)
                loadMoreTriggerListener?.updateStateAfterGetData()
            } else {
                showNoAffiliate()
            }
        })
        affiliatePromotionViewModel.getErrorMessage().observe(this, { error ->
            home_global_error.run {
                when(error) {
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
        })
        affiliatePromotionViewModel.getAffiliateItemCount().observe(this, { itemCount ->
            affiliate_products_count.text = getString(R.string.affiliate_product_count, itemCount.toString())
            totalDataItemsCount = itemCount
        })
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

    override fun getViewModelType(): Class<AffiliatePromotionHistoryViewModel> {
        return AffiliatePromotionHistoryViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliatePromotionViewModel = viewModel as AffiliatePromotionHistoryViewModel
    }

    override fun onProductClick(productId : String, productName: String, productImage: String, productUrl: String, productIdentifier: String, status : Int?) {
        if(status == AffiliateSharedProductCardsItemVH.PRODUCT_ACTIVE){
            AffiliatePromotionBottomSheet.newInstance(AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                    null,null,productId , productName , productImage, productUrl,productIdentifier,
                    AffiliatePromotionBottomSheet.ORIGIN_HOME,!isUserBlackListed).show(childFragmentManager, "")
        }else {
            AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_PRODUCT_INACTIVE).show(childFragmentManager, "")
        }
    }

}
