package com.tokopedia.vouchercreation.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.detail.view.VoucherDetailListener
import com.tokopedia.vouchercreation.detail.view.viewholder.*

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailAdapterFactoryImpl(
        private val voucherDetailListener: VoucherDetailListener
) : BaseAdapterTypeFactory(), VoucherDetailAdapterFactory {

    override fun type(model: VoucherHeaderUiModel): Int = HeaderViewHolder.RES_LAYOUT

    override fun type(model: UsageProgressUiModel): Int = UsageProgressViewHolder.RES_LAYOUT

    override fun type(model: DividerUiModel): Int = DividerViewHolder.RES_LAYOUT

    override fun type(model: TipsUiModel): Int = TipsViewHolder.RES_LAYOUT

    override fun type(model: InfoContainerUiModel): Int = InfoContainerViewHolder.RES_LAYOUT

    override fun type(model: PromoPerformanceUiModel): Int = PromoPerformanceViewHolder.RES_LAYOUT

    override fun type(model: FooterButtonUiModel): Int = FooterButtonViewHolder.RES_LAYOUT

    override fun type(model: FooterUiModel): Int = FooterViewHolder.RES_LAYOUT

    override fun type(model: VoucherPreviewUiModel): Int = VoucherPreviewViewHolder.RES_LAYOUT

    override fun type(model: VoucherTickerUiModel): Int = VoucherTickerViewHolder.RES_LAYOUT

    override fun type(model: WarningPeriodUiModel): Int = WarningPeriodViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HeaderViewHolder.RES_LAYOUT -> HeaderViewHolder(parent)
            PromoPerformanceViewHolder.RES_LAYOUT -> PromoPerformanceViewHolder(parent) { title, content ->
                voucherDetailListener.showDescriptionBottomSheet(title, content)
            }
            UsageProgressViewHolder.RES_LAYOUT -> UsageProgressViewHolder(parent) { title, content ->
                voucherDetailListener.showDescriptionBottomSheet(title, content)
            }
            DividerViewHolder.RES_LAYOUT -> DividerViewHolder(parent)
            TipsViewHolder.RES_LAYOUT -> TipsViewHolder(parent) {
                voucherDetailListener.showTipsAndTrickBottomSheet()
            }
            InfoContainerViewHolder.RES_LAYOUT -> InfoContainerViewHolder(parent) {
                voucherDetailListener.onInfoContainerCtaClick(it)
            }
            FooterButtonViewHolder.RES_LAYOUT -> FooterButtonViewHolder(parent) {
                voucherDetailListener.onFooterButtonClickListener()
            }
            FooterViewHolder.RES_LAYOUT -> FooterViewHolder(parent) {
                voucherDetailListener.onFooterCtaTextClickListener()
            }
            VoucherPreviewViewHolder.RES_LAYOUT -> VoucherPreviewViewHolder(parent)
            VoucherTickerViewHolder.RES_LAYOUT -> VoucherTickerViewHolder(parent) {

            }
            WarningPeriodViewHolder.RES_LAYOUT -> WarningPeriodViewHolder(parent) {
                voucherDetailListener.onInfoContainerCtaClick(it)
            }
            else -> super.createViewHolder(parent, type)
        }
    }
}