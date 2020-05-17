package com.tokopedia.vouchercreation.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchercreation.create.domain.model.VoucherSource
import com.tokopedia.vouchercreation.create.view.uimodel.validation.PromoCodeValidation
import javax.inject.Inject

class PromoCodeValidationUseCase @Inject constructor(gqlRepository: GraphqlRepository) : BaseValidationUseCase<PromoCodeValidation>(gqlRepository) {

    companion object {
        const val QUERY = "query validateVoucherTarget(\$code: String!, \$source: String!) {\n" +
                "  VoucherValidationPartial(VoucherValidationPartialInput: \n" +
                "    {\n" +
                "      is_public: 0,\n" +
                "      code: \$code,\n" +
                "      source: \$source\n" +
                "    }) {\n" +
                "    data{\n" +
                "      validation_error{\n" +
                "        code\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val CODE = "code"

        @JvmStatic
        fun createRequestParam(promoCode: String) =
                VoucherSource.getVoucherRequestParams().apply {
                    putString(CODE, promoCode)
                }
    }

    override val queryString: String = QUERY

    override val validationClassType: Class<out PromoCodeValidation> = PromoCodeValidation::class.java
}