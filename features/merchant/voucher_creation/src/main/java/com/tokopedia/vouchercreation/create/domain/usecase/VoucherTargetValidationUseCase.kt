package com.tokopedia.vouchercreation.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.vouchercreation.create.domain.model.VoucherSource
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherValidationPartial
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherValidationPartialResponse
import com.tokopedia.vouchercreation.create.view.uimodel.validation.VoucherTargetValidation
import javax.inject.Inject

class VoucherTargetValidationUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : UseCase<VoucherTargetValidation>() {

    companion object {
        const val QUERY = "query validateVoucherTarget(\$is_public: Int!, \$code: String!, \$coupon_name: String!, \$source: String!) {\n" +
                "  VoucherValidationPartial(VoucherValidationPartialInput: \n" +
                "    {\n" +
                "      is_public: \$is_public,\n" +
                "      code: \$code,\n" +
                "      coupon_name: \$coupon_name,\n" +
                "      source : \$source,\n" +
                "    }) {\n" +
                "    data{\n" +
                "      validation_error{\n" +
                "        is_public\n" +
                "        code\n" +
                "        coupon_name\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val IS_PUBLIC = "is_public"
        private const val CODE = "code"
        private const val COUPON_NAME = "coupon_name"

        @JvmStatic
        fun getRequestParams(@VoucherTargetType targetType: Int,
                             promoCode: String,
                             couponName: String) =
                VoucherSource.getVoucherRequestParams().apply {
                    putInt(IS_PUBLIC, targetType)
                    putString(CODE, promoCode)
                    putString(COUPON_NAME, couponName)
                }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): VoucherTargetValidation {
        val request = GraphqlRequest(QUERY, VoucherValidationPartialResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val error = response.getError(VoucherValidationPartialResponse::class.java)
        if (error.isNullOrEmpty()) {
            val validationPartialResponse: VoucherValidationPartialResponse = response.getData(VoucherValidationPartialResponse::class.java)
            val validationPartial = validationPartialResponse.response.validationPartialErrorData.validationPartial
            return mapToUiModel(validationPartial)
        } else {
            throw MessageErrorException(error.first().toString())
        }
    }

    private fun mapToUiModel(voucherValidationPartial: VoucherValidationPartial) =
            voucherValidationPartial.run {
                VoucherTargetValidation(
                        isPublicError,
                        promoCodeError,
                        couponNameError
                )
            }
}