package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.request.UpdateVoucherRequest
import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.UpdateVoucher
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.util.constant.Source
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class UpdateVoucherPeriodUseCase @Inject constructor(
    val gqlRepository: GraphqlRepository
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

    suspend fun updateVoucherPeriod(
        voucher: UpdateVoucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String,
        imageUrl: String,
        imageSquare: String,
        imagePortrait: String
    ): Boolean {
        val request = buildRequest(
            voucher,
            token,
            startDate,
            startHour,
            endDate,
            endHour,
            imageUrl,
            imageSquare,
            imagePortrait
        )
        val response = gqlRepository.response(listOf(request))
        val dataSuccess = response.getSuccessData<UpdateVoucherResponse>()
        val updateQuotaResponse = dataSuccess.updateVoucherModel
        with(updateQuotaResponse) {
            if (this.getIsSuccess()) {
                return true
            } else {
                throw MessageErrorException(updateQuotaResponse.message)
            }
        }
    }

    private fun buildRequest(
        voucher: UpdateVoucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String,
        imageUrl: String,
        imageSquare: String,
        imagePortrait: String
    ): GraphqlRequest {
        val params = getParams(voucher, token, startDate, startHour, endDate, endHour, imageUrl, imageSquare, imagePortrait)
        return GraphqlRequest(
            query,
            UpdateVoucherResponse::class.java,
            params
        )
    }

    private fun getParams(
        voucher: UpdateVoucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String,
        imageUrl: String,
        imageSquare: String,
        imagePortrait: String
    ): Map<String, Any> {
        return mapOf(
            UPDATE_PARAM_KEY to createRequestBody(
                voucher,
                token,
                startDate,
                startHour,
                endDate,
                endHour,
                imageUrl,
                imageSquare,
                imagePortrait
            )
        )
    }

    private fun createRequestBody(
        voucher: UpdateVoucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String,
        imageUrl: String,
        imageSquare: String,
        imagePortrait: String
    ): UpdateVoucherRequest {
        with(voucher) {
            return UpdateVoucherRequest(
                voucherId = voucherId,
                benefitIdr = discountAmt,
                benefitMax = discountAmtMax,
                benefitPercent = discountAmt,
                benefitType = discountTypeFormatted,
                code = voucherCode,
                couponName = voucherName,
                couponType = PromoType.mapToString(type),
                dateStart = startDate,
                dateEnd = endDate,
                hourStart = startHour,
                hourEnd = endHour,
                image = imageUrl,
                imageSquare = imageSquare,
                imagePortrait = imagePortrait,
                isPublic = VoucherTarget.mapToIsPublic(isPublic),
                minPurchase = minimumAmt,
                quota = quota,
                token = token,
                source = Source.SOURCE
            )
        }
    }
}
