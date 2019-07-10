package com.tokopedia.topupbills.telco.view.fragment

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
import com.tokopedia.graphql.data.GraphqlClient
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

    private val favNumberList = mutableListOf<TelcoFavNumber>()
    private var operatorData: TelcoCustomComponentData =
            TelcoCustomComponentData(TelcoCustomData(mutableListOf()))
    private val categoryId = TelcoCategoryType.CATEGORY_PASCABAYAR

    private lateinit var inputNumberActionType: InputNumberActionType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            enquiryViewModel = viewModelProvider.get(DigitalTelcoEnquiryViewModel::class.java)
        }
    }

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun getScreenName(): String {
        return getString(R.string.digital_track_title_postpaid)
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
        recentNumbersView = view.findViewById(R.id.recent_numbers)
        postpaidClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListView = view.findViewById(R.id.promo_widget)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        layoutProgressBar = view.findViewById(R.id.layout_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInputFilterDataCollections()
        handleFocusClientNumber()
        renderClientNumber()
        getCatalogMenuDetail()
        getDataFromBundle()
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
                R.raw.temp_query_fav_number_digital), this::onSuccessFavNumbers, this::onErrorFavNumbers)
    }

    fun getDataFromBundle() {
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM) as DigitalTelcoExtraParam
            postpaidClientNumberWidget.setInputNumber(digitalTelcoExtraParam.clientNumber)
        }
    }

    fun renderClientNumber() {
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
                recentNumbersView.visibility = View.VISIBLE
                promoListView.visibility = View.VISIBLE
                buyWidget.setVisibilityLayout(false)
            }

            override fun onClientNumberHasFocus(clientNumber: String) {
                startActivityForResult(activity?.let {
                    DigitalSearchNumberActivity.newInstance(it,
                            ClientNumberType.TYPE_INPUT_TEL, "", favNumberList)
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
                val prefixClientNumber = postpaidClientNumberWidget.getInputNumber().substring(0, 4)

                operatorSelected = this.operatorData.rechargeCustomData.customDataCollections.filter {
                    it.value.equals(prefixClientNumber)
                }.single()
                val operatorName = operatorSelected.operator.attributes.name
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
                        topupAnalytics.eventClickOnContactPickerHomepage(categoryId, operatorName)
                    }
                }

                postpaidClientNumberWidget.setIconOperator(operatorSelected.operator.attributes.imageUrl)
            }
        } catch (exception: Exception) {
            view?.run {
                Toaster.showError(this, ErrorHandler.getErrorMessage(activity, exception), Snackbar.LENGTH_LONG)
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
            recentNumbersView.visibility = View.GONE
            promoListView.visibility = View.GONE
        } else {
            layoutProgressBar.visibility = View.GONE
            recentNumbersView.visibility = View.VISIBLE
            promoListView.visibility = View.VISIBLE
        }
    }

    fun onSuccessEnquiry(telcoEnquiryData: TelcoEnquiryData) {
        postpaidClientNumberWidget.showEnquiryResultPostpaid(telcoEnquiryData)
        recentNumbersView.visibility = View.GONE
        promoListView.visibility = View.GONE

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

    override fun setInputNumberFromContact(contactNumber: String) {
        inputNumberActionType = InputNumberActionType.CONTACT_HOMEPAGE
        postpaidClientNumberWidget.setInputNumber(contactNumber)
    }

    override fun handleCallbackSearchNumber(orderClientNumber: TelcoFavNumber, inputNumberActionTypeIndex: Int) {
        inputNumberActionType = InputNumberActionType.values()[inputNumberActionTypeIndex]
        postpaidClientNumberWidget.setInputNumber(orderClientNumber.clientNumber)
        postpaidClientNumberWidget.clearFocus()
    }

    override fun handleCallbackSearchNumberCancel() {
        postpaidClientNumberWidget.clearFocus()
    }

    override fun onClickItemRecentNumber(telcoRecommendation: TelcoRecommendation) {
        inputNumberActionType = InputNumberActionType.LATEST_TRANSACTION
        postpaidClientNumberWidget.setInputNumber(telcoRecommendation.clientNumber)
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
        postpaidClientNumberWidget.clearFocus()

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

    override fun onBackPressed() {
        topupAnalytics.eventClickBackButton(categoryId)
    }

    companion object {

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
