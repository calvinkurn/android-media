package com.tokopedia.shop.flash_sale.presentation.draft.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemDraftBinding
import com.tokopedia.shop.flash_sale.common.extension.formatTo
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.utils.view.binding.viewBinding

class DraftListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.ssfs_item_draft, parent, false)
    }

    private val binding: SsfsItemDraftBinding? by viewBinding()

    fun bind(item: DraftItemModel) {
        binding?.typographyDraftTitle?.text = item.title
        binding?.typographyDraftDesc?.text = item.description
        binding?.typographyDraftStart?.text = item.startDate.formatTo("dd/MM/yyyy")
        binding?.typographyDraftEnd?.text = item.endDate.formatTo("dd/MM/yyyy")
    }

}