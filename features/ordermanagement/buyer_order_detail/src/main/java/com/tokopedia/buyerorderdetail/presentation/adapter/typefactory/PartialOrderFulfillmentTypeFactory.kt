package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import com.tokopedia.buyerorderdetail.presentation.model.PofAvailableLabelUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofHeaderInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductFulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductUnfulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundEstimateUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofThinDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ThickDividerUiModel

interface PartialOrderFulfillmentTypeFactory {

    fun type(pofAvailableLabelUiModel: PofAvailableLabelUiModel): Int

    fun type(pofDetailUiModel: PofDetailUiModel): Int

    fun type(pofFulfilledToggleUiModel: PofFulfilledToggleUiModel): Int

    fun type(pofHeaderInfoUiModel: PofHeaderInfoUiModel): Int

    fun type(pofProductFulfilledUiModel: PofProductFulfilledUiModel): Int

    fun type(pofProductUnfulfilledUiModel: PofProductUnfulfilledUiModel): Int

    fun type(pofRefundEstimateUiModel: PofRefundEstimateUiModel): Int

    fun type(pofThickDividerUiModel: ThickDividerUiModel): Int

    fun type(pofThinDividerUiModel: PofThinDividerUiModel): Int

}
