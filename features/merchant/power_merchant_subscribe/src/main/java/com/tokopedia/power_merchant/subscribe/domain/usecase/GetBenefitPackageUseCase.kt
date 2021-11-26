package com.tokopedia.power_merchant.subscribe.domain.usecase

import com.tokopedia.gm.common.domain.interactor.BaseGqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.power_merchant.subscribe.domain.mapper.BenefitPackageMapper
import com.tokopedia.power_merchant.subscribe.domain.model.BenefitPackageResponse
import com.tokopedia.power_merchant.subscribe.view.model.BaseBenefitPackageUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetBenefitPackageUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val benefitPackageMapper: BenefitPackageMapper
): BaseGqlUseCase<List<BaseBenefitPackageUiModel>>() {

    override suspend fun executeOnBackground(): List<BaseBenefitPackageUiModel> {
        val gqlRequest = GraphqlRequest(BENEFIT_PACKAGE_QUERY,
            BenefitPackageResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))

        val gqlError = gqlResponse.getError(BenefitPackageResponse::class.java)
        if (gqlError.isNullOrEmpty()) {
            val benefitPackageResponse: BenefitPackageResponse =
                gqlResponse.getData(BenefitPackageResponse::class.java)
            return benefitPackageMapper.mapToBenefitPackageList(benefitPackageResponse)
        } else {
            throw MessageErrorException(gqlError.joinToString { it.message })
        }
    }

    fun setParams(shopId: Long) {
        params = RequestParams.create().apply {
            putLong(SHOP_ID_PARAM, shopId)
        }
    }

    companion object {
        const val SHOP_ID_PARAM = "shopId"
        val BENEFIT_PACKAGE_QUERY = """
            query BenefitPackage(${'$'}shopId: Int!) {
               shopLevel(input: {
                  shopID: "${'$'}shopId"
                  source: "android"
                  lang: "id"
                }){
                   result {
                     period
                   }
                   error {
                     message
                   }
              }
              goldGetPMGradeBenefitInfo(
                 shop_id: ${'$'}shopId,
                 source: "android-goldmerchant", 
                 lang: "id", 
                 device: "android",
                 fields: ["current_pm_grade", "next_level_benefit_package_list"]
              ) {
                  next_monthly_refresh_date
                  next_level_benefit_package_list {
                      pm_grade_name
                      image_badge_url
                      is_active                
                      pm_tier
                      benefit_list {
                        benefit_description
                        benefit_name
                        related_link_url
                        related_link_applink
                        related_link_name
                        seq_num
                        image_url
                      }
                }
                current_pm_grade {
                    grade_name
                }
              }     
            }
        """.trimIndent()
    }
}