package com.tokopedia.managename.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.managename.data.model.UpdateNameModel
import com.tokopedia.managename.domain.AddNameUseCase
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

    fun updateName(fullname: String) {
        addNameUseCase.update(AddNameUseCase.params(fullname),
                onSuccess = {
                    mutableUpdateName.value = Success(it)
                },
                onError = {
                    mutableUpdateName.value = Fail(it)
                })
    }
}