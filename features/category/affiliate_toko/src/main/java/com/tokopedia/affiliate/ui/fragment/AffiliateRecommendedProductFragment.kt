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
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.AffiliateSearchData
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.viewmodel.AffiliateRecommendedProductViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_recommended_product_fragment_layout.*
import java.util.*
import javax.inject.Inject

class AffiliateRecommendedProductFragment : BaseViewModelFragment<AffiliateRecommendedProductViewModel>(),
         PromotionClickInterface{

    private var totalDataItemsCount: Int = 0
    private var isSwipeRefresh = false

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private lateinit var affiliateRecommendedProductViewModel: AffiliateRecommendedProductViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(promotionClickInterface = this)
    )

    companion object {
        private const val GRID_SPAN_COUNT: Int = 2
        fun getFragmentInstance(): Fragment {
            return AffiliateRecommendedProductFragment().apply {
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
        affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(PAGE_ZERO)
    }

    private fun setUpEmptyState() {
        recommended_global_error.run {
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_never_bought_product)
            errorDescription.text = getString(R.string.affiliate_still_buy_products)
            setButtonFull(true)
            errorAction.text = getString(R.string.affiliate_paste_link)
            errorAction.setOnClickListener {
                // TODO Interface parent fragment
                // product_link_et.editingState(true)
            }
            errorSecondaryAction.gone()
        }
    }

    private fun setUpRecyclerView(){
        val layoutManager = StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        adapter.setVisitables(ArrayList())
        view?.findViewById<RecyclerView>(R.id.recommended_recycler_view)?.let { recyclerView ->
            recyclerView.layoutManager = layoutManager
            swipe_refresh_layout.setOnRefreshListener {
                isSwipeRefresh = true
                loadMoreTriggerListener?.resetState()
                affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(PAGE_ZERO)
            }
            loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
            recyclerView.adapter = adapter
            loadMoreTriggerListener?.let { recyclerView.addOnScrollListener(it) }
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(totalItemsCount < totalDataItemsCount)
                    affiliateRecommendedProductViewModel.getAffiliateRecommendedProduct(page - 1)
            }
        }
    }

    private fun setObservers() {
        affiliateRecommendedProductViewModel.getShimmerVisibility().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.startShimmer(true)
                else
                    adapter.stopShimmer()
            }
        })

        affiliateRecommendedProductViewModel.getAffiliateDataItems().observe(this ,{ dataList ->
            if(isSwipeRefresh){
                swipe_refresh_layout.isRefreshing = false
                isSwipeRefresh = !isSwipeRefresh
            }
            if (dataList.isNotEmpty()) {
                error_group.hide()
                adapter.addMoreData(dataList)
                loadMoreTriggerListener?.updateStateAfterGetData()
            } else {
                error_group.show()
            }
        })

        affiliateRecommendedProductViewModel.getAffiliateItemCount().observe(this, { itemCount ->

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

    override fun getViewModelType(): Class<AffiliateRecommendedProductViewModel> {
        return AffiliateRecommendedProductViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateRecommendedProductViewModel = viewModel as AffiliateRecommendedProductViewModel
    }

    private fun sendScreenEvent() {
//        AffiliateAnalytics.sendEvent(
//                AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
//                AffiliateAnalytics.ActionKeys.IMPRESSION_HOME_PORTAL,
//                AffiliateAnalytics.CategoryKeys.HOME_PORTAL,
//                "",userSessionInterface.userId)
    }

    override fun onPromotionClick(productId: String, productName: String, productImage: String, productUrl: String, productIdentifier: String) {
        AffiliatePromotionBottomSheet.newInstance(productId, productName, productImage, productUrl,
                productIdentifier,AffiliatePromotionBottomSheet.ORIGIN_PROMOSIKAN).show(childFragmentManager, "")
    }

    override fun onButtonClick(errorCta: AffiliateSearchData.SearchAffiliate.Data.Error.ErrorCta?) {

    }
}
