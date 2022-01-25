package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressResponse
import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.usecase.*
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.tokomember.model.MembershipGetShopRegistrationWidget
import com.tokopedia.tokomember.model.MembershipRegister
import com.tokopedia.tokomember.usecase.MembershipRegisterUseCase
import com.tokopedia.tokomember.usecase.TokomemberUsecase
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
    private val gyroEngineMapperUseCase: GyroEngineMapperUseCase,
    private val topTickerDataUseCase: TopTickerUseCase,
    private val getDefaultAddressUseCase: GetDefaultAddressUseCase,
    private val thankYouTopAdsViewModelUseCase: ThankYouTopAdsViewModelUseCase,
    private val membershipRegisterUseCase: MembershipRegisterUseCase,
    private val tokomemberUsecase: TokomemberUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    val thanksPageDataResultLiveData = MutableLiveData<Result<ThanksPageData>>()
    val gyroRecommendationLiveData = MutableLiveData<GyroRecommendation>()
    val topTickerLiveData = MutableLiveData<Result<List<TickerData>>>()
    val defaultAddressLiveData = MutableLiveData<Result<GetDefaultChosenAddressResponse>>()
    val membershipRegisterData = MutableLiveData<Result<MembershipRegister>>()
    val tokomemberShopData = MutableLiveData<Result<MembershipGetShopRegistrationWidget>>()


    val topAdsDataLiveData = MutableLiveData<TopAdsRequestParams>()

    private val gyroResponseLiveData = MutableLiveData<FeatureEngineData>()


    fun getThanksPageData(paymentId: String, merchant: String) {
        thanksPageDataUseCase.cancelJobs()
        thanksPageDataUseCase.getThankPageData(
            ::onThanksPageDataSuccess,
            ::onThanksPageDataError,
            paymentId,
            merchant
        )
    }

    fun getFeatureEngine(thanksPageData: ThanksPageData) {
        gyroEngineRequestUseCase.cancelJobs()
        gyroEngineRequestUseCase.getFeatureEngineData(
            thanksPageData
        ) {
            if (it.success) {
                it.engineData?.let { featureEngineData ->
                    gyroResponseLiveData.value = featureEngineData

                    val topAdsRequestParams = getTopAdsRequestParams(it.engineData)
                    if (topAdsRequestParams != null) {
                        loadTopAdsViewModelData(topAdsRequestParams, thanksPageData)
                    }
                    if (isTokomemberWidgetShow(it.engineData)){
                        val queryParam : Pair<Int,Float> =
                            getTokomemberRequestParams(thanksPageData)
                        getTokomemberData(queryParam.first, queryParam.second)
                    }
                    postGyroRecommendation(it.engineData)
                }
            }
        }
    }

    private fun loadTopAdsViewModelData(
        topAdsRequestParams: TopAdsRequestParams,
        thanksPageData: ThanksPageData
    ) {
        thankYouTopAdsViewModelUseCase.cancelJobs()
        thankYouTopAdsViewModelUseCase.getTopAdsData(topAdsRequestParams, thanksPageData, {
            if (it.isNotEmpty()) {
                topAdsRequestParams.topAdsUIModelList = it
                topAdsDataLiveData.postValue(topAdsRequestParams)
            }
        }, { it.printStackTrace() })
    }

    private fun getTopAdsRequestParams(engineData: FeatureEngineData?): TopAdsRequestParams? {
        return FeatureRecommendationMapper.getTopAdsParams(engineData)
    }

    private fun isTokomemberWidgetShow(engineData: FeatureEngineData?): Boolean {
        return FeatureRecommendationMapper.isTokomemberWidgetShow(engineData)
    }

    private fun getTokomemberRequestParams(engineData: ThanksPageData): Pair<Int,Float> {
        return FeatureRecommendationMapper.getTokomemberRequestParams(engineData)
    }

    private fun postGyroRecommendation(engineData: FeatureEngineData?) {
        gyroEngineMapperUseCase.cancelJobs()
        gyroEngineMapperUseCase.getFeatureListData(engineData, {
            gyroRecommendationLiveData.postValue(it)
        }, { it.printStackTrace() })
    }

    private fun onThanksPageDataSuccess(thanksPageData: ThanksPageData) {
        thanksPageMapperUseCase.cancelJobs()
        thanksPageMapperUseCase.populateThanksPageDataFields(thanksPageData, {
            thanksPageDataResultLiveData.postValue(Success(it))
        }, {
            thanksPageDataResultLiveData.postValue(Fail(it))
        })
    }

    private fun onThanksPageDataError(throwable: Throwable) {
        thanksPageDataResultLiveData.value = Fail(throwable)
    }

    fun getThanksPageTicker(configList: String?) {
        topTickerDataUseCase.getTopTickerData(configList, {
            topTickerLiveData.postValue(Success(it))
        }, {
            topTickerLiveData.postValue(Fail(it))
        })
    }

    fun resetAddressToDefault() {
        getDefaultAddressUseCase.getDefaultChosenAddress({
            defaultAddressLiveData.postValue(Success(it))
        }, {
            defaultAddressLiveData.postValue(Fail(it))
        })
    }

    fun getTokomemberData(shopId:Int, amount:Float) {
        tokomemberUsecase.getTokomemberData(shopId,amount,{
            it?.let {
                tokomemberShopData.postValue(Success(it))
            }
        },{
            tokomemberShopData.postValue(Fail(it))
        })
    }

    fun registerTokomember(membershipCardID:String) {
        membershipRegisterUseCase.registerMembership(membershipCardID ,{
            it?.let {
                membershipRegisterData.postValue(Success(it))
            }
        },{
            membershipRegisterData.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        topTickerDataUseCase.cancelJobs()
        thanksPageDataUseCase.cancelJobs()
        gyroEngineRequestUseCase.cancelJobs()
        thankYouTopAdsViewModelUseCase.cancelJobs()
        thanksPageMapperUseCase.cancelJobs()
        gyroEngineMapperUseCase.cancelJobs()
        tokomemberUsecase.cancelJobs()
        membershipRegisterUseCase.cancelJobs()
        super.onCleared()
    }

}