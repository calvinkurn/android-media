package com.tokopedia.shopadmin.acceptinvitation.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shopadmin.acceptinvitation.presentation.FeatureAccessUiModel
import com.tokopedia.shopadmin.databinding.ItemAdminFeatureAccessBinding

class ItemFeatureAccessAdapter {

    inner class ItemFeatureAccessViewHolder(private val binding: ItemAdminFeatureAccessBinding):
        RecyclerView.ViewHolder(binding.root) {

            fun bind(item: FeatureAccessUiModel) {
                with(binding) {
                    dividerTopVertical
                }
            }
        }
}