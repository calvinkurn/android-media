package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.NEW_DATE_FORMAT
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.TRAFFIC_TYPE
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateInfoClickInterfaces
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateRecylerBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateRecylerBottomSheet.Companion.TYPE_WITHDRAWAL
import com.tokopedia.affiliate.utils.DateUtils
import com.tokopedia.affiliate.viewmodel.AffiliateTransactionDetailViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AffiliateTransactionDetailFragment :
    BaseViewModelFragment<AffiliateTransactionDetailViewModel>(), AffiliateInfoClickInterfaces {

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    private var affiliateVM: AffiliateTransactionDetailViewModel? = null

    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private var listCount = 0

    private val adapter: AffiliateAdapter = AffiliateAdapter(
        AffiliateAdapterFactory(affiliateInfoClickInterfaces = this)
    )
    private var detailsRV: RecyclerView? = null
    override fun onInfoClick(
        title: String?,
        desc: String?,
        advanceTooltip: List<AffiliateCommissionDetailsData.GetAffiliateCommissionDetail.Data.Detail.Tooltip?>?
    ) {
        AffiliateRecylerBottomSheet.newInstance(TYPE_WITHDRAWAL, title, desc, advanceTooltip)
            .show(childFragmentManager, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_transaction_detail_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        initNavBar()
        initRv()
        initObserver()
        getData()
    }

    private fun initObserver() {
        affiliateVM?.getErrorMessage()?.observe(viewLifecycleOwner) {
            hideView()
            showError(it)
        }
        affiliateVM?.getCommissionData()?.observe(viewLifecycleOwner) {
            setData(it)
        }
        affiliateVM?.getDetailList()?.observe(viewLifecycleOwner) {
            if (it?.isNotEmpty() == true) {
                listCount += it.size
                adapter.addMoreData(it)
                loadMoreTriggerListener?.updateStateAfterGetData()
            }
        }
        affiliateVM?.getShimmerVisibility()?.observe(viewLifecycleOwner) { shimmer ->
            shimmer?.let {
                if (it) {
                    adapter.addShimmer(false)
                } else {
                    adapter.removeShimmer(listCount)
                }
            }
        }
        affiliateVM?.progressBar()?.observe(viewLifecycleOwner) { visibility ->
            if (visibility != null) {
                if (visibility) {
                    view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.show()
                    hideView()
                } else {
                    view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.gone()
                }
            }
        }
        affiliateVM?.getDetailTitle()?.observe(viewLifecycleOwner) { title ->
            title?.let {
                view?.findViewById<NavToolbar>(R.id.transaction_navToolbar)?.setToolbarTitle(it)
            }
        }
        affiliateVM?.getAppLink()?.observe((viewLifecycleOwner)) {
            RouteManager.route(context, it)
        }
    }

    private fun showError(it: Throwable?) {
        view?.findViewById<GlobalError>(R.id.commision_global_error)?.run {
            when (it) {
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
                getData()
            }
        }
    }

    private fun hideView() {
        view?.findViewById<Group>(R.id.details_view)?.hide()
        view?.findViewById<RecyclerView>(R.id.details_rv)?.hide()
    }

    private fun showView() {
        view?.findViewById<Group>(R.id.details_view)?.show()
        view?.findViewById<RecyclerView>(R.id.details_rv)?.show()
        view?.findViewById<GlobalError>(R.id.commision_global_error)?.hide()
    }

    private fun setData(commissionData: AffiliateCommissionDetailsData.GetAffiliateCommissionDetail?) {
        if (commissionData?.commissionDetailData?.commissionType != TRAFFIC_TYPE) {
            showView()
            sendOrderOpenScreenEvent(commissionData?.commissionDetailData?.pageType)
            setProductTransactionData(commissionData)
        } else {
            sendTrafficOpenScreenEvent(commissionData.commissionDetailData?.pageType)
            setTrafficTransactionView()
        }
    }

    private fun sendOrderOpenScreenEvent(pageType: String?) {
        AffiliateAnalytics.sendOpenScreenEvent(
            AffiliateAnalytics.EventKeys.OPEN_SCREEN,
            if (pageType.equals(
                    PAGE_PDP,
                    true
                )
            ) {
                AffiliateAnalytics.ScreenKeys.AFFILIATE_PENDAPATAN_PAGE_TRANSACTION_ORDER_PRODUCT
            } else {
                AffiliateAnalytics.ScreenKeys.AFFILIATE_PENDAPATAN_PAGE_TRANSACTION_ORDER_SHOP
            },
            userSessionInterface?.isLoggedIn.orFalse(),
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun sendTrafficOpenScreenEvent(pageType: String?) {
        AffiliateAnalytics.sendOpenScreenEvent(
            AffiliateAnalytics.EventKeys.OPEN_SCREEN,
            if (pageType.equals(
                    PAGE_PDP,
                    true
                )
            ) {
                AffiliateAnalytics.ScreenKeys.AFFILIATE_PENDAPATAN_PAGE_TRANSACTION_TRAFFIC_PRODUCT
            } else {
                AffiliateAnalytics.ScreenKeys.AFFILIATE_PENDAPATAN_PAGE_TRANSACTION_TRAFFIC_SHOP
            },
            userSessionInterface?.isLoggedIn.orFalse(),
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun setTrafficTransactionView() {
        view?.findViewById<RecyclerView>(R.id.details_rv)?.show()
        view?.findViewById<Group>(R.id.details_view)?.hide()
    }

    private fun setProductTransactionData(
        commissionData: AffiliateCommissionDetailsData.GetAffiliateCommissionDetail?
    ) {
        commissionData?.commissionDetailData?.cardDetail?.image?.androidURL?.let { url ->
            view?.findViewById<ImageUnify>(R.id.product_image)?.setImageUrl(
                url
            )
        }
        view?.findViewById<Typography>(R.id.product_name)?.text =
            commissionData?.commissionDetailData?.cardDetail?.cardTitle
        view?.findViewById<Typography>(R.id.product_status)?.text =
            commissionData?.commissionDetailData?.cardDetail?.cardPriceFormatted
        view?.findViewById<Typography>(R.id.shop_name)?.text =
            commissionData?.commissionDetailData?.cardDetail?.shopName
        view?.findViewById<Typography>(R.id.transaction_date)?.text =
            commissionData?.commissionDetailData?.createdAt?.let {
                DateUtils().formatDate(newFormat = NEW_DATE_FORMAT, dateString = it)
            }
        view?.findViewById<Typography>(R.id.promotion_link)?.text = getPageType(commissionData)

        view?.findViewById<Typography>(R.id.redirection_link)?.apply {
            text = buildString {
                append("Lihat ")
                append(getPageType(commissionData))
            }
            this.setOnClickListener {
                commissionData?.commissionDetailData?.cardDetail?.hyperlink?.androidURL?.let { url ->
                    affiliateVM?.extractBranchLink(url)
                }
            }
        }
    }

    private fun getPageType(commissionData: AffiliateCommissionDetailsData.GetAffiliateCommissionDetail?): String {
        return when (commissionData?.commissionDetailData?.pageType?.uppercase()) {
            PAGE_PDP -> "Produk"
            PAGE_SHOP -> "Toko"
            PAGE_EVENT -> "Event"
            else -> "None"
        }
    }

    private fun initRv() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        detailsRV = view?.findViewById(R.id.details_rv)
        detailsRV?.layoutManager = layoutManager
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
        detailsRV?.adapter = adapter
        loadMoreTriggerListener?.let { detailsRV?.addOnScrollListener(it) }
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (affiliateVM?.commissionType == TRAFFIC_TYPE) {
                    affiliateVM?.affiliateCommission(transactionId, page - 1)
                }
            }
        }
    }

    private fun initNavBar() {
        view?.findViewById<NavToolbar>(R.id.transaction_navToolbar)?.run {
            setOnBackButtonClickListener {
                activity?.finish()
            }
        }
    }

    private var transactionId = ""

    private fun getData() {
        arguments?.getString(PARAM_TRANSACTION, null)?.let { tID ->
            transactionId = tID
            affiliateVM?.affiliateCommission(tID, PAGE_ZERO)
        }
        listCount = 0
        adapter.clearAllElements()
    }

    companion object {
        private const val PARAM_TRANSACTION = "PARAM_TRANSACTION"
        private const val PAGE_PDP = "PDP"
        private const val PAGE_SHOP = "SHOP"
        private const val PAGE_EVENT = "CAMPAIGN"
        fun newInstance(transactionId: String?): Fragment {
            return AffiliateTransactionDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_TRANSACTION, transactionId)
                }
            }
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<AffiliateTransactionDetailViewModel> {
        return AffiliateTransactionDetailViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliateTransactionDetailViewModel
    }

    override fun initInject() {
        getComponent().injectWithdrawalInfoFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
}
