package com.tokopedia.common_wallet.balance.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.common_wallet.R
import com.tokopedia.common_wallet.balance.data.CacheUtil
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceEntity
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.balance.view.ActionBalanceModel
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import rx.functions.Func1
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 9/10/19.
 */

class GetWalletBalanceUseCase @Inject constructor(@param:ApplicationContext private val context: Context,
                                                  private val graphqlUseCase: GraphqlUseCase,
                                                  private val remoteConfig: RemoteConfig,
                                                  private val userSession: UserSessionInterface)
    : UseCase<WalletBalanceModel>() {

    //ovo cache 15 seconds as PM requirement
    private val ovoCacheExpiredTime: Long = 15

    override fun createObservable(requestParams: RequestParams): Observable<WalletBalanceModel> {
        return Observable.just(requestParams)
                .flatMap(Func1<RequestParams, Observable<GraphqlResponse>> {
                    val query = GraphqlHelper.loadRawString(context.resources, R.raw.wallet_balance_query)
                    if (!TextUtils.isEmpty(query)) {
                        graphqlUseCase.clearRequest()
                        graphqlUseCase.addRequest(GraphqlRequest(query, WalletBalanceResponse::class.java, false))
                        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(TimeUnit.SECONDS.toMillis(ovoCacheExpiredTime)).build())
                        return@Func1 graphqlUseCase.createObservable(null)
                    }
                    Observable.error(Exception("Query variable are empty"))
                })
                .map(Func1<GraphqlResponse, WalletBalanceResponse> { graphqlResponse ->
                    graphqlResponse.getData(WalletBalanceResponse::class.java)
                })
                .map(Func1<WalletBalanceResponse, WalletBalanceModel> {
                    return@Func1 mapper(it.wallet)
                })
    }

    private fun mapper(walletBalanceEntity: WalletBalanceEntity?): WalletBalanceModel {
        walletBalanceEntity?.let {

            val balanceTokoCash = WalletBalanceModel()

            //create an object if tokocash is not activated
            if (!walletBalanceEntity.linked) {
                val localCacheHandler = LocalCacheHandler(context, CacheUtil.KEY_POPUP_INTRO_OVO_CACHE)
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
        throw RuntimeException("Get Wallet Balance Failed")
    }

    companion object {
        private const val OVO_TYPE = "OVO"
    }
}
