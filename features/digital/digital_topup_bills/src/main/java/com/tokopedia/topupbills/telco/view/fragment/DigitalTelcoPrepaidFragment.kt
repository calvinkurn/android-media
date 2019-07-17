package com.tokopedia.topupbills.telco.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.network.utils.ErrorHandler.getErrorMessage
import com.tokopedia.showcase.ShowCaseBuilder
import com.tokopedia.showcase.ShowCaseDialog
import com.tokopedia.showcase.ShowCaseObject
import com.tokopedia.showcase.ShowCasePreference
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.generateRechargeCheckoutToken
import com.tokopedia.topupbills.telco.data.*
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.adapter.DigitalTelcoProductTabAdapter
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.fragment.DigitalSearchNumberFragment.InputNumberActionType
import com.tokopedia.topupbills.telco.view.model.DigitalTabTelcoItem
import com.tokopedia.topupbills.telco.view.model.DigitalTelcoExtraParam
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoBuyWidget
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_digital_telco_prepaid.*
import java.util.*

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var telcoClientNumberWidget: DigitalClientNumberWidget
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var buyWidget: DigitalTelcoBuyWidget
    private lateinit var sharedModel: SharedProductTelcoViewModel
    private lateinit var layoutProgressBar: RelativeLayout
    private lateinit var productSelected: TelcoProductDataCollection
    private lateinit var selectedOperator: TelcoCustomDataCollection
    private lateinit var selectedOperatorName: String
    private lateinit var inputNumberActionType: InputNumberActionType
    private lateinit var selectedTelcoRecommendation: TelcoRecommendation

    private val favNumberList = mutableListOf<TelcoFavNumber>()
    private var operatorData: TelcoCustomComponentData =
            TelcoCustomComponentData(TelcoCustomData(mutableListOf()))
    private var selectedProductId = ""
    private var selectedCategoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            sharedModel = viewModelProvider.get(SharedProductTelcoViewModel::class.java)
            sharedModel.setShowTotalPrice(false)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModel.productItem.observe(this, Observer {
            it?.run {
                productSelected = it
                buyWidget.setTotalPrice(it.product.attributes.price)
                it.product.attributes.productPromo?.run {
                    if (this.newPrice.isNotEmpty()) {
                        buyWidget.setTotalPrice(this.newPrice)
                    }
                }

                checkoutPassData = DigitalCheckoutPassData.Builder()
                        .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                        .categoryId(it.product.attributes.categoryId.toString())
                        .clientNumber(telcoClientNumberWidget.getInputNumber())
                        .instantCheckout("0")
                        .isPromo(if (it.product.attributes.productPromo != null) "1" else "0")
                        .operatorId(it.product.attributes.operatorId.toString())
                        .productId(it.product.id)
                        .utmCampaign(it.product.attributes.categoryId.toString())
                        .utmContent(GlobalConfig.VERSION_NAME)
                        .idemPotencyKey(userSession.userId.generateRechargeCheckoutToken())
                        .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                        .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                        .voucherCodeCopied("")
                        .build()
            }
        })
        sharedModel.showTotalPrice.observe(this, Observer {
            it?.run {
                buyWidget.setVisibilityLayout(it)
            }
        })
        sharedModel.promoItem.observe(this, Observer {
            it?.run {
                promoListWidget.notifyPromoItemChanges(this)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        mainContainer = view.findViewById(R.id.main_container)
        recentNumbersWidget = view.findViewById(R.id.recent_numbers)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListWidget = view.findViewById(R.id.promo_widget)
        viewPager = view.findViewById(R.id.product_view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        layoutProgressBar = view.findViewById(R.id.layout_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInputFilterDataCollections()
        handleFocusClientNumber()
        renderInputNumber()
        getCatalogMenuDetail()
        renderBuyProduct()
        getDataFromBundle()
    }

    fun getInputFilterDataCollections() {
        customViewModel.getCustomDataPrepaid(GraphqlHelper.loadRawString(resources,
                R.raw.query_custom_digital_telco),
                this::onSuccessCustomData, this::onErrorCustomData)
    }

    fun getCatalogMenuDetail() {
        catalogMenuDetailViewModel.getCatalogMenuDetailPrepaid(GraphqlHelper.loadRawString(resources,
                R.raw.query_telco_catalog_menu_detail),
                this::onLoadingMenuDetail, this::onSuccessCatalogMenuDetail, this::onErrorCatalogMenuDetail)
        catalogMenuDetailViewModel.getFavNumbersPrepaid(GraphqlHelper.loadRawString(resources,
                R.raw.query_fav_number_digital), this::onSuccessFavNumbers, this::onErrorFavNumbers)
    }

    fun getDataFromBundle() {
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM) as DigitalTelcoExtraParam
            telcoClientNumberWidget.setInputNumber(digitalTelcoExtraParam.clientNumber)
            selectedProductId = digitalTelcoExtraParam.productId
            if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                selectedCategoryId = digitalTelcoExtraParam.categoryId.toInt()
            }
        }
    }

    override fun onSuccessCatalogMenuDetail(catalogMenuDetailData: TelcoCatalogMenuDetailData) {
        super.onSuccessCatalogMenuDetail(catalogMenuDetailData)
        showOnBoarding()
    }

    override fun onSuccessCustomData(telcoData: TelcoCustomComponentData) {
        this.operatorData = telcoData
        renderProductFromCustomData()
    }

    fun renderProductFromCustomData() {
        try {
            if (telcoClientNumberWidget.getInputNumber().isNotEmpty()) {
                selectedOperator = this.operatorData.rechargeCustomData.customDataCollections.single {
                    telcoClientNumberWidget.getInputNumber().startsWith(it.value)
                }
                selectedOperatorName = selectedOperator.operator.attributes.name
                when (inputNumberActionType) {
                    InputNumberActionType.MANUAL -> {
                        topupAnalytics.eventInputNumberManual(selectedCategoryId, selectedOperatorName)
                    }
                    InputNumberActionType.CONTACT -> {
                        topupAnalytics.eventInputNumberContactPicker(selectedCategoryId, selectedOperatorName)
                    }
                    InputNumberActionType.FAVORITE -> {
                        topupAnalytics.eventInputNumberFavorites(selectedCategoryId, selectedOperatorName)
                    }
                    InputNumberActionType.CONTACT_HOMEPAGE -> {
                        topupAnalytics.eventInputNumberContactPicker(selectedCategoryId, selectedOperatorName)
                    }
                    InputNumberActionType.LATEST_TRANSACTION -> {
                        if (::selectedTelcoRecommendation.isInitialized) {
                            topupAnalytics.clickEnhanceCommerceRecentTransaction(selectedTelcoRecommendation, selectedOperatorName,
                                    selectedTelcoRecommendation.position)
                        }
                    }
                }

                renderViewPager(selectedOperator.operator.id)
                telcoClientNumberWidget.setIconOperator(selectedOperator.operator.attributes.imageUrl)

                recentNumbersWidget.visibility = View.GONE
                promoListWidget.visibility = View.GONE
                tabLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                separator.visibility = View.VISIBLE
            }
        } catch (exception: Exception) {
            view?.run {
                telcoClientNumberWidget.setErrorInputNumber(
                        getString(R.string.telco_number_error_not_found))
            }
        }
    }

    override fun onErrorCustomData(throwable: Throwable) {
        view?.run {
            Toaster.showError(this, getErrorMessage(activity, throwable), Snackbar.LENGTH_LONG)
        }
    }

    fun onLoadingMenuDetail(showLoading: Boolean) {
        if (showLoading) {
            layoutProgressBar.visibility = View.VISIBLE
            recentNumbersWidget.visibility = View.GONE
            promoListWidget.visibility = View.GONE
        } else {
            layoutProgressBar.visibility = View.GONE
            recentNumbersWidget.visibility = View.VISIBLE
            promoListWidget.visibility = View.VISIBLE
        }
    }

    fun renderInputNumber() {
        telcoClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
            override fun onNavigateToContact() {
                inputNumberActionType = InputNumberActionType.CONTACT
                navigateContact()
            }

            override fun onRenderOperator() {
                operatorData.rechargeCustomData.customDataCollections.isEmpty()?.let {
                    if (it) {
                        getInputFilterDataCollections()
                    } else {
                        renderProductFromCustomData()
                    }
                }
            }

            override fun onClearAutoComplete() {
                topupAnalytics.eventClearInputNumber()

                separator.visibility = View.GONE
                recentNumbersWidget.visibility = View.VISIBLE
                promoListWidget.visibility = View.VISIBLE
                tabLayout.visibility = View.GONE
                viewPager.visibility = View.GONE
                sharedModel.setShowTotalPrice(false)
                selectedProductId = ""
            }

            override fun onClientNumberHasFocus(clientNumber: String) {
                startActivityForResult(activity?.let {
                    DigitalSearchNumberActivity.newInstance(it,
                            ClientNumberType.TYPE_INPUT_TEL, clientNumber, favNumberList)
                },
                        REQUEST_CODE_DIGITAL_SEARCH_NUMBER)
            }
        })
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        inputNumberActionType = InputNumberActionType.CONTACT_HOMEPAGE
        telcoClientNumberWidget.setInputNumber(contactNumber)
    }

    override fun clickCopyOnPromoCode(promoId: Int) {
        sharedModel.setPromoSelected(promoId)
    }

    fun renderViewPager(operatorId: String) {
        val listProductTab = mutableListOf<DigitalTabTelcoItem>()
        listProductTab.add(DigitalTabTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PULSA, TelcoComponentName.PRODUCT_PULSA, operatorId, selectedOperatorName,
                TelcoProductType.PRODUCT_GRID, selectedProductId), TelcoComponentName.PRODUCT_PULSA))
        listProductTab.add(DigitalTabTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PAKET_DATA, TelcoComponentName.PRODUCT_PAKET_DATA, selectedOperatorName,
                operatorId, TelcoProductType.PRODUCT_LIST, selectedProductId), TelcoComponentName.PRODUCT_PAKET_DATA))
        listProductTab.add(DigitalTabTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_ROAMING, TelcoComponentName.PRODUCT_ROAMING, operatorId, selectedOperatorName,
                TelcoProductType.PRODUCT_LIST, selectedProductId), TelcoComponentName.PRODUCT_ROAMING))
        val pagerAdapter = DigitalTelcoProductTabAdapter(listProductTab, childFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        setTabFromProductSelected()
        setCustomFont()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(pos: Int) {
                topupAnalytics.eventClickTelcoPrepaidCategory(listProductTab[pos].title)
            }
        })
    }

    fun setCustomFont() {

        val vg = tabLayout.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount

        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup

            val tabChildsCount = vgTab.childCount

            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    context?.run {
                        tabViewChild.typeface = Typeface.createFromAsset(this.assets,
                                TAB_FONT_NUNITO_SANS)
                    }

                }
            }
        }
    }

    fun setTabFromProductSelected() {
        var itemId = 0
        if (selectedCategoryId == TelcoCategoryType.CATEGORY_PULSA) {
            itemId = 0
        } else if (selectedCategoryId == TelcoCategoryType.CATEGORY_PAKET_DATA) {
            itemId = 1
        } else if (selectedCategoryId == TelcoCategoryType.CATEGORY_ROAMING) {
            itemId = 2
        }
        viewPager.setCurrentItem(itemId, true)
    }

    override fun showErrorCartDigital(message: String) {
        view?.run {
            Toaster.showError(this, message, Snackbar.LENGTH_LONG)
        }
    }

    override fun handleCallbackSearchNumber(orderClientNumber: TelcoFavNumber, inputNumberActionTypeIndex: Int) {
        inputNumberActionType = InputNumberActionType.values()[inputNumberActionTypeIndex]

        if (orderClientNumber.productId.isNotEmpty() && orderClientNumber.categoryId.isNotEmpty()) {
            selectedProductId = orderClientNumber.productId
            selectedCategoryId = Integer.valueOf(orderClientNumber.categoryId)
        }
        telcoClientNumberWidget.setInputNumber(orderClientNumber.clientNumber)
        telcoClientNumberWidget.clearFocus()
    }

    override fun handleCallbackSearchNumberCancel() {
        telcoClientNumberWidget.clearFocus()
    }

    override fun onClickItemRecentNumber(telcoRecommendation: TelcoRecommendation) {
        inputNumberActionType = InputNumberActionType.LATEST_TRANSACTION
        selectedProductId = telcoRecommendation.productId.toString()
        selectedCategoryId = telcoRecommendation.categoryId
        telcoClientNumberWidget.setInputNumber(telcoRecommendation.clientNumber)
        this.selectedTelcoRecommendation = telcoRecommendation
    }

    override fun setFavNumbers(data: TelcoRechargeFavNumberData) {
        favNumberList.addAll(data.favNumber.favNumberList)
    }

    fun renderBuyProduct() {
        buyWidget.setListener(object : DigitalTelcoBuyWidget.ActionListener {
            override fun onClickNextBuyButton() {
                processToCart()
            }
        })
    }

    private fun showOnBoarding() {
        activity?.run {
            val showcaseTag = javaClass.name + ".BroadcastMessage"
            if (ShowCasePreference.hasShown(this, showcaseTag)) {
                return
            }

            val showCaseDialog = generateShowcaseDialog()
            val showCaseList = ArrayList<ShowCaseObject>()
            showCaseList.add(ShowCaseObject(telcoClientNumberWidget, getString(R.string.Telco_title_showcase_client_number),
                    getString(R.string.telco_label_showcase_client_number)))
            showCaseList.add(ShowCaseObject(promoListWidget, getString(R.string.telco_title_showcase_promo),
                    getString(R.string.telco_label_showcase_promo)))
            showCaseDialog.show(activity, showcaseTag, showCaseList)
        }
    }

    private fun generateShowcaseDialog(): ShowCaseDialog {
        return ShowCaseBuilder()
                .backgroundContentColorRes(R.color.black)
                .shadowColorRes(R.color.shadow)
                .textColorRes(R.color.grey_400)
                .textSizeRes(R.dimen.sp_12)
                .titleTextSizeRes(R.dimen.sp_16)
                .finishStringRes(R.string.finish)
                .clickable(true)
                .useArrow(true)
                .build()
    }

    override fun onDestroy() {
        sharedModel.clear()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        telcoClientNumberWidget.clearFocus()

        if (::productSelected.isInitialized) {
            checkoutPassData = DigitalCheckoutPassData.Builder()
                    .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                    .categoryId(productSelected.product.attributes.categoryId.toString())
                    .clientNumber(telcoClientNumberWidget.getInputNumber())
                    .instantCheckout("0")
                    .isPromo(if (productSelected.product.attributes.productPromo != null) "1" else "0")
                    .operatorId(productSelected.product.attributes.operatorId.toString())
                    .productId(productSelected.product.id)
                    .utmCampaign(productSelected.product.attributes.categoryId.toString())
                    .utmContent(GlobalConfig.VERSION_NAME)
                    .idemPotencyKey(userSession.userId.generateRechargeCheckoutToken())
                    .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                    .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                    .voucherCodeCopied("")
                    .build()
        }
    }

    override fun onBackPressed() {
        topupAnalytics.eventClickBackButton(selectedCategoryId)
    }

    companion object {

        private const val EXTRA_PARAM = "extra_param"
        const val TAB_FONT_NUNITO_SANS = "fonts/NunitoSans-ExtraBold.ttf"

        fun newInstance(telcoExtraParam: DigitalTelcoExtraParam): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }
}