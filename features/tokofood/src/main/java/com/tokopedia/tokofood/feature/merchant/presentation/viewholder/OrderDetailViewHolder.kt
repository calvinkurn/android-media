package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokofood.databinding.TokofoodItemOrderInfoLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.AddOnInfoAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail

class OrderDetailViewHolder(private val binding: TokofoodItemOrderInfoLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var context: Context? = null

    private var addOnInfoAdapter: AddOnInfoAdapter = AddOnInfoAdapter()

    init {
        context = binding.root.context
        binding.rvSelectedAddOns.apply {
            adapter = addOnInfoAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        binding.tpgEditButton.setOnClickListener {

        }
        binding.removeFromCartButton.setOnClickListener {

        }
        binding.qeuProductQtyEditor.setSubstractListener {

        }
        binding.qeuProductQtyEditor.setAddClickListener {

        }
    }

    fun bindData(customOrderDetail: CustomOrderDetail) {
        binding.customProductPrice.text = customOrderDetail.subTotalFmt
        binding.notesLabel.isVisible = customOrderDetail.orderNote.isNotBlank()
        binding.tpgOrderNote.text = customOrderDetail.orderNote
        addOnInfoAdapter.setCustomListItems(customOrderDetail.customListItems)
    }
}