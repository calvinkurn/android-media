package com.tokopedia.home_account.stub.data

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_account.common.AndroidFileUtil
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.data.model.WalletEligibleDataModel
import com.tokopedia.home_account.test.R
import javax.inject.Inject

class GraphqlRepositoryStub : GraphqlRepository {
    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        requests.firstOrNull()?.query?.let {
            return when {
                it.contains("query get_centralized_user_asset_config") -> GraphqlResponse(
                    mapOf(CentralizedUserAssetDataModel::class.java to provideCentralizedUserAssetDataModelSuccess()),
                    mapOf(),
                    false
                )
                it.contains("midasGetSaldoWidgetBalance") -> GraphqlResponse(
                    mapOf(BalanceAndPointDataModel::class.java to provideSaldoBalanceAndPointDataModelSuccess()),
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
                it.contains("wallet_app_get_account_balance") && requests.firstOrNull()?.variables!!["partnerCode"] == "OVO" -> GraphqlResponse(
                    mapOf(BalanceAndPointDataModel::class.java to provideOvoBalanceAndPointDataModelSuccess()),
                    mapOf(),
                    false
                )
                it.contains("wallet_app_get_account_balance") && requests.firstOrNull()?.variables!!["partnerCode"] == "PEMUDA" -> GraphqlResponse(
                    mapOf(BalanceAndPointDataModel::class.java to provideOvoBalanceAndPointDataModelSuccess()),
                    mapOf(),
                    false
                )
                it.contains("get_wallet_eligible") -> GraphqlResponse(
                    mapOf(WalletEligibleDataModel::class.java to provideWalletEligibileSuccess()),
                    mapOf(),
                    false
                )
                else -> throw Exception("query is not exists")
            }
        }
        throw Exception("request empty")
    }

    private fun provideCentralizedUserAssetDataModelSuccess(): CentralizedUserAssetDataModel =
        AndroidFileUtil.parse(
            R.raw.success_get_centralized_user_asset_config,
            CentralizedUserAssetDataModel::class.java
        )

//    private fun provideGopayBalanceAndPointDataModelSuccess(): BalanceAndPointDataModel = AndroidFileUtil.parse(
//        "gopay_balance_and_point_data_model_success.json",
//        BalanceAndPointDataModel::class.java
//    )

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

    private fun provideSaldoBalanceAndPointDataModelSuccess(): BalanceAndPointDataModel =
        AndroidFileUtil.parse(
            R.raw.success_get_saldo_balance_and_point,
            BalanceAndPointDataModel::class.java
        )

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

    private fun provideWalletEligibileSuccess(): WalletEligibleDataModel =
        AndroidFileUtil.parse(
            R.raw.success_get_wallet_eligible,
            WalletEligibleDataModel::class.java
        )


}