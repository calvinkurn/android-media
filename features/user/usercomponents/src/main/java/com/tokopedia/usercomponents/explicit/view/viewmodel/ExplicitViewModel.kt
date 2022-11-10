package com.tokopedia.usercomponents.explicit.view.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.explicit.domain.GetQuestionUseCase
import com.tokopedia.usercomponents.explicit.domain.SaveAnswerUseCase
import com.tokopedia.usercomponents.explicit.domain.UpdateStateUseCase
import com.tokopedia.usercomponents.explicit.domain.model.InputParam
import com.tokopedia.usercomponents.explicit.domain.model.OptionsItem
import com.tokopedia.usercomponents.explicit.domain.model.Property
import com.tokopedia.usercomponents.explicit.domain.model.Template
import com.tokopedia.usercomponents.explicit.domain.model.UpdateStateParam
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ExplicitViewModel @Inject constructor(
    private val getQuestionUseCase: GetQuestionUseCase,
    private val saveAnswerUseCase: SaveAnswerUseCase,
    private val updateStateUseCase: UpdateStateUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : ExplicitViewContract, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatchers.main

    private val _explicitContent = SingleLiveEvent<Result<Pair<Boolean, Property?>>>()
    override val explicitContent: LiveData<Result<Pair<Boolean, Property?>>> get() = _explicitContent

    private val _statusSaveAnswer = SingleLiveEvent<Result<Pair<OptionsItem?, String>>>()
    override val statusSaveAnswer: LiveData<Result<Pair<OptionsItem?, String>>> get() = _statusSaveAnswer

    private val _statusUpdateState = SingleLiveEvent<Boolean>()
    override val statusUpdateState: LiveData<Boolean> get() = _statusUpdateState

    private val preferenceAnswer = InputParam()
    private val preferenceUpdateState = UpdateStateParam()
    private val preferenceOptions = mutableListOf(OptionsItem(), OptionsItem())

    override fun isLoggedIn(): Boolean {
        return userSession.isLoggedIn
    }

    override fun getExplicitContent(templateName: String) {
        launchCatchError(coroutineContext, {
            val response = getQuestionUseCase(templateName)
            val activeConfig = response.explicitprofileGetQuestion.activeConfig.value
            val sections = response.explicitprofileGetQuestion.template.sections

            val isDataValid = sections.isNotEmpty() && sections.first().questions.isNotEmpty() && sections.first().questions.first().property.options.size == 2
            if (activeConfig && isDataValid) {
                val property = sections.first().questions.first().property

                _explicitContent.value = Success(Pair(true, property))

                setPreferenceAnswer(response.explicitprofileGetQuestion.template)
            } else {
                _explicitContent.value = Success(Pair(false, null))
            }
        }, {
            _explicitContent.value = Fail(it)
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

        val positiveOptions = template.sections.first().questions.first().property.options.first()
        val negativeOptions = template.sections.first().questions.first().property.options[1]
        preferenceOptions.clear()
        preferenceOptions.addAll(listOf(positiveOptions, negativeOptions))
    }

    override fun sendAnswer(answers: Boolean?) {
        launchCatchError(coroutineContext, {
            val optionsItem = if (answers == true) {
                preferenceOptions.first()
            } else {
                preferenceOptions[1]
            }

            preferenceAnswer.sections.first().questions.first().answerValue = optionsItem.value

            val response = saveAnswerUseCase(preferenceAnswer)

            _statusSaveAnswer.value = Success(Pair(optionsItem, response.explicitprofileSaveMultiAnswers.message))
        }, {
            _statusSaveAnswer.value = Fail(it)
        })
    }

    override fun updateState() {
        launchCatchError(coroutineContext, {
            updateStateUseCase(preferenceUpdateState)
            _statusUpdateState.value = true
        }, {
            _statusUpdateState.value = false
        })
    }

}