package com.tokopedia.topchat.chattemplate.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.domain.mapper.TemplateChatMapper.mapToGetTemplateUiModel
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.RearrangeTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.ToggleTemplateUseCase
import com.tokopedia.topchat.chattemplate.view.uimodel.ArrangeResultModel
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateResultModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChatTemplateViewModel @Inject constructor(
    private val getTemplateUseCase: GetTemplateUseCase,
    private val toggleTemplateUseCase: ToggleTemplateUseCase,
    private val rearrangeTemplateUseCase: RearrangeTemplateUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var _chatTemplate = MutableLiveData<Result<GetTemplateResultModel>>()
    val chatTemplate: LiveData<Result<GetTemplateResultModel>>
        get() = _chatTemplate

    private var _templateAvailability = MutableLiveData<Pair<Boolean, Result<Int>>>()
    val templateAvailability: LiveData<Pair<Boolean, Result<Int>>>
        get() = _templateAvailability

    private var _arrangeTemplate = MutableLiveData<ArrangeResultModel>()
    val arrangeTemplate: LiveData<ArrangeResultModel>
        get() = _arrangeTemplate

    fun getTemplate(isSeller: Boolean) {
        launchCatchError(block = {
            val param = GetTemplateUseCase.Param(isSeller)
            val response = getTemplateUseCase(param)
            val uiModel = response.mapToGetTemplateUiModel(isSeller)
            _chatTemplate.value = Success(uiModel)
        }, onError = {
                _chatTemplate.value = Fail(it)
            })
    }

    fun toggleTemplate(isSeller: Boolean, isEnabled: Boolean) {
        launchCatchError(block = {
            val response = toggleTemplateUseCase(
                ToggleTemplateUseCase.Param(isEnabled)
            )
            if (response.chatToggleTemplate.success == Int.ONE) {
                _templateAvailability.value = Pair(
                    isSeller,
                    Success(response.chatToggleTemplate.success)
                )
                if (isEnabled) {
                    getTemplate(isSeller)
                }
            } else {
                _templateAvailability.value = Pair(
                    isSeller,
                    Fail(MessageErrorException(ERROR_TOGGLE_TEMPLATE))
                )
            }
        }, onError = {
                _templateAvailability.value = Pair(isEnabled, Fail(it))
            })
    }

    fun rearrangeTemplate(
        isSeller: Boolean,
        from: Int,
        to: Int
    ) {
        launchCatchError(block = {
            val response = rearrangeTemplateUseCase(
                RearrangeTemplateUseCase.Param(
                    isSeller = isSeller,
                    index = from + Int.ONE,
                    moveTo = to + Int.ONE
                )
            )
            if (response.chatMoveTemplate.success == Int.ONE) {
                val arrangeResult = ArrangeResultModel(
                    to = to,
                    from = from
                )
                _arrangeTemplate.value = arrangeResult
            } else {
                throw MessageErrorException(ERROR_REARRANGE_TEMPLATE)
            }
        }, onError = {
                val arrangeResult = ArrangeResultModel(
                    to = to,
                    from = from,
                    error = it
                )
                _arrangeTemplate.value = arrangeResult
            })
    }

    companion object {
        const val ERROR_TOGGLE_TEMPLATE = "Gagal mengubah status template"
        const val ERROR_REARRANGE_TEMPLATE = "Gagal mengubah urutan template"
    }
}
