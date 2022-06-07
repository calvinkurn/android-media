package com.tokopedia.shop.flashsale.presentation.list.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemCampaignListMoreMenuBinding
import com.tokopedia.shop.flashsale.common.constant.Constant.ZERO
import com.tokopedia.shop.flashsale.domain.entity.CampaignListMoreMenu

class CampaignListMoreMenuAdapter: RecyclerView.Adapter<CampaignListMoreMenuAdapter.MoreMenuViewHolder>() {

    private var menus: MutableList<CampaignListMoreMenu> = mutableListOf()
    private var onMenuClicked: (CampaignListMoreMenu) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreMenuViewHolder {
        val binding =
            SsfsItemCampaignListMoreMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoreMenuViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: MoreMenuViewHolder, position: Int) {
        menus.getOrNull(position)?.let { menu ->
            holder.bind(menu, onMenuClicked)
        }
    }

    fun setOnMenuClick(onMenuClicked : (CampaignListMoreMenu) -> Unit) {
        this.onMenuClicked = onMenuClicked
    }

    fun submit(items: List<CampaignListMoreMenu>) {
        this.menus.addAll(items)
        notifyItemRangeChanged(ZERO, items.size)
    }


    inner class MoreMenuViewHolder(private val binding: SsfsItemCampaignListMoreMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: CampaignListMoreMenu, onMenuClicked: (CampaignListMoreMenu) -> Unit) {
            binding.tpgMenuName.text = binding.tpgMenuName.context.getString(menu.nameResourceId)
            binding.imgMenu.setImageResource(menu.imageResourceId)
            binding.root.setOnClickListener { onMenuClicked(menu) }
        }

    }
}