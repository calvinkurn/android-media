package com.tokopedia.buyerorderdetail.domain.mapper

import com.tokopedia.buyerorderdetail.domain.models.ApprovePartialOrderFulfillment
import com.tokopedia.buyerorderdetail.domain.models.ApprovePartialOrderFulfillmentResponse
import com.tokopedia.buyerorderdetail.domain.models.InfoRespondPartialOrderFulfillment
import com.tokopedia.buyerorderdetail.domain.models.RejectPartialOrderFulfillment
import com.tokopedia.buyerorderdetail.presentation.model.ApprovePartialOrderFulfillmentUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BasePofVisitableUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PartialOrderFulfillmentWrapperUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofAvailableLabelUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofHeaderInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductFulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductUnfulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundEstimateUiModel
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
            addAll(infoRespondPartialOrderFulfillment.detailsUnfulfill.map {
                PofProductUnfulfilledUiModel(
                    productPictureUrl = it.productPicture,
                    productName = it.productName,
                    productPrice = it.productPrice,
                    productQtyCheckout = it.productQuantityCheckout,
                    productQtyRequest = it.productQuantityRequest
                )
            })
            val productFulfilledList = infoRespondPartialOrderFulfillment.detailsFulfilled.map {
                PofProductFulfilledUiModel(
                    productPictureUrl = it.productPicture,
                    productName = it.productName,
                    productPrice = it.productPrice,
                    productQty = it.productQuantity
                )
            }
            add(
                PofFulfilledToggleUiModel(
                    totalFulfilled = infoRespondPartialOrderFulfillment.totalFulfilled,
                    isExpanded = true,
                    pofProductFulfilledList = productFulfilledList
                )
            )

            productFulfilledList.forEachIndexed { index, pofProductFulfilledUiModel ->
                add(pofProductFulfilledUiModel)
                if (index < productFulfilledList.size - Int.ONE) {
                    add(PofThinDividerUiModel())
                }
            }

            add(PofThickDividerUiModel())

            infoRespondPartialOrderFulfillment.summary.pofDetails.forEach {
                add(PofDetailUiModel(key = it.key, label = it.label, value = it.value))
            }

            add(PofThinDividerUiModel(MARGIN_HORIZONTAL))

            val estimateRefund = infoRespondPartialOrderFulfillment.summary.estimateRefund
            add(
                PofRefundEstimateUiModel(
                    refundEstimateLabel = estimateRefund.label,
                    refundEstimateValue = estimateRefund.value,
                    pofFooterInfo = infoRespondPartialOrderFulfillment.footerInfo
                )
            )
        }.toList()

        return PartialOrderFulfillmentWrapperUiModel(
            partialOrderFulfillmentUiModelList = partialOrderFulfillmentUiModelList
        )
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

    companion object {
        const val MARGIN_HORIZONTAL = 16
    }
}
