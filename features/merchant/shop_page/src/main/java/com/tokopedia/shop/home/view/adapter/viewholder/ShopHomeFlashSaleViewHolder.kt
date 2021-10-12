package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography

class ShopHomeFlashSaleViewHolder(
    itemView: View,
    private val listener: ShopHomeFlashSaleWidgetListener
) : AbstractViewHolder<ShopHomeFlashSaleUiModel>(itemView) {

    private var uiModel: ShopHomeFlashSaleUiModel? = null
    private val flashSaleCampaignNameView: Typography? = itemView.findViewById(R.id.tgp_flash_sale_campaign_name)
    private val tncInfoIconView: AppCompatImageView? = itemView.findViewById(R.id.iv_tnc_info_icon)
    private val ctaSeeAllView: Typography? = itemView.findViewById(R.id.tgp_cta_see_all)
    private val backGroundView: View? = itemView.findViewById(R.id.bg_flash_sale)
    private val timerTitleView: Typography? = itemView.findViewById(R.id.tgp_flash_sale_timer_title)
    private val timerView: TimerUnifySingle? = itemView.findViewById(R.id.tus_flash_sale_timer)
    private val productCarouselView: RecyclerView? = itemView.findViewById(R.id.rv_flash_sale_product_carousel)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_flash_sale_widget
    }

    init { setupClickListener(listener) }

    override fun bind(element: ShopHomeFlashSaleUiModel?) {
        this.uiModel = element
//        setupHeader(uiModel.name)
    }

    private fun setupClickListener(listener: ShopHomeFlashSaleWidgetListener) {
        // info icon => tnc bottom sheet
        tncInfoIconView?.setOnClickListener {
            uiModel?.run {
                listener.onClickTncFlashSaleWidget(this)
            }
        }
        //
        ctaSeeAllView?.setOnClickListener {

        }
    }

    private fun setupHeader(campaignName: String) {
        flashSaleCampaignNameView?.text = campaignName
    }

    private fun setupFlashSaleBackgroundView() {

    }

    private fun setupFlashSaleCountDownTimer() {

    }

    private fun setupProductCardCarousel() {

    }
}