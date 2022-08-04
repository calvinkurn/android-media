package com.tokopedia.usercomponents.explicit.view.interactor

import androidx.lifecycle.LiveData
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usercomponents.explicit.domain.model.Property

interface ExplicitViewContract {
    fun getExplicitContent(templateName: String)
    fun sendAnswer(answers: Boolean?)
    fun updateState()
    fun clear()

    val explicitContent: LiveData<Result<Pair<Boolean, Property?>>>
    val statusSaveAnswer: LiveData<Result<String>>
    val statusUpdateState: LiveData<Boolean>
}