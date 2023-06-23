package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignVoucherSliderAdapter
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.model.ExclusiveLaunchMoreVoucherUiModel
import com.tokopedia.shop.databinding.ShopCampaignVoucherSliderWidgetBinding
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignVoucherSliderViewHolder(
    itemView: View,
    private val shopCampaignListener: ShopCampaignInterface,
    private val voucherSliderItemListener: ShopCampaignVoucherSliderItemViewHolder.Listener,
    private val voucherSliderMoreItemListener: ShopCampaignVoucherSliderMoreItemViewHolder.Listener
) : AbstractViewHolder<ShopWidgetVoucherSliderUiModel>(itemView) {

    interface Listener {
        fun onVoucherImpression(model: ShopHomeVoucherUiModel, position: Int)
        fun onVoucherTokoMemberInformationImpression(model: ShopHomeVoucherUiModel, position: Int)
        fun onVoucherReloaded()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_campaign_voucher_slider_widget
        private const val MAXIMUM_VOUCHER = 5
    }

    private val binding: ShopCampaignVoucherSliderWidgetBinding? by viewBinding()
    private var rvVoucherList: RecyclerView? = binding?.rvVoucherSlider
    private var merchantVoucherReload: CardView? =
        binding?.voucherSliderReload?.merchantVoucherReload
    private val adapterShopCampaignVoucherSlider: ShopCampaignVoucherSliderAdapter by lazy {
        ShopCampaignVoucherSliderAdapter(
            shopCampaignListener,
            voucherSliderItemListener,
            voucherSliderMoreItemListener
        )
    }
    private var textReload: Typography? = binding?.voucherSliderReload?.textReload
    private var imageReload: ImageView? = binding?.voucherSliderReload?.imageReload
    private var textReloadDesc: Typography? = binding?.voucherSliderReload?.textReloadDesc
    private var model: ShopWidgetVoucherSliderUiModel? = null
    init {
        rvVoucherList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                model?.rvState = recyclerView.layoutManager?.onSaveInstanceState()
            }
        })
    }
    override fun bind(uiModel: ShopWidgetVoucherSliderUiModel) {
        model = uiModel
        setVoucherList(uiModel)
    }

    private fun setVoucherList(uiModel: ShopWidgetVoucherSliderUiModel) {
        val rvState = model?.rvState
        if (null != rvState) {
            rvVoucherList?.layoutManager?.onRestoreInstanceState(rvState)
        }
        val voucherListModel = getVoucherListModel(uiModel)
        rvVoucherList?.addHorizontalSpacing()
        rvVoucherList?.adapter = adapterShopCampaignVoucherSlider
        adapterShopCampaignVoucherSlider.submit(voucherListModel)
    }

    private fun getVoucherListModel(uiModel: ShopWidgetVoucherSliderUiModel): List<Any> {
        return mutableListOf<Any>().apply {
            val voucherList = uiModel.listVoucher.take(MAXIMUM_VOUCHER)
            addAll(voucherList)
            if (uiModel.listVoucher.size > MAXIMUM_VOUCHER) {
                add(
                    ExclusiveLaunchMoreVoucherUiModel(
                        totalRemainingVoucher = uiModel.listVoucher.size - MAXIMUM_VOUCHER,
                        listCategorySlug = uiModel.listCategorySlug
                    )
                )
            }
        }
    }

    private fun getReloadDesc(): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(getString(R.string.shop_page_reload))
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            getString(R.string.shop_page_reload).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return SpannableStringBuilder()
            .append(getString(R.string.shop_page_reload_beginning_of_description))
            .append(" ")
            .append(spannableStringBuilder)
            .append(" ")
            .append(getString(R.string.shop_page_reload_end_of_description))
    }

    private fun RecyclerView.addHorizontalSpacing() {
        if(itemDecorationCount == 0) {
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: android.graphics.Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view)
                    if (position != RecyclerView.NO_POSITION) {
                        outRect.right = context.resources.getDimensionPixelSize(
                            R.dimen.voucher_slider_widget_item_spacing
                        )
                    }
                }
            })
        }
    }
}
