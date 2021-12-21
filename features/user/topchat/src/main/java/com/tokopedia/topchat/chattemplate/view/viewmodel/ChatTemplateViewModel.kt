package com.tokopedia.topchat.chattemplate.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapperKt.mapToTemplateUiModel
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCaseNew
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChatTemplateViewModel @Inject constructor(
    private val getTemplateUseCase: GetTemplateUseCaseNew,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var _chatTemplate = MutableLiveData<Result<GetTemplateUiModel>>()
    val chatTemplate: LiveData<Result<GetTemplateUiModel>>
        get() = _chatTemplate

    fun getTemplate(isSeller: Boolean) {
        launchCatchError(block = {
            val response = getTemplateUseCase.getTemplate(isSeller)
            _chatTemplate.value = Success(response.mapToTemplateUiModel())
        }, onError = {
            _chatTemplate.value = Fail(it)
        })
    }
}