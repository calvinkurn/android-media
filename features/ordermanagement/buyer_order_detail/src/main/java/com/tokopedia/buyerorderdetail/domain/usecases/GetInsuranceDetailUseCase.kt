package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailParams
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class GetInsuranceDetailUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers, private val repository: GraphqlRepository
) : BaseGraphqlUseCase<GetInsuranceDetailParams, GetInsuranceDetailRequestState>(dispatchers) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetInsuranceDetailParams) = flow {
        EmbraceMonitoring.logBreadcrumb("Fetching order insurance data with params: $params")
        emit(GetInsuranceDetailRequestState.Requesting)
        EmbraceMonitoring.logBreadcrumb("Success fetching order insurance data")
        emit(GetInsuranceDetailRequestState.Complete.Success(sendRequest(params).ppGetInsuranceDetail?.data))
    }.catch {
        EmbraceMonitoring.logBreadcrumb("Error fetching order insurance data with error: ${it.stackTraceToString()}")
        emit(GetInsuranceDetailRequestState.Complete.Error(it))
    }.onCompletion {
        EmbraceMonitoring.logBreadcrumb("Finish fetching order insurance data")
    }

    private fun createRequestParam(params: GetInsuranceDetailParams): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_INPUT, params)
        }.parameters
    }

    private suspend fun sendRequest(
        params: GetInsuranceDetailParams
    ): GetInsuranceDetailResponse.Data {
        return repository.request(
            graphqlQuery(),
            createRequestParam(params),
            getCacheStrategy(params.shouldCheckCache)
        )
    }

    companion object {
        private const val PARAM_INPUT = "input"

        private const val QUERY = """
            query ppGetInsuranceDetail(${'$'}$PARAM_INPUT: PPGetInsuranceDetailRequest!) {
                ppGetInsuranceDetail(params: ${'$'}$PARAM_INPUT) {
                    data {
                      protectionProduct {
                        protections {
                          bundleID
                          isBundle
                          productID
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
