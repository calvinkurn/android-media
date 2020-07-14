package com.tokopedia.home.account.revamp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.R
import com.tokopedia.affiliatecommon.data.pojo.checkaffiliate.AffiliateCheckData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.home.account.data.model.AccountModel
import com.tokopedia.home.account.revamp.domain.GetBuyerAccountDataUseCase
import com.tokopedia.home.account.revamp.domain.GetUserSaldoUseCase
import com.tokopedia.navigation_common.model.SaldoModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class BuyerAccountViewModel @Inject constructor (
        private val getBuyerAccountDataUseCase: GetBuyerAccountDataUseCase,
        private val checkAffiliateUseCase: GraphqlUseCase<AffiliateCheckData>,
        private val getUserSaldoUseCase: GetUserSaldoUseCase,
        private val userSession: UserSessionInterface,
        private val walletPref: WalletPref,
        private val context: Context,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val _buyerAccountData = MutableLiveData<Result<AccountModel>>()
    val buyerAccountData: LiveData<Result<AccountModel>>
        get() = _buyerAccountData

    private val _isAffiliate = MutableLiveData<Boolean>()
    val isAffiliate: LiveData<Boolean>
        get() = _isAffiliate

    fun getBuyerData(buyerQuery: String, saldoQuery: String) {
        getBuyerAccountDataUseCase.getBuyerAccount(onSuccessGetBuyerData(saldoQuery), onErrorGetBuyerData(), buyerQuery)
    }

    private fun getSaldo(rawQuery: String) {
        getUserSaldoUseCase.getUserSaldo(onSuccessGetSaldo(), onErrorSaldo(), rawQuery)
    }

    private fun checkIsAffiliate() {
        if (userSession.isAffiliate) {
            _isAffiliate.postValue(true)
        } else {
            val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_affiliate_check)
            checkAffiliateUseCase.setTypeClass(AffiliateCheckData::class.java)
            checkAffiliateUseCase.setGraphqlQuery(query)
            checkAffiliateUseCase.execute(onSuccessCheckAffiliate(), onErrorCheckAffiliate())
        }
    }

    private fun onErrorGetBuyerData(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _buyerAccountData.value = Fail(it)
        }
    }

    private fun onSuccessGetBuyerData(query: String): (AccountModel) -> Unit {
        return {
            Log.d("BUYER-ACC", it.toString())
            getSaldo(query)
            checkIsAffiliate()

            saveLocallyWallet(it)
            saveLocallyVccUserStatus(it)
            savePhoneVerified(it)
            saveIsAffiliateStatus(it)
            saveDebitInstantData(it)

            _buyerAccountData.value = Success(it)
        }
    }

    private fun onErrorSaldo(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
        }
    }

    private fun onSuccessGetSaldo(): (SaldoModel) -> Unit {
        return {
            Log.d("BUYER-ACC-Saldo", it.toString())
            _buyerAccountData.value = _buyerAccountData.value.also {accountData ->
                when(accountData) {
                    is Success -> { accountData.data.saldoModel = it }
                }
            }
        }
    }

    private fun onErrorCheckAffiliate(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
        }
    }

    private fun onSuccessCheckAffiliate(): (AffiliateCheckData) -> Unit {
        return {
            Log.d("BUYER-ACC-CA", it.toString())
            _buyerAccountData.value = _buyerAccountData.value.also {accountData ->
                when(accountData) {
                    is Success -> { accountData.data.isAffiliate = it.affiliateCheck.isIsAffiliate }
                }
            }
        }
    }

    private fun saveLocallyWallet(accountModel: AccountModel) {
        walletPref.saveWallet(accountModel.wallet)
        if (accountModel.vccUserStatus != null) {
            walletPref.tokoSwipeUrl = accountModel.vccUserStatus.redirectionUrl
        }
    }

    private fun saveLocallyVccUserStatus(accountModel: AccountModel) {
        if (accountModel.vccUserStatus != null) {
            walletPref.saveVccUserStatus(accountModel.vccUserStatus)
        }
    }

    private fun savePhoneVerified(accountModel: AccountModel) {
        if (accountModel.profile != null) {
            userSession.setIsMSISDNVerified(accountModel.profile.isPhoneVerified)
        }
    }

    private fun saveIsAffiliateStatus(accountModel: AccountModel) {
        userSession.setIsAffiliateStatus(accountModel.isAffiliate)
    }

    private fun saveDebitInstantData(accountModel: AccountModel) {
        if (accountModel.debitInstant != null) {
            walletPref.saveDebitInstantUrl(accountModel.debitInstant.data.redirectUrl)
        }
    }

    override fun onCleared() {
        super.onCleared()
        getBuyerAccountDataUseCase.cancelJobs()
        getUserSaldoUseCase.cancelJobs()
    }
}