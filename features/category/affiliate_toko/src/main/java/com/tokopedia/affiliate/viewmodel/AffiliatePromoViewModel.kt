package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateSearchUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePromoViewModel  @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateSearchUseCase: AffiliateSearchUseCase,
    private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase,
) : BaseViewModel() {
    private var progressBar = MutableLiveData<Boolean>()
    private var affiliateSearchData = MutableLiveData<AffiliateSearchData>()
    private var errorMessage = MutableLiveData<String>()
    private var validateUserState = MutableLiveData<String>()
    private var isSoftBan = false
    fun getSearch(productLink : String) {
        progressBar.value =  true
        launchCatchError(block = {
            affiliateSearchData.value =
                    affiliateSearchUseCase.affiliateSearchWithLink(arrayListOf(productLink))
            progressBar.value = false
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }
    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            validateUserdata.value =
                affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
            progressBar.value = false
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
        })
    }
    fun setValidateUserType(onRegistered: String) {
        validateUserState.value = onRegistered
    }

    fun setSoftBan(isBan: Boolean) {
        isSoftBan = isBan
    }

    fun getSoftBan(): Boolean {
        return isSoftBan
    }

    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getAffiliateSearchData(): LiveData<AffiliateSearchData> = affiliateSearchData
    fun progressBar(): LiveData<Boolean> = progressBar
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getValidateUserType(): LiveData<String> = validateUserState


}
