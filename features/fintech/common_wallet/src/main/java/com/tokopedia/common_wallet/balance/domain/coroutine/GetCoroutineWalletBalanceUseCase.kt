package com.tokopedia.common_wallet.balance.domain.coroutine

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.common_wallet.balance.data.CacheUtil
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceEntity
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.balance.domain.query.WalletBalance
import com.tokopedia.common_wallet.balance.view.ActionBalanceModel
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class GetCoroutineWalletBalanceUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val remoteConfig: RemoteConfig,
        private val userSession: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
)
    : UseCase<WalletBalanceModel>() {
    override suspend fun executeOnBackground(): WalletBalanceModel = withContext(Dispatchers.IO){
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlRecommendationRequest = GraphqlRequest(
                WalletBalance.query,
                WalletBalanceResponse::class.java,
                mapOf()
        )
        val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        val errors = response.getError(WalletBalanceResponse::class.java)
        if(errors?.isNotEmpty() == true){
            error(errors.first().message)
        }
        val wallet = response.getSuccessData<WalletBalanceResponse>().wallet
        mapper(wallet)
    }

    private fun mapper(walletBalanceEntity: WalletBalanceEntity?): WalletBalanceModel {
        walletBalanceEntity?.let {

            val balanceTokoCash = WalletBalanceModel()

            //create an object if tokocash is not activated
            if (!walletBalanceEntity.linked) {
                var popupHasShown = true
                if (userSession.isLoggedIn &&
                        walletBalanceEntity.walletType != null &&
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
                actionBalance.applinks = walletBalanceEntity.action.applinks
                actionBalance.labelAction = walletBalanceEntity.action.text
                actionBalance.redirectUrl = walletBalanceEntity.action.redirectUrl
                actionBalance.visibility = walletBalanceEntity.action.visibility
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

            var labelName = remoteConfig.getString(RemoteConfigKey.MAINAPP_WALLET_LABEL_NAME)
            if (labelName.isEmpty()) {
                labelName = walletBalanceEntity.text
            }
            balanceTokoCash.titleText = labelName

            //set ab tags
            val abTags = ArrayList<String>()
            if (walletBalanceEntity.abTags != null) {
                var index = 0
                for (abtag in walletBalanceEntity.abTags) {
                    abTags.add(abtag.tag)
                    index++
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
    }
}