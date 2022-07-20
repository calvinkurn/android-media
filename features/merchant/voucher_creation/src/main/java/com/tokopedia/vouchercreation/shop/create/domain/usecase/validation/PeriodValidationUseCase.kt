package com.tokopedia.vouchercreation.shop.create.domain.usecase.validation

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.PeriodValidation
import javax.inject.Inject

class PeriodValidationUseCase @Inject constructor(gqlRepository: GraphqlRepository) : BaseValidationUseCase<PeriodValidation>(gqlRepository) {

    companion object {
        const val QUERY = "query ValidateVoucherPeriod (\$date_start: String!, \$date_end: String!, \$hour_start: String!, \$hour_end: String!, \$source: String!) {\n" +
                "  VoucherValidationPartial(VoucherValidationPartialInput: \n" +
                "    {\n" +
                "      date_start: \$date_start,\n" +
                "      date_end: \$date_end,\n" +
                "      hour_start: \$hour_start,\n" +
                "      hour_end: \$hour_end,\n" +
                "      source : \$source,\n" +
                "    }) {\n" +
                "    header{\n" +
                "      process_time\n" +
                "      messages\n" +
                "      reason\n" +
                "      error_code\n" +
                "    } \n" +
                "    data{\n" +
                "      validation_error{\n" +
                "        date_start\n" +
                "        date_end\n" +
                "        hour_start\n" +
                "        hour_end\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val DATE_START = "date_start"
        private const val DATE_END = "date_end"
        private const val HOUR_START = "hour_start"
        private const val HOUR_END = "hour_end"

        @JvmStatic
        fun createRequestParam(dateStart: String,
                               dateEnd: String,
                               hourStart: String,
                               hourEnd: String) =
                VoucherSource.getVoucherRequestParams().apply {
                    putString(DATE_START, dateStart)
                    putString(DATE_END, dateEnd)
                    putString(HOUR_START, hourStart)
                    putString(HOUR_END, hourEnd)
                }
    }

    override val queryString: String = QUERY

    override val validationClassType: Class<out PeriodValidation> = PeriodValidation::class.java
}