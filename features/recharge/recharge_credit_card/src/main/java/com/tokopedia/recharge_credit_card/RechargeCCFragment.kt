package com.tokopedia.recharge_credit_card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.recharge_credit_card.analytics.CreditCardAnalytics
import com.tokopedia.recharge_credit_card.bottomsheet.CCBankListBottomSheet
import com.tokopedia.recharge_credit_card.datamodel.TickerCreditCard
import com.tokopedia.recharge_credit_card.di.RechargeCCInstance
import com.tokopedia.recharge_credit_card.util.RechargeCCUtil
import com.tokopedia.recharge_credit_card.viewmodel.RechargeCCViewModel
import com.tokopedia.recharge_credit_card.viewmodel.RechargeSubmitCCViewModel
import com.tokopedia.recharge_credit_card.widget.CCClientNumberWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_recharge_cc.*
import javax.inject.Inject

class RechargeCCFragment : BaseDaggerFragment() {

    private lateinit var rechargeCCViewModel: RechargeCCViewModel
    private lateinit var rechargeSubmitCCViewModel: RechargeSubmitCCViewModel
    private lateinit var checkoutPassDataState: DigitalCheckoutPassData
    private lateinit var saveInstanceManager: SaveInstanceCacheManager
    private lateinit var performanceMonitoring: PerformanceMonitoring

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var creditCardAnalytics: CreditCardAnalytics

