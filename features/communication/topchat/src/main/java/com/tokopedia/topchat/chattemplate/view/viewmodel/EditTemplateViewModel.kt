package com.tokopedia.topchat.chattemplate.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.domain.usecase.CreateTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.DeleteTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.UpdateTemplateUseCase
import com.tokopedia.topchat.chattemplate.view.uimodel.CreateEditTemplateResultModel
import javax.inject.Inject

class EditTemplateViewModel @Inject constructor(
    private val createTemplateUseCase: CreateTemplateUseCase,
    private val updateTemplateUseCase: UpdateTemplateUseCase,
    private val deleteTemplateUseCase: DeleteTemplateUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _createEditTemplate = MutableLiveData<CreateEditTemplateResultModel>()
    val createEditTemplate: LiveData<CreateEditTemplateResultModel>
        get() = _createEditTemplate

    private val _deleteTemplate = MutableLiveData<Int>()
    val deleteTemplate: LiveData<Int>
        get() = _deleteTemplate

    private val _errorAction = MutableLiveData<Throwable>()
    val errorAction: LiveData<Throwable>
        get() = _errorAction

    fun submitText(text: String, existingText: String, list: List<String>, isSeller: Boolean) {
        val index = list.indexOf(existingText)
        if (index < Int.ZERO) {
            createTemplate(index, text, isSeller)
        } else {
            updateTemplate(index, text, isSeller)
        }
    }

    private fun createTemplate(index: Int, value: String, isSeller: Boolean) {
        launchCatchError(block = {
            val response = createTemplateUseCase(
                CreateTemplateUseCase.Param(
                    isSeller = isSeller,
                    value = value
                )
            )
            if (response.chatAddTemplate.success == Int.ONE) {
                _createEditTemplate.value = CreateEditTemplateResultModel(index, value)
            } else {
                _errorAction.value = MessageErrorException(ERROR_CREATE_TEMPLATE)
            }
        }, onError = {
                _errorAction.value = it
            })
    }

    private fun updateTemplate(index: Int, text: String, isSeller: Boolean) {
        launchCatchError(block = {
            val response = updateTemplateUseCase(
                UpdateTemplateUseCase.Param(
                    isSeller = isSeller,
                    index = index + Int.ONE,
                    value = text
                )
            )
            if (response.chatUpdateTemplate.success == Int.ONE) {
                _createEditTemplate.value = CreateEditTemplateResultModel(index, text)
            } else {
                _errorAction.value = MessageErrorException(ERROR_EDIT_TEMPLATE)
            }
        }, onError = {
                _errorAction.value = it
            })
    }

    fun deleteTemplate(index: Int, isSeller: Boolean) {
        launchCatchError(block = {
            val response = deleteTemplateUseCase(
                DeleteTemplateUseCase.Param(
                    isSeller = isSeller,
                    templateIndex = index + Int.ONE
                )
            )
            if (response.chatDeleteTemplate.success == Int.ONE) {
                _deleteTemplate.value = index
            } else {
                _errorAction.value = MessageErrorException(ERROR_DELETE_TEMPLATE)
            }
        }, onError = {
                _errorAction.value = it
            })
    }

    companion object {
        const val ERROR_DELETE_TEMPLATE = "Gagal menghapus template"
        const val ERROR_CREATE_TEMPLATE = "Gagal membuat template"
        const val ERROR_EDIT_TEMPLATE = "Gagal mengubah template"
    }
}
