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
import com.tokopedia.home.account.domain.GetBuyerWalletBalanceUseCase
import com.tokopedia.home.account.revamp.domain.GetBuyerAccountDataUseCase
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
        private val getBuyerWalletBalanceUseCase: GetBuyerWalletBalanceUseCase,
        private val userSession: UserSessionInterface,
        private val walletPref: WalletPref,
        private val context: Context,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val _buyerAccountData = MutableLiveData<Result<AccountModel>>()
    val buyerAccountData: LiveData<Result<AccountModel>>
        get() = _buyerAccountData

    fun getBuyerData(buyerQuery: String) {
        getBuyerAccountDataUseCase.getBuyerAccount(onSuccessGetBuyerData(), onErrorGetBuyerData(), buyerQuery)
    }

    private fun checkIsAffiliate() {
        if (userSession.isAffiliate) {
            (_buyerAccountData.value as Success).data.isAffiliate = true
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

    private fun onSuccessGetBuyerData(): (AccountModel) -> Unit {
        return {
            Log.d("BUYER-ACC", it.toString())
            checkIsAffiliate()

            saveLocallyWallet(it)
            saveLocallyVccUserStatus(it)
            savePhoneVerified(it)
            saveIsAffiliateStatus(it)
            saveDebitInstantData(it)

            _buyerAccountData.value = Success(it)
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
            (_buyerAccountData.value as Success).data.isAffiliate = true
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
    }
}