    private var operatorIdSelected: String = ""
    private var productIdSelected: String = ""
    private var categoryId: String = ""
    private var menuId: String = ""

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.let {
            val creditCardComponent = RechargeCCInstance.getComponent(it.application)
            creditCardComponent.inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        activity?.let {
            saveInstanceManager = SaveInstanceCacheManager(it, savedInstanceState)
        }

        if (savedInstanceState != null) {
            checkoutPassDataState = saveInstanceManager.get(EXTRA_STATE_CHECKOUT_PASS_DATA,
                    DigitalCheckoutPassData::class.java, null)!!
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (::checkoutPassDataState.isInitialized) {
            saveInstanceManager.put(EXTRA_STATE_CHECKOUT_PASS_DATA, checkoutPassDataState)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recharge_cc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializedViewModel()
        getDataBundle()
        getTickerData()
        
        cc_widget_client_number.setListener(object : CCClientNumberWidget.ActionListener {
            override fun onClickNextButton(clientNumber: String) {
                creditCardAnalytics.clickToConfirmationPage(
                        categoryId, operatorIdSelected)
                dialogConfirmation()
            }

            override fun onShowErrorCreditCard(message: String) {
                KeyboardHandler.hideSoftKeyboard(activity)
                showErrorToaster(message)
            }

            override fun onCheckPrefix(clientNumber: String) {
                checkPrefixCreditCardNumber(clientNumber)
            }
        })

        list_bank_btn.setOnClickListener {
            activity?.let {
                val bottomSheetBankList = CCBankListBottomSheet(categoryId)
                bottomSheetBankList.show(it.supportFragmentManager, "Bank list")
            }
        }

        creditCardAnalytics.impressionInitialPage(categoryId, "")
    }

    private fun getDataBundle() {
        arguments?.let {
            categoryId = it.getString(CATEGORY_ID)
            menuId = it.getString(MENU_ID)
        }
    }

    private fun initializedViewModel() {
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            rechargeCCViewModel = viewModelProvider.get(RechargeCCViewModel::class.java)
            rechargeSubmitCCViewModel = viewModelProvider.get(RechargeSubmitCCViewModel::class.java)
        }
    }

    private fun getTickerData() {
        rechargeCCViewModel.getMenuDetail(
                GraphqlHelper.loadRawString(resources, R.raw.query_cc_menu_detail), menuId)
        rechargeCCViewModel.tickers.observe(this, Observer {
            renderTicker(it)
            performanceMonitoring.stopTrace()
        })
    }

    private fun renderTicker(tickers: List<TickerCreditCard>) {
        if (tickers.isNotEmpty()) {
            val messages = ArrayList<TickerData>()
            for (item in tickers) {
                messages.add(TickerData(item.name, item.content,
                        when (item.type) {
                            "warning" -> Ticker.TYPE_WARNING
                            "info" -> Ticker.TYPE_INFORMATION
                            "success" -> Ticker.TYPE_ANNOUNCEMENT
                            "error" -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }))
            }
            context?.run {
                cc_ticker_view.addPagerView(TickerPagerAdapter(this, messages), messages)
            }
            cc_ticker_view.visibility = View.VISIBLE
        } else {
            cc_ticker_view.visibility = View.GONE
        }
    }

    private fun checkPrefixCreditCardNumber(clientNumber: String) {
        rechargeCCViewModel.getPrefixes(
                GraphqlHelper.loadRawString(resources, R.raw.query_cc_prefix_operator),
                clientNumber, menuId)

        rechargeCCViewModel.creditCardSelected.observe(viewLifecycleOwner, Observer {
            operatorIdSelected = it.operatorId.toString()
            productIdSelected = it.defaultProductId.toString()
            cc_widget_client_number.setImageIcon(it.imageUrl)
        })

        rechargeCCViewModel.errorPrefix.observe(viewLifecycleOwner, Observer {
            showErrorToaster(it)
        })

        rechargeCCViewModel.bankNotSupported.observe(viewLifecycleOwner, Observer {
            cc_widget_client_number.setErrorTextField(getString(R.string.cc_bank_is_not_supported))
        })
    }

    private fun showErrorToaster(message: String) {
        view?.run {
            Toaster.make(this, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
        }
    }

    private fun dialogConfirmation() {
        if (operatorIdSelected.isNotEmpty() && productIdSelected.isNotEmpty()) {
            context?.let {
                val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                dialog.setTitle(getString(R.string.cc_title_dialog))
                dialog.setDescription(getString(R.string.cc_desc_dialog))
                dialog.setPrimaryCTAText(getString(R.string.cc_cta_btn_primary))
                dialog.setSecondaryCTAText(getString(R.string.cc_cta_btn_secondary))
                dialog.setPrimaryCTAClickListener {
                    dialog.dismiss()
                    creditCardAnalytics.clickToContinueCheckout(
                            categoryId.toString(), operatorIdSelected)
                    submitCreditCard(categoryId, operatorIdSelected,
                            productIdSelected, cc_widget_client_number.getClientNumber())
                }
                dialog.setSecondaryCTAClickListener {
                    creditCardAnalytics.clickBackOnConfirmationPage(
                            categoryId, operatorIdSelected)
                    dialog.dismiss()
                }
                dialog.show()
            }
        } else {
            showErrorToaster(getString(R.string.cc_error_default_message))
        }
    }

    private fun submitCreditCard(categoryId: String, operatorId: String, productId: String, clientNumber: String) {
        showLoading()
        if (userSession.isLoggedIn) {
            val mapParam = rechargeSubmitCCViewModel.createMapParam(clientNumber, operatorId, productId,
                    userSession.userId)

            rechargeSubmitCCViewModel.postCreditCard(GraphqlHelper.loadRawString(resources,
                    R.raw.query_cc_signature), categoryId, mapParam)

            rechargeSubmitCCViewModel.errorSignature.observe(viewLifecycleOwner, Observer {
                hideLoading()
                showErrorToaster(it)
            })

            rechargeSubmitCCViewModel.redirectUrl.observe(viewLifecycleOwner, Observer {
                hideLoading()
                val passData = DigitalCheckoutPassData.Builder()
                        .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                        .categoryId(categoryId)
                        .clientNumber(clientNumber)
                        .instantCheckout("0")
                        .isPromo("0")
                        .operatorId(operatorId)
                        .productId(productId)
                        .idemPotencyKey(RechargeCCUtil.generateIdemPotencyCheckout(userSession.userId))
                        .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                        .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                        .needGetCart(true)
                        .build()
                checkoutPassDataState = passData
                navigateToCart(passData)
            })

            rechargeSubmitCCViewModel.errorSubmitCreditCard.observe(viewLifecycleOwner, Observer {
                hideLoading()
                showErrorToaster(it)
            })

        } else {
            navigateUserLogin()
        }
    }

    private fun navigateUserLogin() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun hideLoading() {
        cc_progress_bar.visibility = View.GONE
    }

    private fun showLoading() {
        cc_progress_bar.visibility = View.VISIBLE
    }

    private fun navigateToCart(passData: DigitalCheckoutPassData) {
        val intent = RouteManager.getIntent(activity, ApplinkConsInternalDigital.CART_DIGITAL)
        intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData)
        startActivityForResult(intent, REQUEST_CODE_CART)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CART -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.hasExtra(DigitalExtraParam.EXTRA_MESSAGE)) {
                        val message = data.getStringExtra(DigitalExtraParam.EXTRA_MESSAGE)
                        if (!TextUtils.isEmpty(message)) {
                            showErrorToaster(message)
                        }
                    }
                }
            }
            REQUEST_CODE_LOGIN -> {
                if (userSession.isLoggedIn && ::checkoutPassDataState.isInitialized) {
                    navigateToCart(checkoutPassDataState)
                }
            }
        }
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(RECHARGE_CC_PAGE_PERFORMANCE)
    }

    companion object {

        const val EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA"

        private const val CATEGORY_ID = "category_id"
        private const val MENU_ID = "menu_id"
        const val RECHARGE_CC_PAGE_PERFORMANCE="dg_tagihan_cc_pdp"

        const val REQUEST_CODE_CART = 1000
        const val REQUEST_CODE_LOGIN = 1001

        fun newInstance(categoryId: String, menuId: String): Fragment {
            val fragment = RechargeCCFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY_ID, categoryId)
            bundle.putString(MENU_ID, menuId)
            fragment.arguments = bundle
            return fragment
        }
    }
}