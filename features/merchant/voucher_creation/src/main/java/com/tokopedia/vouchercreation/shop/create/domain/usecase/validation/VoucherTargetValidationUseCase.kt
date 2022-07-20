package com.tokopedia.vouchercreation.shop.create.domain.usecase.validation

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.shop.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.VoucherTargetValidation
import javax.inject.Inject

class VoucherTargetValidationUseCase @Inject constructor(gqlRepository: GraphqlRepository) : BaseValidationUseCase<VoucherTargetValidation>(gqlRepository) {

    companion object {
        const val QUERY = "query validateVoucherTarget(\$is_public: Int!, \$code: String!, \$coupon_name: String!, \$source: String!) {\n" +
                "  VoucherValidationPartial(VoucherValidationPartialInput: \n" +
                "    {\n" +
                "      is_public: \$is_public,\n" +
                "      code: \$code,\n" +
                "      coupon_name: \$coupon_name,\n" +
                "      source : \$source,\n" +
                "    }) {\n" +
                "    header{\n" +
                "      process_time\n" +
                "      messages\n" +
                "      reason\n" +
                "      error_code\n" +
                "    }\n" +
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
        fun createRequestParam(@VoucherTargetType targetType: Int,
                               promoCode: String,
                               couponName: String) =
                VoucherSource.getVoucherRequestParams().apply {
                    putInt(IS_PUBLIC, targetType)
                    putString(CODE, promoCode)
                    putString(COUPON_NAME, couponName)
                }
    }

    override val queryString: String = QUERY

    override val validationClassType: Class<out VoucherTargetValidation> = VoucherTargetValidation::class.java

}