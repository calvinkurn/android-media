package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponDetailUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponCreateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponCreateValidateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponInitialUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponProgramValidateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponUpdateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmSingleCouponCreateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberCardColorMapperUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashCardUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashEditCardUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramFormUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashUpdateProgramUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemeberCardBgUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardModifyInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponCreateRequest
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponUpdateRequest
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponValidateRequest
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmMerchantCouponUnifyRequest
import com.tokopedia.tokomember_seller_dashboard.model.CardData
import com.tokopedia.tokomember_seller_dashboard.model.CardDataTemplate
import com.tokopedia.tokomember_seller_dashboard.model.CouponCreateMultiple
import com.tokopedia.tokomember_seller_dashboard.model.CouponCreateSingle
import com.tokopedia.tokomember_seller_dashboard.model.MemberShipValidateResponse
import com.tokopedia.tokomember_seller_dashboard.model.MembershipCreateEditCardResponse
import com.tokopedia.tokomember_seller_dashboard.model.ProgramDetailData
import com.tokopedia.tokomember_seller_dashboard.model.ProgramUpdateResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponDetailResponseData
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponInitialResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmKuponUpdateMVResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmVoucherValidationPartialResponse
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.util.UploadPremiumCouponError
import com.tokopedia.tokomember_seller_dashboard.util.UploadVipCouponError
import com.tokopedia.tokomember_seller_dashboard.util.ValidateCouponError
import com.tokopedia.tokomember_seller_dashboard.util.ValidatePremiumError
import com.tokopedia.tokomember_seller_dashboard.util.ValidateVipError
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBgItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColor
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColorItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import java.io.File
import javax.inject.Inject

const val SOURCE_ID_VIP = "mxzxFi"
const val SOURCE_ID_PREMIUM = "togDTI"

