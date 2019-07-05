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
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.network.utils.ErrorHandler.getErrorMessage
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
import com.tokopedia.topupbills.telco.view.model.DigitalTabTelcoItem
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoBuyWidget
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_digital_telco_prepaid.*

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

    private val favNumberList = mutableListOf<TelcoFavNumber>()
    private var operatorData: TelcoCustomComponentData =
            TelcoCustomComponentData(TelcoCustomData(mutableListOf()))
    private var selectedProductId = ""
    private var selectedCategoryId = 0

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            sharedModel = viewModelProvider.get(SharedProductTelcoViewModel::class.java)
            sharedModel.setShowTotalPrice(false)
        }
    }

    override fun getScreenName(): String {
        return DigitalTelcoPrepaidFragment::class.java.simpleName
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        mainContainer = view.findViewById(R.id.main_container)
        recentNumbersView = view.findViewById(R.id.recent_numbers)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListView = view.findViewById(R.id.promo_widget)
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
    }

    override fun getMapCustomData(): Map<String, Any> {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("componentID", TelcoComponentType.CLIENT_NUMBER_PREPAID)
        return mapParam
    }

    override fun onSuccessCustomData(telcoData: TelcoCustomComponentData) {
        this.operatorData = telcoData
        renderProductFromCustomData()
    }

    fun renderProductFromCustomData() {
        try {
            if (telcoClientNumberWidget.getInputNumber().isNotEmpty()) {
                val prefixClientNumber = telcoClientNumberWidget.getInputNumber().substring(0, 4)

                val operatorSelected = this.operatorData.rechargeCustomData.customDataCollections.filter {
                    it.value.equals(prefixClientNumber)
                }.single()

                renderViewPager(operatorSelected.operator.id)
                telcoClientNumberWidget.setIconOperator(operatorSelected.operator.attributes.imageUrl)

                recentNumbersView.visibility = View.GONE
                promoListView.visibility = View.GONE
                tabLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                separator.visibility = View.VISIBLE
            }
        } catch (exception: Exception) {
            view?.run {
                Toaster.showError(this, getErrorMessage(activity, exception), Snackbar.LENGTH_LONG)
            }
        }
    }

    override fun onErrorCustomData(throwable: Throwable) {
        view?.run {
            Toaster.showError(this, getErrorMessage(activity, throwable), Snackbar.LENGTH_LONG)
        }
    }

    override fun onLoadingMenuDetail(showLoading: Boolean) {
        if (showLoading) {
            layoutProgressBar.visibility = View.VISIBLE
            recentNumbersView.visibility = View.GONE
            promoListView.visibility = View.GONE
        } else {
            layoutProgressBar.visibility = View.GONE
            recentNumbersView.visibility = View.VISIBLE
            promoListView.visibility = View.VISIBLE
        }
    }

    fun renderInputNumber() {
        telcoClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
            override fun onNavigateToContact() {
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
                separator.visibility = View.GONE
                recentNumbersView.visibility = View.VISIBLE
                promoListView.visibility = View.VISIBLE
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
        telcoClientNumberWidget.setInputNumber(contactNumber)
    }

    fun renderViewPager(operatorId: String) {
        val listProductTab = mutableListOf<DigitalTabTelcoItem>()
        listProductTab.add(DigitalTabTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PULSA, TelcoComponentName.PRODUCT_PULSA, operatorId, TelcoProductType.PRODUCT_GRID, selectedProductId),
                TelcoComponentName.PRODUCT_PULSA))
        listProductTab.add(DigitalTabTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PAKET_DATA, TelcoComponentName.PRODUCT_PAKET_DATA, operatorId, TelcoProductType.PRODUCT_LIST, selectedProductId),
                TelcoComponentName.PRODUCT_PAKET_DATA))
        listProductTab.add(DigitalTabTelcoItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_ROAMING, TelcoComponentName.PRODUCT_ROAMING, operatorId, TelcoProductType.PRODUCT_LIST, selectedProductId),
                TelcoComponentName.PRODUCT_ROAMING))
        val pagerAdapter = DigitalTelcoProductTabAdapter(listProductTab, childFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        setTabFromProductSelected()
        setCustomFont()
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
                                "fonts/NunitoSans-ExtraBold.ttf")
                    }

                }
            }
        }
    }

    fun setTabFromProductSelected() {
        var itemId = 0
        if (selectedProductId.isNotEmpty()) {
            if (selectedCategoryId == TelcoCategoryType.CATEGORY_PULSA) {
                itemId = 0
            } else if (selectedCategoryId == TelcoCategoryType.CATEGORY_PAKET_DATA) {
                itemId = 1
            } else if (selectedCategoryId == TelcoCategoryType.CATEGORY_ROAMING) {
                itemId = 2
            }
        }
        viewPager.setCurrentItem(itemId, true)
    }

    override fun getMapCatalogMenuDetail(): Map<String, Any> {
        var mapParam = HashMap<String, kotlin.Any>()
        mapParam.put("menuID", TelcoComponentType.TELCO_PREPAID)
        return mapParam
    }

    override fun showErrorCartDigital(message: String) {
        view?.run {
            Toaster.showError(this, message, Snackbar.LENGTH_LONG)
        }
    }

    override fun handleCallbackSearchNumber(orderClientNumber: TelcoFavNumber) {
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
        selectedProductId = telcoRecommendation.productId.toString()
        selectedCategoryId = telcoRecommendation.categoryId
        telcoClientNumberWidget.setInputNumber(telcoRecommendation.clientNumber)
    }

    override fun getMapFavNumbers(): Map<String, Any> {
        var mapParam = HashMap<String, Any>()
        mapParam.put("categoryID", TelcoComponentType.FAV_NUMBER_PREPAID)
        return mapParam
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

    companion object {

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            return fragment
        }
    }
}