package com.tokopedia.topchat.chattemplate.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper.mapToEditTemplateUiModel
import com.tokopedia.topchat.chattemplate.domain.usecase.*
import com.tokopedia.topchat.chattemplate.view.uimodel.CreateEditTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.uimodel.EditTemplateResultModel
import javax.inject.Inject

class EditTemplateViewModel @Inject constructor(
    private val editTemplateUseCase: EditTemplateUseCaseNew,
    private val createTemplateUseCase: CreateTemplateUseCase,
    private val deleteTemplateUseCase: DeleteTemplateUseCaseNew,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _createEditTemplate = MutableLiveData<CreateEditTemplateResultModel>()
    val createEditTemplate: LiveData<CreateEditTemplateResultModel>
        get() = _createEditTemplate

    private val _deleteTemplate = MutableLiveData<Pair<EditTemplateResultModel, Int>>()
    val deleteTemplate: LiveData<Pair<EditTemplateResultModel, Int>>
        get() = _deleteTemplate

    private val _errorAction = MutableLiveData<Throwable>()
    val errorAction: LiveData<Throwable>
        get() = _errorAction

    fun submitText(text: String, existingText: String, list: List<String>, isSeller: Boolean) {
        val index = list.indexOf(existingText)
        if (index < 0) {
            createTemplate(index, text, isSeller)
        } else {
            editTemplate(index, text, isSeller)
        }
    }

    private fun createTemplate(index: Int, value: String, isSeller: Boolean) {
        launchCatchError(block = {
            val response = createTemplateUseCase.createTemplate(value, isSeller)
            if (response.isSuccess) {
                _createEditTemplate.value = CreateEditTemplateResultModel(
                    response.mapToEditTemplateUiModel(),
                    index, value
                )
            } else {
                _errorAction.value = MessageErrorException(ERROR_CREATE_TEMPLATE)
            }
        }, onError = {
            _errorAction.value = it
        })
    }

    private fun editTemplate(index: Int, text: String, isSeller: Boolean) {
        launchCatchError(block = {
            val response = editTemplateUseCase.editTemplate(index + 1, text, isSeller)
            if (response.isSuccess) {
                _createEditTemplate.value = CreateEditTemplateResultModel(
                    response.mapToEditTemplateUiModel(),
                    index, text
                )
            } else {
                _errorAction.value = MessageErrorException(ERROR_EDIT_TEMPLATE)
            }
        }, onError = {
            _errorAction.value = it
        })
    }

    fun deleteTemplate(index: Int, isSeller: Boolean) {
        launchCatchError(block = {
            val response = deleteTemplateUseCase.deleteTemplate(index + 1, isSeller)
            if (response.isSuccess) {
                _deleteTemplate.value = Pair(response.mapToEditTemplateUiModel(), index)
            } else {
                _errorAction.value = MessageErrorException(ERROR_DELETE_TEMPLATE)
            }
        }, onError = {
            _errorAction.value = it
        })
    }

    companion object {
        private const val ERROR_DELETE_TEMPLATE = "Gagal menghapus template"
        private const val ERROR_CREATE_TEMPLATE = "Gagal membuat template"
        private const val ERROR_EDIT_TEMPLATE = "Gagal mengubah template"
    }
}