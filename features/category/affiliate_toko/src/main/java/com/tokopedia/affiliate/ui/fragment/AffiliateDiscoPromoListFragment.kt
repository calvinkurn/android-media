package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDiscoBannerListUiModel
import com.tokopedia.affiliate.viewmodel.AffiliateDiscoPromoListViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AffiliateDiscoPromoListFragment :
    BaseViewModelFragment<AffiliateDiscoPromoListViewModel>() {

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private var affiliateCommissionEventViewModel: AffiliateDiscoPromoListViewModel? = null

    private var isNoMoreData: Boolean = false
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var page: Int = PAGE_ZERO

    private val discoBannerAdapter = AffiliateAdapter(AffiliateAdapterFactory())


    companion object {
        fun newInstance() = AffiliateDiscoPromoListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_affiliate_disco_promo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
        affiliateCommissionEventViewModel?.getDiscoBanners(page = page, limit = 20)
        view.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.affiliate_disco_inspiration_title)

    }

    private fun afterViewCreated() {
        view?.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.affiliate_edukasi)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)

        view?.findViewById<RecyclerView>(R.id.ssa_shop_recycler_view)?.apply {
            this.layoutManager = layoutManager
            this.adapter = discoBannerAdapter
            loadMoreTriggerListener?.let { this.addOnScrollListener(it) }
        }

        view?.findViewById<SwipeToRefresh>(R.id.discovery_promo_list_swipe_refresh)?.setOnRefreshListener {
            loadMoreTriggerListener?.resetState()
            discoBannerAdapter.resetList()
            affiliateCommissionEventViewModel?.getDiscoBanners(PAGE_ZERO)
        }
    }

    private fun setObservers() {
        affiliateCommissionEventViewModel?.getDiscoCampaignBanners()?.observe(this) {
            if (!it?.recommendedAffiliateDiscoveryCampaign?.data?.items.isNullOrEmpty()) {
                view?.findViewById<RecyclerView>(R.id.discovery_promo_list_recycler_view)?.apply {
                    discoBannerAdapter.addMoreData(
                        it.recommendedAffiliateDiscoveryCampaign?.data?.items?.mapNotNull { campaign ->
                            AffiliateDiscoBannerListUiModel(campaign)
                        }
                    )
                    this.adapter = discoBannerAdapter
                }
                loadMoreTriggerListener?.updateStateAfterGetData()
            }
        }
        affiliateCommissionEventViewModel?.getErrorMessage()?.observe(this) {
            onGetError(it)
        }
        affiliateCommissionEventViewModel?.progressBar()?.observe(this) {
            view?.findViewById<LoaderUnify>(R.id.discovery_promo_list_progress_bar)?.isVisible = it.orTrue()
        }
        affiliateCommissionEventViewModel?.noMoreDataAvailable()?.observe(this) {
            isNoMoreData = it
        }
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!isNoMoreData) {
                    this@AffiliateDiscoPromoListFragment.page = page - 1
                    affiliateCommissionEventViewModel?.getDiscoBanners(this@AffiliateDiscoPromoListFragment.page)
                }
            }
        }
    }

    private fun onGetError(error: Throwable?) {
        view?.findViewById<GlobalError>(R.id.discovery_promo_list_global_error)?.run {
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
            view?.findViewById<SwipeToRefresh>(R.id.discovery_promo_list_swipe_refresh)?.hide()
            show()
            errorAction.text = getString(R.string.affiliate_empty_ssa_list_cta)
            setActionClickListener {
                requireActivity().finish()
            }
        }
    }

    override fun getViewModelType(): Class<AffiliateDiscoPromoListViewModel> =
        AffiliateDiscoPromoListViewModel::class.java

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateCommissionEventViewModel = viewModel as AffiliateDiscoPromoListViewModel
    }

    override fun getVMFactory(): ViewModelProvider.Factory? = viewModelFactory
    override fun initInject() {
        DaggerAffiliateComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().injectCommissionEventFragment(this)
    }
}
