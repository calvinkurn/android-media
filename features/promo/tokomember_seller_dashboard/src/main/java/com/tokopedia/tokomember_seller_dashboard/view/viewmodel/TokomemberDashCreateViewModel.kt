package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.*
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardModifyInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCardPreviewRequestParams
import com.tokopedia.tokomember_seller_dashboard.model.CardData
import com.tokopedia.tokomember_seller_dashboard.model.CardDataTemplate
import com.tokopedia.tokomember_seller_dashboard.model.MembershipCreateEditCard
import com.tokopedia.tokomember_seller_dashboard.model.PreviewData
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

    private val _tokomemberCardModifyLiveData = MutableLiveData<Result<MembershipCreateEditCard>>()
    val tokomemberCardModifyLiveData: LiveData<Result<MembershipCreateEditCard>> =
        _tokomemberCardModifyLiveData

    private val _tokomemberCardPreviewLiveData = MutableLiveData<Result<PreviewData>>()
    val tokomemberCardPreviewLiveData: LiveData<Result<PreviewData>> =
        _tokomemberCardPreviewLiveData

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
            it.printStackTrace()
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
        tokomemberDashEditCardUsecase.modifyShopCard( {
            _tokomemberCardModifyLiveData.postValue(Success(it))
        }, {
            _tokomemberCardModifyLiveData.postValue(Fail(it))
        },tmCardModifyInput)
    }

    fun getPreviewData(tmCardPreviewRequestParams: TmCardPreviewRequestParams){
        tokomemberDashPreviewUsecase.cancelJobs()
        tokomemberDashPreviewUsecase.getPreviewData( {
            _tokomemberCardPreviewLiveData.postValue(Success(it))
        }, {
            _tokomemberCardPreviewLiveData.postValue(Fail(it))
        },tmCardPreviewRequestParams)
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