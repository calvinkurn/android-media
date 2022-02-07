package com.tokopedia.vouchercreation.product.create.domain.usecase.update

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.domain.model.UpdateVoucherParam
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.UpdateVoucherResponse
import javax.inject.Inject


class UpdateCouponPeriodUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    BaseGqlUseCase<Boolean>() {

    companion object {
        const val MUTATION = "mutation ChangeVoucherPromo(\$update_param: mvUpdateData!) {\n" +
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

        const val UPDATE_PARAM_KEY = "update_param"
    }

    override suspend fun executeOnBackground(): Boolean {
        val request = GraphqlRequest(MUTATION, UpdateVoucherResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))

        val error = response.getError(UpdateVoucherResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<UpdateVoucherResponse>().updateVoucher
            with(data.updateVoucherSuccessData) {
                if (getIsSuccess()) {
                    return true
                } else {
                    throw MessageErrorException(data.message)
                }
            }
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun createRequestParam(
        couponId : Long,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        token: String,
        imageUrl : String,
        imageSquare: String
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

        val params = UpdateVoucherParam(
            voucherId = couponId.toInt(),
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
            imageSquare = imageSquare,
            isPublic = isPublic,
            minPurchase = couponSettings.minimumPurchase,
            quota = couponSettings.quota,
            token = token,
            source = VoucherSource.SELLERAPP,
        )


        return RequestParams.create().apply {
            putObject(UPDATE_PARAM_KEY, params)
        }
    }
}
