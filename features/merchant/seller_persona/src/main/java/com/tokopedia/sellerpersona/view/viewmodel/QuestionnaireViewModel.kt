package com.tokopedia.sellerpersona.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerpersona.data.remote.model.QuestionnaireAnswerParam
import com.tokopedia.sellerpersona.data.remote.model.SetUserPersonaDataModel
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaQuestionnaireUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class QuestionnaireViewModel @Inject constructor(
    private val getQuestionnaireUseCase: Lazy<GetPersonaQuestionnaireUseCase>,
    private val setPersonaUseCase: Lazy<SetPersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val questionnaire: LiveData<Result<List<QuestionnairePagerUiModel>>>
        get() = _questionnaire
    val setPersonaResult: LiveData<Result<SetUserPersonaDataModel>>
        get() = _setPersonaResult

    private val _questionnaire = MutableLiveData<Result<List<QuestionnairePagerUiModel>>>()
    private val _setPersonaResult = MutableLiveData<Result<SetUserPersonaDataModel>>()

    fun fetchQuestionnaire() {
        launchCatchError(block = {
            val data = getQuestionnaireUseCase.get().execute()
            _questionnaire.postValue(Success(data))
        }, onError = {
            _questionnaire.postValue(Fail(it))
        })
    }

    fun submitAnswer(answers: List<QuestionnaireAnswerParam>) {
        launchCatchError(block = {
            val shopId = userSession.get().shopId
            val response = setPersonaUseCase.get().execute(shopId, String.EMPTY, answers)
            _setPersonaResult.postValue(Success(response))
        }, onError = {
            _setPersonaResult.postValue(Fail(it))
        })
    }
}