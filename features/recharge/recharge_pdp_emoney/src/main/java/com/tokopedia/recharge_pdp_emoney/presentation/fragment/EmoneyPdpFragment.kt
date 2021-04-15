package com.tokopedia.recharge_pdp_emoney.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlMutation
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.cart.DigitalCheckoutUtil
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.activity.EmoneyPdpActivity
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.EmoneyPdpFragmentPagerAdapter
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.EmoneyPdpProductViewHolder
import com.tokopedia.recharge_pdp_emoney.presentation.viewmodel.EmoneyPdpViewModel
import com.tokopedia.recharge_pdp_emoney.presentation.widget.EmoneyPdpHeaderViewWidget
import com.tokopedia.recharge_pdp_emoney.presentation.widget.EmoneyPdpInputCardNumberWidget
import com.tokopedia.recharge_pdp_emoney.presentation.widget.EmoneyProductDetailBottomSheet
import com.tokopedia.recharge_pdp_emoney.utils.EmoneyPdpMapper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.fragment_emoney_pdp.*
import javax.inject.Inject

/**
 * @author by jessica on 29/03/21
 */

class EmoneyPdpFragment : BaseDaggerFragment(), EmoneyPdpHeaderViewWidget.ActionListener,
        EmoneyPdpInputCardNumberWidget.ActionListener, EmoneyPdpProductViewHolder.ActionListener,
        TopupBillsCheckoutWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val topUpBillsViewModel by lazy { viewModelFragmentProvider.get(TopupBillsViewModel::class.java) }
    private val emoneyPdpViewModel by lazy { viewModelFragmentProvider.get(EmoneyPdpViewModel::class.java) }
    private val addToCartViewModel by lazy { viewModelFragmentProvider.get(DigitalAddToCartViewModel::class.java) }

    private var categoryId: Int = 0
    private var menuId: Int = 0
    private var operatorId: Int = 0
    private var productId: Int = 0
    private var clientNumber: String = ""

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(EXTRA_PARAM_CATEGORY_ID, 0)
            menuId = it.getInt(EXTRA_PARAM_MENU_ID, 0)
            operatorId = it.getInt(EXTRA_PARAM_OPERATOR_ID, 0)
            productId = it.getInt(EXTRA_PARAM_PRODUCT_ID, 0)
            clientNumber = it.getString(EXTRA_PARAM_CLIENT_NUMBER, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_emoney_pdp, container, false)
    }

    override fun initInjector() {
        getComponent(EmoneyPdpComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        // dummy
        // will replace this
        emoneyPdpHeaderView.configureCheckBalanceView()
        emoneyPdpHeaderView.actionListener = this
        emoneyPdpInputCardWidget.initView(this)
        emoneyBuyWidget.listener = this
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        topUpBillsViewModel.menuDetailData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderRecommendationsAndPromoList(it.data.recommendations, it.data.promos)
                    renderTicker(EmoneyPdpMapper.mapTopUpBillsTickersToTickersData(it.data.tickers))
                }
                is Fail -> {
                    emoneyPdpViewModel.setErrorMessage(it.throwable)
                }
            }
        })

        emoneyPdpViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            renderErrorMessage(it)
        })

        emoneyPdpViewModel.catalogPrefixSelect.observe(viewLifecycleOwner, Observer {
            if (it is Fail) emoneyPdpViewModel.setErrorMessage(it.throwable)
        })

        emoneyPdpViewModel.selectedOperator.observe(viewLifecycleOwner, Observer {
            renderOperatorIcon(it)
            loadProducts(it)
        })

        emoneyPdpViewModel.selectedRecentNumber.observe(viewLifecycleOwner, Observer {
            addToCart(it)
        })

        emoneyPdpViewModel.catalogData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderProducts(it.data.product.dataCollections.firstOrNull()?.products
                            ?: listOf())
                }
                is Fail -> emoneyPdpViewModel.setErrorMessage(it.throwable)
            }
        })

        addToCartViewModel.addToCartResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    navigateToCart(it.data)
                }
                is Fail -> emoneyPdpViewModel.setErrorMessage(it.throwable)
            }
        })
    }

    private fun loadData() {
        topUpBillsViewModel.getMenuDetail(CommonTopupBillsGqlQuery.catalogMenuDetail,
                topUpBillsViewModel.createMenuDetailParams(menuId))

        topUpBillsViewModel.getFavoriteNumbers(
                CommonTopupBillsGqlMutation.favoriteNumber,
                topUpBillsViewModel.createFavoriteNumbersParams(categoryId))

        emoneyPdpViewModel.getPrefixOperator(menuId)
    }

    private fun renderRecommendationsAndPromoList(recommendations: List<TopupBillsRecommendation>,
                                                  promoList: List<TopupBillsPromo>) {

        if (recommendations.isEmpty() && promoList.isEmpty()) {
            emoneyPdpViewPager.hide()
            return
        }

        emoneyPdpViewPager.show()
        if (recommendations.isNotEmpty() && promoList.isNotEmpty()) {
            emoneyPdpTab.addNewTab(getString(R.string.recharge_pdp_emoney_recents_tab))
            emoneyPdpTab.addNewTab(getString(R.string.recharge_pdp_emoney_promo_tab))
            emoneyPdpTab.show()
        } else {
            emoneyPdpTab.hide()
        }

        val adapter = EmoneyPdpFragmentPagerAdapter(this, recommendations, promoList)
        emoneyPdpViewPager.adapter = adapter

        emoneyPdpTab.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.select()
                emoneyPdpViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabReselected(p0: TabLayout.Tab?) {}
        })

        emoneyPdpViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val tab = emoneyPdpTab?.getUnifyTabLayout()?.getTabAt(position)
                tab?.select()
            }
        })
    }

    private fun renderTicker(tickers: List<TickerData>) {
        if (tickers.isEmpty()) {
            emoneyPdpTicker.hide()
            return
        }

        if (tickers.size == 1) setUpSingleTicker(tickers.first())
        else setUpMultipleTicker(tickers)
        emoneyPdpTicker.show()
    }

    private fun setUpSingleTicker(ticker: TickerData) {
        emoneyPdpTicker.tickerTitle = ticker.title
        emoneyPdpTicker.setHtmlDescription(ticker.description)
        emoneyPdpTicker.tickerType = ticker.type
        emoneyPdpTicker.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
            }

            override fun onDismiss() {}
        })
    }

    private fun setUpMultipleTicker(tickers: List<TickerData>) {
        context?.let { context ->
            val tickerAdapter = TickerPagerAdapter(context, tickers)
            tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
                }
            })
            emoneyPdpTicker.addPagerView(tickerAdapter, tickers)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_EMONEY_PDP_DIGITAL_SEARCH_NUMBER -> {
                    val favNumber = data?.getParcelableExtra<TopupBillsFavNumberItem>(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER)
                    favNumber?.let { renderClientNumber(it) }
                }

                REQUEST_CODE_EMONEY_PDP_CAMERA_OCR -> {
                    val clientNumber = data?.getStringExtra(DigitalExtraParam.EXTRA_NUMBER_FROM_CAMERA_OCR)

                    clientNumber?.let {
                        showToastMessage(getString(R.string.recharge_pdp_success_message_scan_ocr));
                        renderClientNumber(TopupBillsFavNumberItem(clientNumber = clientNumber))
                    }
                }

                REQUEST_CODE_EMONEY_PDP_CHECK_SALDO -> {
                    val checkSaldoData = data?.getParcelableExtra<DigitalCategoryDetailPassData>(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA)
                    checkSaldoData?.run {
                        val clientNumberData = TopupBillsFavNumberItem(
                                clientNumber = clientNumber,
                                productId = productId,
                                operatorId = operatorId,
                                categoryId = categoryId)

                        renderClientNumber(clientNumberData)
                        //renderProduct
                        renderCardState(this)
                    }
                }
            }
        }
    }

    private fun showToastMessage(message: String) {
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
    }

    private fun renderClientNumber(item: TopupBillsFavNumberItem) {
        emoneyPdpInputCardWidget.setNumber(item.clientNumber)
    }

    private fun renderErrorMessage(message: String) {
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG).show()
    }

    override fun onClickCheckBalance() {
        val intent = RouteManager.getIntent(activity,
                ApplinkConsInternalDigital.SMARTCARD, DigitalExtraParam.EXTRA_NFC_FROM_PDP, "false")
        startActivityForResult(intent, REQUEST_CODE_EMONEY_PDP_CHECK_SALDO)
    }

    override fun onClickUpdateBalance() {
        val intent = RouteManager.getIntent(activity,
                ApplinkConsInternalDigital.SMARTCARD, DigitalExtraParam.EXTRA_NFC_FROM_PDP, "false")
        startActivityForResult(intent, REQUEST_CODE_EMONEY_PDP_CHECK_SALDO)
    }

    override fun onClickCameraIcon() {
        val intent = RouteManager.getIntent(activity, ApplinkConsInternalDigital.CAMERA_OCR)
        startActivityForResult(intent, REQUEST_CODE_EMONEY_PDP_CAMERA_OCR)
    }

    override fun onClickInputView(inputNumber: String) {
        showFavoriteNumbersPage((topUpBillsViewModel.favNumberData.value as Success).data.favNumberList)
    }

    override fun onRemoveNumberIconClick() {
        showRecentNumberAndPromo()
    }

    override fun onInputNumberChanged(inputNumber: String) {
        // call be to get operator name
        emoneyPdpViewModel.getSelectedOperator(inputNumber)
    }

    private fun showFavoriteNumbersPage(favoriteNumbers: List<TopupBillsFavNumberItem>) {
        startActivityForResult(
                TopupBillsSearchNumberActivity.getCallingIntent(requireContext(),
                        ClientNumberType.TYPE_INPUT_NUMERIC, "", favoriteNumbers),
                REQUEST_CODE_EMONEY_PDP_DIGITAL_SEARCH_NUMBER)
    }

    private fun renderOperatorIcon(operator: RechargePrefix) {
        if (operator.operator != null) {
            emoneyPdpInputCardWidget.setOperator(operator.operator.attributes.imageUrl)
        }
    }

    private fun renderCardState(detailPassData: DigitalCategoryDetailPassData) {
        //
    }

    private fun loadProducts(prefix: RechargePrefix) {
        // to be changed to operator.id // NEED ACTION
        showProducts()
        emoneyPdpProductWidget.showShimmering()
        emoneyPdpViewModel.getProductFromOperator(menuId, prefix.key)
    }

    private fun renderProducts(productList: List<CatalogProduct>) {
        emoneyPdpProductWidget.titleText = "Pilih Nominal"
        emoneyPdpProductWidget.setProducts(productList)
        emoneyPdpProductWidget.setListener(this)
    }

    private fun showProducts() {
        emoneyPdpProductWidget.show()
        emoneyPdpTab.hide()
        emoneyPdpViewPager.hide()
    }

    private fun showRecentNumberAndPromo() {
        emoneyPdpProductWidget.hide()
        if ((emoneyPdpViewPager.adapter as EmoneyPdpFragmentPagerAdapter).itemCount > 1) emoneyPdpTab.show()
        emoneyPdpViewPager.show()
        emoneyPdpProductWidget.showPaddingBottom(false)
        emoneyBuyWidgetLayout.hide()
    }

    override fun onClickProduct(product: CatalogProduct, position: Int) {
        //atc
        emoneyPdpViewModel.setSelectedProduct(product)
        emoneyBuyWidget.setTotalPrice(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(product.attributes.pricePlain.toIntOrZero()))
        emoneyBuyWidgetLayout.show()
        emoneyPdpProductWidget.showPaddingBottom(true)
        emoneyBuyWidget.setVisibilityLayout(true)
    }

    override fun onClickSeeDetailProduct(product: CatalogProduct) {
        val bottomSheet = EmoneyProductDetailBottomSheet(product)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onClickNextBuyButton() {
        addToCart()
    }

    private fun addToCart() {
        val digitalCheckoutPassData = emoneyPdpViewModel.generateCheckoutPassData(
                (requireActivity() as EmoneyPdpActivity).promoCode,
                emoneyPdpInputCardWidget.getNumber())
        addToCartViewModel.addToCart(digitalCheckoutPassData, DeviceUtil.getDigitalIdentifierParam(requireActivity()),
                DigitalSubscriptionParams())
    }

    private fun addToCart(recentNumber: TopupBillsRecommendation) {
        val digitalCheckoutPassData = emoneyPdpViewModel.generateCheckoutPassData(
                (requireActivity() as EmoneyPdpActivity).promoCode,
                recentNumber.clientNumber,
                recentNumber.productId.toString(),
                recentNumber.operatorId.toString())
        addToCartViewModel.addToCart(digitalCheckoutPassData,
                DeviceUtil.getDigitalIdentifierParam(requireActivity()),
                DigitalSubscriptionParams())
    }

    private fun navigateToCart(categoryId: String) {
        context?.let { context ->
            val intent = RouteManager.getIntent(context, DigitalCheckoutUtil.getApplinkCartDigital(context))
            emoneyPdpViewModel.digitalCheckoutPassData.categoryId = categoryId
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, emoneyPdpViewModel.digitalCheckoutPassData)
            startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
        }
    }

    companion object {
        private const val REQUEST_CODE_EMONEY_PDP_CHECK_SALDO = 1007
        private const val REQUEST_CODE_EMONEY_PDP_CAMERA_OCR = 1008
        private const val REQUEST_CODE_CART_DIGITAL = 1090
        private const val REQUEST_CODE_EMONEY_PDP_DIGITAL_SEARCH_NUMBER = 1004

        const val EXTRA_PARAM_MENU_ID = "EXTRA_PARAM_MENU_ID"
        const val EXTRA_PARAM_CATEGORY_ID = "EXTRA_PARAM_CATEGORY_ID"
        const val EXTRA_PARAM_OPERATOR_ID = "EXTRA_PARAM_OPERATOR_ID"
        const val EXTRA_PARAM_PRODUCT_ID = "EXTRA_PARAM_PRODUCT_ID"
        const val EXTRA_PARAM_CLIENT_NUMBER = "EXTRA_PARAM_CLIENT_NUMBER"

        fun newInstance(categoryId: Int,
                        menuId: Int,
                        operatorId: Int = 0,
                        productId: Int = 0,
                        clientNumber: String): EmoneyPdpFragment {
            val fragment = EmoneyPdpFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PARAM_CATEGORY_ID, categoryId)
            bundle.putInt(EXTRA_PARAM_MENU_ID, menuId)
            bundle.putInt(EXTRA_PARAM_OPERATOR_ID, operatorId)
            bundle.putInt(EXTRA_PARAM_PRODUCT_ID, productId)
            bundle.putString(EXTRA_PARAM_CLIENT_NUMBER, clientNumber)
            fragment.arguments = bundle
            return fragment
        }
    }

}