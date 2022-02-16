package com.tokopedia.topchat.chattemplate.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper.mapToTemplateUiModel
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.SetAvailabilityTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.SetAvailabilityTemplateUseCase.Companion.getAvailabilityJson
import com.tokopedia.topchat.chattemplate.domain.usecase.SetAvailabilityTemplateUseCase.Companion.toJsonArray
import com.tokopedia.topchat.chattemplate.view.uimodel.ArrangeResultModel
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateResultModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChatTemplateViewModel @Inject constructor(
    private val getTemplateUseCase: GetTemplateUseCase,
    private val setAvailabilityTemplateUseCase: SetAvailabilityTemplateUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var _chatTemplate = MutableLiveData<Result<GetTemplateResultModel>>()
    val chatTemplate: LiveData<Result<GetTemplateResultModel>>
        get() = _chatTemplate

    private var _templateAvailability = MutableLiveData<Pair<Boolean, Result<GetTemplateResultModel>>>()
    val templateAvailability: LiveData<Pair<Boolean, Result<GetTemplateResultModel>>>
        get() = _templateAvailability

    private var _arrangeTemplate = MutableLiveData<ArrangeResultModel>()
    val arrangeTemplate: LiveData<ArrangeResultModel>
        get() = _arrangeTemplate

    fun getTemplate(isSeller: Boolean) {
        launchCatchError(block = {
            val response = getTemplateUseCase.getTemplate(isSeller)
            _chatTemplate.value = Success(response.mapToTemplateUiModel())
        }, onError = {
            _chatTemplate.value = Fail(it)
        })
    }

    fun switchTemplateAvailability(isSeller: Boolean, isEnabled: Boolean) {
        launchCatchError(block = {
            val response = setAvailabilityTemplateUseCase.setAvailability(
                getAvailabilityJson(null, isSeller, isEnabled)
            )
            val result = Success(response.mapToTemplateUiModel())
            _templateAvailability.value = Pair(isEnabled, result)
        }, onError = {
            _templateAvailability.value = Pair(isEnabled, Fail(it))
        })
    }

    fun setArrange(isSeller: Boolean, isEnabled: Boolean, arrangedList: List<Int>, from: Int, to: Int) {
        launchCatchError(block = {
            val response = setAvailabilityTemplateUseCase.setAvailability(
                getAvailabilityJson(toJsonArray(arrangedList), isSeller, isEnabled)
            )
            val arrangeResult = ArrangeResultModel(
                to = to,
                from = from,
                templateResult = Success(response.mapToTemplateUiModel())
            )
            _arrangeTemplate.value = arrangeResult
        }, onError = {
            val arrangeResult = ArrangeResultModel(
                to = to,
                from = from,
                templateResult = Fail(it)
            )
            _arrangeTemplate.value = arrangeResult
        })
    }
}