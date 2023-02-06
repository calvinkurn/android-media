package com.tokopedia.buyerorderdetail.domain.mapper

import com.tokopedia.buyerorderdetail.domain.models.ApprovePartialOrderFulfillment
import com.tokopedia.buyerorderdetail.domain.models.EstimateInfo
import com.tokopedia.buyerorderdetail.domain.models.InfoRespondPartialOrderFulfillment
import com.tokopedia.buyerorderdetail.domain.models.RejectPartialOrderFulfillment
import com.tokopedia.buyerorderdetail.presentation.model.ApprovePartialOrderFulfillmentUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BasePofVisitableUiModel
import com.tokopedia.buyerorderdetail.presentation.model.EstimateInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PartialOrderFulfillmentWrapperUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofAvailableLabelUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofHeaderInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductFulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductUnfulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundEstimateBottomSheetUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofThickDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofThinDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.RejectPartialOrderFulfillmentUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import javax.inject.Inject

class PartialOrderFulfillmentMapper @Inject constructor() {

    fun mapToPartialOrderFulfillmentUiModelList(
        infoRespondPartialOrderFulfillment: InfoRespondPartialOrderFulfillment
    ): PartialOrderFulfillmentWrapperUiModel {
        val partialOrderFulfillmentUiModelList = mutableListOf<BasePofVisitableUiModel>().apply {
            add(PofHeaderInfoUiModel(headerInfoHtmlStr = infoRespondPartialOrderFulfillment.headerInfo))
            add(PofAvailableLabelUiModel())
            add(PofThinDividerUiModel())

            addAll(
                infoRespondPartialOrderFulfillment.detailsUnfulfill.map {
                    PofProductUnfulfilledUiModel(
                        productPictureUrl = it.productPicture,
                        productName = it.productName,
                        productPrice = it.productPrice,
                        productQtyCheckout = it.productQuantityCheckout,
                        productQtyRequest = it.productQuantityRequest
                    )
                }
            )

            add(PofThinDividerUiModel())

            val productFulfilledList =
                infoRespondPartialOrderFulfillment.detailsFulfilled.mapIndexed { index, detailsFulfilled ->
                    val isShowDivider = isShowDividerUnfulfilledProduct(index, infoRespondPartialOrderFulfillment.detailsFulfilled.size)
                    PofProductFulfilledUiModel(
                        productPictureUrl = detailsFulfilled.productPicture,
                        productName = detailsFulfilled.productName,
                        productPrice = detailsFulfilled.productPrice,
                        productQty = detailsFulfilled.productQuantity,
                        isShowDivider = isShowDivider
                    )
                }

            add(
                PofFulfilledToggleUiModel(
                    orderId = infoRespondPartialOrderFulfillment.orderId.toString(),
                    totalFulfilled = infoRespondPartialOrderFulfillment.totalFulfilled,
                    isExpanded = false,
                    pofProductFulfilledList = productFulfilledList
                )
            )

            add(PofThickDividerUiModel())

            infoRespondPartialOrderFulfillment.summary.pofDetails.forEach {
                add(PofDetailUiModel(key = it.key, label = it.label, value = it.value))
            }

            add(PofThinDividerUiModel(marginHorizontal = MARGIN_16, marginTop = MARGIN_12))

            val estimateRefund = infoRespondPartialOrderFulfillment.summary.estimateRefund
            add(
                PofRefundEstimateBottomSheetUiModel(
                    refundEstimateLabel = estimateRefund.label,
                    refundEstimateValue = estimateRefund.value,
                    pofFooterInfo = infoRespondPartialOrderFulfillment.footerInfo,
                    estimateInfoUiModel = mapToEstimateInfoUiModel(infoRespondPartialOrderFulfillment.estimateInfo)
                )
            )
        }.toList()

        return PartialOrderFulfillmentWrapperUiModel(
            partialOrderFulfillmentUiModelList = partialOrderFulfillmentUiModelList
        )
    }

    fun mapToEstimateInfoUiModel(estimateInfo: EstimateInfo): EstimateInfoUiModel {
        return EstimateInfoUiModel(estimateInfo.title, estimateInfo.info)
    }

    fun mapToApprovePartialOrderFulfillmentUiModel(
        approvePartialOrderFulfillment: ApprovePartialOrderFulfillment
    ): ApprovePartialOrderFulfillmentUiModel {
        val isSuccess = approvePartialOrderFulfillment.success == Int.ONE
        return ApprovePartialOrderFulfillmentUiModel(isSuccess)
    }

    fun mapToRejectPartialOrderFulfillmentUiModel(
        rejectPartialOrderFulfillment: RejectPartialOrderFulfillment
    ): RejectPartialOrderFulfillmentUiModel {
        val isSuccess = rejectPartialOrderFulfillment.success == Int.ONE
        return RejectPartialOrderFulfillmentUiModel(isSuccess)
    }

    private fun isShowDividerUnfulfilledProduct(index: Int, fullFilledProductsSize: Int): Boolean {
        return index < fullFilledProductsSize - Int.ONE
    }

    companion object {
        const val MARGIN_16 = 16
        const val MARGIN_12 = 12
    }
}
