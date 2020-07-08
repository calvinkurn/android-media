package com.tokopedia.attachvoucher.usecase

import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.attachvoucher.mapper.VoucherMapper
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherCoroutineContextProvider
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetVoucherUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetMerchantPromotionGetMVListResponse>,
        private val dispatchers: AttachVoucherCoroutineContextProvider,
        private val mapper: VoucherMapper
) : CoroutineScope {

    var hasNext = false

    private val paramShopId = "shop_id"
    private val paramFilter = "Filter"

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun getVouchers(
            page: Int,
            onSuccess: (List<VoucherUiModel>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.IO,
                {
                    val params = generateParams(page)
                    val response = gqlUseCase.apply {
                        setTypeClass(GetMerchantPromotionGetMVListResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(privateVoucherQuery)
                    }.executeOnBackground()
                    hasNext = response.merchantPromotionGetMVList.data.paging.hasNext
                    val vouchers = mapper.map(response)
                    withContext(dispatchers.Main) {
                        onSuccess(vouchers)
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

    private fun generateParams(page: Int): Map<String, Any> {
        return mapOf(
                paramFilter to mapOf(
                        MVFilter.VoucherStatus.param to MVFilter.VoucherStatus.paramOnGoing,
                        MVFilter.PerPage.param to MVFilter.PerPage.default,
                        MVFilter.paramPage to page
                )
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

    private val privateVoucherQuery = """
        query MerchantPromotionGetMVListQuery($$paramFilter: MVFilter!){
            MerchantPromotionGetMVList(Filter: $$paramFilter){
                header{
                    process_time
                    message
                    reason
                    error_code
                }
                data{
                    paging{
                        per_page
                        page
                        has_prev
                        has_next
                    }
                    vouchers{
                        voucher_id
                        shop_id
                        voucher_name
                        voucher_type
                        voucher_type_formatted
                        voucher_image
                        voucher_image_square
                        voucher_status
                        voucher_status_formatted
                        voucher_discount_type
                        voucher_discount_type_formatted
                        voucher_discount_amt
                        voucher_discount_amt_formatted
                        voucher_discount_amt_max
                        voucher_discount_amt_max_formatted
                        voucher_minimum_amt
                        voucher_minimum_amt_formatted
                        voucher_quota
                        remaining_quota
                        booked_global_quota
                        voucher_start_time
                        voucher_finish_time
                        voucher_code
                        galadriel_voucher_id
                        galadriel_catalog_id
                        create_time
                        create_by
                        update_time
                        update_by
                        is_public
                        is_quota_avaiable
                        tnc
                        hyperlink{
                            edit
                            edit_quota_ajax
                            delete
                            stop
                            share
                        }
                    }
                }
            }
        }
    """.trimIndent()

    object MVFilter {
        const val paramPage = "page"
        object VoucherStatus {
            const val param = "voucher_status"
            const val paramOnGoing = "2"
            const val paramDeleted = "-1"
            const val paramProcessing = "0"
            const val paramNotStarted = "1"
            const val paramEnded = "3"
            const val paramStopped = "4"
        }

        object VoucherType {
            const val param = "voucher_type"
            const val paramFreeOngkir = 1
            const val paramDiscount = 2
            const val paramCashback = 3
        }
        object IsPublic {
            const val param = "is_public"
        }
        object PerPage {
            const val param = "per_page"
            const val default = 15
        }
    }
}