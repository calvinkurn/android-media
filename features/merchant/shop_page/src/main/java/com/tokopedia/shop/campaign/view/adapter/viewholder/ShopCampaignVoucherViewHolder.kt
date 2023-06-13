package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopCampaignMerchantVoucherBinding
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopCampaignVoucherViewHolder(
    itemView: View,
    private val shopHomeVoucherViewHolderListener: ShopHomeVoucherViewHolder.ShopHomeVoucherViewHolderListener
) : AbstractViewHolder<ShopHomeVoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_merchant_voucher
    }
    private val viewBinding: ItemShopCampaignMerchantVoucherBinding? by viewBinding()
    private var merchantVoucherWidget: MvcView? = null
    private var merchantVoucherReload: CardView? = null
    private var merchantVoucherUiModel: ShopHomeVoucherUiModel? = null
    private var merchantVoucherShimmering: LoaderUnify? = null
    private var textReload: Typography? = null
    private var imageReload: ImageView? = null
    private var textReloadDesc: Typography? = null

    init {
        findView()
    }

    private fun findView() {
        merchantVoucherWidget = viewBinding?.merchantVoucherWidget
        merchantVoucherReload = viewBinding?.merchantVoucherReload?.root
        merchantVoucherShimmering = viewBinding?.merchantVoucherShimmering?.root
        textReload = viewBinding?.merchantVoucherReload?.textReload
        imageReload = viewBinding?.merchantVoucherReload?.imageReload
        textReloadDesc = viewBinding?.merchantVoucherReload?.textReloadDesc
    }

    override fun bind(model: ShopHomeVoucherUiModel) {
        if (model.isError) {
            merchantVoucherShimmering?.hide()
            merchantVoucherWidget?.hide()
            merchantVoucherReload?.show()
            textReloadDesc?.text = getReloadDesc()

            textReload?.setOnClickListener {
                shopHomeVoucherViewHolderListener.onVoucherReloaded()
                merchantVoucherShimmering?.show()
                merchantVoucherReload?.hide()
            }
            imageReload?.setOnClickListener {
                shopHomeVoucherViewHolderListener.onVoucherReloaded()
                merchantVoucherShimmering?.show()
                merchantVoucherReload?.hide()
            }
        } else {
            if (model.data != null && model.data.isShown == true) {
                if (model.data.animatedInfoList?.size.orZero() > 1)
                    shopHomeVoucherViewHolderListener.onVoucherTokoMemberInformationImpression(model, adapterPosition)
                else
                    shopHomeVoucherViewHolderListener.onVoucherImpression(model, adapterPosition)
                merchantVoucherShimmering?.hide()
                merchantVoucherWidget?.show()
                merchantVoucherReload?.hide()
                merchantVoucherUiModel = model

                model.data.apply {
                    merchantVoucherWidget?.setData(
                        MvcData(
                            model.data.animatedInfoList
                        ),
                        shopId = model.data.shopId ?: "0",
                        source = MvcSource.SHOP
                    )
                }
            }
        }
    }

    private fun getReloadDesc(): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(getString(R.string.shop_page_reload))
        spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD), 0, getString(R.string.shop_page_reload).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableStringBuilder()
            .append(getString(R.string.shop_page_reload_beginning_of_description))
            .append(" ")
            .append(spannableStringBuilder)
            .append(" ")
            .append(getString(R.string.shop_page_reload_end_of_description))
    }
}
