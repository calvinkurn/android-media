package com.tokopedia.tokofood.purchase.purchasepage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.requestAsFlow
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.GetConsentStateBottomsheet
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.GetConsentStateData
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.GetConsentStateResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetConsentStateUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): FlowUseCase<Unit, GetConsentStateResponse>(dispatchers.io) {

    private val isDebug = true

    override fun graphqlQuery(): String = """
        query GetConsentState {
          get_consent_state() {
            message
            success
            data {
              success
              message
              is_agreed
              bottomsheet {
                is_show_bottomsheet
                image_url
                title
                description
                tnc_link
                }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): Flow<GetConsentStateResponse> {
        if (isDebug) {
            return flow {
                kotlinx.coroutines.delay(1000)
                emit(getDummyResponse())
            }
        } else {
            return repository.requestAsFlow(graphqlQuery(), params)
        }
    }

    private fun getDummyResponse(): GetConsentStateResponse {
        return GetConsentStateResponse(
            data = GetConsentStateData(
                isAgreed = false,
                bottomSheet = GetConsentStateBottomsheet(
                    isShowBottomsheet = true,
                    imageUrl = "https://i.pinimg.com/736x/74/fc/48/74fc4817f31d9d36537366c740d08015.jpg",
                    title = "Pesananmu dilayani restoran GoFood",
                    description = "Gojek perlu beberapa datamu biar pesananmu sampai tujuan. ",
                    termsAndCondition = "Saya menyetujui Syarat & Ketentuan yang sedang berlaku"
                )
            )
        )
    }

}