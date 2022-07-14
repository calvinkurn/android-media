package com.tokopedia.usercomponents.explicit.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.explicit.domain.GetQuestionUseCase
import com.tokopedia.usercomponents.explicit.domain.SaveAnswerUseCase
import com.tokopedia.usercomponents.explicit.domain.UpdateStateUseCase
import com.tokopedia.usercomponents.explicit.domain.model.InputParam
import com.tokopedia.usercomponents.explicit.domain.model.OptionsItem
import com.tokopedia.usercomponents.explicit.domain.model.Property
import com.tokopedia.usercomponents.explicit.domain.model.UpdateStateParam
import com.tokopedia.usercomponents.explicit.domain.model.Template
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class ExplicitViewModel @Inject constructor(
    private val getQuestionUseCase: GetQuestionUseCase,
    private val saveAnswerUseCase: SaveAnswerUseCase,
    private val updateStateUseCase: UpdateStateUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _explicitContent = SingleLiveEvent<Result<Pair<Boolean, Property?>>>()
    val explicitContent: LiveData<Result<Pair<Boolean, Property?>>> get() = _explicitContent

    private val _statusSaveAnswer = SingleLiveEvent<Result<String>>()
    val statusSaveAnswer: LiveData<Result<String>> get() = _statusSaveAnswer

    private val _isQuestionLoading = SingleLiveEvent<Boolean>()
    val isQuestionLoading: LiveData<Boolean> get() = _isQuestionLoading

    private val _statusUpdateState = SingleLiveEvent<Boolean>()
    val statusUpdateState: LiveData<Boolean> get() = _statusUpdateState

    private val preferenceAnswer = InputParam()
    private val preferenceUpdateState = UpdateStateParam()
    private val preferenceOptions = listOf(OptionsItem(), OptionsItem())

    fun getExplicitContent(templateName: String) {
        _isQuestionLoading.value = true
        launchCatchError(coroutineContext, {
            val response = getQuestionUseCase(templateName)
            val activeConfig = response.explicitprofileGetQuestion.activeConfig.value
            val sections = response.explicitprofileGetQuestion.template.sections

            if (activeConfig && sections.isNotEmpty() && sections.first().questions.isNotEmpty() && sections.first().questions.first().property.options.size == 2) {
                val property = sections.first().questions.first().property
                _explicitContent.value = Success(Pair(true, property))
                setPreferenceAnswer(response.explicitprofileGetQuestion.template)
            } else {
                _explicitContent.value = Success(Pair(false, null))
            }
            _isQuestionLoading.value = false
        }, {
            _explicitContent.value = Fail(it)
            _isQuestionLoading.value = false
        })
    }

    private fun setPreferenceAnswer(template: Template) {
        preferenceUpdateState.template.name = template.name
        preferenceAnswer.apply {
            templateId = template.id
            templateName = template.name
            sections.first().sectionId = template.sections.first().sectionID
            sections.first().questions.first().questionId =
                template.sections.first().questions.first().questionId
        }

        val options = template.sections.first().questions.first().property.options

        preferenceOptions.first().apply {
            value = options.first().value
        }

        preferenceOptions[1].apply {
            value = options[1].value
        }
    }

    fun sendAnswer(answers: Boolean?) {
        preferenceAnswer.sections.first().questions.first().answerValue =
            if (answers == true) preferenceOptions.first().value else preferenceOptions[1].value

        launchCatchError(coroutineContext, {
            val response = saveAnswerUseCase(preferenceAnswer)

            _statusSaveAnswer.value = Success(response.explicitprofileSaveMultiAnswers.message)
        }, {
            _statusSaveAnswer.value = Fail(it)
        })
    }

    fun updateState() {
        launchCatchError(coroutineContext, {
            updateStateUseCase(preferenceUpdateState)
            _statusUpdateState.value = true
        }, {
            _statusUpdateState.value = false
        })
    }

}