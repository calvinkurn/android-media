package com.tokopedia.vouchercreation.shop.create.domain.usecase.validation

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.CashbackRupiahValidation
import javax.inject.Inject

class CashbackRupiahValidationUseCase @Inject constructor(gqlRepository: GraphqlRepository): BaseValidationUseCase<CashbackRupiahValidation>(gqlRepository) {

    companion object {
        const val QUERY = "query ValidateCashbackIdr (\$benefit_idr: Int!, \$min_purchase: Int!, \$quota: Int!, \$source: String!) {\n" +
                "  VoucherValidationPartial(VoucherValidationPartialInput: \n" +
                "    {\n" +
                "      benefit_type: \"idr\",\n" +
                "      coupon_type: \"cashback\",\n" +
                "      benefit_idr: \$benefit_idr,\n" +
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
                "        benefit_idr\n" +
                "        min_purchase\n" +
                "        quota\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val BENEFIT_IDR = "benefit_idr"
        private const val MIN_PURCHASE = "min_purchase"
        private const val QUOTA = "quota"

        @JvmStatic
        fun createRequestParam(benefitMax: Int,
                               minPurchase: Int,
                               quota: Int) =
                VoucherSource.getVoucherRequestParams().apply {
                    putInt(BENEFIT_IDR, benefitMax)
                    putInt(MIN_PURCHASE, minPurchase)
                    putInt(QUOTA, quota)
                }
    }

    override val queryString: String = QUERY

    override val validationClassType: Class<out CashbackRupiahValidation> = CashbackRupiahValidation::class.java
}