package com.tokopedia.topupbills.telco.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.generateRechargeCheckoutToken
import com.tokopedia.topupbills.telco.data.*
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.fragment.DigitalSearchNumberFragment.InputNumberActionType
import com.tokopedia.topupbills.telco.view.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.view.model.DigitalTelcoExtraParam
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoEnquiryViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalPostpaidClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoBuyWidget
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoPostpaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var postpaidClientNumberWidget: DigitalPostpaidClientNumberWidget
    private lateinit var buyWidget: DigitalTelcoBuyWidget
    private lateinit var enquiryViewModel: DigitalTelcoEnquiryViewModel
    private lateinit var layoutProgressBar: RelativeLayout
    private lateinit var operatorSelected: TelcoCustomDataCollection
    private lateinit var selectedTelcoRecommendation: TelcoRecommendation

    private lateinit var operatorName: String
    private val favNumberList = mutableListOf<TelcoFavNumber>()
    private var operatorData: TelcoCustomComponentData =
            TelcoCustomComponentData(TelcoCustomData(mutableListOf()))
    private val categoryId = TelcoCategoryType.CATEGORY_PASCABAYAR

    private var inputNumberActionType = InputNumberActionType.MANUAL
    private lateinit var sharedModel: SharedProductTelcoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            enquiryViewModel = viewModelProvider.get(DigitalTelcoEnquiryViewModel::class.java)
            sharedModel = viewModelProvider.get(SharedProductTelcoViewModel::class.java)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_postpaid, container, false)
        mainContainer = view.findViewById(R.id.main_container)
        recentNumbersWidget = view.findViewById(R.id.recent_numbers)
        postpaidClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListWidget = view.findViewById(R.id.promo_widget)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        layoutProgressBar = view.findViewById(R.id.layout_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInputFilterDataCollections()
        renderClientNumber()
        handleFocusClientNumber()
        getCatalogMenuDetail()
        getDataFromBundle(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModel.promoItem.observe(this, Observer {
            it?.run {
                promoListWidget.notifyPromoItemChanges(this)
            }
        })
        sharedModel.enquiryResult.observe(this, Observer {
            it?.run {
                if (::operatorSelected.isInitialized) {
                    checkoutPassData = DigitalCheckoutPassData.Builder()
                            .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                            .categoryId(categoryId.toString())
                            .clientNumber(postpaidClientNumberWidget.getInputNumber())
                            .instantCheckout("0")
                            .isPromo("0")
                            .operatorId(operatorSelected.operator.id)
                            .productId(operatorSelected.operator.attributes.defaultProductId.toString())
                            .utmCampaign(categoryId.toString())
                            .utmContent(GlobalConfig.VERSION_NAME)
                            .idemPotencyKey(userSession.userId.generateRechargeCheckoutToken())
                            .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                            .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                            .voucherCodeCopied("")
                            .build()
                }
            }
        })
    }

    fun getInputFilterDataCollections() {
        customViewModel.getCustomDataPostpaid(GraphqlHelper.loadRawString(resources,
                R.raw.query_custom_digital_telco),
                this::onSuccessCustomData, this::onErrorCustomData)
    }

    fun getCatalogMenuDetail() {
        catalogMenuDetailViewModel.getCatalogMenuDetailPostpaid(GraphqlHelper.loadRawString(resources,
                R.raw.query_telco_catalog_menu_detail), this::onLoadingMenuDetail,
                this::onSuccessCatalogMenuDetail, this::onErrorCatalogMenuDetail)
        catalogMenuDetailViewModel.getFavNumbersPostpaid(GraphqlHelper.loadRawString(resources,
                R.raw.query_fav_number_digital), this::onSuccessFavNumbers, this::onErrorFavNumbers)
    }

    private fun getDataFromBundle(savedInstanceState: Bundle?) {
        var clientNumber = ""
        if (savedInstanceState == null) {
            arguments?.run {
                val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM) as DigitalTelcoExtraParam
                clientNumber = digitalTelcoExtraParam.clientNumber
            }
        } else {
            clientNumber = savedInstanceState.getString(CACHE_CLIENT_NUMBER)
        }
        postpaidClientNumberWidget.setInputNumber(clientNumber)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(CACHE_CLIENT_NUMBER, postpaidClientNumberWidget.getInputNumber())
    }

    private fun renderClientNumber() {
        postpaidClientNumberWidget.resetClientNumberPostpaid()
        postpaidClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
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
                postpaidClientNumberWidget.setButtonEnquiryEnable()
            }

            override fun onClearAutoComplete() {
                topupAnalytics.eventClearInputNumber()

                postpaidClientNumberWidget.resetClientNumberPostpaid()
                recentNumbersWidget.visibility = View.VISIBLE
                promoListWidget.visibility = View.VISIBLE
                buyWidget.setVisibilityLayout(false)
            }

            override fun onClientNumberHasFocus(clientNumber: String) {
                postpaidClientNumberWidget.clearFocusAutoComplete()
                startActivityForResult(activity?.let {
                    DigitalSearchNumberActivity.newInstance(it,
                            ClientNumberType.TYPE_INPUT_TEL, clientNumber, favNumberList)
                },
                        REQUEST_CODE_DIGITAL_SEARCH_NUMBER)
            }
        })
        postpaidClientNumberWidget.setPostpaidListener(object : ClientNumberPostpaidListener {
            override fun enquiryNumber() {
                getEnquiryNumber()
            }
        })
    }

    fun getEnquiryNumber() {
        if (::operatorSelected.isInitialized) {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(KEY_CLIENT_NUMBER, postpaidClientNumberWidget.getInputNumber())
            mapParam.put(KEY_PRODUCT_ID, operatorSelected.operator.attributes.defaultProductId.toString())

            enquiryViewModel.getEnquiry(GraphqlHelper.loadRawString(resources, R.raw.query_enquiry_digital_telco),
                    mapParam, this::onSuccessEnquiry, this::onErrorEnquiry)
        }
    }

    override fun onSuccessCustomData(telcoData: TelcoCustomComponentData) {
        this.operatorData = telcoData
        renderProductFromCustomData()
    }

    fun renderProductFromCustomData() {
        try {
            if (postpaidClientNumberWidget.getInputNumber().isNotEmpty()) {
                operatorSelected = this.operatorData.rechargeCustomData.customDataCollections.single {
                    postpaidClientNumberWidget.getInputNumber().startsWith(it.value)
                }
                operatorName = operatorSelected.operator.attributes.name
                when (inputNumberActionType) {
                    InputNumberActionType.MANUAL -> {
                        topupAnalytics.eventInputNumberManual(categoryId, operatorName)
                    }
                    InputNumberActionType.CONTACT -> {
                        topupAnalytics.eventInputNumberContactPicker(categoryId, operatorName)
                    }
                    InputNumberActionType.FAVORITE -> {
                        topupAnalytics.eventInputNumberFavorites(categoryId, operatorName)
                    }
                    InputNumberActionType.CONTACT_HOMEPAGE -> {
                        topupAnalytics.eventInputNumberContactPicker(categoryId, operatorName)
                    }
                }

                postpaidClientNumberWidget.setIconOperator(operatorSelected.operator.attributes.imageUrl)
            }
        } catch (exception: Exception) {
            view?.run {
                postpaidClientNumberWidget.setErrorInputNumber(
                        getString(R.string.telco_number_error_not_found))
            }
        }
    }

    override fun onErrorCustomData(throwable: Throwable) {
        view?.run {
            Toaster.showError(this, ErrorHandler.getErrorMessage(activity, throwable), Snackbar.LENGTH_LONG)
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

    fun onSuccessEnquiry(telcoEnquiryData: TelcoEnquiryData) {
        sharedModel.setEnquiryResult(telcoEnquiryData)
        postpaidClientNumberWidget.showEnquiryResultPostpaid(telcoEnquiryData)
        recentNumbersWidget.visibility = View.GONE
        promoListWidget.visibility = View.GONE

        buyWidget.setTotalPrice(telcoEnquiryData.enquiry.attributes.price)
        buyWidget.setVisibilityLayout(true)
        buyWidget.setListener(object : DigitalTelcoBuyWidget.ActionListener {
            override fun onClickNextBuyButton() {
                processToCart()
            }
        })
    }

    fun onErrorEnquiry(throwable: Throwable) {
        view?.run {
            Toaster.showError(this, ErrorHandler.getErrorMessage(activity, throwable), Snackbar.LENGTH_LONG)
        }
    }

    override fun clickCopyOnPromoCode(promoId: Int) {
        sharedModel.setPromoSelected(promoId)
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        inputNumberActionType = InputNumberActionType.CONTACT_HOMEPAGE
        postpaidClientNumberWidget.setInputNumber(contactNumber)
    }

    override fun handleCallbackSearchNumber(orderClientNumber: TelcoFavNumber, inputNumberActionTypeIndex: Int) {
        inputNumberActionType = InputNumberActionType.values()[inputNumberActionTypeIndex]
        postpaidClientNumberWidget.setInputNumber(orderClientNumber.clientNumber)
        postpaidClientNumberWidget.clearFocusAutoComplete()
    }

    override fun handleCallbackSearchNumberCancel() {
        postpaidClientNumberWidget.clearFocusAutoComplete()
    }

    override fun onClickItemRecentNumber(telcoRecommendation: TelcoRecommendation) {
        inputNumberActionType = InputNumberActionType.LATEST_TRANSACTION
        postpaidClientNumberWidget.setInputNumber(telcoRecommendation.clientNumber)

        topupAnalytics.clickEnhanceCommerceRecentTransaction(selectedTelcoRecommendation,
                operatorName, selectedTelcoRecommendation.position)
    }

    override fun setFavNumbers(data: TelcoRechargeFavNumberData) {
        favNumberList.addAll(data.favNumber.favNumberList)
    }

    override fun showErrorCartDigital(message: String) {
        view?.run {
            Toaster.showError(this, message, Snackbar.LENGTH_LONG)
        }
    }

    override fun onDestroy() {
        enquiryViewModel.clear()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        postpaidClientNumberWidget.clearFocusAutoComplete()
    }

    override fun onBackPressed() {
        topupAnalytics.eventClickBackButton(categoryId)
    }

    companion object {

        private const val CACHE_CLIENT_NUMBER = "cache_client_number"
        private const val EXTRA_PARAM = "extra_param"
        const val KEY_CLIENT_NUMBER = "clientNumber"
        const val KEY_PRODUCT_ID = "productId"


        fun newInstance(telcoExtraParam: DigitalTelcoExtraParam): Fragment {
            val fragment = DigitalTelcoPostpaidFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }
}
