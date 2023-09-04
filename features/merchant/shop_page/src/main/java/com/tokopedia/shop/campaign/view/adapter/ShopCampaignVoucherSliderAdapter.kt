package com.tokopedia.shop.campaign.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderItemViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderMoreItemViewHolder
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.model.ExclusiveLaunchMoreVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel

class ShopCampaignVoucherSliderAdapter(
    private val shopCampaignListener: ShopCampaignInterface,
    private val voucherSliderItemListener: ShopCampaignVoucherSliderItemViewHolder.Listener,
    private val voucherSliderMoreItemListener: ShopCampaignVoucherSliderMoreItemViewHolder.Listener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var parentUiModel: ShopWidgetVoucherSliderUiModel? = null

    private val differCallback = object :
        DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return false
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return when (viewType) {
            ShopCampaignVoucherSliderItemViewHolder.LAYOUT -> ShopCampaignVoucherSliderItemViewHolder(
                view,
                shopCampaignListener,
                voucherSliderItemListener,
                itemCount,
                parentUiModel
            )

            else -> ShopCampaignVoucherSliderMoreItemViewHolder(
                view,
                shopCampaignListener,
                voucherSliderMoreItemListener,
                parentUiModel
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShopCampaignVoucherSliderItemViewHolder -> holder.bind(differ.currentList[position] as? ExclusiveLaunchVoucher)
            is ShopCampaignVoucherSliderMoreItemViewHolder -> holder.bind(differ.currentList[position] as? ExclusiveLaunchMoreVoucherUiModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position] is ExclusiveLaunchVoucher) {
            ShopCampaignVoucherSliderItemViewHolder.LAYOUT
        } else {
            ShopCampaignVoucherSliderMoreItemViewHolder.LAYOUT
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submit(data: List<Any>) {
        differ.submitList(data)
    }

    fun setParentUiModel(uiModel: ShopWidgetVoucherSliderUiModel) {
        this.parentUiModel = uiModel
    }

}
