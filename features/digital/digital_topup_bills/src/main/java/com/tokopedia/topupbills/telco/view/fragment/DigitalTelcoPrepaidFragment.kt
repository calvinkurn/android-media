package com.tokopedia.topupbills.telco.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalCartExtraParam
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.*
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.adapter.DigitalTelcoProductTabAdapter
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.model.DigitalFavNumber
import com.tokopedia.topupbills.telco.view.model.DigitalTabTelcoItem
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoBuyWidget
import com.tokopedia.user.session.UserSessionInterface
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject
import kotlin.experimental.and

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

    private val favNumberList = mutableListOf<TelcoFavNumber>()
    private var operatorData: TelcoCustomComponentData =
            TelcoCustomComponentData(TelcoCustomData(mutableListOf()))
    private var selectedProductId = ""
    private var selectedCategoryId = 0
    private lateinit var checkoutPassData: DigitalCheckoutPassData

    @Inject
    lateinit var userSession: UserSessionInterface

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
                        .idemPotencyKey(generateATokenRechargeCheckout(userSession.userId))
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

    private fun generateATokenRechargeCheckout(userLoginId: String): String {
        val timeMillis = System.currentTimeMillis().toString()
        val token = md5(timeMillis)
        return userLoginId + "_" + if (token.isEmpty()) timeMillis else token
    }

    private fun md5(s: String): String {
        try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()
            val hexString = StringBuilder()
            for (b in messageDigest) {
                hexString.append(String.format("%02x", b and 0xff.toByte()))
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        }

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

        handleFocusClientNumber()
        getInputFilterDataCollections()
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
        val prefixClientNumber = telcoClientNumberWidget.getInputNumber().substring(0, 4)
        try {
            val operatorSelected = this.operatorData.rechargeCustomData.customDataCollections.filter {
                it.value.equals(prefixClientNumber)
            }.single()

            renderViewPager(operatorSelected.operator.id)
            telcoClientNumberWidget.setIconOperator(operatorSelected.operator.attributes.imageUrl)

            recentNumbersView.visibility = View.GONE
            promoListView.visibility = View.GONE
            tabLayout.visibility = View.VISIBLE
            viewPager.visibility = View.VISIBLE
        } catch (exception: Exception) {
            Toast.makeText(activity, "error exception", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onErrorCustomData(throwable: Throwable) {
        Toast.makeText(activity, "input filter " + throwable.message, Toast.LENGTH_SHORT).show()
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
                recentNumbersView.visibility = View.VISIBLE
                promoListView.visibility = View.VISIBLE
                tabLayout.visibility = View.GONE
                viewPager.visibility = View.GONE
                sharedModel.setShowTotalPrice(false)
                selectedProductId = ""
            }

            override fun onClientNumberHasFocus(clientNumber: String) {
                startActivityForResult(activity?.let { DigitalSearchNumberActivity.newInstance(it, DigitalFavNumber(), clientNumber, favNumberList) },
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
                if (userSession.isLoggedIn) {
                    val intent = RouteManager.getIntent(activity, ApplinkConsInternalDigital.CART_DIGITAL)
                    intent.putExtra(DigitalCartExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, checkoutPassData)
                    startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
                } else {
                    val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
                    startActivityForResult(intent, REQUEST_CODE_LOGIN)
                }
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
    }

    companion object {

        val REQUEST_CODE_CART_DIGITAL = 1090
        val REQUEST_CODE_LOGIN = 1010

        fun newInstance(): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            return fragment
        }
    }
}