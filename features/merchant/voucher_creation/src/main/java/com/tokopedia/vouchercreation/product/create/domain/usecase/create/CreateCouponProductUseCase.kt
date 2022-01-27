package com.tokopedia.vouchercreation.product.create.domain.usecase.create

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.data.request.CreateCouponProductParams
import com.tokopedia.vouchercreation.product.create.data.response.CreateCouponResponse
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import javax.inject.Inject

class CreateCouponProductUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    BaseGqlUseCase<Int>() {


    companion object {

        const val MUTATION = "mutation CreateVoucher(\$merchantVoucherData: mvCreateData!){\n" +
                " merchantPromotionCreateMV(merchantVoucherData: \$merchantVoucherData){\n" +
                "  status\n" +
                "  message\n" +
                "  process_time\n" +
                "  data{\n" +
                "    redirect_url\n" +
                "    voucher_id\n" +
                "    status\n" +
                "  }\n" +
                "}\n" +
                "}"

        const val STATUS_SUCCESS = "Success"

        private const val CREATE_PARAM_KEY = "merchantVoucherData"
    }

    fun createRequestParam(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
        token: String,
        imageUrl : String
    ): RequestParams {
        val isPublic = if (couponInformation.target == CouponInformation.Target.PUBLIC) 1 else 0
        val startDate =
            couponInformation.period.startDate.parseTo(DateTimeUtils.DASH_DATE_FORMAT)
        val startHour = couponInformation.period.startDate.parseTo(DateTimeUtils.HOUR_FORMAT)
        val endDate = couponInformation.period.endDate.parseTo(DateTimeUtils.DASH_DATE_FORMAT)
        val endHour = couponInformation.period.endDate.parseTo(DateTimeUtils.HOUR_FORMAT)

        val benefitType = when {
            couponSettings.type == CouponType.FREE_SHIPPING -> "idr"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.NOMINAL -> "idr"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.PERCENTAGE -> "percent"
            else -> "idr"
        }

        val couponType = when (couponSettings.type) {
            CouponType.NONE -> ""
            CouponType.CASHBACK -> "cashback"
            CouponType.FREE_SHIPPING -> "shipping"
        }

        val params = CreateCouponProductParams(
            benefitIdr = couponSettings.discountAmount,
            benefitMax = couponSettings.maxDiscount,
            benefitPercent = couponSettings.discountPercentage,
            benefitType = benefitType,
            code = couponInformation.code,
            couponName = couponInformation.name,
            couponType = couponType,
            dateStart = startDate,
            dateEnd = endDate,
            hourStart = startHour,
            hourEnd = endHour,
            image = imageUrl,
            imageSquare = imageUrl,
            imagePortrait = imageUrl,
            isPublic = isPublic,
            minPurchase = couponSettings.minimumPurchase,
            quota = couponSettings.quota,
            token = token,
            source = VoucherSource.SELLERAPP,
            targetBuyer = 0,
            minimumTierLevel = 0,
            isLockToProduct = 1,
            productIds = couponProducts.joinToString(separator = ",") { it.id },
            productIdsCsvUrl = ""
        )


        return RequestParams.create().apply {
            putObject(CREATE_PARAM_KEY, params)
        }
    }

    override suspend fun executeOnBackground(): Int {
        val request = GraphqlRequest(MUTATION, CreateCouponResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))

        val error = response.getError(CreateCouponResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<CreateCouponResponse>()

            if (data.merchantPromotionCreateMV.data.status != STATUS_SUCCESS) {
                throw MessageErrorException(data.merchantPromotionCreateMV.message)
            } else {
                return data.merchantPromotionCreateMV.data.voucherId
            }
        } else {
            throw MessageErrorException(error.joinToString(", ") {
                it.message
            })
        }
    }

}