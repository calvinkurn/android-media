package com.tokopedia.vouchercreation.voucherlist.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.mapper.VoucherMapper
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherListParam
import com.tokopedia.vouchercreation.voucherlist.model.remote.GetMerchantVoucherListResponse
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 11/05/20
 */

class GetVoucherListUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val mapper: VoucherMapper
) : BaseGqlUseCase<List<VoucherUiModel>>() {

    companion object {
        private const val QUERY = "query getAllMerchantPromotion {\n" +
                "              MerchantPromotionGetMVList(Filter: {}) {\n" +
                "                data {\n" +
                "                  paging {\n" +
                "                    per_page\n" +
                "                    page\n" +
                "                    has_prev\n" +
                "                    has_next\n" +
                "                  }\n" +
                "                  vouchers {\n" +
                "                    voucher_id\n" +
                "                    shop_id\n" +
                "                    voucher_name\n" +
                "                    voucher_type\n" +
                "                    voucher_type_formatted\n" +
                "                    voucher_image\n" +
                "                    voucher_image_square\n" +
                "                    voucher_status\n" +
                "                    voucher_status_formatted\n" +
                "                    voucher_discount_type\n" +
                "                    voucher_discount_type_formatted\n" +
                "                    voucher_discount_amt\n" +
                "                    voucher_discount_amt_formatted\n" +
                "                    voucher_discount_amt_max\n" +
                "                    voucher_discount_amt_max_formatted\n" +
                "                    voucher_minimum_amt\n" +
                "                    voucher_minimum_amt_formatted\n" +
                "                    voucher_quota\n" +
                "                    remaining_quota\n" +
                "                    booked_global_quota\n" +
                "                    voucher_start_time\n" +
                "                    voucher_finish_time\n" +
                "                    voucher_code\n" +
                "                    galadriel_voucher_id\n" +
                "                    galadriel_catalog_id\n" +
                "                    create_time\n" +
                "                    create_by\n" +
                "                    update_time\n" +
                "                    update_by\n" +
                "                    is_public\n" +
                "                    is_quota_avaiable\n" +
                "                    tnc\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }"

        private const val FILTER_KEY = "filter"

        @JvmStatic
        fun createRequestParam(voucherListParam: VoucherListParam) : RequestParams {
            return RequestParams.create().apply {
                putObject(FILTER_KEY, voucherListParam)
            }
        }

    }

    override suspend fun executeOnBackground(): List<VoucherUiModel> {
//        val gqlRequest = GraphqlRequest(QUERY, GetMerchantVoucherListResponse::class.java, params.parameters)
//        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))
//
//        val errors = gqlResponse.getError(GetMerchantVoucherListResponse::class.java)
//        if (errors.isNullOrEmpty()) {
//            val data = gqlResponse.getData<GetMerchantVoucherListResponse>()
//            return mapper.mapRemoteModelToUiModel(data.result.data.vouchers)
//        } else {
//            throw MessageErrorException(errors.joinToString(", ") { it.message })
//        }
        val gqlResponse = Gson().fromJson(DummyVoucherList.DUMMY_ACTIVE_VOUCHER, GetMerchantVoucherListResponse::class.java)
        return mapper.mapRemoteModelToUiModel(gqlResponse.result.data.vouchers)
    }

    object DummyVoucherList {
        internal const val DUMMY_ACTIVE_VOUCHER = """
            {
                "MerchantPromotionGetMVList": {
                  "data": {
                    "paging": {
                      "per_page": 20,
                      "page": 1,
                      "has_prev": false,
                      "has_next": false
                    },
                    "vouchers": [
                        {
                            "voucher_id": 123,
                            "shop_id": 123,
                            "voucher_name": "SAMPLVOUCHERE",
                            "voucher_type": 1,
                            "Voucher_type_formatted" : "Gratis Ongkir",
                            "voucher_image": "sampleimage.jpg",
                            "voucher_image_square": "sampleimage_square",
                            "voucher_status": 2,
                            "voucher_status_formatted" : "ONGOING",
                            "voucher_discount_type": 1,
                            "voucher_discount_type_formatted" : "idr",
                            "voucher_discount_amt": 20000,
                            "voucher_discount_amt_formatted" : "20rb",
                            "voucher_discount_amt_max": 20000,
                            "voucher_discount_amt_max_formatted": "20rb",
                            "voucher_minimum_amt": 100000,
                            "voucher_quota": 10,
                            "remaining_quota" : 10,
                            "booked_global_quota" : 5,
                            "voucher_start_time": "2020-11-10T23:00:00Z",
                            "voucher_finish_time": "2020-12-10T23:00:00Z",
                            "voucher_code": "CODESAMPEL",
                            "galadriel_voucher_id": 123,
                            "galadriel_catalog_id": 123,
                            "create_time": "2009-11-10T23:00:00Z",
                            "create_by": 123,
                            "update_time": "2009-11-10T23:00:00Z",
                            "update_by": 123,
                            "is_public": 1,
                            "is_quota_avaiable": 1,
                            "tnc": "<ol><li>tnc</li></ol>"
                        },
                        {
                            "voucher_id": 123,
                            "shop_id": 123,
                            "voucher_name": "SAMPLVOUCHERE",
                            "voucher_type": 3,
                            "Voucher_type_formatted" : "Discount",
                            "voucher_image": "sampleimage.jpg",
                            "voucher_image_square": "sampleimage_square",
                            "voucher_status": 2,
                            "voucher_status_formatted" : "ONGOING",
                            "voucher_discount_type": 1,
                            "voucher_discount_type_formatted" : "idr",
                            "voucher_discount_amt": 20000,
                            "voucher_discount_amt_formatted" : "20rb",
                            "voucher_discount_amt_max": 20000,
                            "voucher_discount_amt_max_formatted": "20rb",
                            "voucher_minimum_amt": 100000,
                            "voucher_quota": 10,
                            "remaining_quota" : 10,
                            "booked_global_quota" : 5,
                            "voucher_start_time": "2020-11-10T23:00:00Z",
                            "voucher_finish_time": "2020-12-10T23:00:00Z",
                            "voucher_code": "CODESAMPEL",
                            "galadriel_voucher_id": 123,
                            "galadriel_catalog_id": 123,
                            "create_time": "2020-11-10T23:00:00Z",
                            "create_by": 123,
                            "update_time": "2020-11-10T23:00:00Z",
                            "update_by": 123,
                            "is_public": 0,
                            "is_quota_avaiable": 1,
                            "tnc": "<ol><li>tnc</li></ol>"
                        },
                        {
                            "voucher_id": 123,
                            "shop_id": 123,
                            "voucher_name": "SAMPLVOUCHERE",
                            "voucher_type": 2,
                            "Voucher_type_formatted" : "Cashback",
                            "voucher_image": "sampleimage.jpg",
                            "voucher_image_square": "sampleimage_square",
                            "voucher_status": 1,
                            "voucher_status_formatted" : "ONGOING",
                            "voucher_discount_type": 1,
                            "voucher_discount_type_formatted" : "idr",
                            "voucher_discount_amt": 20,
                            "voucher_discount_amt_formatted" : "20%",
                            "voucher_discount_amt_max": 20000,
                            "voucher_discount_amt_max_formatted": "20rb",
                            "voucher_minimum_amt": 100000,
                            "voucher_quota": 10,
                            "remaining_quota" : 10,
                            "booked_global_quota" : 5,
                            "voucher_start_time": "2020-11-10T23:00:00Z",
                            "voucher_finish_time": "2020-12-10T23:00:00Z",
                            "voucher_code": "CODESAMPEL",
                            "galadriel_voucher_id": 123,
                            "galadriel_catalog_id": 123,
                            "create_time": "2020-11-10T23:00:00Z",
                            "create_by": 123,
                            "update_time": "2020-11-10T23:00:00Z",
                            "update_by": 123,
                            "is_public": 0,
                            "is_quota_avaiable": 1,
                            "tnc": "<ol><li>tnc</li></ol>"
                        }
                    ]
                  }
                }
            }
        """
    }


}