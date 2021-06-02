package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ChooseAccountFingerprintViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                      private val userSession: UserSessionInterface,
                                                      private val cryptographyUtils: Cryptography?,
                                                      private val fingerprintSetting: FingerprintSetting,
                                                      private val registerFingerprintUseCase: RegisterFingerprintUseCase
)
    : BaseViewModel(dispatcher.io){

    private val mutableGetAccountListResponse = MutableLiveData<Result<AccountList>>()
    val getAccountListResponse: LiveData<Result<AccountList>>
        get() = mutableGetAccountListResponse

    fun getFingerprintUserList() {

    }
}