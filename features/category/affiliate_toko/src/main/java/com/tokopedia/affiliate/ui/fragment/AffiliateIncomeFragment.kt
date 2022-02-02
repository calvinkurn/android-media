package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerRangeChangeInterface
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.viewmodel.AffiliateIncomeViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.model.response.AffiliateKycDetailsData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AffiliateIncomeFragment : TkpdBaseV4Fragment(), AffiliateDatePickerRangeChangeInterface{

    private lateinit var affiliateIncomeViewModel : AffiliateIncomeViewModel
    private var userName : String = ""
    private var profilePicture : String = ""
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())
    private var listVisitable: List<Visitable<AffiliateAdapterTypeFactory>> = arrayListOf()
    private var listSize = 0
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var bottomNavBarClickListener : AffiliateBottomNavBarInterface? = null

    companion object {
        fun getFragmentInstance(userNameParam : String, profilePictureParam : String, affiliateBottomNavBarClickListener: AffiliateBottomNavBarInterface): Fragment {
            return AffiliateIncomeFragment().apply {
                userName = userNameParam
                profilePicture = profilePictureParam
                bottomNavBarClickListener = affiliateBottomNavBarClickListener
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_income_fragment_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        affiliateIncomeViewModel = ViewModelProviders.of(this)[AffiliateIncomeViewModel::class.java]
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    override fun getScreenName(): String {
        return  ""
    }

    private fun setObservers() {
        affiliateIncomeViewModel.getAffiliateBalanceData().observe(this, {
            if(it.status == 1)
                setAffiliateBalance(it)
            else
                it.error?.message?.let { it1 -> setErrorState(it1) }
        })

        affiliateIncomeViewModel.getAffiliateDataItems().observe(this, {
            adapter.removeShimmer(listSize)
            stopSwipeRefresh()
            if(it.isEmpty() && listSize == 0){
                showGlobalErrorEmptyState()
            } else {
                hideGlobalErrorEmptyState()
                listSize += it.size
                adapter.addMoreData(it)
                loadMoreTriggerListener?.updateStateAfterGetData()
            }
        })
        affiliateIncomeViewModel.getShimmerVisibility().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.addShimmer()
                else {
                    stopSwipeRefresh()
                    adapter.removeShimmer(listSize)
                }
            }
        })
        affiliateIncomeViewModel.getErrorMessage().observe(this, { error ->
            view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.run {
                when(error) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        setType(GlobalError.NO_CONNECTION)
                        show()
                    }
                    is IllegalStateException -> {
                        setType(GlobalError.PAGE_FULL)
                        show()
                    }
                    else -> {
                        setErrorState(error.message)
                    }
                }
                setActionClickListener {
                    hide()
                    affiliateIncomeViewModel.getAffiliateBalance()
                    affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)
                }
            }

        })
        affiliateIncomeViewModel.getRangeChange().observe(this,{changed ->
            if(changed) {
                resetItems()
                view?.findViewById<Typography>(R.id.date_range_text)?.text = affiliateIncomeViewModel.getSelectedDate()
            }
        })
        affiliateIncomeViewModel.getAffiliateKycData().observe(this,{
            onGetAffiliateKycData(it)
        })

        affiliateIncomeViewModel.getAffiliateKycLoader().observe(this,{ show ->
            if(show){
                view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.invisible()
                view?.findViewById<LoaderUnify>(R.id.tarik_saldo_loader)?.show()
            }
            else{
                view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.show()
                view?.findViewById<LoaderUnify>(R.id.tarik_saldo_loader)?.hide()
            }
        })

        affiliateIncomeViewModel.getKycErrorMessage().observe(this,{
            view?.let {
            Toaster.build(it, getString(R.string.affiliate_retry_message), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        })}

    private fun onGetAffiliateKycData(kycProjectInfo: AffiliateKycDetailsData.KycProjectInfo) {
        when(kycProjectInfo.status){
            KYC_DONE ->{
                if(TokopediaUrl.getInstance().GQL.contains("staging"))
                    RouteManager.route(context, WITHDRAWAL_APPLINK_STAGING)
                else
                    RouteManager.route(context, WITHDRAWAL_APPLINK_PROD)
            }
            else -> RouteManager.route(context, APP_LINK_KYC)
        }
    }

    private fun stopSwipeRefresh() {
        view?.findViewById<SwipeRefreshLayout>(R.id.swipe)?.isRefreshing = false
    }

    private fun hideGlobalErrorEmptyState() {
        view?.findViewById<DeferredImageView>(R.id.affiliate_no_transaction_iv)?.hide()
        view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.hide()
        view?.findViewById<DeferredImageView>(R.id.affiliate_no__default_transaction_iv)?.hide()
    }

    private fun resetItems() {
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        hideGlobalErrorEmptyState()
        affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)
    }

    private fun showGlobalErrorEmptyState() {
        val defaultSelected = affiliateIncomeViewModel.getSelectedDate() == AffiliateBottomDatePicker.SEVEN_DAYS
        if(defaultSelected) view?.findViewById<DeferredImageView>(R.id.affiliate_no__default_transaction_iv)?.show()
        else view?.findViewById<DeferredImageView>(R.id.affiliate_no_transaction_iv)?.show()

        view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.apply {
            show()
            errorIllustration.hide()
            errorTitle.text = if(!defaultSelected) getString(R.string.affiliate_empty_transaction) else getString(R.string.affiliate_default_no_transaction)
            errorDescription.text = if(!defaultSelected) getString(R.string.affiliate_empty_transaction_description) else getString(R.string.affiliate_default_no_transaction_description)
            setButtonFull(true)
            errorSecondaryAction.gone()
            errorAction.text = if(!defaultSelected) getString(R.string.affiliate_choose_date) else getString(R.string.affiliate_promote_product_cta)
            setActionClickListener {
                if (!defaultSelected) AffiliateBottomDatePicker.newInstance(affiliateIncomeViewModel.getSelectedDate(),this@AffiliateIncomeFragment).show(childFragmentManager, "")
                else bottomNavBarClickListener?.selectItem(AffiliateActivity.PROMO_MENU,R.id.menu_promo_affiliate)
            }
        }
    }

    private fun setErrorState(message: String?) {
        view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.apply {
            show()
            errorTitle.text = message
            setButtonFull(true)
            errorSecondaryAction.gone()
            setActionClickListener {
                hide()
                affiliateIncomeViewModel.getAffiliateBalance()
                affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)
            }
        }
    }

    private fun sendPendapatanEvent(eventAction: String, eventLabel: String) {
        context?.let {
            AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.CLICK_PG,
                eventAction,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE,
                eventLabel,
                UserSession(it).userId
            )
        }
    }

    private fun setAffiliateBalance(affiliateBalance: AffiliateBalance.AffiliateBalance.Data) {
        affiliateBalance.apply {
            view?.findViewById<Typography>(R.id.saldo_amount_affiliate)?.let {
                it.text = amountFormatted
                it.show()
            }
            view?.findViewById<LoaderUnify>(R.id.affiliate_saldo_progress_bar)?.gone()
        }
    }

    private fun afterViewCreated() {
        initUi()
        affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)
        view?.findViewById<Typography>(R.id.withdrawal_user_name)?.text = userName
        view?.findViewById<SwipeRefreshLayout>(R.id.swipe)?.let {
            it.setOnRefreshListener {
                affiliateIncomeViewModel.getAffiliateBalance()
                resetItems()
            }
            view?.findViewById<AppBarLayout>(R.id.appbar)?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener {appBarLayout, verticalOffset ->
                it.isEnabled = verticalOffset == 0
            })
        }
        view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.setOnClickListener {
           sendPendapatanEvent(AffiliateAnalytics.ActionKeys.CLICK_TARIK_SALDO,"")
           sendTarikSaldoClickEvent()
           openWithdrawalScreen()
        }
        ImageHandler.loadImageCircle2(context, view?.findViewById<ImageUnify>(R.id.withdrawal_user_image), profilePicture)
        view?.findViewById<Typography>(R.id.date_range_text)?.text = affiliateIncomeViewModel.getSelectedDate()
        view?.findViewById<ConstraintLayout>(R.id.date_range)?.setOnClickListener {
            AffiliateBottomDatePicker.newInstance(AffiliateBottomDatePicker.TODAY,this).show(childFragmentManager, "")
        }
        view?.findViewById<RecyclerView>(R.id.withdrawal_transactions_rv)?.let {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setVisitables(listVisitable)
            it.layoutManager = layoutManager
            it.adapter = adapter

            loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
            it.adapter = adapter
            loadMoreTriggerListener?.let { listener ->
                it.addOnScrollListener(listener)
            }
        }
        view?.findViewById<NavToolbar>(R.id.withdrawal_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.affiliate_withdrawal)
        }
        initDateRangeClickListener()
        affiliateIncomeViewModel.getAffiliateBalance()
    }

    private fun sendTarikSaldoClickEvent() {
        context?.let { ctx ->
            AffiliateAnalytics.sendEvent(
                    AffiliateAnalytics.EventKeys.EVENT_VALUE_CLICK,
                    AffiliateAnalytics.CategoryKeys.PENDAPATAN_PAGE,
                    AffiliateAnalytics.ActionKeys.CLICK_TARIK_SALDO,
                    "",
                    UserSession(ctx).userId)
        }
    }

    private fun initUi() {
        when (RemoteConfigInstance.getInstance().abTestPlatform.getString(
            AFFILIATE_WITHDRAWAL,
            ""
        )) {
            AFFILIATE_WITHDRAWAL -> view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.show()
            else -> view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.invisible()
        }
    }

    private fun openWithdrawalScreen() {
        affiliateIncomeViewModel.getKycDetails()
    }

    private fun initDateRangeClickListener() {
        view?.findViewById<ConstraintLayout>(R.id.date_range)?.setOnClickListener {
            sendPendapatanEvent(AffiliateAnalytics.ActionKeys.CLICK_FILTER_DATE,"")
            AffiliateBottomDatePicker.newInstance(affiliateIncomeViewModel.getSelectedDate(),this).show(childFragmentManager, "")
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(affiliateIncomeViewModel.hasNext)
                    affiliateIncomeViewModel.getAffiliateTransactionHistory(page - 1)
            }
        }
    }

//    override fun getVMFactory(): ViewModelProvider.Factory {
//        return viewModelProvider
//    }
//
//    override fun initInject() {
//        getComponent().injectIncomeFragment(this)
//    }

//    private fun getComponent(): AffiliateComponent =
//            DaggerAffiliateComponent
//                    .builder()
//                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
//                    .build()
//
//    override fun getViewModelType(): Class<AffiliateIncomeViewModel> {
//        return AffiliateIncomeViewModel::class.java
//    }
//
//    override fun setViewModel(viewModel: BaseViewModel) {
//        affiliateIncomeViewModel = viewModel as AffiliateIncomeViewModel
//    }

    override fun rangeChanged(range: AffiliateDatePickerData) {
        sendPendapatanEvent(AffiliateAnalytics.ActionKeys.CLICK_SIMPAN,range.value)
        affiliateIncomeViewModel.onRangeChanged(range)
    }

    override fun onRangeSelectionButtonClicked() {

    }

}