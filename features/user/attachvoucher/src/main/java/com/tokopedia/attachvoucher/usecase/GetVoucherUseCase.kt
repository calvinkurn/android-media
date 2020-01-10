package com.tokopedia.attachvoucher.usecase

import com.tokopedia.attachvoucher.data.GetVoucherResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class GetVoucherUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetVoucherResponse>
) {

    private val paramShopId = "shop_id"

    fun getVouchers(
            onSuccess: (GetVoucherResponse) -> Unit,
            onError: (Throwable) -> Unit,
            shopId: Int
    ) {
        val params = generateParams(shopId)
        gqlUseCase.apply {
            setTypeClass(GetVoucherResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
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
            vouchers{
              voucher_name
              voucher_type {
                voucher_type
                identifier
              }
              voucher_code
              amount{
                amount_type
                amount
              }
              minimum_spend
              valid_thru
              tnc
              banner{
                desktop_url
                mobile_url
              }
            }
          }
        }
    """.trimIndent()

}