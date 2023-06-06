package com.tokopedia.home_account.stub.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.common.AndroidFileUtil
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.explicitprofile.data.CategoriesDataModel
import com.tokopedia.home_account.explicitprofile.data.ExplicitProfileSaveMultiAnswers
import com.tokopedia.home_account.explicitprofile.data.ExplicitprofileGetQuestion
import com.tokopedia.home_account.privacy_account.data.LinkStatusResponse
import com.tokopedia.home_account.stub.data.mocks.GetCentralizedUserMocks
import com.tokopedia.home_account.test.R
import com.tokopedia.home_account.view.fragment.FundsAndInvestmentFragment
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import timber.log.Timber

class GraphqlRepositoryStub : GraphqlRepository {

    var mapParam = mutableMapOf(
        TestStateParam.WALLET to TestStateValue.WALLET_ELIGIBLE,
        TestStateParam.LINK_STATUS to TestStateValue.NOT_LINKED
    )

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return when (GqlQueryParser.parse(requests).joinToString()) {
            "explicitprofileGetAllCategories" -> {
                GqlMockUtil.createSuccessResponse<CategoriesDataModel>(
                    R.raw.explicit_profile_get_categories
                )
            }
            "explicitprofileGetQuestion" -> {
                GqlMockUtil.createSuccessResponse<ExplicitprofileGetQuestion>(
                    R.raw.explicit_profile_get_questions
                )
            }
            "explicitprofileSaveMultiAnswers" -> {
                GqlMockUtil.createSuccessResponse<ExplicitProfileSaveMultiAnswers>(
                    EXPLICIT_PROFILE_SAVE_RESPONSE
                )
            }
            "GetCentralizedUserAssetConfig" -> {
                if (requests.first().variables?.get("entryPoint") == FundsAndInvestmentFragment.ASSET_PAGE) {
                    GqlMockUtil.createSuccessResponse<CentralizedUserAssetDataModel>(
                        GetCentralizedUserMocks.assetPageResponse
                    )
                } else {
                    GqlMockUtil.createSuccessResponse<CentralizedUserAssetDataModel>(
                        GetCentralizedUserMocks.userPageResponse
                    )
                }
            }
            "midasGetSaldoWidgetBalance" -> {
                GqlMockUtil.createSuccessResponse<SaldoBalanceDataModel>(
                    R.raw.success_get_saldo_balance_and_point
                )
            }
            "tokopointsAccountPage" -> {
                GqlMockUtil.createSuccessResponse<TokopointsBalanceDataModel>(
                    R.raw.success_get_tokopoint_balance_and_point
                )
            }
            "productRecommendationWidgetV2" -> {
                GqlMockUtil.createSuccessResponse<RecommendationEntity>(R.raw.product_recommendation)
            }
            "productRecommendationWidget" -> {
                GqlMockUtil.createSuccessResponse<RecommendationEntity>(R.raw.product_recommendation)
            }
            else -> {
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
                            false
                        )
                        it.contains("offer_interrupt") -> {
                            val response = when (mapParam[TestStateParam.ADD_VERIFY_PHONE]) {
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
                        else -> {
                            Timber.w("unhandled request: ${GqlQueryParser.parse(requests)}")
                            throw Exception("query is not exists")
                        }
                    }
                }
                throw Exception("request empty")
            }
        }
    }

    private fun provideUserAccountDataModel(): UserAccountDataModel =
        AndroidFileUtil.parse(
            R.raw.success_get_profile,
            UserAccountDataModel::class.java
        ) as UserAccountDataModel

    private fun provideLinkStatus(): LinkStatusResponse =
        AndroidFileUtil.parse(R.raw.success_get_link_status, LinkStatusResponse::class.java)

    private fun provideNotLinkedStatus(): LinkStatusResponse =
        AndroidFileUtil.parse(
            R.raw.success_get_link_status_not_linked,
            LinkStatusResponse::class.java
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
        AndroidFileUtil.parse(
            R.raw.success_tokopoints_status_filtered,
            ShortcutResponse::class.java
        )
}

const val EXPLICIT_PROFILE_SAVE_RESPONSE = """
{
  "explicitprofileSaveMultiAnswers": {
    "message": "User answers have been saved"
  }
}
"""
