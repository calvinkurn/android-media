package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.common_wallet.balance.data.CacheUtil
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceEntity
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.balance.view.ActionBalanceModel
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class GetCoroutineWalletBalanceUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<WalletBalanceResponse>,
        private val remoteConfig: RemoteConfig,
        private val userSession: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
) : UseCase<Result<WalletBalanceModel>>() {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(WalletBalanceResponse::class.java)
    }
    override suspend fun executeOnBackground(): Result<WalletBalanceModel> {
        return try {
            Success(withContext(Dispatchers.IO) {
                graphqlUseCase.clearCache()
                graphqlUseCase.setRequestParams(getParams().parameters)
                mapper(graphqlUseCase.executeOnBackground().wallet)
            })
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun getParams(): RequestParams {
        val params= RequestParams.create()
        params.putBoolean(KEY_IS_GET_TOPUP, true)
        return params
    }

    private fun mapper(walletBalanceEntity: WalletBalanceEntity?): WalletBalanceModel {
        walletBalanceEntity?.let { walletBalanceEntity ->

            val balanceTokoCash = WalletBalanceModel()

            //create an object if tokocash is not activated
            if (!walletBalanceEntity.linked) {
                var popupHasShown = true
                if (userSession.isLoggedIn &&
                        walletBalanceEntity.walletType.equals(OVO_TYPE, ignoreCase = true)) {
                    popupHasShown = localCacheHandler.getBoolean(CacheUtil.FIRST_TIME_POPUP, false)!!
                    if (!popupHasShown) {
                        localCacheHandler.putBoolean(CacheUtil.FIRST_TIME_POPUP, true)
                        localCacheHandler.applyEditor()
                    }
                }
                balanceTokoCash.isShowAnnouncement = walletBalanceEntity.isShowAnnouncement && !popupHasShown

                walletBalanceEntity.action?.let {
                    var applinkActivation = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_APPLINK_REGISTER)
                    if (applinkActivation.isEmpty()) {
                        applinkActivation = it.applinks
                    }
                    val action = ActionBalanceModel()
                    action.applinks = applinkActivation
                    action.visibility = it.visibility


                    var labelActionName = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_LABEL_REGISTER)
                    if (labelActionName.isEmpty()) {
                        labelActionName = it.text
                    }
                    action.labelAction = labelActionName
                    balanceTokoCash.actionBalanceModel = action
                }

                var labelName = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_LABEL_NAME)
                if (labelName.isEmpty()) {
                    labelName = walletBalanceEntity.text
                }
                balanceTokoCash.titleText = labelName

                balanceTokoCash.balance = walletBalanceEntity.balance
                balanceTokoCash.applinks = walletBalanceEntity.applinks
                balanceTokoCash.walletType = walletBalanceEntity.walletType
                balanceTokoCash.helpApplink = walletBalanceEntity.helpApplink
                balanceTokoCash.tncApplink = walletBalanceEntity.tncApplink
                return balanceTokoCash
            }

            if (walletBalanceEntity.action != null) {
                val actionBalance = ActionBalanceModel()
                actionBalance.applinks = walletBalanceEntity.action!!.applinks
                actionBalance.labelAction = walletBalanceEntity.action!!.text
                actionBalance.redirectUrl = walletBalanceEntity.action!!.redirectUrl
                actionBalance.visibility = walletBalanceEntity.action!!.visibility
                balanceTokoCash.actionBalanceModel = actionBalance
            }

            var applinkBalance = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_APPLINK)
            if (applinkBalance.isEmpty()) {
                applinkBalance = walletBalanceEntity.applinks
            }
            balanceTokoCash.applinks = applinkBalance
            balanceTokoCash.balance = walletBalanceEntity.balance
            balanceTokoCash.holdBalance = walletBalanceEntity.holdBalance
            balanceTokoCash.link = walletBalanceEntity.linked
            balanceTokoCash.rawBalance = walletBalanceEntity.rawBalance.toLong()
            balanceTokoCash.rawHoldBalance = walletBalanceEntity.rawHoldBalance.toLong()
            balanceTokoCash.rawTotalBalance = walletBalanceEntity.rawTotalBalance.toLong()
            balanceTokoCash.redirectUrl = walletBalanceEntity.redirectUrl
            balanceTokoCash.totalBalance = walletBalanceEntity.totalBalance
            balanceTokoCash.isShowTopup = walletBalanceEntity.isShowTopup
            balanceTokoCash.topupUrl = walletBalanceEntity.topupUrl
            balanceTokoCash.topupLimit = walletBalanceEntity.topupLimit

            var labelName = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_LABEL_NAME)
            if (labelName.isEmpty()) {
                labelName = walletBalanceEntity.text
            }
            balanceTokoCash.titleText = labelName

            //set ab tags
            val abTags = ArrayList<String>()
            if (walletBalanceEntity.abTags != null) {
                for (abTag in walletBalanceEntity.abTags!!) {
                    abTags.add(abTag.tag)
                }
            }
            balanceTokoCash.abTags = abTags
            balanceTokoCash.pointBalance = walletBalanceEntity.pointBalance
            balanceTokoCash.rawPointBalance = walletBalanceEntity.rawPointBalance
            balanceTokoCash.cashBalance = walletBalanceEntity.cashBalance
            balanceTokoCash.rawCashBalance = walletBalanceEntity.rawCashBalance
            balanceTokoCash.walletType = walletBalanceEntity.walletType

            return balanceTokoCash
        }
        throw RuntimeException("Error")
    }

    companion object {
        private const val OVO_TYPE = "OVO"
        private const val KEY_IS_GET_TOPUP = "isGetTopup"
    }
}