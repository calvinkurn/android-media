package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateMutationResults
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.AddTemplateUseCase
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.DeleteSpecificTemplateUseCase
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.UpdateSpecificTemplateUseCase
import javax.inject.Inject

class TalkEditTemplateViewModel @Inject constructor(
        private val addTemplateUseCase: AddTemplateUseCase,
        private val deleteSpecificTemplateUseCase: DeleteSpecificTemplateUseCase,
        private val updateSpecificTemplateUseCase: UpdateSpecificTemplateUseCase,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _templateMutation = MutableLiveData<TalkTemplateMutationResults>()
    val templateMutation: LiveData<TalkTemplateMutationResults>
        get() = _templateMutation

    fun addTemplate(isSeller: Boolean, template: String) {
        launchCatchError(block = {
            addTemplateUseCase.setParams(isSeller, template)
            val response = addTemplateUseCase.executeOnBackground()
            if(response.chatAddTemplate.isMutationSuccess()) {
                _templateMutation.postValue(TalkTemplateMutationResults.TemplateMutationSuccess)
            } else {
                _templateMutation.postValue(TalkTemplateMutationResults.MutationFailed())
            }
        }) {
            _templateMutation.postValue(TalkTemplateMutationResults.MutationFailed(it))
        }
    }

    fun deleteSpecificTemplate(index: Int, isSeller: Boolean) {
        launchCatchError(block = {
            deleteSpecificTemplateUseCase.setParams(index, isSeller)
            val response = deleteSpecificTemplateUseCase.executeOnBackground()
            if(response.chatDeleteTemplate.isMutationSuccess()) {
                _templateMutation.postValue(TalkTemplateMutationResults.DeleteTemplate)
            } else {
                _templateMutation.postValue(TalkTemplateMutationResults.DeleteTemplateFailed())
            }
        }) {
            _templateMutation.postValue(TalkTemplateMutationResults.DeleteTemplateFailed(it))
        }
    }

    fun updateSpecificTemplate(isSeller: Boolean, value: String, index: Int) {
        launchCatchError(block = {
            updateSpecificTemplateUseCase.setParams(isSeller, value, index)
            val response = updateSpecificTemplateUseCase.executeOnBackground()
            if(response.chatUpdateTemplate.isMutationSuccess()) {
                _templateMutation.postValue(TalkTemplateMutationResults.TemplateMutationSuccess)
            } else {
                _templateMutation.postValue(TalkTemplateMutationResults.MutationFailed())
            }
        }) {
            _templateMutation.postValue(TalkTemplateMutationResults.MutationFailed(it))
        }
    }
}