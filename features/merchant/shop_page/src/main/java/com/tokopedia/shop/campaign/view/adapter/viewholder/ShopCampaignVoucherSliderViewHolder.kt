package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignVoucherSliderAdapter
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.model.ExclusiveLaunchMoreVoucherUiModel
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.ShopCampaignVoucherSliderWidgetBinding
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignVoucherSliderViewHolder(
    itemView: View,
    private val shopCampaignListener: ShopCampaignInterface,
    private val voucherSliderWidgetListener: Listener,
    private val voucherSliderItemListener: ShopCampaignVoucherSliderItemViewHolder.Listener,
    private val voucherSliderMoreItemListener: ShopCampaignVoucherSliderMoreItemViewHolder.Listener
) : AbstractViewHolder<ShopWidgetVoucherSliderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_campaign_voucher_slider_widget
        private const val MAXIMUM_VOUCHER = 5
    }

    interface Listener{
        fun onImpressionVoucherSliderWidget(model: ShopWidgetVoucherSliderUiModel, position: Int)
    }

    private val binding: ShopCampaignVoucherSliderWidgetBinding? by viewBinding()
    private var rvVoucherList: RecyclerView? = binding?.rvVoucherSlider
    private val adapterShopCampaignVoucherSlider: ShopCampaignVoucherSliderAdapter by lazy {
        ShopCampaignVoucherSliderAdapter(
            shopCampaignListener,
            voucherSliderItemListener,
            voucherSliderMoreItemListener
        )
    }
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
        setWidgetImpressionListener(uiModel)
    }

    private fun setWidgetImpressionListener(uiModel: ShopWidgetVoucherSliderUiModel) {
        itemView.addOnImpressionListener(uiModel.impressHolder) {
            voucherSliderWidgetListener.onImpressionVoucherSliderWidget(
                uiModel,
                ShopUtil.getActualPositionFromIndex(bindingAdapterPosition),
            )
        }
    }

    private fun setVoucherList(uiModel: ShopWidgetVoucherSliderUiModel) {
        val rvState = model?.rvState
        if (null != rvState) {
            rvVoucherList?.layoutManager?.onRestoreInstanceState(rvState)
        }
        val voucherListModel = getVoucherListModel(uiModel)
        rvVoucherList?.addHorizontalSpacing()
        rvVoucherList?.adapter = adapterShopCampaignVoucherSlider
        adapterShopCampaignVoucherSlider.setParentUiModel(uiModel)
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
                        listCategorySlug = uiModel.getListCategorySlug()
                    )
                )
            }
        }
    }

    private fun RecyclerView.addHorizontalSpacing() {
        if(itemDecorationCount == Int.ZERO) {
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
