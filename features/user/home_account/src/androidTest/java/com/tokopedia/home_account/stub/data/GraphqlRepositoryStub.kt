package com.tokopedia.home_account.stub.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.common.AndroidFileUtil
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.privacy_account.data.LinkStatusResponse
import com.tokopedia.home_account.test.R

class GraphqlRepositoryStub : GraphqlRepository {

    var mapParam = mutableMapOf(TestStateParam.WALLET to TestStateValue.WALLET_ELIGIBLE, TestStateParam.LINK_STATUS to TestStateValue.NOT_LINKED)

    override suspend fun response(
            requests: List<GraphqlRequest>,
            cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        requests.firstOrNull()?.query?.let {
            return when {
                it.contains("status") && it.contains("profile") -> GraphqlResponse(
                        mapOf(UserAccountDataModel::class.java to provideUserAccountDataModel()),
                        mapOf(),
                        false
                )
                it.contains("link_status") && mapParam[TestStateParam.LINK_STATUS] == TestStateValue.NOT_LINKED -> GraphqlResponse(
                        mapOf(LinkStatusResponse::class.java to provideNotLinkedStatus()),
                        mapOf(),
                        false
                )
                it.contains("link_status") && mapParam[TestStateParam.LINK_STATUS] == TestStateValue.LINKED -> GraphqlResponse(
                        mapOf(LinkStatusResponse::class.java to provideLinkStatus()),
                        mapOf(),
                        false
                )
                it.contains("get_centralized_user_asset_config") -> GraphqlResponse(
                        mapOf(CentralizedUserAssetDataModel::class.java to provideCentralizedUserAssetDataModelSuccess()),
                        mapOf(),
                        false
                )
                it.contains("midasGetSaldoWidgetBalance") -> GraphqlResponse(
                        mapOf(SaldoBalanceDataModel::class.java to provideSaldoBalanceAndPointDataModelSuccess()),
                        mapOf(),
                        false
                )
                it.contains("tokopointsAccountPage") -> GraphqlResponse(
                        mapOf(
                                BalanceAndPointDataModel::class.java to provideTokopointsBalanceAndPointSuccess()
                        ),
                        mapOf(),
                        false
                )
                it.contains("walletappGetAccountBalance") && requests.firstOrNull()?.variables!!["partnerCode"] == "OVO" -> GraphqlResponse(
                        mapOf(BalanceAndPointDataModel::class.java to provideOvoBalanceAndPointDataModelSuccess()),
                        mapOf(),
                        false
                )
                it.contains("walletappGetAccountBalance") && requests.firstOrNull()?.variables!!["partnerCode"] == "PEMUDA" -> GraphqlResponse(
                        mapOf(BalanceAndPointDataModel::class.java to provideGopayBalanceAndPointDataModelSuccess()),
                        mapOf(),
                        false
                )
                it.contains("tokopoints") && it.contains("tier") -> GraphqlResponse(
                        mapOf(ShortcutResponse::class.java to provideStatusFilteredSuccess()),
                        mapOf(),
                        false)
                it.contains("offer_interrupt") -> {
                    val response = when(mapParam[TestStateParam.ADD_VERIFY_PHONE]) {
                        TestStateValue.ADD_PHONE -> {
                            provideOfferInterruptResponse()
                        }
                        TestStateValue.VERIFY_PHONE -> {
                            provideOfferInterruptResponse().apply {
                                val phoneData = data.offers.find { offerList ->
                                    offerList.name == AccountConstants.OfferInterruptionList.OFFER_PHONE
                                }
                                data.offers.remove(phoneData)
                            }
                        }
                        else -> provideOfferInterruptResponse()
                    }

                    GraphqlResponse(
                        mapOf(OfferInterruptResponse::class.java to response),
                        mapOf(),
                        false
                    )
                }
                else -> throw Exception("query is not exists")
            }
        }
        throw Exception("request empty")
    }

    private fun provideUserAccountDataModel(): UserAccountDataModel =
            AndroidFileUtil.parse(R.raw.success_get_profile, UserAccountDataModel::class.java) as UserAccountDataModel

    private fun provideLinkStatus(): LinkStatusResponse =
            AndroidFileUtil.parse(R.raw.success_get_link_status, LinkStatusResponse::class.java)

    private fun provideNotLinkedStatus(): LinkStatusResponse =
            AndroidFileUtil.parse(R.raw.success_get_link_status_not_linked, LinkStatusResponse::class.java)

    private fun provideCentralizedUserAssetDataModelSuccess(): CentralizedUserAssetDataModel =
            AndroidFileUtil.parse(
                    R.raw.success_get_centralized_user_asset_config,
                    CentralizedUserAssetDataModel::class.java
            )

    private fun provideGopayBalanceAndPointDataModelSuccess(): BalanceAndPointDataModel =
            AndroidFileUtil.parse(
                    R.raw.success_gopay_get_balance_and_point,
                    BalanceAndPointDataModel::class.java
            )

    private fun provideOvoBalanceAndPointDataModelSuccess(): BalanceAndPointDataModel =
            AndroidFileUtil.parse(
                    R.raw.success_ovo_get_balance_and_point,
                    BalanceAndPointDataModel::class.java
            )

    private fun provideSaldoBalanceAndPointDataModelSuccess(): SaldoBalanceDataModel {
        val response = AndroidFileUtil.parse(
                R.raw.success_get_saldo_balance_and_point,
                SaldoBalanceDataModel::class.java
        ) as SaldoBalanceDataModel
        return response
    }


    private fun provideTokopointsBalanceAndPointSuccess(): BalanceAndPointDataModel =
            AndroidFileUtil.parse(
                    R.raw.success_get_tokopoint_balance_and_point,
                    BalanceAndPointDataModel::class.java
            )

    private fun provideCobrandCCBalanceAndPointDataModelSuccess(): BalanceAndPointDataModel =
            AndroidFileUtil.parse(
                    R.raw.success_get_cobrandcc_balance_and_point,
                    BalanceAndPointDataModel::class.java
            )

    private fun provideOfferInterruptResponse(): OfferInterruptResponse {
        return AndroidFileUtil.parse(
            R.raw.success_get_offer_interrupt,
            OfferInterruptResponse::class.java
        )
    }

    private fun provideStatusFilteredSuccess(): ShortcutResponse =
            AndroidFileUtil.parse(R.raw.success_tokopoints_status_filtered, ShortcutResponse::class.java)

}
