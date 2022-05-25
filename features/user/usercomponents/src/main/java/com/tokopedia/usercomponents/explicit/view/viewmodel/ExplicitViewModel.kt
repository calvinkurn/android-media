package com.tokopedia.usercomponents.explicit.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.explicit.domain.ExplicitUseCase
import com.tokopedia.usercomponents.explicit.domain.model.Property
import com.tokopedia.usercomponents.explicit.domain.model.QuestionsItem
import javax.inject.Inject

class ExplicitViewModel @Inject constructor(
    private val explicitUseCase: ExplicitUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _explicitContent = MutableLiveData<Result<Property>>()
    val explicitContent: LiveData<Result<Property>> get() = _explicitContent

    fun getExplicitContent(templateName: String) {
        launchCatchError(coroutineContext, {
            val response = explicitUseCase.invoke(templateName)
            val property = response.explicitprofileGetQuestion.template.sections[0].questions[0].property
            _explicitContent.value = Success(property)
        }, {
            _explicitContent.value = Fail(it)
        })
    }

    fun sendAnswer(answers: Boolean) {

    }

    companion object {
        private const val FIRST_TIME_LAUNCH = "FIRST_TIME_LAUNCH"
        private const val ON_QUESTION = "ON_QUESTION"
    }

}