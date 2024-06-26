package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import com.tokopedia.buyerorderdetail.presentation.model.PofAvailableLabelUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofErrorUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofHeaderInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductFulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductUnfulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundEstimateBottomSheetUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofThickDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofThinDividerUiModel

interface PartialOrderFulfillmentTypeFactory {

    fun type(pofAvailableLabelUiModel: PofAvailableLabelUiModel): Int

    fun type(pofDetailUiModel: PofDetailUiModel): Int

    fun type(pofFulfilledToggleUiModel: PofFulfilledToggleUiModel): Int

    fun type(pofHeaderInfoUiModel: PofHeaderInfoUiModel): Int

    fun type(pofProductFulfilledUiModel: PofProductFulfilledUiModel): Int

    fun type(pofProductUnfulfilledUiModel: PofProductUnfulfilledUiModel): Int

    fun type(pofRefundEstimateBottomSheetUiModel: PofRefundEstimateBottomSheetUiModel): Int

    fun type(pofThickDividerUiModel: PofThickDividerUiModel): Int

    fun type(pofThinDividerUiModel: PofThinDividerUiModel): Int

    fun type(pofErrorUiModel: PofErrorUiModel): Int

}
