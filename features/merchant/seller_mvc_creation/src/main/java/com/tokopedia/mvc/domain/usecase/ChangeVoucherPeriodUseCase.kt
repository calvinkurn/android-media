package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mvc.data.mapper.UpdateVoucherMapper
import com.tokopedia.mvc.data.request.UpdateVoucherRequest
import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.UpdateVoucherResult
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.util.constant.Source
import javax.inject.Inject

class ChangeVoucherPeriodUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val mapper: UpdateVoucherMapper
) : GraphqlUseCase<UpdateVoucherResponse>(gqlRepository) {

    companion object {
        private val query = object : GqlQueryInterface {
            private val OPERATION_NAME = "ChangeVoucherPromo"
            private val MUTATION = "mutation ChangeVoucherPromo(\$update_param: mvUpdateData!) {\n" +
                "  merchantPromotionUpdateMV(merchantVoucherUpdateData: \$update_param) {\n" +
                "    status\n" +
                "    message\n" +
                "    process_time\n" +
                "    data{\n" +
                "      redirect_url\n" +
                "      voucher_id\n" +
                "      status\n" +
                "    }\n" +
                "  }\n" +
                "}"

            override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
            override fun getQuery(): String = MUTATION
            override fun getTopOperationName(): String = OPERATION_NAME
        }
        const val UPDATE_PARAM_KEY = "update_param"
    }

    fun updateVoucherPeriod(
        onSuccess: (UpdateVoucherResult) -> Unit,
        onError: (Throwable) -> Unit,
        voucher: Voucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String
    ) {
        try {
            this.setTypeClass(UpdateVoucherResponse::class.java)
            this.setRequestParams(
                getParams(
                    voucher,
                    token,
                    startDate,
                    startHour,
                    endDate,
                    endHour
                )
            )
            this.setGraphqlQuery(query)

            this.execute(
                { result ->
                    onSuccess(mapper.map(result))
                },
                { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getParams(
        voucher: Voucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String
    ): Map<String, Any> {
        return mapOf(
            UPDATE_PARAM_KEY to createRequestBody(
                voucher,
                token,
                startDate,
                startHour,
                endDate,
                endHour
            )
        )
    }

    private fun createRequestBody(
        voucher: Voucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String
    ): UpdateVoucherRequest {
        with(voucher) {
            return UpdateVoucherRequest(
                voucherId = id,
                benefitIdr = discountAmt,
                benefitMax = discountAmtMax,
                benefitPercent = discountAmt,
                benefitType = discountTypeFormatted,
                code = code,
                couponName = name,
                couponType = PromoType.mapFromString(type),
                dateStart = startDate,
                dateEnd = endDate,
                hourStart = startHour,
                hourEnd = endHour,
                image = image,
                imageSquare = imageSquare,
                isPublic = VoucherTarget.mapToIsPublic(isPublic),
                minPurchase = minimumAmt,
                quota = quota,
                token = token,
                source = Source.source
            )
        }
    }
}
