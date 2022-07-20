package com.tokopedia.vouchercreation.shop.create.domain.usecase.validation

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.CashbackPercentageValidation
import javax.inject.Inject

class CashbackPercentageValidationUseCase @Inject constructor(gqlRepository: GraphqlRepository): BaseValidationUseCase<CashbackPercentageValidation>(gqlRepository) {

    companion object {
        const val QUERY = "query ValidateCashbackPercentage (\$benefit_percent: Int!, \$benefit_max: Int!, \$min_purchase: Int!, \$quota: Int!, \$source: String!) {\n" +
                "  VoucherValidationPartial(VoucherValidationPartialInput: \n" +
                "    {\n" +
                "      benefit_type: \"percent\",\n" +
                "      coupon_type: \"cashback\",\n" +
                "      benefit_percent: \$benefit_percent,\n" +
                "      benefit_max: \$benefit_max,\n" +
                "      min_purchase: \$min_purchase,\n" +
                "      quota: \$quota,\n" +
                "      source : \$source\n" +
                "    }) {\n" +
                "    header{\n" +
                "      process_time\n" +
                "      messages\n" +
                "      reason\n" +
                "      error_code\n" +
                "    } \n" +
                "    data{\n" +
                "      validation_error{\n" +
                "        benefit_type\n" +
                "        coupon_type\n" +
                "        benefit_percent\n" +
                "        benefit_max\n" +
                "        min_purchase\n" +
                "        quota\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val BENEFIT_PERCENT = "benefit_percent"
        private const val BENEFIT_MAX = "benefit_max"
        private const val MIN_PURCHASE = "min_purchase"
        private const val QUOTA = "quota"

        @JvmStatic
        fun createRequestParam(benefitPercent: Int,
                               benefitMax: Int,
                               minPurchase: Int,
                               quota: Int) =
                VoucherSource.getVoucherRequestParams().apply {
                    putInt(BENEFIT_PERCENT, benefitPercent)
                    putInt(BENEFIT_MAX, benefitMax)
                    putInt(MIN_PURCHASE, minPurchase)
                    putInt(QUOTA, quota)
                }
    }

    override val queryString: String = QUERY

    override val validationClassType: Class<out CashbackPercentageValidation> = CashbackPercentageValidation::class.java
}