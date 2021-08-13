package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.feature.sellersettings.template.data.ChatTemplatesAll
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateMutationResults
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.ArrangeTemplateUseCase
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.EnableTemplateUseCase
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.GetAllTemplatesUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TalkTemplateViewModel @Inject constructor(
        private val arrangeTemplateUseCase: ArrangeTemplateUseCase,
        private val enableTemplateUseCase: EnableTemplateUseCase,
        private val getAllTemplatesUseCase: GetAllTemplatesUseCase,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _templateList = MutableLiveData<Result<ChatTemplatesAll>>()
    val templateList: LiveData<Result<ChatTemplatesAll>>
        get() = _templateList

    private val _templateMutation = MutableLiveData<TalkTemplateMutationResults>()
    val templateMutation: LiveData<TalkTemplateMutationResults>
        get() = _templateMutation

    fun arrangeTemplate(originalIndex: Int, moveTo: Int, isSeller: Boolean) {
        if (originalIndex == moveTo) {
            return
        }
        launchCatchError(block = {
            arrangeTemplateUseCase.setParams(originalIndex, moveTo, isSeller)
            val response = arrangeTemplateUseCase.executeOnBackground()
            if (response.chatMoveTemplate.isMutationSuccess()) {
                _templateMutation.postValue(TalkTemplateMutationResults.TemplateMutationSuccess)
            } else {
                _templateMutation.postValue(TalkTemplateMutationResults.RearrangeTemplateFailed())
            }
        }) {
            _templateMutation.postValue(TalkTemplateMutationResults.RearrangeTemplateFailed(it))
        }
    }

    fun enableTemplate(isEnable: Boolean) {
        launchCatchError(block = {
            enableTemplateUseCase.setParams(isEnable)
            val response = enableTemplateUseCase.executeOnBackground()
            if (response.chatToggleTemplate.isMutationSuccess()) {
                _templateMutation.postValue(if (isEnable) TalkTemplateMutationResults.TemplateActivateSuccess else TalkTemplateMutationResults.TemplateDeactivateSuccess)
            } else {
                _templateMutation.postValue(TalkTemplateMutationResults.MutationFailed())
            }
        }) {
            _templateMutation.postValue(TalkTemplateMutationResults.MutationFailed(it))
        }
    }

    fun getTemplateList(isSeller: Boolean) {
        launchCatchError(block = {
            getAllTemplatesUseCase.setParams(isSeller)
            val response = getAllTemplatesUseCase.executeOnBackground()
            _templateList.postValue(Success(response.chatTemplatesAll))
        }) {
            _templateList.postValue(Fail(it))
        }
    }

}