package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.localizationchooseaddress.domain.model.GetDefaultChosenAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetDefaultChosenAddressUseCase
import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentPageMapper
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.model.WalletBalance
import com.tokopedia.thankyou_native.domain.usecase.FetchWalletBalanceUseCase
import com.tokopedia.thankyou_native.domain.usecase.GyroEngineMapperUseCase
import com.tokopedia.thankyou_native.domain.usecase.GyroEngineRequestUseCase
import com.tokopedia.thankyou_native.domain.usecase.ThankYouTopAdsViewModelUseCase
import com.tokopedia.thankyou_native.domain.usecase.ThanksPageDataUseCase
import com.tokopedia.thankyou_native.domain.usecase.ThanksPageMapperUseCase
import com.tokopedia.thankyou_native.domain.usecase.TopTickerUseCase
import com.tokopedia.thankyou_native.presentation.adapter.model.BannerWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.DigitalRecommendationWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.HeadlineAdsWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.MarketplaceRecommendationWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.TokoMemberRequestParam
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.thankyou_native.presentation.adapter.model.WidgetTag
import com.tokopedia.tokomember.model.MembershipRegister
import com.tokopedia.tokomember.usecase.MembershipRegisterUseCase
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThanksPageDataViewModel @Inject constructor(
    private val thanksPageDataUseCase: ThanksPageDataUseCase,
    private val thanksPageMapperUseCase: ThanksPageMapperUseCase,
    private val gyroEngineRequestUseCase: GyroEngineRequestUseCase,
    private val fetchWalletBalanceUseCase: FetchWalletBalanceUseCase,
    private val gyroEngineMapperUseCase: GyroEngineMapperUseCase,
    private val topTickerDataUseCase: TopTickerUseCase,
    private val getDefaultAddressUseCase: GetDefaultChosenAddressUseCase,
    private val thankYouTopAdsViewModelUseCase: ThankYouTopAdsViewModelUseCase,
    private val membershipRegisterUseCase: MembershipRegisterUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    companion object {
        private const val TYP_SOURCE_ADDRESS = "typ"
    }

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
        thanksPageDataUseCase.cancelJobs()
        thanksPageDataUseCase.getThankPageData(
            ::onThanksPageDataSuccess,
            ::onThanksPageDataError,
            paymentId,
            merchant
        )
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
                topAdsRequestParams.tag = TopAdsRequestParams.TAG
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
                MarketplaceRecommendationWidgetModel.TAG,
                HeadlineAdsWidgetModel.TAG,
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
        launch {
            try {
                val response =
                    getDefaultAddressUseCase(GetDefaultChosenAddressParam(source = TYP_SOURCE_ADDRESS))
                _defaultAddressLiveData.postValue(Success(response.response))
            } catch (e: Exception) {
                _defaultAddressLiveData.postValue(Fail(e))
            }
        }
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
            _bottomContentVisitableList.value = visitableList.filter {
                widgetOrder.contains((it as WidgetTag).tag)
            }.sortedBy {
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