class TmDashCreateViewModel @Inject constructor(
    private val tokomemberDashCardUsecase: TokomemberDashCardUsecase,
    private val tokomemberCardColorMapperUsecase: TokomemberCardColorMapperUsecase,
    private val tokomemeberCardBgUsecase: TokomemeberCardBgUsecase,
    private val tokomemberDashEditCardUsecase: TokomemberDashEditCardUsecase,
    private val tokomemberDashGetProgramFormUsecase: TokomemberDashGetProgramFormUsecase,
    private val tokomemberDashUpdateProgramUsecase: TokomemberDashUpdateProgramUsecase,
    private val tmKuponCreateValidateUsecase: TmKuponCreateValidateUsecase,
    private val tmKuponCreateUsecase:TmKuponCreateUsecase,
    private val tmSingleCouponCreateUsecase: TmSingleCouponCreateUsecase,
    private val tmKuponUpdateUsecase: TmKuponUpdateUsecase,
    private val tmKuponProgramValidateUsecase: TmKuponProgramValidateUsecase,
    private val tmKuponInitialUsecase:TmKuponInitialUsecase,
    private val uploaderUseCase: UploaderUseCase,
    private val tmCouponDetailUsecase: TmCouponDetailUsecase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _tokomemberCardColorResultLiveData =
        MutableLiveData<Result<TokomemberCardColorItem>>()
    val tokomemberCardColorResultLiveData: LiveData<Result<TokomemberCardColorItem>> =
        _tokomemberCardColorResultLiveData

    private val _tokomemberCardBgResultLiveData = MutableLiveData<Result<TokomemberCardBgItem>>()
    val tokomemberCardBgResultLiveData: LiveData<Result<TokomemberCardBgItem>> =
        _tokomemberCardBgResultLiveData

    private val _tmCardResultLiveData = MutableLiveData<TokoLiveDataResult<CardDataTemplate>>()
    val tmCardResultLiveData: LiveData<TokoLiveDataResult<CardDataTemplate>> =
        _tmCardResultLiveData

    private val _tokomemberCardModifyLiveData = MutableLiveData<TokoLiveDataResult<MembershipCreateEditCardResponse>>()
    val tokomemberCardModifyLiveData: LiveData<TokoLiveDataResult<MembershipCreateEditCardResponse>> =
        _tokomemberCardModifyLiveData

    private val _tmCouponPreValidateSingleCouponLiveData = MutableLiveData<TokoLiveDataResult<TmVoucherValidationPartialResponse>>()
    val tmCouponPreValidateSingleCouponLiveData: LiveData<TokoLiveDataResult<TmVoucherValidationPartialResponse>> =
        _tmCouponPreValidateSingleCouponLiveData

    private val _tmCouponPreValidateMultipleCouponLiveData = MutableLiveData<TokoLiveDataResult<TmVoucherValidationPartialResponse>>()
    val tmCouponPreValidateMultipleCouponLiveData: LiveData<TokoLiveDataResult<TmVoucherValidationPartialResponse>> =
        _tmCouponPreValidateMultipleCouponLiveData

    private val _tmCouponCreateLiveData = MutableLiveData<Result<CouponCreateMultiple>>()
    val tmCouponCreateLiveData: LiveData<Result<CouponCreateMultiple>> =
        _tmCouponCreateLiveData

    private val _tmSingleCouponCreateLiveData = MutableLiveData<Result<CouponCreateSingle>>()
    val tmSingleCouponCreateLiveData: LiveData<Result<CouponCreateSingle>> =
        _tmSingleCouponCreateLiveData

    private val _tmCouponUpdateLiveData = MutableLiveData<TokoLiveDataResult<TmKuponUpdateMVResponse>>()
    val tmCouponUpdateLiveData: LiveData<TokoLiveDataResult<TmKuponUpdateMVResponse>> =
        _tmCouponUpdateLiveData

    private val _tmProgramValidateLiveData = MutableLiveData<TokoLiveDataResult<MemberShipValidateResponse>>()
    val tmProgramValidateLiveData: LiveData<TokoLiveDataResult<MemberShipValidateResponse>> =
        _tmProgramValidateLiveData

    private val _tmCouponInitialLiveData = MutableLiveData<TokoLiveDataResult<TmCouponInitialResponse>>()
    val tmCouponInitialLiveData: LiveData<TokoLiveDataResult<TmCouponInitialResponse>> =
        _tmCouponInitialLiveData

    private val _tmCouponDetailLiveData = MutableLiveData<TokoLiveDataResult<TmCouponDetailResponseData>>()
    val tmCouponDetaillLiveData: LiveData<TokoLiveDataResult<TmCouponDetailResponseData>> =
        _tmCouponDetailLiveData

    private val _tmCouponUploadLiveData = MutableLiveData<TokoLiveDataResult<UploadResult>>()
    val tmCouponUploadLiveData: LiveData<TokoLiveDataResult<UploadResult>> =
        _tmCouponUploadLiveData

    private val _tmCouponUploadMultipleLiveData = MutableLiveData<TokoLiveDataResult<UploadResult>>()
    val tmCouponUploadMultipleLiveData: LiveData<TokoLiveDataResult<UploadResult>> =
        _tmCouponUploadMultipleLiveData

    private val _tmProgramResultLiveData = MutableLiveData<TokoLiveDataResult<ProgramDetailData>>()
    val tmProgramResultLiveData: LiveData<TokoLiveDataResult<ProgramDetailData>> = _tmProgramResultLiveData

    private val _tokomemberProgramUpdateResultLiveData = MutableLiveData<TokoLiveDataResult<ProgramUpdateResponse>>()
    val tokomemberProgramUpdateResultLiveData: LiveData<TokoLiveDataResult<ProgramUpdateResponse>> = _tokomemberProgramUpdateResultLiveData

    fun getProgramInfo(programID: Int ,shopId: Int ,actionType: String, query: String = "") {
        tokomemberDashGetProgramFormUsecase.cancelJobs()
        _tmProgramResultLiveData.postValue(TokoLiveDataResult.loading())
        tokomemberDashGetProgramFormUsecase.getProgramInfo({
            _tmProgramResultLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmProgramResultLiveData.postValue(TokoLiveDataResult.error(it))
        }, programID,shopId,actionType, query)
    }

    fun updateProgram(programUpdateDataInput: ProgramUpdateDataInput) {
        tokomemberDashUpdateProgramUsecase.cancelJobs()
        _tokomemberProgramUpdateResultLiveData.postValue(TokoLiveDataResult.loading())
        tokomemberDashUpdateProgramUsecase.updateProgram({
            _tokomemberProgramUpdateResultLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tokomemberProgramUpdateResultLiveData.postValue(TokoLiveDataResult.error(it))
        }, programUpdateDataInput)
    }

    fun getCardInfo(cardID: Int) {
        tokomemberDashCardUsecase.cancelJobs()
        _tmCardResultLiveData.postValue(TokoLiveDataResult.loading())
        tokomemberDashCardUsecase.getMembershipCardInfo({
            _tmCardResultLiveData.postValue(
                TokoLiveDataResult.success(
                    CardDataTemplate(
                        card = it.membershipGetCardForm?.card,
                        cardTemplate = it.membershipGetCardForm?.cardTemplate,
                        cardTemplateImageList = it.membershipGetCardForm?.cardTemplateImageList,
                        shopAvatar = it.membershipGetCardForm?.shopAvatar?:""
                    )
                )
            )
            getCardColorData(it)
        }, {
            _tmCardResultLiveData.postValue(TokoLiveDataResult.error(it))
        }, cardID)
    }

    fun getCardBackgroundData(cardData: CardData, colorcode:String, pattern: ArrayList<String>) {
        tokomemeberCardBgUsecase.cancelJobs()
        tokomemeberCardBgUsecase.getCardBgDataN(cardData,colorcode,pattern,{
            _tokomemberCardBgResultLiveData.postValue(Success(it))
        }) {
            _tokomemberCardBgResultLiveData.postValue(Fail(it))
        }
    }

    fun getCardColorData(cardData: CardData) {
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

    fun createSingleCoupon(tmMerchantCouponUnifyRequest: TmCouponCreateRequest){
        tmSingleCouponCreateUsecase.cancelJobs()
        tmSingleCouponCreateUsecase.createSingleCoupon( {
            _tmSingleCouponCreateLiveData.postValue(Success(it))
        }, {
            _tmSingleCouponCreateLiveData.postValue(Fail(it))
        },tmMerchantCouponUnifyRequest)
    }

    fun updateCoupon(tmCouponUpdateRequest: TmCouponUpdateRequest){
        tmKuponUpdateUsecase.cancelJobs()
        _tmCouponUpdateLiveData.postValue(TokoLiveDataResult.loading())
        tmKuponUpdateUsecase.updateCoupon( {
            _tmCouponUpdateLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmCouponUpdateLiveData.postValue(TokoLiveDataResult.error(it))
        },tmCouponUpdateRequest)
    }

    fun getInitialCouponData(actionType: String, couponType: String){
        tmKuponInitialUsecase.cancelJobs()
        tmKuponInitialUsecase.getInitialCoupon( {
            _tmCouponInitialLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmCouponInitialLiveData.postValue(TokoLiveDataResult.error(it))
        }, actionType, couponType)
    }

    fun validateProgram(shopId: String, startTime:String ,endTime:String , source:String){
        tmKuponProgramValidateUsecase.cancelJobs()
        tmKuponProgramValidateUsecase.getMembershipValidateInfo( {
            _tmProgramValidateLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tmProgramValidateLiveData.postValue(TokoLiveDataResult.error(it, ValidateCouponError()))
        }, shopId, startTime, endTime , source)
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
        val param = uploaderUseCase.createParams(SOURCE_ID_VIP, image)
        launchCatchError(dispatcher, block = {
            val result = uploaderUseCase(param)
            _tmCouponUploadLiveData.postValue(TokoLiveDataResult.success(result))
        }, onError = {
            _tmCouponUploadLiveData.postValue(TokoLiveDataResult.error(it, UploadVipCouponError()))
        })
    }

     fun uploadImagePremium(image: File) {
        val param = uploaderUseCase.createParams(SOURCE_ID_PREMIUM, image)
        launchCatchError(dispatcher, block = {
            val result = uploaderUseCase(param)
            _tmCouponUploadMultipleLiveData.postValue(TokoLiveDataResult.success(result))
        }, onError = {
            _tmCouponUploadMultipleLiveData.postValue(TokoLiveDataResult.error(it,
                UploadPremiumCouponError()
            ))
        })
    }

    fun getCouponDetail(voucherId: Int){
        tmCouponDetailUsecase.cancelJobs()
        tmCouponDetailUsecase.getCouponDetail({
            _tmCouponDetailLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
           _tmCouponDetailLiveData.postValue(TokoLiveDataResult.error(it))
        }, voucherId)
    }

    override fun onCleared() {
        tokomemberCardColorMapperUsecase.cancelJobs()
        tokomemeberCardBgUsecase.cancelJobs()
        tokomemberDashCardUsecase.cancelJobs()
        tokomemberDashEditCardUsecase.cancelJobs()
        super.onCleared()
    }
}
