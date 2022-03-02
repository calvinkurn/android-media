package com.tokopedia.vouchercreation.shop.detail.view.adapter.factory

import android.app.Activity
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.shop.create.view.uimodel.voucherimage.PostVoucherUiModel
import com.tokopedia.vouchercreation.shop.create.view.viewholder.voucherimage.PostVoucherViewHolder
import com.tokopedia.vouchercreation.shop.detail.model.*
import com.tokopedia.vouchercreation.shop.detail.view.VoucherDetailListener
import com.tokopedia.vouchercreation.shop.detail.view.viewholder.*

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailAdapterFactoryImpl(
        private val voucherDetailListener: VoucherDetailListener,
        private val activity: Activity? = null
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

    override fun type(model: PostVoucherUiModel): Int = PostVoucherViewHolder.RES_LAYOUT

    override fun type(model: DetailLoadingStateUiModel): Int = DetailLoadingStateViewHolder.RES_LAYOUT

    override fun type(model: ErrorDetailUiModel): Int = ErrorDetailViewHolder.RES_LAYOUT

    override fun type(model: WarningTickerUiModel): Int = WarningTickerViewHolder.RES_LAYOUT

    override fun type(model: InformationDetailTickerUiModel): Int = InformationDetailTickerViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HeaderViewHolder.RES_LAYOUT -> HeaderViewHolder(parent) {
                voucherDetailListener.showDownloadBottomSheet()
            }
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
            InfoContainerViewHolder.RES_LAYOUT -> InfoContainerViewHolder(parent,
                    onCtaClick = {
                        voucherDetailListener.onInfoContainerCtaClick(it)
                    },
                    onImpression = {
                        voucherDetailListener.onImpression(it)
                    })
            FooterButtonViewHolder.RES_LAYOUT -> FooterButtonViewHolder(parent) {
                voucherDetailListener.onFooterButtonClickListener()
            }
            FooterViewHolder.RES_LAYOUT -> FooterViewHolder(parent) {
                voucherDetailListener.onFooterCtaTextClickListener()
            }
            VoucherPreviewViewHolder.RES_LAYOUT -> VoucherPreviewViewHolder(parent)
            VoucherTickerViewHolder.RES_LAYOUT -> VoucherTickerViewHolder(parent) {
                voucherDetailListener.onTickerClicked()
            }
            WarningPeriodViewHolder.RES_LAYOUT -> WarningPeriodViewHolder(parent) {
                voucherDetailListener.onInfoContainerCtaClick(it)
            }
            PostVoucherViewHolder.RES_LAYOUT -> PostVoucherViewHolder(parent, activity) {
                voucherDetailListener.onSuccessDrawPostVoucher(it)
            }
            DetailLoadingStateViewHolder.RES_LAYOUT -> DetailLoadingStateViewHolder(parent)
            ErrorDetailViewHolder.RES_LAYOUT -> ErrorDetailViewHolder(parent) {
                voucherDetailListener.onErrorTryAgain()
            }
            WarningTickerViewHolder.RES_LAYOUT -> WarningTickerViewHolder(parent) {
                voucherDetailListener.onInfoContainerCtaClick(it)
            }
            InformationDetailTickerViewHolder.RES_LAYOUT -> InformationDetailTickerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}