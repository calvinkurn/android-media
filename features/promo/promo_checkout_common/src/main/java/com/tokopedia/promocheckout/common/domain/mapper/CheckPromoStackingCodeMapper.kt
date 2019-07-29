package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.*
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.TrackingDetail
import com.tokopedia.promocheckout.common.view.uimodel.*
import javax.inject.Inject

open class CheckPromoStackingCodeMapper @Inject constructor() {
    private val STATUS_OK = "OK"
    private val STATUS_ERROR = "ERROR"

    var isFinal: Boolean = false

    fun call(response: GraphqlResponse?): ResponseGetPromoStackUiModel {
        var status = ""
        val listMessage = ArrayList<String>()
        var data = DataUiModel()
        val responseFirst: ResponseGetPromoStackFirst?
        val responseFinal: ResponseGetPromoStackFinal?

        if (isFinal) {
            responseFinal = response?.getData(ResponseGetPromoStackFinal::class.java)
            responseFinal.let { responseGetPromoStackFinal ->
                responseGetPromoStackFinal?.getPromoStackUse.let {
                    status = it?.status ?: STATUS_ERROR
                    it?.message?.forEach { message ->
                        listMessage.add(message)
                    }
                    if (it?.status?.equals(STATUS_ERROR) == false) {
                        data = mapData(it.data)
                    }
                }
            }
        } else {
            responseFirst = response?.getData(ResponseGetPromoStackFirst::class.java)
            responseFirst.let { responseGetPromoStackFirst ->
                responseGetPromoStackFirst?.getPromoStackFirst.let {
                    status = it?.status ?: STATUS_ERROR
                    it?.message?.forEach { message ->
                        listMessage.add(message)
                    }
                    when (it?.status) {
                        STATUS_OK -> {
                            data = mapData(it.data)
                        }
                    }
                }
            }
        }

        return ResponseGetPromoStackUiModel(
                status,
                listMessage,
                data
        )
    }

    private fun mapData(data: Data): DataUiModel {
        val listCodes = ArrayList<String>()
        data.codes.forEach {
            if (it != null) {
                listCodes.add(it)
            }
        }
        return DataUiModel(
                globalSuccess = data.globalSuccess,
                success = data.success,
                message = mapMessage(data.message),
                codes = listCodes,
                promoCodeId = data.promoCodeId,
                titleDescription = data.titleDescription,
                discountAmount = data.discountAmount,
                cashbackWalletAmount = data.cashbackWalletAmount,
                invoiceDescription = data.invoiceDescription,
                voucherOrders = data.voucherOrders.map {
                    mapVoucherOrders(it)
                },
                cashbackAdvocateReferralAmount = data.cashbackAdvocateReferralAmount,
                cashbackVoucherDescription = data.cashbackVoucherDescription,
                couponDescription = data.couponDescription,
                benefit = mapBenefit(data.benefitSummaryInfo),
                clashings = mapClashing(data.clashingInfoDetail),
                gatewayId = data.gatewayId,
                isCoupon = data.isCoupon,
                trackingDetailUiModel = mapTrackingDetails(data.trackingDetail)
        )
    }

    private fun mapTrackingDetails(trackingDetails: List<TrackingDetail>): List<TrackingDetailUiModel> {
        val trackingDetailUiModels = ArrayList<TrackingDetailUiModel>()
        trackingDetails.forEach {
            trackingDetailUiModels.add(mapTrackingDetail(it))
        }

        return trackingDetailUiModels
    }

    private fun mapTrackingDetail(trackingDetail: TrackingDetail): TrackingDetailUiModel {
        return TrackingDetailUiModel(
                productId = trackingDetail.productId,
                promoCodesTracking = trackingDetail.promoCodesTracking,
                promoDetailsTracking = trackingDetail.promoDetailsTracking
        )
    }

    private fun mapVoucherOrders(voucherOrdersItem: VoucherOrdersItem): VoucherOrdersItemUiModel {
        return VoucherOrdersItemUiModel(
                success = voucherOrdersItem.success,
                code = voucherOrdersItem.code,
                uniqueId = voucherOrdersItem.uniqueId,
                cartId = voucherOrdersItem.cartId,
                type = voucherOrdersItem.type,
                cashbackWalletAmount = voucherOrdersItem.cashbackWalletAmount,
                discountAmount = voucherOrdersItem.discountAmount,
                invoiceDescription = voucherOrdersItem.invoiceDescription,
                titleDescription = voucherOrdersItem.titleDescription,
                message = mapMessage(voucherOrdersItem.message)
        )
    }

    private fun mapMessage(message: Message): MessageUiModel {
        return MessageUiModel(
                color = message.color,
                state = message.state,
                text = message.text
        )
    }

    private fun mapBenefit(benefit: BenefitSummaryInfo): BenefitSummaryInfoUiModel {
        return BenefitSummaryInfoUiModel(
                finalBenefitText = benefit.finalBenefitText,
                finalBenefitAmountStr = benefit.finalBenefitAmountStr,
                finalBenefitAmount = benefit.finalBenefitAmount,
                summaries = benefit.summaries.map {
                    mapSummariesBenefit(it)
                }
        )
    }

    private fun mapSummariesBenefit(summaries: SummariesItem): SummariesUiModel {
        return SummariesUiModel(
                description = summaries.description,
                type = summaries.type,
                amountStr = summaries.amountStr,
                amount = summaries.amount
        )
    }

    private fun mapClashing(clash: ClashingInfoDetail): ClashingInfoDetailUiModel {
        val listOptions = ArrayList<ClashingVoucherOptionUiModel>()
        clash.options.forEach {
            listOptions.add(mapClashingVoucherOption(it))
        }

        return ClashingInfoDetailUiModel(
                isClashedPromos = clash.isClashedPromos,
                clashMessage = clash.clashMessage,
                clashReason = clash.clashReason,
                options = listOptions
        )
    }

    fun mapClashingVoucherOption(clashingVoucherOption: ClashingVoucherOption): ClashingVoucherOptionUiModel {
        val clashingVoucherOptionUiModel = ClashingVoucherOptionUiModel()
        val clashingVoucherOrderUiModelList = ArrayList<ClashingVoucherOrderUiModel>()
        clashingVoucherOption.voucherOrders.forEach {
            val clashingVoucherOrderUiModel = mapClashingVoucherOrder(it)
            clashingVoucherOrderUiModelList.add(clashingVoucherOrderUiModel)
        }
        clashingVoucherOptionUiModel.voucherOrders = clashingVoucherOrderUiModelList

        return clashingVoucherOptionUiModel
    }

    fun mapClashingVoucherOrder(clashingVoucherOrder: ClashingVoucherOrder): ClashingVoucherOrderUiModel {
        return ClashingVoucherOrderUiModel(
                clashingVoucherOrder.code,
                clashingVoucherOrder.uniqueId,
                clashingVoucherOrder.cartId,
                clashingVoucherOrder.promoName,
                clashingVoucherOrder.potentialBenefit,
                clashingVoucherOrder.shopName
        )
    }
}