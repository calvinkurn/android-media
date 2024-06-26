package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
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
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.viewmodel.AffiliateSSAShopViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AffiliateSSAShopListFragment :
    BaseViewModelFragment<AffiliateSSAShopViewModel>(),
    ProductClickInterface {

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    @JvmField
    @Inject
    var userSessionInterface: UserSessionInterface? = null

    private var affiliateSSAShopViewModel: AffiliateSSAShopViewModel? = null
    private val ssaAdapter: AffiliateAdapter by lazy {
        AffiliateAdapter(
            AffiliateAdapterFactory(productClickInterface = this),
            source = AffiliateAdapter.PageSource.SOURCE_SSA_SHOP
        )
    }

    private var isNoMoreData: Boolean = false
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var page: Int = PAGE_ZERO

    companion object {
        fun newInstance() = AffiliateSSAShopListFragment()
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
        return inflater.inflate(R.layout.fragment_affiliate_ssa_shop_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
        affiliateSSAShopViewModel?.fetchSSAShopList(page)
    }

    private fun afterViewCreated() {
        view?.findViewById<NavToolbar>(R.id.ssa_shop_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                getString(R.string.affiliate_ssa_shop_title)
            setOnBackButtonClickListener {
                activity?.finish()
            }
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
        ssaAdapter.setVisitables(ArrayList())
        view?.findViewById<RecyclerView>(R.id.ssa_shop_recycler_view)?.apply {
            this.layoutManager = layoutManager
            this.adapter = ssaAdapter
            loadMoreTriggerListener?.let { this.addOnScrollListener(it) }
        }
        view?.findViewById<SwipeToRefresh>(R.id.ssa_shop_swipe_refresh)?.setOnRefreshListener {
            loadMoreTriggerListener?.resetState()
            ssaAdapter.resetList()
            affiliateSSAShopViewModel?.fetchSSAShopList(PAGE_ZERO)
        }
        view?.findViewById<UnifyButton>(R.id.empty_ssa_state_cta)?.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setObservers() {
        affiliateSSAShopViewModel?.getSSAShopList()?.observe(this) {
            view?.findViewById<SwipeToRefresh>(R.id.ssa_shop_swipe_refresh)?.isRefreshing = false
            if (it.isNullOrEmpty() && page == PAGE_ZERO) {
                view?.findViewById<SwipeToRefresh>(R.id.ssa_shop_swipe_refresh)?.hide()
                view?.findViewById<Group>(R.id.empty_ssa_state_group)?.show()
            } else {
                view?.findViewById<SwipeToRefresh>(R.id.ssa_shop_swipe_refresh)?.show()
                view?.findViewById<Group>(R.id.empty_ssa_state_group)?.hide()
                ssaAdapter.addMoreData(it)
                loadMoreTriggerListener?.updateStateAfterGetData()
            }
        }
        affiliateSSAShopViewModel?.getErrorMessage()?.observe(this) {
            view?.findViewById<SwipeToRefresh>(R.id.ssa_shop_swipe_refresh)?.isRefreshing = false
            onGetError(it)
        }
        affiliateSSAShopViewModel?.progressBar()?.observe(this) {
            view?.findViewById<LoaderUnify>(R.id.ssa_shop_progress_bar)?.isVisible = it.orTrue()
        }
        affiliateSSAShopViewModel?.noMoreDataAvailable()?.observe(this) {
            isNoMoreData = it
        }
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!isNoMoreData) {
                    this@AffiliateSSAShopListFragment.page = page - 1
                    affiliateSSAShopViewModel?.fetchSSAShopList(this@AffiliateSSAShopListFragment.page)
                }
            }
        }
    }

    private fun onGetError(error: Throwable?) {
        view?.findViewById<GlobalError>(R.id.ssa_shop_global_error)?.run {
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
            view?.findViewById<SwipeToRefresh>(R.id.ssa_shop_swipe_refresh)?.hide()
            show()
            errorAction.text = getString(R.string.affiliate_empty_ssa_list_cta)
            setActionClickListener {
                requireActivity().finish()
            }
        }
    }

    override fun getViewModelType(): Class<AffiliateSSAShopViewModel> =
        AffiliateSSAShopViewModel::class.java

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateSSAShopViewModel = viewModel as AffiliateSSAShopViewModel
    }

    override fun getVMFactory(): ViewModelProvider.Factory? = viewModelFactory
    override fun initInject() {
        DaggerAffiliateComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().injectSSAShopListFragment(this)
    }

    override fun onProductClick(
        productId: String,
        productName: String,
        productImage: String,
        productUrl: String,
        productIdentifier: String,
        status: Int?,
        type: String?,
        ssaInfo: AffiliatePromotionBottomSheetParams.SSAInfo?,
        imageArray: List<String?>?
    ) {
        AffiliatePromotionBottomSheet.newInstance(
            AffiliatePromotionBottomSheetParams(
                null, productId, productName, productImage, productUrl, productIdentifier,
                AffiliatePromotionBottomSheet.ORIGIN_SSA_SHOP, true, type = type,
                ssaInfo = ssaInfo
            ),
            AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
            null
        ).show(childFragmentManager, "")
    }
}
