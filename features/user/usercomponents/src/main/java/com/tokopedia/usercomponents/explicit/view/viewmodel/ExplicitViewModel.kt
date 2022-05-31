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
import com.tokopedia.usercomponents.explicit.domain.model.*
import javax.inject.Inject

class ExplicitViewModel @Inject constructor(
    private val getQuestionUseCase: GetQuestionUseCase,
    private val saveAnswerUseCase: SaveAnswerUseCase,
    private val updateStateUseCase: UpdateStateUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _explicitContent = MutableLiveData<Result<Pair<Boolean, Property?>>>()
    val explicitContent: LiveData<Result<Pair<Boolean, Property?>>> get() = _explicitContent

    private val _statusSaveAnswer = MutableLiveData<Result<String>>()
    val statusSaveAnswer: LiveData<Result<String>> get() = _statusSaveAnswer

    private val _isQuestionLoading = MutableLiveData<Boolean>()
    val isQuestionLoading: LiveData<Boolean> get() = _isQuestionLoading

    private val preferenceAnswer = InputParam()
    private val preferenceUpdateState = UpdateStateParam()
    private val preferenceOptions = listOf(OptionsItem(), OptionsItem())

    fun getExplicitContent(templateName: String) {
        _isQuestionLoading.value = true
        launchCatchError(coroutineContext, {
            val response = getQuestionUseCase(templateName)
            val activeConfig = response.explicitprofileGetQuestion.activeConfig.value

            if (activeConfig) {
                val property = response.explicitprofileGetQuestion.template.sections[0].questions[0].property
                _explicitContent.value = Success(Pair(activeConfig, property))
                setPreferenceAnswer(response.explicitprofileGetQuestion.template)
            } else {
                _explicitContent.value = Success(Pair(activeConfig, null))
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
            sections[0].sectionId = template.sections[0].sectionID
            sections[0].questions[0].questionId = template.sections[0].questions[0].questionId
        }

        val options = template.sections[0].questions[0].property.options

        if (options.isNotEmpty()) {
            preferenceOptions[0].apply {
                value = options[0].value
            }
        }

        if (options.size > 1) {
            preferenceOptions[1].apply {
                value = options[1].value
            }
        }
    }

    fun sendAnswer(answers: Boolean) {
        preferenceAnswer.sections[0].questions[0].answerValue =
            if (answers) preferenceOptions[0].value else preferenceOptions[1].value

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
        }, {})
    }

}