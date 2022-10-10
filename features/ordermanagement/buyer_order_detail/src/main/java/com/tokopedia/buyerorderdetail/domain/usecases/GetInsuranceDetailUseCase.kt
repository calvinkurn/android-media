package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetInsuranceDetailUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers, private val repository: GraphqlRepository
) : FlowUseCase<GetInsuranceDetailParams, GetInsuranceDetailRequestState>(dispatchers.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetInsuranceDetailParams) = flow {
        emit(GetInsuranceDetailRequestState.Requesting)
        delay(2000L)
        emit(GetInsuranceDetailRequestState.Success(sendRequest(params).ppGetInsuranceDetail?.data))
    }.catch {
        emit(GetInsuranceDetailRequestState.Error(it))
    }

    private fun createRequestParam(params: GetInsuranceDetailParams): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_INPUT, params)
        }.parameters
    }

    private suspend fun sendRequest(
        params: GetInsuranceDetailParams
    ): GetInsuranceDetailResponse.Data {
        return repository.request(graphqlQuery(), createRequestParam(params))
    }

    companion object {
        private const val PARAM_INPUT = "input"

        private const val QUERY = """
            query ppGetInsuranceDetail(${'$'}$PARAM_INPUT: BomDetailV2Request!) {
                ppGetInsuranceDetail(input: ${'$'}$PARAM_INPUT) {
                    header {
                      process_time
                      messages
                      reason
                      error_code
                    }
                    data {
                      invoice
                      insuranceOrderID
                      insuranceOrderStatus
                      protectionProduct {
                        protections {
                          orderDetailID
                          bundleID
                          isBundle
                          productID
                          protectionID
                          protectionStatus
                          protectionType
                          protectionConfig {
                            icon {
                              label
                            }
                            wording {
                              id {
                                label
                              }
                            }
                          }
                        }
                      }
                      orderConfig {
                        redirection
                        icon {
                          banner
                        }
                        wording {
                          id {
                            bannerTitle
                            bannerSubtitle
                          }
                        }
                      }
                    }
                }
            }
        """
    }
}
