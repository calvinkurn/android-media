package com.tokopedia.usercomponents.explicit.view.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usercomponents.explicit.domain.model.OptionsItem
import com.tokopedia.usercomponents.explicit.domain.model.Property

interface ExplicitViewContract {
    fun getExplicitContent(templateName: String)
    fun sendAnswer(answers: Boolean?)
    fun updateState()
    fun isLoggedIn(): Boolean

    val explicitContent: LiveData<Result<Pair<Boolean, Property?>>>
    val statusSaveAnswer: LiveData<Result<Pair<OptionsItem?, String>>>
    val statusUpdateState: LiveData<Boolean>
}