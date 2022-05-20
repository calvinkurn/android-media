package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.*
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardModifyInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponCreateRequest
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponValidateRequest
import com.tokopedia.tokomember_seller_dashboard.model.*
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBgItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColor
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColorItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
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
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
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

    private val _tmCouponPreValidateLiveData = MutableLiveData<Result<TmVoucherValidationPartialResponse>>()
    val tmCouponPreValidateLiveData: LiveData<Result<TmVoucherValidationPartialResponse>> =
        _tmCouponPreValidateLiveData

    private val _tmCouponCreateLiveData = MutableLiveData<Result<TmKuponCreateMVResponse>>()
    val tmCouponCreateLiveData: LiveData<Result<TmKuponCreateMVResponse>> =
        _tmCouponCreateLiveData

    private val _tmProgramValidateLiveData = MutableLiveData<Result<MemberShipValidateResponse>>()
    val tmProgramValidateLiveData: LiveData<Result<MemberShipValidateResponse>> =
        _tmProgramValidateLiveData

    private val _tmCouponInitialLiveData = MutableLiveData<Result<TmCouponInitialResponse>>()
    val tmCouponInitialLiveData: LiveData<Result<TmCouponInitialResponse>> =
        _tmCouponInitialLiveData

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

    fun createCoupon(tmCouponCreateRequest: TmCouponCreateRequest){
        tmKuponCreateUsecase.cancelJobs()
        tmKuponCreateUsecase.createKupon( {
            _tmCouponCreateLiveData.postValue(Success(it))
        }, {
            _tmCouponCreateLiveData.postValue(Fail(it))
        },tmCouponCreateRequest)
    }

    fun getInitialCouponData(){
        tmKuponInitialUsecase.cancelJobs()
        tmKuponInitialUsecase.getInitialCoupon( {
            _tmCouponInitialLiveData.postValue(Success(it))
        }, {
            _tmCouponInitialLiveData.postValue(Fail(it))
        })
    }

    fun validateProgram(){
        tmKuponProgramValidateUsecase.cancelJobs()
        tmKuponProgramValidateUsecase.getMembershipValidateInfo( {
            _tmProgramValidateLiveData.postValue(Success(it))
        }, {
            _tmProgramValidateLiveData.postValue(Fail(it))
        })
    }

    fun preValidateCoupon(tmCouponValidateRequest: TmCouponValidateRequest){
        tmKuponCreateValidateUsecase.cancelJobs()
        tmKuponCreateValidateUsecase.getPartialValidateData( {
            _tmCouponPreValidateLiveData.postValue(Success(it))
        }, {
            _tmCouponPreValidateLiveData.postValue(Fail(it))
        },tmCouponValidateRequest)
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