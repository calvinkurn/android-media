package com.tokopedia.oneclickcheckout.address

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.ItemLoadingAddressListBinding

class AddressLoadingViewHolder(private val binding: ItemLoadingAddressListBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LOADING_LAYOUT = R.layout.item_loading_address_list
    }

    fun bind() {
        binding.progressBarAddress.visible()
    }
}