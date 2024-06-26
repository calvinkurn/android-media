package com.tokopedia.shopadmin.feature.invitationaccepted.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isEven
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shopadmin.databinding.ItemAdminFeatureAccessBinding
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel

class ItemFeatureAccessAdapter(private val featureAccessList: List<AdminPermissionUiModel>):
    RecyclerView.Adapter<ItemFeatureAccessAdapter.ItemFeatureAccessViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFeatureAccessViewHolder {
        val binding = ItemAdminFeatureAccessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFeatureAccessViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemFeatureAccessViewHolder, position: Int) {
        if (featureAccessList.isNotEmpty()) {
            holder.bind(featureAccessList[position])
        }
    }

    override fun getItemCount(): Int = featureAccessList.size

    inner class ItemFeatureAccessViewHolder(private val binding: ItemAdminFeatureAccessBinding):
        RecyclerView.ViewHolder(binding.root) {

            fun bind(item: AdminPermissionUiModel) {
                with(binding) {
                    dividerTopHorizontal.showWithCondition(adapterPosition < featureAccessList.size - Int.ONE)
                    dividerFeatureAccessVertical.showWithCondition(adapterPosition.isEven())

                    setFeatureAccessImg(item.iconUrl)
                    setFeatureAccessTitle(item.permissionName)
                }
            }

            private fun setFeatureAccessImg(iconUrl: String) {
                if (iconUrl.isNotBlank()) {
                    binding.ivFeatureAccessIcon.run {
                        show()
                        setImageUrl(iconUrl)
                    }
                } else {
                    binding.ivFeatureAccessIcon.hide()
                }
            }

            private fun setFeatureAccessTitle(title: String) {
                if (title.isNotBlank()) {
                    binding.tvFeatureAccessName.run {
                        show()
                        text = title
                    }
                } else {
                    binding.tvFeatureAccessName.hide()
                }
            }
        }

    companion object {
        const val TWO_LIMIT = 2
    }
}