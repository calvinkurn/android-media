package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.feature.sellersettings.template.data.ChatTemplatesAll
import com.tokopedia.talk.feature.sellersettings.template.data.TemplateMutationResult
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TalkTemplateViewModel @Inject constructor(
        private val addTemplateUseCase: AddTemplateUseCase,
        private val arrangeTemplateUseCase: ArrangeTemplateUseCase,
        private val deleteSpecificTemplateUseCase: DeleteSpecificTemplateUseCase,
        private val enableTemplateUseCase: EnableTemplateUseCase,
        private val getAllTemplatesUseCase: GetAllTemplatesUseCase,
        private val updateSpecificTemplateUseCase: UpdateSpecificTemplateUseCase,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _templateList = MutableLiveData<Result<ChatTemplatesAll>>()
    val templateList: LiveData<Result<ChatTemplatesAll>>
        get() = _templateList

    private val _templateMutation = MutableLiveData<Boolean>()
    val templateMutation: LiveData<Boolean>
        get() = _templateMutation

    fun addTemplate(isSeller: Boolean, template: String) {
        launchCatchError(block = {
            addTemplateUseCase.setParams(isSeller, template)
            val response = addTemplateUseCase.executeOnBackground()
            _templateMutation.postValue(response.chatAddTemplate.success.isMutationSuccess())
        }) {
            _templateMutation.postValue(false)
        }
    }

    fun arrangeTemplate(originalIndex: Int, moveTo: Int, isSeller: Boolean) {
        launchCatchError(block = {
            arrangeTemplateUseCase.setParams(originalIndex, moveTo, isSeller)
            val response = arrangeTemplateUseCase.executeOnBackground()
            _templateMutation.postValue(response.chatMoveTemplate.success.isMutationSuccess())
        }) {
            _templateMutation.postValue(false)
        }
    }

    fun deleteSpecificTemplate(index: Int, isSeller: Boolean) {
        launchCatchError(block = {
            deleteSpecificTemplateUseCase.setParams(index, isSeller)
            val response = deleteSpecificTemplateUseCase.executeOnBackground()
            _templateMutation.postValue(response.chatDeleteTemplate.success.isMutationSuccess())
        }) {
            _templateMutation.postValue(false)
        }
    }

    fun enableTemplateUseCase(isEnable: Boolean) {
        launchCatchError(block = {
            enableTemplateUseCase.setParams(isEnable)
            val response = enableTemplateUseCase.executeOnBackground()
            _templateMutation.postValue(response.chatToggleTemplate.success.isMutationSuccess())
        }) {
            _templateMutation.postValue(false)
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

    fun updateSpecificTemplate(isSeller: Boolean, value: String, index: Int) {
        launchCatchError(block = {
            updateSpecificTemplateUseCase.setParams(isSeller, value, index)
            val response = updateSpecificTemplateUseCase.executeOnBackground()
            _templateMutation.postValue(response.chatUpdateTemplate.success.isMutationSuccess())
        }) {
            _templateMutation.postValue(false)
        }
    }

    private fun Int.isMutationSuccess(): Boolean {
        return this == 1
    }


}