package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.tokomember_common_widget.util.CouponType
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.*
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponCreateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponCreateValidateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponInitialUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponProgramValidateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberCardColorMapperUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashCardUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashEditCardUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramFormUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashPreviewUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashUpdateProgramUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemeberCardBgUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardModifyInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponValidateRequest
import com.tokopedia.tokomember_seller_dashboard.model.CardData
import com.tokopedia.tokomember_seller_dashboard.model.CardDataTemplate
import com.tokopedia.tokomember_seller_dashboard.model.MemberShipValidateResponse
import com.tokopedia.tokomember_seller_dashboard.model.MembershipCreateEditCard
import com.tokopedia.tokomember_seller_dashboard.model.ProgramDetailData
import com.tokopedia.tokomember_seller_dashboard.model.ProgramUpdateResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponInitialResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmKuponCreateMVResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmVoucherValidationPartialResponse
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBgItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColor
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColorItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class TokomemberDashCreateViewModel @Inject constructor(
    private val tokomemberDashCardUsecase: TokomemberDashCardUsecase,
    private val tokomemberCardColorMapperUsecase: TokomemberCardColorMapperUsecase,
    private val tokomemeberCardBgUsecase: TokomemeberCardBgUsecase,
    private val tokomemberDashEditCardUsecase: TokomemberDashEditCardUsecase,
    private val tokomemberDashPreviewUsecase: TokomemberDashPreviewUsecase,
    private val tokomemberDashGetProgramFormUsecase: TokomemberDashGetProgramFormUsecase,
    private val tokomemberDashUpdateProgramUsecase: TokomemberDashUpdateProgramUsecase,
    private val tmKuponCreateValidateUsecase: TmKuponCreateValidateUsecase,
    private val tmKuponCreateUsecase:TmKuponCreateUsecase,
    private val tmKuponProgramValidateUsecase: TmKuponProgramValidateUsecase,
    private val tmKuponInitialUsecase:TmKuponInitialUsecase,
    private val uploaderUseCase: UploaderUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _tokomemberCardColorResultLiveData =
        MutableLiveData<Result<TokomemberCardColorItem>>()
    val tokomemberCardColorResultLiveData: LiveData<Result<TokomemberCardColorItem>> =
        _tokomemberCardColorResultLiveData

    private val _tokomemberCardBgResultLiveData = MutableLiveData<Result<TokomemberCardBgItem>>()
    val tokomemberCardBgResultLiveData: LiveData<Result<TokomemberCardBgItem>> =
        _tokomemberCardBgResultLiveData

    private val _tokomemberCardResultLiveData = MutableLiveData<Result<CardDataTemplate>>()
    val tokomemberCardResultLiveData: LiveData<Result<CardDataTemplate>> =
        _tokomemberCardResultLiveData

    private val _tokomemberCardModifyLiveData = MutableLiveData<TokoLiveDataResult<MembershipCreateEditCard>>()
    val tokomemberCardModifyLiveData: LiveData<TokoLiveDataResult<MembershipCreateEditCard>> =
        _tokomemberCardModifyLiveData

    private val _tmCouponPreValidateSingleCouponLiveData = MutableLiveData<TokoLiveDataResult<TmVoucherValidationPartialResponse>>()
    val tmCouponPreValidateSingleCouponLiveData: LiveData<TokoLiveDataResult<TmVoucherValidationPartialResponse>> =
        _tmCouponPreValidateSingleCouponLiveData

    private val _tmCouponPreValidateMultipleCouponLiveData = MutableLiveData<TokoLiveDataResult<TmVoucherValidationPartialResponse>>()
    val tmCouponPreValidateMultipleCouponLiveData: LiveData<TokoLiveDataResult<TmVoucherValidationPartialResponse>> =
        _tmCouponPreValidateMultipleCouponLiveData

    private val _tmCouponCreateLiveData = MutableLiveData<Result<TmKuponCreateMVResponse>>()
    val tmCouponCreateLiveData: LiveData<Result<TmKuponCreateMVResponse>> =
        _tmCouponCreateLiveData

    private val _tmProgramValidateLiveData = MutableLiveData<TokoLiveDataResult<MemberShipValidateResponse>>()
    val tmProgramValidateLiveData: LiveData<TokoLiveDataResult<MemberShipValidateResponse>> =
        _tmProgramValidateLiveData

    private val _tmCouponInitialLiveData = MutableLiveData<TokoLiveDataResult<TmCouponInitialResponse>>()
    val tmCouponInitialLiveData: LiveData<TokoLiveDataResult<TmCouponInitialResponse>> =
        _tmCouponInitialLiveData

    private val _tmCouponUploadLiveData = MutableLiveData<TokoLiveDataResult<UploadResult>>()
    val tmCouponUploadLiveData: LiveData<TokoLiveDataResult<UploadResult>> =
        _tmCouponUploadLiveData

    private val _tmCouponUploadMultipleLiveData = MutableLiveData<TokoLiveDataResult<UploadResult>>()
    val tmCouponUploadMultipleLiveData: LiveData<TokoLiveDataResult<UploadResult>> =
        _tmCouponUploadMultipleLiveData

    private val _tokomemberProgramResultLiveData = MutableLiveData<Result<ProgramDetailData>>()
    val tokomemberProgramResultLiveData: LiveData<Result<ProgramDetailData>> = _tokomemberProgramResultLiveData

    private val _tokomemberProgramUpdateResultLiveData = MutableLiveData<Result<ProgramUpdateResponse>>()
    val tokomemberProgramUpdateResultLiveData: LiveData<Result<ProgramUpdateResponse>> = _tokomemberProgramUpdateResultLiveData

    fun getProgramInfo(programID: Int ,shopId: Int ,actionType: String, query: String = "") {
        tokomemberDashGetProgramFormUsecase.cancelJobs()
        tokomemberDashGetProgramFormUsecase.getProgramInfo({
            _tokomemberProgramResultLiveData.postValue(Success(it))
        }, {
            _tokomemberProgramResultLiveData.postValue(Fail(it))
        }, programID,shopId,actionType, query)
    }

    fun updateProgram(programUpdateDataInput: ProgramUpdateDataInput) {
        tokomemberDashUpdateProgramUsecase.cancelJobs()
        tokomemberDashUpdateProgramUsecase.updateProgram({
            _tokomemberProgramUpdateResultLiveData.postValue(Success(it))
        }, {
            _tokomemberProgramUpdateResultLiveData.postValue(Fail(it))
        }, programUpdateDataInput)
    }

    fun getCardInfo(cardID: Int) {
        tokomemberDashCardUsecase.cancelJobs()
        tokomemberDashCardUsecase.getMembershipCardInfo({
            _tokomemberCardResultLiveData.postValue(
                Success(
                    CardDataTemplate(
                        card = it.membershipGetCardForm?.card,
                        cardTemplate = it.membershipGetCardForm?.cardTemplate,
                        cardTemplateImageList = it.membershipGetCardForm?.cardTemplateImageList
                    )
                )
            )
            getCardColorData(it)
        }, {
            _tokomemberCardResultLiveData.postValue(Fail(it))
        }, cardID)
    }

    private fun getCardBackgroundData(cardData: CardData, colorcode:String, pattern: ArrayList<String>) {
        tokomemeberCardBgUsecase.cancelJobs()
        tokomemeberCardBgUsecase.getCardBgDataN(cardData,colorcode,pattern,{
            _tokomemberCardBgResultLiveData.postValue(Success(it))
        }) {
            _tokomemberCardBgResultLiveData.postValue(Fail(it))
        }
    }

    private fun getCardColorData(cardData: CardData) {
        tokomemberDashCardUsecase.cancelJobs()
        tokomemberCardColorMapperUsecase.getCardColorData(cardData, {
            _tokomemberCardColorResultLiveData.postValue(Success(it))
            getCardBackgroundData(cardData,(it.tokoVisitableCardColor[0] as TokomemberCardColor).id?:"",(it.tokoVisitableCardColor[0] as TokomemberCardColor).tokoCardPatternList)
        }, {
            _tokomemberCardColorResultLiveData.postValue(Fail(it))
        })
    }

    fun modifyShopCard(tmCardModifyInput: TmCardModifyInput){
        tokomemberDashEditCardUsecase.cancelJobs()
        _tokomemberCardModifyLiveData.postValue(TokoLiveDataResult.loading())
        tokomemberDashEditCardUsecase.modifyShopCard( {
            _tokomemberCardModifyLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tokomemberCardModifyLiveData.postValue(TokoLiveDataResult.error(it))
        },tmCardModifyInput)
    }

    fun createCoupon(tmMerchantCouponUnifyRequest: TmMerchantCouponUnifyRequest){
        tmKuponCreateUsecase.cancelJobs()
        tmKuponCreateUsecase.createKupon( {
            _tmCouponCreateLiveData.postValue(Success(it))
        }, {
            _tmCouponCreateLiveData.postValue(Fail(it))
        },tmMerchantCouponUnifyRequest)
    }

    fun getInitialCouponData(actionType: String, couponType: String){
        tmKuponInitialUsecase.cancelJobs()
        tmKuponInitialUsecase.getInitialCoupon( {
            _tmCouponInitialLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmCouponInitialLiveData.postValue(TokoLiveDataResult.error(it))
        }, actionType, couponType)
    }

    fun validateProgram(shopId: String, startTime:String ,endTime:String){
        tmKuponProgramValidateUsecase.cancelJobs()
        tmKuponProgramValidateUsecase.getMembershipValidateInfo( {
            _tmProgramValidateLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmProgramValidateLiveData.postValue(TokoLiveDataResult.error(it, ValidateCouponError()))
        }, shopId, startTime, endTime)
    }

    fun preValidateCoupon(tmCouponValidateRequest: TmCouponValidateRequest){
        tmKuponCreateValidateUsecase.cancelJobs()
        _tmCouponPreValidateSingleCouponLiveData.postValue(TokoLiveDataResult.loading())
        tmKuponCreateValidateUsecase.getPartialValidateData( {
            _tmCouponPreValidateSingleCouponLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmCouponPreValidateSingleCouponLiveData.postValue(TokoLiveDataResult.error(it, ValidateVipError()))
        },tmCouponValidateRequest)
    }

     fun preValidateMultipleCoupon(tmCouponValidateRequest: TmCouponValidateRequest){
        tmKuponCreateValidateUsecase.cancelJobs()
        tmKuponCreateValidateUsecase.getPartialValidateData( {
            _tmCouponPreValidateMultipleCouponLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmCouponPreValidateMultipleCouponLiveData.postValue(TokoLiveDataResult.error(it,ValidatePremiumError()))
        },tmCouponValidateRequest)
    }

    fun uploadImageVip(image: File) {
        val param = uploaderUseCase.createParams("", image)
        launchCatchError(dispatcher, block = {
            val result = uploaderUseCase(param)
            _tmCouponUploadLiveData.postValue(TokoLiveDataResult.success(result))
        }, onError = {
            _tmCouponUploadLiveData.postValue(TokoLiveDataResult.error(it, UploadVipCouponError()))
        })
    }

     fun uploadImagePremium(image: File) {
        val param = uploaderUseCase.createParams("", image)
        launchCatchError(dispatcher, block = {
            val result = uploaderUseCase(param)
            _tmCouponUploadMultipleLiveData.postValue(TokoLiveDataResult.success(result))
        }, onError = {
            _tmCouponUploadMultipleLiveData.postValue(TokoLiveDataResult.error(it,
                UploadPremiumCouponError()
            ))
        })
    }

    override fun onCleared() {
        tokomemberCardColorMapperUsecase.cancelJobs()
        tokomemeberCardBgUsecase.cancelJobs()
        tokomemberDashCardUsecase.cancelJobs()
        tokomemberDashEditCardUsecase.cancelJobs()
        tokomemberDashPreviewUsecase.cancelJobs()
        super.onCleared()
    }
}