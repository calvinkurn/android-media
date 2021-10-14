package com.tokopedia.attachvoucher.usecase

import androidx.collection.ArrayMap
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.attachvoucher.mapper.VoucherMapper
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachvoucher.data.FilterParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetVoucherUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher,
    private val mapper: VoucherMapper
) : CoroutineUseCase<FilterParam, List<VoucherUiModel>>(dispatcher) {

    private val paramFilter = "Filter"
    var hasNext = false


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
            const val noFilter = -1
        }

        object IsPublic {
            const val param = "is_public"
        }

        object PerPage {
            const val param = "per_page"
            const val default = 15
        }
    }

    override suspend fun execute(params: FilterParam): List<VoucherUiModel> {
        val data = repository.request<FilterParam, GetMerchantPromotionGetMVListResponse>(graphqlQuery(), params)
        hasNext = data.merchantPromotionGetMVList.data.paging.hasNext
        return mapper.map(data)
    }

    override fun graphqlQuery(): String {
        return privateVoucherQuery
    }
}