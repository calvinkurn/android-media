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
import com.tokopedia.thankyou_native.presentation.adapter.model.*
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
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
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
    val membershipRegisterData: LiveData<Result<MembershipRegister>> = _membershipRegisterData

    private val _bottomContentVisitableList = MutableLiveData<List<Visitable<*>>>()
    val bottomContentVisitableList: LiveData<List<Visitable<*>>> = _bottomContentVisitableList

    private val _bannerLiveData = MutableLiveData<BannerWidgetModel>()
    val bannerLiveData: LiveData<BannerWidgetModel> = _bannerLiveData

    var widgetOrder = listOf<String>()
        set(value) {
            field = value
            orderWidget(_bottomContentVisitableList.value.orEmpty())
        }

    fun getThanksPageData(paymentId: String, merchant: String) {
//        thanksPageDataUseCase.cancelJobs()
//        thanksPageDataUseCase.getThankPageData(
//            ::onThanksPageDataSuccess,
//            ::onThanksPageDataError,
//            paymentId,
//            merchant
//        )

        onThanksPageDataSuccess(
            Teleporter.gson.fromJson("{\n" +
                "        \"thanksPageData\": {\n" +
                "            \"merchant_code\": \"tokopedia\",\n" +
                "            \"profile_code\": \"TKPD_APPS\",\n" +
                "            \"payment_id\": \"2147677380\",\n" +
                "            \"payment_status\": 3,\n" +
                "            \"payment_type\": \"PEMUDA\",\n" +
                "            \"gateway_name\": \"GoPay\",\n" +
                "            \"gateway_image\": \"https://images.tokopedia.net/img/toppay/pemuda.png\",\n" +
                "            \"expire_time_unix\": 1686026384,\n" +
                "            \"expire_time_str\": \"Selasa, 06 Jun 2023 11:39 WIB\",\n" +
                "            \"page_type\": \"Success\",\n" +
                "            \"title\": \"Thanks Page\",\n" +
                "            \"message\": \"\",\n" +
                "            \"order_amount\": 638400,\n" +
                "            \"order_amount_str\": \"638.400\",\n" +
                "            \"amount\": 640900,\n" +
                "            \"amount_str\": \"640.900\",\n" +
                "            \"how_to_pay_applink\": \"tokopedia://howtopay?deadline=Selasa%2C+6+Jun+2023%2C+11%3A39+WIB&gateway_code=PEMUDA&gateway_logo=https%3A%2F%2Fimages.tokopedia.net%2Fimg%2Ftoppay%2Fpemuda.png&gateway_name=GoPay&merchant_code=tokopedia&payment_code=&payment_type=&total_amount=77.000&transaction_id=2147677380\",\n" +
                "            \"payment_items\": [\n" +
                "                {\n" +
                "                    \"item_name\": \"donation\",\n" +
                "                    \"item_desc\": \"Donasi\",\n" +
                "                    \"amount\": 5000,\n" +
                "                    \"amount_str\": \"5.000\",\n" +
                "                    \"__typename\": \"ThanksPaymentItem\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"item_name\": \"total_fee\",\n" +
                "                    \"item_desc\": \"Biaya Layanan\",\n" +
                "                    \"amount\": 2000,\n" +
                "                    \"amount_str\": \"2.000\",\n" +
                "                    \"__typename\": \"ThanksPaymentItem\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"item_name\": \"total_shipping\",\n" +
                "                    \"item_desc\": \"Total Ongkos Kirim\",\n" +
                "                    \"amount\": 94500,\n" +
                "                    \"amount_str\": \"94.500\",\n" +
                "                    \"__typename\": \"ThanksPaymentItem\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"item_name\": \"total_insurance_fee\",\n" +
                "                    \"item_desc\": \"Total Asuransi Pengiriman\",\n" +
                "                    \"amount\": 2400,\n" +
                "                    \"amount_str\": \"2.400\",\n" +
                "                    \"__typename\": \"ThanksPaymentItem\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"item_name\": \"total_add_on\",\n" +
                "                    \"item_desc\": \"Total Pelengkap\",\n" +
                "                    \"amount\": 36000,\n" +
                "                    \"amount_str\": \"36.000\",\n" +
                "                    \"__typename\": \"ThanksPaymentItem\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"fee_details\": [\n" +
                "                {\n" +
                "                    \"name\": \"Biaya Jasa Aplikasi\",\n" +
                "                    \"tooltip_desc\": \"Biaya jasa aplikasi kami pakai untuk kasih kamu layanan terbaik. Jumlahnya disesuaikan dengan total belanja & hanya dikenakan 1x per transaksi.\",\n" +
                "                    \"tooltip_title\": \"Tentang Biaya Jasa Aplikasi\",\n" +
                "                    \"show_tooltip\": true,\n" +
                "                    \"amount\": 2000,\n" +
                "                    \"__typename\": \"ThanksFeeDetails\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"payment_deduction\": [\n" +
                "                {\n" +
                "                    \"item_name\": \"cashback_stacked\",\n" +
                "                    \"item_desc\": \"(5.000 GoPay Coins)\",\n" +
                "                    \"amount\": 5000,\n" +
                "                    \"amount_str\": \"5.000\",\n" +
                "                    \"__typename\": \"ThanksPaymentItem\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"item_name\": \"total_logistic_discount\",\n" +
                "                    \"item_desc\": \"Potongan Ongkos Kirim\",\n" +
                "                    \"amount\": 44500,\n" +
                "                    \"amount_str\": \"44.500\",\n" +
                "                    \"__typename\": \"ThanksPaymentItem\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"payment_details\": [\n" +
                "                {\n" +
                "                    \"gateway_code\": \"PEMUDA\",\n" +
                "                    \"gateway_name\": \"GoPay\",\n" +
                "                    \"amount\": 674900,\n" +
                "                    \"amount_str\": \"684.900\",\n" +
                "                    \"amount_combine\": 0,\n" +
                "                    \"__typename\": \"PaymentDetails\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"promo_data\": [\n" +
                "                {\n" +
                "                    \"promo_code\": \"TESTACK3S\",\n" +
                "                    \"promo_desc\": \"\",\n" +
                "                    \"total_cashback\": 5000,\n" +
                "                    \"total_cashback_str\": \"5.000\",\n" +
                "                    \"total_discount\": 0,\n" +
                "                    \"total_discount_str\": \"0\",\n" +
                "                    \"__typename\": \"PromoData\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"order_group_list\": [\n" +
                "                {\n" +
                "                    \"id\": \"1\",\n" +
                "                    \"total_shipping_fee\": 20000,\n" +
                "                    \"total_bebasongkir_price\": 0,\n" +
                "                    \"dest_address\": \"jalan rumah\",\n" +
                "                    \"shipping_service_name\": \"Reguler\",\n" +
                "                    \"total_insurance_price\": 0,\n" +
                "                    \"shipper_name\": \"AnterAja\",\n" +
                "                    \"shipper_eta\": \"Estimasi tiba 9 - 10 Jun\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"2\",\n" +
                "                    \"total_shipping_fee\": 40000,\n" +
                "                    \"total_bebasongkir_price\": 0,\n" +
                "                    \"dest_address\": \"Tokopedia Care Tower Ground, Apartemen Ciputra International, Jl. Lkr. Luar Barat No.101, Rw. Buaya, Kecamatan Cengkareng, Kota Jakarta Barat, Daerah Khusus Ibukota Jakarta 11740\",\n" +
                "                    \"shipping_service_name\": \"Reguler\",\n" +
                "                    \"total_insurance_price\": 0,\n" +
                "                    \"shipper_name\": \"JNE\",\n" +
                "                    \"shipper_eta\": \"Estimasi tiba 9 - 11 Jun\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"3\",\n" +
                "                    \"total_shipping_fee\": 11500,\n" +
                "                    \"total_bebasongkir_price\": 0,\n" +
                "                    \"dest_address\": \"Tokopedia Tower, Jl. Prof. DR. Satrio, Setia Budi, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta, 12950 [Tokopedia Note: Mailing Room Lantai 29]\",\n" +
                "                    \"shipping_service_name\": \"Reguler\",\n" +
                "                    \"total_insurance_price\": 0,\n" +
                "                    \"shipper_name\": \"AnterAja\",\n" +
                "                    \"shipper_eta\": \"Estimasi tiba 9 - 10 Jun\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"4\",\n" +
                "                    \"total_shipping_fee\": 23000,\n" +
                "                    \"total_bebasongkir_price\": 0,\n" +
                "                    \"dest_address\": \"Jl. Agung Bar. 6, Tj. Priok, Kota Jkt Utara, Daerah Khusus Ibukota Jakarta, 14350 (Blok B20/2B)\",\n" +
                "                    \"shipping_service_name\": \"Reguler\",\n" +
                "                    \"total_insurance_price\": 2400,\n" +
                "                    \"shipper_name\": \"SiCepat Reg\",\n" +
                "                    \"shipper_eta\": \"Estimasi tiba 14 - 17 Jun\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"order_list\": [\n" +
                "                {\n" +
                "                    \"order_id\": \"ERR-13556262-1\",\n" +
                "                    \"order_group_id\": \"1\",\n" +
                "                    \"store_id\": \"6552061\",\n" +
                "                    \"store_type\": \"official_store\",\n" +
                "                    \"logistic_type\": \"AnterAja\",\n" +
                "                    \"store_name\": \"KilatanCahayas\",\n" +
                "                    \"item_list\": [\n" +
                "                        {\n" +
                "                            \"product_id\": \"2150389770\",\n" +
                "                            \"product_name\": \"TestKupon1\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 25000,\n" +
                "                            \"price_str\": \"25.000\",\n" +
                "                            \"quantity\": 2,\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"category_id\": \"777\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"addon_item\": [\n" +
                "                                {\n" +
                "                                    \"name\": \"Bungkusan keren\",\n" +
                "                                    \"quantity\": 1,\n" +
                "                                    \"price\": 10000,\n" +
                "                                    \"price_str\": \"10.000\",\n" +
                "                                    \"__typename\": \"AddOnItem\"\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"weight\": 0,\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"total_price\": 50000,\n" +
                "                            \"total_price_str\": \"50.000\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"category\": \"just-ignore-tes-staging-101\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"bundle_group_id\": \"\",\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2022/6/23/02deb07f-55b5-4aa0-abcb-7d2133e9c2bf.jpg\",\n" +
                "                            \"__typename\": \"ItemData\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"add_ons_section_description\": \"Biaya lain-lain\",\n" +
                "                    \"addon_item\": [\n" +
                "                        {\n" +
                "                            \"name\": \"gifting order\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"price\": 1000,\n" +
                "                            \"price_str\": \"1.000\",\n" +
                "                            \"__typename\": \"AddOnItem\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"bundle_group_data\": [],\n" +
                "                    \"shipping_amount\": 10000,\n" +
                "                    \"shipping_amount_str\": \"10.000\",\n" +
                "                    \"shipping_desc\": \"Reguler\",\n" +
                "                    \"logistic_eta\": \"Estimasi tiba 9 - 10 Jun\",\n" +
                "                    \"logistic_duration\": \"Reguler\",\n" +
                "                    \"insurance_amount\": 0,\n" +
                "                    \"insurance_amount_str\": \"0\",\n" +
                "                    \"address\": \"jalan rumah\",\n" +
                "                    \"promo_data\": [\n" +
                "                        {\n" +
                "                            \"promo_code\": \"logistic\",\n" +
                "                            \"promo_desc\": \"\",\n" +
                "                            \"total_cashback\": 0,\n" +
                "                            \"total_cashback_str\": \"0\",\n" +
                "                            \"total_discount\": 10000,\n" +
                "                            \"total_discount_str\": \"10.000\",\n" +
                "                            \"__typename\": \"PromoData\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"tax\": 2000,\n" +
                "                    \"coupon\": \"TESTACK3S\",\n" +
                "                    \"revenue\": 71000,\n" +
                "                    \"__typename\": \"ThanksOrderData\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"order_id\": \"ERR-13556262-2\",\n" +
                "                    \"order_group_id\": \"1\",\n" +
                "                    \"store_id\": \"6552061\",\n" +
                "                    \"store_type\": \"official_store\",\n" +
                "                    \"logistic_type\": \"AnterAja\",\n" +
                "                    \"store_name\": \"KilatanCahayas\",\n" +
                "                    \"item_list\": [\n" +
                "                        {\n" +
                "                            \"product_id\": \"2150389770\",\n" +
                "                            \"product_name\": \"TestKupon1\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 25000,\n" +
                "                            \"price_str\": \"25.000\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"category_id\": \"777\",\n" +
                "                            \"quantity\": 2,\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"addon_item\": [\n" +
                "                                {\n" +
                "                                    \"name\": \"Bungkusan keren\",\n" +
                "                                    \"quantity\": 1,\n" +
                "                                    \"price\": 10000,\n" +
                "                                    \"price_str\": \"10.000\",\n" +
                "                                    \"__typename\": \"AddOnItem\"\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"weight\": 0,\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"total_price\": 50000,\n" +
                "                            \"total_price_str\": \"50.000\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"category\": \"just-ignore-tes-staging-101\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"bundle_group_id\": \"\",\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2022/6/23/02deb07f-55b5-4aa0-abcb-7d2133e9c2bf.jpg\",\n" +
                "                            \"__typename\": \"ItemData\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"bundle_group_data\": [],\n" +
                "                    \"add_ons_section_description\": \"\",\n" +
                "                    \"addon_item\": [],\n" +
                "                    \"shipping_amount\": 10000,\n" +
                "                    \"shipping_amount_str\": \"10.000\",\n" +
                "                    \"shipping_desc\": \"Reguler\",\n" +
                "                    \"logistic_eta\": \"Estimasi tiba 9 - 10 Jun\",\n" +
                "                    \"logistic_duration\": \"Reguler\",\n" +
                "                    \"insurance_amount\": 0,\n" +
                "                    \"insurance_amount_str\": \"0\",\n" +
                "                    \"address\": \"jalan rumah\",\n" +
                "                    \"promo_data\": [],\n" +
                "                    \"tax\": 2000,\n" +
                "                    \"coupon\": \"\",\n" +
                "                    \"revenue\": 70000,\n" +
                "                    \"__typename\": \"ThanksOrderData\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"order_id\": \"ERR-13560415-1\",\n" +
                "                    \"order_group_id\": \"2\",\n" +
                "                    \"store_id\": \"6552061\",\n" +
                "                    \"store_type\": \"official_store\",\n" +
                "                    \"logistic_type\": \"JNE\",\n" +
                "                    \"store_name\": \"KilatanCahayas\",\n" +
                "                    \"add_ons_section_description\": \"Biaya lain-lain 1\",\n" +
                "                    \"addon_item\": [\n" +
                "                        {\n" +
                "                            \"name\": \"Pelengkap Pesanan 1\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"price\": 1000,\n" +
                "                            \"price_str\": \"1.000\",\n" +
                "                            \"__typename\": \"AddOnItem\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Pelengkap Pesanan 1\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"price\": 1000,\n" +
                "                            \"price_str\": \"1.000\",\n" +
                "                            \"__typename\": \"AddOnItem\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Pelengkap Pesanan 1\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"price\": 1000,\n" +
                "                            \"price_str\": \"1.000\",\n" +
                "                            \"__typename\": \"AddOnItem\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"item_list\": [\n" +
                "                        {\n" +
                "                            \"product_id\": \"2148008540\",\n" +
                "                            \"product_name\": \"Pastelnonvar2\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 1950,\n" +
                "                            \"price_str\": \"1.950\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"weight\": 0,\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"total_price\": 1950,\n" +
                "                            \"total_price_str\": \"1.950\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"category\": \"buku_hobi_humor\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2021/10/6/80776147-06e8-4072-9b57-fac1004912de.jpg\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"category_id\": \"777\",\n" +
                "                            \"bundle_group_id\": \"bid:62114-pid:2148008539-pid1:2148008539-pid2:2148008540\",\n" +
                "                            \"addon_item\": []\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"product_id\": \"2148008539\",\n" +
                "                            \"product_name\": \"pastelnonvar1\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 1750,\n" +
                "                            \"price_str\": \"1.750\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"weight\": 0,\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"total_price\": 1750,\n" +
                "                            \"total_price_str\": \"1.750\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"category\": \"samplemakanankering-edit\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2021/10/6/6e807f98-816a-4fb3-af3d-749489fd767c.jpg\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"category_id\": \"3230\",\n" +
                "                            \"bundle_group_id\": \"bid:62114-pid:2148008539-pid1:2148008539-pid2:2148008540\",\n" +
                "                            \"addon_item\": []\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"product_id\": \"2148008539\",\n" +
                "                            \"product_name\": \"pastelnonvar1\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 1850,\n" +
                "                            \"price_str\": \"1.850\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"weight\": 0,\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"total_price\": 1850,\n" +
                "                            \"total_price_str\": \"1.850\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"category\": \"samplemakanankering-edit\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2021/10/6/6e807f98-816a-4fb3-af3d-749489fd767c.jpg\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"category_id\": \"3230\",\n" +
                "                            \"bundle_group_id\": \"\",\n" +
                "                            \"addon_item\": [\n" +
                "                                {\n" +
                "                                    \"name\": \"Bungkusan keren\",\n" +
                "                                    \"quantity\": 1,\n" +
                "                                    \"price\": 10000,\n" +
                "                                    \"price_str\": \"10.000\",\n" +
                "                                    \"__typename\": \"AddOnItem\"\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"bundle_group_data\": [\n" +
                "                        {\n" +
                "                            \"group_id\": \"bid:62114-pid:2148008539-pid1:2148008539-pid2:2148008540\",\n" +
                "                            \"icon\": \"https://assets.tokopedia.net/asts/product-service/product_bundling_icon-GREY.svg\",\n" +
                "                            \"title\": \"BundlePastel\",\n" +
                "                            \"total_price\": 3700,\n" +
                "                            \"total_price_str\": \"3.700\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"shipping_amount\": 10000,\n" +
                "                    \"shipping_amount_str\": \"10.000\",\n" +
                "                    \"shipping_desc\": \"Reguler\",\n" +
                "                    \"insurance_amount\": 0,\n" +
                "                    \"insurance_amount_str\": \"0\",\n" +
                "                    \"logistic_duration\": \"Reguler\",\n" +
                "                    \"logistic_eta\": \"Estimasi tiba besok - 15 Jun\",\n" +
                "                    \"address\": \"Karet Semanggi, Kecamatan Setiabudi, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta [Tokopedia Note: toto]\",\n" +
                "                    \"promo_data\": [],\n" +
                "                    \"tax\": 2000,\n" +
                "                    \"coupon\": \"\",\n" +
                "                    \"revenue\": 15550\n" +
                "                },\n" +
                "                {\n" +
                "                    \"order_id\": \"ERR-13560415-2\",\n" +
                "                    \"order_group_id\": \"2\",\n" +
                "                    \"store_id\": \"6552061\",\n" +
                "                    \"store_type\": \"official_store\",\n" +
                "                    \"logistic_type\": \"AnterAja\",\n" +
                "                    \"store_name\": \"KilatanCahayas\",\n" +
                "                    \"add_ons_section_description\": \"Biaya lain-lain 2\",\n" +
                "                    \"addon_item\": [\n" +
                "                        {\n" +
                "                            \"name\": \"Pelengkap Pesanan 2\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"price\": 1000,\n" +
                "                            \"price_str\": \"1.000\",\n" +
                "                            \"__typename\": \"AddOnItem\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"name\": \"Pelengkap Pesanan 2\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"price\": 1000,\n" +
                "                            \"price_str\": \"1.000\",\n" +
                "                            \"__typename\": \"AddOnItem\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"item_list\": [\n" +
                "                        {\n" +
                "                            \"product_id\": \"2148008540\",\n" +
                "                            \"product_name\": \"Pastelnonvar2\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 1950,\n" +
                "                            \"price_str\": \"1.950\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"weight\": 0,\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"total_price\": 1950,\n" +
                "                            \"total_price_str\": \"1.950\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"category\": \"buku_hobi_humor\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2021/10/6/80776147-06e8-4072-9b57-fac1004912de.jpg\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"category_id\": \"777\",\n" +
                "                            \"bundle_group_id\": \"\",\n" +
                "                            \"addon_item\": []\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"product_id\": \"2148008539\",\n" +
                "                            \"product_name\": \"pastelnonvar1\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 1750,\n" +
                "                            \"price_str\": \"1.750\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"weight\": 0,\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"total_price\": 1750,\n" +
                "                            \"total_price_str\": \"1.750\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"category\": \"samplemakanankering-edit\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2021/10/6/6e807f98-816a-4fb3-af3d-749489fd767c.jpg\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"category_id\": \"3230\",\n" +
                "                            \"bundle_group_id\": \"bid:62114-pid:2148008539-pid1:2148008539-pid2:2148008540\",\n" +
                "                            \"addon_item\": []\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"product_id\": \"2148008539\",\n" +
                "                            \"product_name\": \"pastelnonvar1\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 1850,\n" +
                "                            \"price_str\": \"1.850\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"weight\": 0,\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"total_price\": 1850,\n" +
                "                            \"total_price_str\": \"1.850\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"category\": \"samplemakanankering-edit\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2021/10/6/6e807f98-816a-4fb3-af3d-749489fd767c.jpg\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"category_id\": \"3230\",\n" +
                "                            \"bundle_group_id\": \"bid:62114-pid:2148008539-pid1:2148008539-pid2:2148008540\",\n" +
                "                            \"addon_item\": []\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"bundle_group_data\": [\n" +
                "                        {\n" +
                "                            \"group_id\": \"bid:62114-pid:2148008539-pid1:2148008539-pid2:2148008540\",\n" +
                "                            \"icon\": \"https://assets.tokopedia.net/asts/product-service/product_bundling_icon-GREY.svg\",\n" +
                "                            \"title\": \"BundlePastel2\",\n" +
                "                            \"total_price\": 3600,\n" +
                "                            \"total_price_str\": \"3.600\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"shipping_amount\": 30000,\n" +
                "                    \"shipping_amount_str\": \"30.000\",\n" +
                "                    \"shipping_desc\": \"Reguler\",\n" +
                "                    \"insurance_amount\": 0,\n" +
                "                    \"insurance_amount_str\": \"0\",\n" +
                "                    \"logistic_duration\": \"Reguler\",\n" +
                "                    \"logistic_eta\": \"Estimasi tiba besok - 15 Jun\",\n" +
                "                    \"address\": \"Karet Semanggi, Kecamatan Setiabudi, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta [Tokopedia Note: toto]\",\n" +
                "                    \"promo_data\": [],\n" +
                "                    \"tax\": 2000,\n" +
                "                    \"coupon\": \"\",\n" +
                "                    \"revenue\": 15550\n" +
                "                },\n" +
                "                {\n" +
                "                    \"order_id\": \"1526952380\",\n" +
                "                    \"order_group_id\": \"3\",\n" +
                "                    \"store_id\": \"5942785\",\n" +
                "                    \"store_type\": \"official_store\",\n" +
                "                    \"logistic_type\": \"AnterAja\",\n" +
                "                    \"store_name\": \"Copper Indonesia\",\n" +
                "                    \"item_list\": [\n" +
                "                        {\n" +
                "                            \"product_id\": \"2324161124\",\n" +
                "                            \"product_name\": \"Xiaomi Mi 11 Lite - COPPER Tempered Glass FULL GLUE PREMIUM GLOSSY - TG GLOSSY\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 59900,\n" +
                "                            \"category_id\": \"777\",\n" +
                "                            \"price_str\": \"59.900\",\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"addon_item\": [],\n" +
                "                            \"weight\": 0,\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"total_price\": 59900,\n" +
                "                            \"total_price_str\": \"59.900\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"category\": \"handphone-tablet_aksesoris-handphone_screen-protector-handphone\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"bundle_group_id\": \"\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2021/6/20/f07fec61-4abb-47ea-81a0-5e04d2de43dc.jpg\",\n" +
                "                            \"__typename\": \"ItemData\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"add_ons_section_description\": \"\",\n" +
                "                    \"addon_item\": [],\n" +
                "                    \"bundle_group_data\": [],\n" +
                "                    \"shipping_amount\": 11500,\n" +
                "                    \"shipping_amount_str\": \"11.500\",\n" +
                "                    \"shipping_desc\": \"Reguler\",\n" +
                "                    \"logistic_eta\": \"Estimasi tiba 13 - 14 Mar\",\n" +
                "                    \"logistic_duration\": \"Reguler\",\n" +
                "                    \"insurance_amount\": 0,\n" +
                "                    \"insurance_amount_str\": \"0\",\n" +
                "                    \"address\": \"Tokopedia Tower, Jl. Prof. DR. Satrio, Setia Budi, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta, 12950 [Tokopedia Note: Mailing Room Lantai 29]\",\n" +
                "                    \"promo_data\": [\n" +
                "                        {\n" +
                "                            \"promo_code\": \"logistic\",\n" +
                "                            \"promo_desc\": \"\",\n" +
                "                            \"total_cashback\": 0,\n" +
                "                            \"total_cashback_str\": \"0\",\n" +
                "                            \"total_discount\": 11500,\n" +
                "                            \"total_discount_str\": \"11.500\",\n" +
                "                            \"__typename\": \"PromoData\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"tax\": 1000,\n" +
                "                    \"coupon\": \"BOREGINTRA20\",\n" +
                "                    \"revenue\": 71400,\n" +
                "                    \"__typename\": \"ThanksOrderData\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"order_id\": \"1598529752\",\n" +
                "                    \"order_group_id\": \"4\",\n" +
                "                    \"store_id\": \"2428015\",\n" +
                "                    \"store_type\": \"Official Store\",\n" +
                "                    \"logistic_type\": \"SiCepat Reg\",\n" +
                "                    \"store_name\": \"venus mobile\",\n" +
                "                    \"item_list\": [\n" +
                "                        {\n" +
                "                            \"product_id\": \"9605691371\",\n" +
                "                            \"product_name\": \"Xiaomi Bag Urban Lifestyle City Backpack - Tas ransel Urban Life style - Dark Grey\",\n" +
                "                            \"product_brand\": \"none / other\",\n" +
                "                            \"price\": 369000,\n" +
                "                            \"price_str\": \"369.000\",\n" +
                "                            \"quantity\": 1,\n" +
                "                            \"addon_item\": [],\n" +
                "                            \"weight\": 0,\n" +
                "                            \"category_id\": \"777\",\n" +
                "                            \"weight_unit\": \"\",\n" +
                "                            \"product_plan_protection\": 0,\n" +
                "                            \"total_price\": 369000,\n" +
                "                            \"total_price_str\": \"369.000\",\n" +
                "                            \"promo_code\": \"\",\n" +
                "                            \"is_bbi\": false,\n" +
                "                            \"category\": \"fashion-pria_tas-pria_tas-ransel-pria\",\n" +
                "                            \"bebas_ongkir_dimension\": \"none / other\",\n" +
                "                            \"variant\": \"\",\n" +
                "                            \"bundle_group_id\": \"\",\n" +
                "                            \"thumbnail_product\": \"https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/5/20/95ceee34-75f6-4aa2-916d-6f572d5157ba.jpg\",\n" +
                "                            \"__typename\": \"ItemData\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"add_ons_section_description\": \"\",\n" +
                "                    \"addon_item\": [],\n" +
                "                    \"bundle_group_data\": [],\n" +
                "                    \"shipping_amount\": 23000,\n" +
                "                    \"shipping_amount_str\": \"23.000\",\n" +
                "                    \"shipping_desc\": \"Reguler\",\n" +
                "                    \"logistic_eta\": \"Estimasi tiba 14 - 17 Jun\",\n" +
                "                    \"logistic_duration\": \"Reguler\",\n" +
                "                    \"insurance_amount\": 2400,\n" +
                "                    \"insurance_amount_str\": \"2.400\",\n" +
                "                    \"address\": \"Jl. Agung Bar. 6, Tj. Priok, Kota Jkt Utara, Daerah Khusus Ibukota Jakarta, 14350 (Blok B20/2B)\",\n" +
                "                    \"promo_data\": [\n" +
                "                        {\n" +
                "                            \"promo_code\": \"logistic\",\n" +
                "                            \"promo_desc\": \"\",\n" +
                "                            \"total_cashback\": 0,\n" +
                "                            \"total_cashback_str\": \"0\",\n" +
                "                            \"total_discount\": 20000,\n" +
                "                            \"total_discount_str\": \"20.000\",\n" +
                "                            \"__typename\": \"PromoData\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"tax\": 3000,\n" +
                "                    \"coupon\": \"BOREGINTRA20\",\n" +
                "                    \"revenue\": 394400,\n" +
                "                    \"__typename\": \"ThanksOrderData\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"additional_info\": {\n" +
                "                \"account_number\": \"\",\n" +
                "                \"account_dest\": \"\",\n" +
                "                \"bank_name\": \"\",\n" +
                "                \"bank_branch\": \"\",\n" +
                "                \"payment_code\": \"\",\n" +
                "                \"masked_number\": \"\",\n" +
                "                \"installment_info\": \"\",\n" +
                "                \"interest\": 0,\n" +
                "                \"__typename\": \"ThanksAdditionalInfo\"\n" +
                "            },\n" +
                "            \"how_to_pay\": \"\",\n" +
                "            \"event\": \"transaction\",\n" +
                "            \"event_category\": \"order complete\",\n" +
                "            \"event_action\": \"\",\n" +
                "            \"event_label\": \"regular checkout\",\n" +
                "            \"currency_code\": \"IDR\",\n" +
                "            \"whitelisted_rba\": true,\n" +
                "            \"current_site\": \"tokopediamarketplace\",\n" +
                "            \"business_unit\": \"payment\",\n" +
                "            \"push_gtm\": false,\n" +
                "            \"custom_data\": {\n" +
                "                \"tracking_data\": \"\",\n" +
                "                \"custom_order_url\": \"\",\n" +
                "                \"custom_order_url_app\": \"\",\n" +
                "                \"custom_home_url\": \"\",\n" +
                "                \"custom_home_url_app\": \"\",\n" +
                "                \"custom_title\": \"\",\n" +
                "                \"custom_subtitle\": \"\",\n" +
                "                \"custom_title_home_button\": \"\",\n" +
                "                \"custom_title_order_button\": \"\",\n" +
                "                \"custom_wtv_text\": \"\",\n" +
                "                \"__typename\": \"ThanksCustomization\"\n" +
                "            },\n" +
                "            \"is_mub\": false,\n" +
                "            \"is_new_user\": false,\n" +
                "            \"config_list\": \"{\\\"tickers\\\":\\\"[]\\\"}\",\n" +
                "            \"gateway_additional_data\": [],\n" +
                "            \"combine_amount\": 0,\n" +
                "            \"custom_data_url\": {\n" +
                "                \"auto_redirect\": \"\",\n" +
                "                \"back\": \"\",\n" +
                "                \"home\": \"\",\n" +
                "                \"order\": \"\",\n" +
                "                \"pms\": \"\"\n" +
                "            },\n" +
                "            \"custom_data_applink\": {\n" +
                "                \"auto_redirect\": \"\",\n" +
                "                \"back\": \"\",\n" +
                "                \"home\": \"\",\n" +
                "                \"order\": \"\",\n" +
                "                \"pms\": \"\"\n" +
                "            },\n" +
                "            \"custom_data_message\": {\n" +
                "                \"loader_text\": \"\",\n" +
                "                \"subtitle\": \"\",\n" +
                "                \"title\": \"\",\n" +
                "                \"title_home_button\": \"\",\n" +
                "                \"title_order_button\": \"\",\n" +
                "                \"wtv_text\": \"\"\n" +
                "            },\n" +
                "            \"custom_data_other\": {\n" +
                "                \"custom_illustration\": \"https://images.tokopedia.net/img/plus/tp/Thank%20You%20Page_Illustration.png\",\n" +
                "                \"delay_duration\": \"\",\n" +
                "                \"is_enjoy_plus_benefit\": \"false\",\n" +
                "                \"is_plus_transaction\": \"false\",\n" +
                "                \"tracking_data\": \"\",\n" +
                "                \"validate_engine_data\": \"{\\\"transaction_type\\\":\\\"3\\\"}\"\n" +
                "            },\n" +
                "            \"__typename\": \"ThanksData\"\n" +
                "        }", ThanksPageResponse::class.java).thanksPageData)
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
        var queryParamTokomember: TokoMemberRequestParam ? = null
        gyroEngineRequestUseCase.getFeatureEngineData(
            thanksPageData,
            null
        ) {
            if (it.success) {
                it.engineData?.let { featureEngineData ->
                    _gyroResponseLiveData.value = featureEngineData

                    widgetOrder = getWidgetOrder(featureEngineData)

                    getFeatureEngineBanner(featureEngineData)?.let { bannerModel ->
                        _bannerLiveData.value = bannerModel
                    }

                    val topAdsRequestParams = getTopAdsRequestParams(it.engineData)
                    if (topAdsRequestParams != null) {
                        loadTopAdsViewModelData(topAdsRequestParams, thanksPageData)
                    }
                    if (isTokomemberWidgetShow(it.engineData)) {
                        queryParamTokomember =
                            getTokomemberRequestParams(thanksPageData, it.engineData)
                        queryParamTokomember?.pageType =
                            PaymentPageMapper.getPaymentPageType(thanksPageData.pageType)
                    }
                    postGyroRecommendation(it.engineData, queryParamTokomember)
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
        return FeatureRecommendationMapper.getTokomemberRequestParams(thanksPageData, engineData)
    }

    private fun getWidgetOrder(featureEngineData: FeatureEngineData?): List<String> {
        val featureEngineWidgetOrder = FeatureRecommendationMapper.getWidgetOrder(featureEngineData)
        return if (featureEngineWidgetOrder.isNotEmpty()) {
            featureEngineWidgetOrder.replace(" ", "").split(",")
        } else {
            arrayListOf(
                TopAdsRequestParams.TAG,
                GyroRecommendationWidgetModel.TAG,
                HeadlineAdsWidgetModel.TAG,
                MarketplaceRecommendationWidgetModel.TAG,
                DigitalRecommendationWidgetModel.TAG,
                BannerWidgetModel.TAG
            )
        }
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
        gyroEngineMapperUseCase.getFeatureListData(engineData, queryParamTokomember, {
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

    fun registerTokomember(membershipCardID: String) {
        membershipRegisterUseCase.registerMembership(membershipCardID, {
            it?.let {
                _membershipRegisterData.postValue(Success(it))
            }
        }, {
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
