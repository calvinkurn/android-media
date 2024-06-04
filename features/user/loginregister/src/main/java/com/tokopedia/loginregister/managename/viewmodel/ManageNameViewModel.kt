package com.tokopedia.loginregister.managename.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginregister.managename.model.UpdateNameModel
import com.tokopedia.loginregister.managename.domain.AddNameUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 04/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ManageNameViewModel @Inject constructor(
    private val addNameUseCase: AddNameUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val mutableUpdateName = MutableLiveData<com.tokopedia.usecase.coroutines.Result<UpdateNameModel>>()
    val updateNameLiveData: LiveData<com.tokopedia.usecase.coroutines.Result<UpdateNameModel>>
        get() = mutableUpdateName

    fun updateName(fullname: String, currValidateToken: String) {
        addNameUseCase.update(
            AddNameUseCase.params(fullname, currValidateToken),
                onSuccess = {
                    mutableUpdateName.value = Success(it)
                },
                onError = {
                    mutableUpdateName.value = Fail(it)
                })
    }
}
