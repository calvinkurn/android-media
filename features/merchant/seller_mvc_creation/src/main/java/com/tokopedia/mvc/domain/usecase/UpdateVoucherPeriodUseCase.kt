package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mvc.data.mapper.UpdateVoucherMapper
import com.tokopedia.mvc.data.request.UpdateVoucherRequest
import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.UpdateVoucher
import com.tokopedia.mvc.domain.entity.UpdateVoucherResult
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.util.constant.Source
import javax.inject.Inject

class UpdateVoucherPeriodUseCase @Inject constructor(
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
        voucher: UpdateVoucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String,
        imageUrl: String,
        imageSquare: String,
        imagePortrait: String
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
                    endHour,
                    imageUrl,
                    imageSquare,
                    imagePortrait
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
                voucherId = 24270,
                benefitIdr = 20000,
                benefitMax = 20000,
                benefitPercent = 0,
                benefitType = "idr",
                code = "",
                couponName = "Sample Upcoming 2",
                couponType = "cashback",
                dateStart = "2022-12-28",
                dateEnd = "2023-01-17",
                hourStart = "7:00",
                hourEnd = "6:30",
                image = imageUrl,
                imageSquare = imageSquare,
                imagePortrait = imagePortrait,
                isPublic = VoucherTarget.mapToIsPublic(isPublic),
                minPurchase = 100000,
                quota = 25,
                token = token,
                source = Source.source
            )
        }
    }
}
