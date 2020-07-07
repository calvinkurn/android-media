package com.tokopedia.attachvoucher.usecase

import com.tokopedia.attachvoucher.data.GetVoucherResponse
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherCoroutineContextProvider
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetVoucherUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetVoucherResponse>,
        private var dispatchers: AttachVoucherCoroutineContextProvider
) : CoroutineScope {

    private val paramShopId = "shop_id"

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun getVouchers(
            onSuccess: (GetVoucherResponse) -> Unit,
            onError: (Throwable) -> Unit,
            shopId: Int
    ) {
        launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(shopId)
                    val response = gqlUseCase.apply {
                        setTypeClass(GetVoucherResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.Main) {
                        onSuccess(response)
                    }
                },
                { exception ->
                    withContext(dispatchers.Main) {
                        onError(exception)
                    }
                }
        )
    }

    fun safeCancel() {
        if (coroutineContext.isActive) {
            cancel()
        }
    }

    private fun generateParams(shopId: Int): Map<String, Any> {
        return mapOf(
                paramShopId to shopId
        )
    }

    private val query = """
        query getPublicMerchantVoucherList($$paramShopId: Int!) {
          getPublicMerchantVoucherList(shop_id:$$paramShopId) {
            vouchers {
              merchant_voucher_id
              voucher_id
              voucher_name
              voucher_code
              minimum_spend
              minimum_spend_formatted
              in_use_expiry
              valid_thru
              tnc
              restricted_for_liquid_product
              owner {
                owner_id
                identifier
              }
              voucher_type {
                voucher_type
                identifier
              }
              amount {
                amount_type
                amount
              }
              banner {
                desktop_url
                mobile_url
              }
              status {
                status
                identifier
              }
            }
          }
        }
    """.trimIndent()

}