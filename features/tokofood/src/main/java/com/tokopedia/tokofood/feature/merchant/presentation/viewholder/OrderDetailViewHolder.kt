package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.util.TokofoodExt
import com.tokopedia.tokofood.common.util.TokofoodExt.setupEditText
import com.tokopedia.tokofood.databinding.TokofoodItemOrderInfoLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.AddOnInfoAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail

class OrderDetailViewHolder(
        private val binding: TokofoodItemOrderInfoLayoutBinding,
        private val clickListener: OnOrderDetailItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnOrderDetailItemClickListener {
        fun onEditButtonClicked(cartId: String)
        fun onDeleteButtonClicked(adapterPosition: Int, cartId: String)
        fun onIncreaseQtyButtonClicked(quantity: Int, customOrderDetail: CustomOrderDetail)
        fun onDecreaseQtyButtonClicked(quantity: Int, customOrderDetail: CustomOrderDetail)
        fun onUpdateQty(quantity: Int, customOrderDetail: CustomOrderDetail)
    }

    private var context: Context? = null

    private var addOnInfoAdapter: AddOnInfoAdapter? = null

    init {
        context = binding.root.context
        binding.qeuProductQtyEditor.setupEditText()
        binding.qeuProductQtyEditor.maxValue = TokofoodExt.MAXIMUM_QUANTITY
        binding.tpgEditButton.setOnClickListener {
            val customOrderDetail = binding.root.getTag(R.id.custom_order_detail) as CustomOrderDetail
            clickListener.onEditButtonClicked(cartId = customOrderDetail.cartId)
        }
        binding.removeFromCartButton.setOnClickListener {
            binding.root.getTag(R.id.dataset_position) as Int
            val customOrderDetail = binding.root.getTag(R.id.custom_order_detail) as CustomOrderDetail
            clickListener.onDeleteButtonClicked(adapterPosition =  adapterPosition, cartId = customOrderDetail.cartId)
        }
        binding.qeuProductQtyEditor.setAddClickListener {
            val quantity = binding.qeuProductQtyEditor.getValue()
            val customOrderDetail = binding.root.getTag(R.id.custom_order_detail) as CustomOrderDetail
            clickListener.onIncreaseQtyButtonClicked(quantity = quantity, customOrderDetail = customOrderDetail)
        }
        binding.qeuProductQtyEditor.setSubstractListener {
            val quantity = binding.qeuProductQtyEditor.getValue()
            val customOrderDetail = binding.root.getTag(R.id.custom_order_detail) as CustomOrderDetail
            clickListener.onDecreaseQtyButtonClicked(quantity = quantity, customOrderDetail = customOrderDetail)
        }
        binding.qeuProductQtyEditor.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val quantity = binding.qeuProductQtyEditor.getValue().orZero()
                val customOrderDetail = binding.root.getTag(R.id.custom_order_detail) as CustomOrderDetail
                clickListener.onUpdateQty(quantity, customOrderDetail)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    fun bindData(customOrderDetail: CustomOrderDetail, dataSetPosition: Int) {
        // bind custom order detail and data set position
        setupAdapter()
        binding.root.setTag(R.id.custom_order_detail, customOrderDetail)
        binding.root.setTag(R.id.dataset_position, dataSetPosition)
        binding.customProductPrice.text = customOrderDetail.subTotalFmt
        binding.notesLabel.isVisible = customOrderDetail.orderNote.isNotBlank()
        binding.tpgOrderNote.text = customOrderDetail.orderNote
        binding.qeuProductQtyEditor.setValue(customOrderDetail.qty)
        addOnInfoAdapter?.setCustomListItems(customOrderDetail.customListItems)
    }

    private fun setupAdapter() {
        addOnInfoAdapter = AddOnInfoAdapter()
        binding.rvSelectedAddOns.apply {
            adapter = addOnInfoAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}