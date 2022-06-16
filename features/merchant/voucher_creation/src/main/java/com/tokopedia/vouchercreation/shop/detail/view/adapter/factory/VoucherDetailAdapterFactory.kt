package com.tokopedia.vouchercreation.shop.detail.view.adapter.factory

import com.tokopedia.vouchercreation.shop.create.view.uimodel.voucherimage.PostVoucherUiModel
import com.tokopedia.vouchercreation.shop.detail.model.*

/**
 * Created By @ilhamsuaib on 30/04/20
 */

interface VoucherDetailAdapterFactory {

    fun type(model: VoucherHeaderUiModel): Int

    fun type(model: UsageProgressUiModel): Int

    fun type(model: DividerUiModel): Int

    fun type(model: TipsUiModel): Int

    fun type(model: InfoContainerUiModel): Int

    fun type(model: PromoPerformanceUiModel): Int

    fun type(model: FooterButtonUiModel): Int

    fun type(model: FooterUiModel): Int

    fun type(model: VoucherPreviewUiModel): Int

    fun type(model: VoucherTickerUiModel): Int

    fun type(model: WarningPeriodUiModel): Int

    fun type(model: PostVoucherUiModel): Int

    fun type(model: DetailLoadingStateUiModel): Int

    fun type(model: ErrorDetailUiModel): Int

    fun type(model: WarningTickerUiModel): Int

    fun type(model: InformationDetailTickerUiModel): Int
}