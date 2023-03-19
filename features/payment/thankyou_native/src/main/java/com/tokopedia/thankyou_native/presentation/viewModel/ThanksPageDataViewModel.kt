package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.teleporter.Teleporter
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressResponse
import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentPageMapper
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.model.ThanksPageResponse
import com.tokopedia.thankyou_native.domain.model.WalletBalance
import com.tokopedia.thankyou_native.domain.usecase.*
import com.tokopedia.thankyou_native.presentation.adapter.model.BannerWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.TokoMemberRequestParam
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.thankyou_native.presentation.views.widgettag.WidgetTag
import com.tokopedia.tokomember.model.MembershipRegister
import com.tokopedia.tokomember.usecase.MembershipRegisterUseCase
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ThanksPageDataViewModel @Inject constructor(
    private val thanksPageDataUseCase: ThanksPageDataUseCase,
    private val thanksPageMapperUseCase: ThanksPageMapperUseCase,
    private val gyroEngineRequestUseCase: GyroEngineRequestUseCase,
    private val fetchWalletBalanceUseCase: FetchWalletBalanceUseCase,
    private val gyroEngineMapperUseCase: GyroEngineMapperUseCase,
    private val topTickerDataUseCase: TopTickerUseCase,
    private val getDefaultAddressUseCase: GetDefaultAddressUseCase,
    private val thankYouTopAdsViewModelUseCase: ThankYouTopAdsViewModelUseCase,
    private val membershipRegisterUseCase: MembershipRegisterUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _thanksPageDataResultLiveData = MutableLiveData<Result<ThanksPageData>>()
    val thanksPageDataResultLiveData: LiveData<Result<ThanksPageData>> =
        _thanksPageDataResultLiveData

    private val _gyroRecommendationLiveData = MutableLiveData<GyroRecommendation>()
    val gyroRecommendationLiveData: LiveData<GyroRecommendation> = _gyroRecommendationLiveData

    private val _topTickerLiveData = MutableLiveData<Result<List<TickerData>>>()
    val topTickerLiveData: LiveData<Result<List<TickerData>>> = _topTickerLiveData

    private val _defaultAddressLiveData = MutableLiveData<Result<GetDefaultChosenAddressResponse>>()
    val defaultAddressLiveData: LiveData<Result<GetDefaultChosenAddressResponse>> =
        _defaultAddressLiveData

    private val _topAdsDataLiveData = MutableLiveData<TopAdsRequestParams>()
    val topAdsDataLiveData: LiveData<TopAdsRequestParams> = _topAdsDataLiveData

    private val _gyroResponseLiveData = MutableLiveData<FeatureEngineData>()
    val gyroResponseLiveData: LiveData<FeatureEngineData> = _gyroResponseLiveData

    private val _membershipRegisterData = MutableLiveData<Result<MembershipRegister>>()
    val membershipRegisterData : LiveData<Result<MembershipRegister>> = _membershipRegisterData

    private val _bottomContentVisitableList = MutableLiveData<List<Visitable<*>>>()
    val bottomContentVisitableList: LiveData<List<Visitable<*>>> = _bottomContentVisitableList

    private var widgetOrder = listOf<String>()

    fun getThanksPageData(paymentId: String, merchant: String) {
        thanksPageDataUseCase.cancelJobs()
        thanksPageDataUseCase.getThankPageData(
            ::onThanksPageDataSuccess,
            ::onThanksPageDataError,
            paymentId,
            merchant
        )
//        onThanksPageDataSuccess(
//            Teleporter.gson.fromJson(
//            "      {\"thanksPageData\": {\n" +
//                "        \"current_site\": \"tokopediamarketplace\",\n" +
//                "        \"business_unit\": \"payment\",\n" +
//                "        \"merchant_code\": \"tokopedia\",\n" +
//                "        \"profile_code\": \"TKPD_APPS\",\n" +
//                "        \"payment_id\": 2147584979,\n" +
//                "        \"payment_status\": 3,\n" +
//                "        \"payment_type\": \"INDODANA\",\n" +
//                "        \"gateway_name\": \"Indodana\",\n" +
//                "        \"gateway_image\": \"https://ecs7.tokopedia.net/img/toppay/logo-indodana.png\",\n" +
//                "        \"expire_time_unix\": 1664249819,\n" +
//                "        \"expire_time_str\": \"Selasa, 27 Sep 2022 10:36 WIB\",\n" +
//                "        \"page_type\": \"Success\",\n" +
//                "        \"title\": \"Thanks Page\",\n" +
//                "        \"message\": \"\",\n" +
//                "        \"order_amount\": 799000,\n" +
//                "        \"order_amount_str\": \"799.000\",\n" +
//                "        \"amount\": 800000,\n" +
//                "        \"amount_str\": \"800.000\",\n" +
//                "        \"combine_amount\": 0,\n" +
//                "        \"payment_items\": [\n" +
//                "          {\n" +
//                "            \"item_name\": \"total_fee\",\n" +
//                "            \"item_desc\": \"Biaya Layanan\",\n" +
//                "            \"amount\": 1000,\n" +
//                "            \"amount_str\": \"1.000\"\n" +
//                "          },\n" +
//                "          {\n" +
//                "            \"item_name\": \"total_shipping\",\n" +
//                "            \"item_desc\": \"Total Ongkos Kirim\",\n" +
//                "            \"amount\": 9000,\n" +
//                "            \"amount_str\": \"9.000\"\n" +
//                "          }\n" +
//                "        ],\n" +
//                "        \"payment_deduction\": [],\n" +
//                "        \"payment_details\": [\n" +
//                "          {\n" +
//                "            \"gateway_code\": \"INDODANA\",\n" +
//                "            \"gateway_name\": \"Indodana\",\n" +
//                "            \"amount\": 800000,\n" +
//                "            \"amount_str\": \"800.000\",\n" +
//                "            \"amount_combine\": 0,\n" +
//                "            \"amount_combine_str\": \"0\"\n" +
//                "          }\n" +
//                "        ],\n" +
//                "        \"promo_data\": [],\n" +
//                "        \"order_list\": [\n" +
//                "          {\n" +
//                "            \"order_id\": \"167275955\",\n" +
//                "            \"store_id\": \"6551412\",\n" +
//                "            \"store_type\": \"official_store\",\n" +
//                "            \"logistic_type\": \"JNE Reg\",\n" +
//                "            \"store_name\": \"QA 001 Shop\",\n" +
//                "            \"add_ons_section_description\": \"\",\n" +
//                "            \"addon_item\": [],\n" +
//                "            \"item_list\": [\n" +
//                "              {\n" +
//                "                \"product_id\": \"2147526300\",\n" +
//                "                \"product_name\": \"Sendal karet\",\n" +
//                "                \"product_brand\": \"none / other\",\n" +
//                "                \"price\": 10000,\n" +
//                "                \"price_str\": \"10.000\",\n" +
//                "                \"quantity\": 79,\n" +
//                "                \"product_plan_protection\": 0,\n" +
//                "                \"weight\": 0,\n" +
//                "                \"weight_unit\": \"\",\n" +
//                "                \"total_price\": 790000,\n" +
//                "                \"total_price_str\": \"790.000\",\n" +
//                "                \"promo_code\": \"\",\n" +
//                "                \"category\": \"fashion-wanita_sepatu_sandal\",\n" +
//                "                \"variant\": \"\",\n" +
//                "                \"thumbnail_product\": \"https://ecs7.tokopedia.net/img/cache/200-square/VqbcmM/2021/3/30/f63a4d90-01b8-4e75-943e-1bea53e223e1.jpg\",\n" +
//                "                \"bebas_ongkir_dimension\": \"none / other\",\n" +
//                "                \"is_bbi\": false,\n" +
//                "                \"category_id\": \"1907\",\n" +
//                "                \"bundle_group_id\": \"\",\n" +
//                "                \"addon_item\": []\n" +
//                "              }\n" +
//                "            ],\n" +
//                "            \"bundle_group_data\": [],\n" +
//                "            \"shipping_amount\": 9000,\n" +
//                "            \"shipping_amount_str\": \"9.000\",\n" +
//                "            \"shipping_desc\": \"Reguler\",\n" +
//                "            \"insurance_amount\": 0,\n" +
//                "            \"insurance_amount_str\": \"0\",\n" +
//                "            \"logistic_duration\": \"Reguler\",\n" +
//                "            \"logistic_eta\": \"Estimasi tiba besok - 30 Sep\",\n" +
//                "            \"address\": \"jalan rumah\",\n" +
//                "            \"promo_data\": [],\n" +
//                "            \"tax\": 1000,\n" +
//                "            \"coupon\": \"\",\n" +
//                "            \"revenue\": 799000\n" +
//                "          }\n" +
//                "        ],\n" +
//                "        \"additional_info\": {\n" +
//                "          \"account_number\": \"\",\n" +
//                "          \"account_dest\": \"\",\n" +
//                "          \"bank_name\": \"\",\n" +
//                "          \"bank_branch\": \" ()\",\n" +
//                "          \"payment_code\": \"\",\n" +
//                "          \"masked_number\": \"\",\n" +
//                "          \"installment_info\": \"Pembayaran 3 Bulan Rp 266.666\",\n" +
//                "          \"interest\": 0,\n" +
//                "          \"revenue\": 799000\n" +
//                "        },\n" +
//                "        \"how_to_pay\": \"\",\n" +
//                "        \"how_to_pay_applink\": \"tokopedia://howtopay?deadline=Selasa%2C+27+Sep+2022%2C+10%3A36+WIB&gateway_code=INDODANA&gateway_logo=https%3A%2F%2Fecs7.tokopedia.net%2Fimg%2Ftoppay%2Flogo-indodana.png&gateway_name=Indodana&merchant_code=tokopedia&payment_code=&payment_type=&total_amount=800.000&transaction_id=2147584979\",\n" +
//                "        \"event\": \"transaction\",\n" +
//                "        \"event_category\": \"order complete\",\n" +
//                "        \"event_action\": \"\",\n" +
//                "        \"event_label\": \"regular checkout\",\n" +
//                "        \"currency_code\": \"IDR\",\n" +
//                "        \"whitelisted_rba\": true,\n" +
//                "        \"push_gtm\": true,\n" +
//                "        \"new_user\": false,\n" +
//                "        \"is_mub\": false,\n" +
//                "        \"custom_data_applink\": {\n" +
//                "          \"auto_redirect\": \"\",\n" +
//                "          \"back\": \"\",\n" +
//                "          \"home\": \"\",\n" +
//                "          \"order\": \"\",\n" +
//                "          \"pms\": \"\"\n" +
//                "        },\n" +
//                "        \"custom_data_message\": {\n" +
//                "          \"loader_text\": \"\",\n" +
//                "          \"subtitle\": \"\",\n" +
//                "          \"title\": \"\",\n" +
//                "          \"title_home_button\": \"\",\n" +
//                "          \"title_order_button\": \"\",\n" +
//                "          \"wtv_text\": \"\"\n" +
//                "        },\n" +
//                "        \"custom_data_other\": {\n" +
//                "          \"custom_illustration\": \"https://images.tokopedia.net/img/plus/tp/Thank%20You%20Page_Illustration.png\",\n" +
//                "          \"delay_duration\": \"\",\n" +
//                "          \"is_enjoy_plus_benefit\": \"true\",\n" +
//                "          \"is_plus_transaction\": \"true\",\n" +
//                "          \"tracking_data\": \"\",\n" +
//                "          \"validate_engine_data\": \"{\\\"transaction_type\\\":\\\"3\\\"}\"\n" +
//                "        },\n" +
//                "        \"config_flag\": \"{\\\"auto_redirect\\\":false,\\\"enable_thanks_widget\\\":true,\\\"hide_dg_recom\\\":false,\\\"hide_feature_recom\\\":false,\\\"hide_global_menu\\\":false,\\\"hide_home_button\\\":false,\\\"hide_order_button\\\":false,\\\"hide_pg_recom\\\":false,\\\"hide_search_bar\\\":false}\",\n" +
//                "        \"config_list\": \"{\\\"tickers\\\":\\\"[]\\\"}\",\n" +
//                "        \"gateway_additional_data\": [],\n" +
//                "        \"fee_details\": [\n" +
//                "          {\n" +
//                "            \"name\": \"Biaya Layanan\",\n" +
//                "            \"amount\": 0\n" +
//                "          },\n" +
//                "          {\n" +
//                "            \"name\": \"Biaya Jasa Aplikasi\",\n" +
//                "            \"amount\": 1000\n" +
//                "          }\n" +
//                "        ],\n" +
//                "        \"thanks_summaries\": []\n" +
//                "      }\n }", ThanksPageResponse::class.java).thanksPageData)
    }

    fun checkForGoPayActivation(thanksPageData: ThanksPageData) {
        fetchWalletBalanceUseCase.cancelJobs()
        fetchWalletBalanceUseCase.getGoPayBalance {
            getFeatureEngine(thanksPageData, it)
        }
    }

    @VisibleForTesting
    fun getFeatureEngine(thanksPageData: ThanksPageData, walletBalance: WalletBalance?) {
        gyroEngineRequestUseCase.cancelJobs()
        var queryParamTokomember : TokoMemberRequestParam ? = null
        gyroEngineRequestUseCase.getFeatureEngineData(
            thanksPageData,
            null
        ) {
            if (it.success) {
                it.engineData?.let { featureEngineData ->
                    _gyroResponseLiveData.value = featureEngineData

                    getFeatureEngineBanner(featureEngineData)?.let { bannerModel ->
                        addBottomContentWidget(bannerModel)
                    }

                    widgetOrder = getWidgetOrder(featureEngineData)
                    orderWidget(_bottomContentVisitableList.value.orEmpty())

                    val topAdsRequestParams = getTopAdsRequestParams(it.engineData)
                    if (topAdsRequestParams != null) {
                        loadTopAdsViewModelData(topAdsRequestParams, thanksPageData)
                    }
                    if (isTokomemberWidgetShow(it.engineData)) {
                        queryParamTokomember  =
                            getTokomemberRequestParams(thanksPageData , it.engineData)
                        queryParamTokomember?.pageType =
                            PaymentPageMapper.getPaymentPageType(thanksPageData.pageType)
                    }
                    postGyroRecommendation(it.engineData , queryParamTokomember)
                }
            }
        }
    }

    @VisibleForTesting
     fun loadTopAdsViewModelData(
        topAdsRequestParams: TopAdsRequestParams,
        thanksPageData: ThanksPageData
    ) {
        thankYouTopAdsViewModelUseCase.cancelJobs()
        thankYouTopAdsViewModelUseCase.getTopAdsData(topAdsRequestParams, thanksPageData, {
            if (it.isNotEmpty()) {
                topAdsRequestParams.topAdsUIModelList = it
                _topAdsDataLiveData.postValue(topAdsRequestParams)
            }
        }, { it.printStackTrace() })
    }

    private fun getTopAdsRequestParams(engineData: FeatureEngineData?): TopAdsRequestParams? {
        return FeatureRecommendationMapper.getTopAdsParams(engineData)
    }

    private fun isTokomemberWidgetShow(engineData: FeatureEngineData?): Boolean {
        return FeatureRecommendationMapper.isTokomemberWidgetShow(engineData)
    }

    private fun getTokomemberRequestParams(
        thanksPageData: ThanksPageData,
        engineData: FeatureEngineData
    ): TokoMemberRequestParam {
        return FeatureRecommendationMapper.getTokomemberRequestParams(thanksPageData,engineData)
    }

    private fun getWidgetOrder(featureEngineData: FeatureEngineData?): List<String> {
        return FeatureRecommendationMapper.getWidgetOrder(featureEngineData).replace(" ", "").split(",")
    }

    private fun getFeatureEngineBanner(featureEngineData: FeatureEngineData?): BannerWidgetModel? {
        return FeatureRecommendationMapper.getBanner(featureEngineData)
    }

    @VisibleForTesting
     fun postGyroRecommendation(
        engineData: FeatureEngineData?,
        queryParamTokomember: TokoMemberRequestParam?
    ) {
        gyroEngineMapperUseCase.cancelJobs()
        gyroEngineMapperUseCase.getFeatureListData(engineData,queryParamTokomember, {
            _gyroRecommendationLiveData.postValue(it)
        }, { it.printStackTrace() })
    }

    private fun onThanksPageDataSuccess(thanksPageData: ThanksPageData) {
        thanksPageMapperUseCase.cancelJobs()
        thanksPageMapperUseCase.populateThanksPageDataFields(thanksPageData, {
            _thanksPageDataResultLiveData.postValue(Success(it))
        }, {
            _thanksPageDataResultLiveData.postValue(Fail(it))
        })
    }

    private fun onThanksPageDataError(throwable: Throwable) {
        _thanksPageDataResultLiveData.value = Fail(throwable)
    }

    fun getThanksPageTicker(configList: String?) {
        topTickerDataUseCase.getTopTickerData(configList, {
            _topTickerLiveData.postValue(Success(it))
        }, {
            _topTickerLiveData.postValue(Fail(it))
        })
    }

    fun resetAddressToDefault() {
        getDefaultAddressUseCase.getDefaultChosenAddress({
            _defaultAddressLiveData.postValue(Success(it))
        }, {
            _defaultAddressLiveData.postValue(Fail(it))
        })
    }

    fun registerTokomember(membershipCardID:String) {
        membershipRegisterUseCase.registerMembership(membershipCardID ,{
            it?.let {
                _membershipRegisterData.postValue(Success(it))
            }
        },{
            _membershipRegisterData.postValue(Fail(it))
        })
    }

    fun addBottomContentWidget(visitable: Visitable<*>) {
        orderWidget(_bottomContentVisitableList.value.orEmpty() + visitable)
    }

    private fun orderWidget(visitableList: List<Visitable<*>>) {
        if (widgetOrder.isEmpty()) {
            _bottomContentVisitableList.value = visitableList
        } else {
            _bottomContentVisitableList.value = visitableList.sortedBy {
                widgetOrder.indexOf((it as WidgetTag).tag)
            }
        }
    }

    override fun onCleared() {
        topTickerDataUseCase.cancelJobs()
        thanksPageDataUseCase.cancelJobs()
        gyroEngineRequestUseCase.cancelJobs()
        thankYouTopAdsViewModelUseCase.cancelJobs()
        thanksPageMapperUseCase.cancelJobs()
        gyroEngineMapperUseCase.cancelJobs()
        membershipRegisterUseCase.cancelJobs()
        fetchWalletBalanceUseCase.cancelJobs()
        super.onCleared()
    }

}
