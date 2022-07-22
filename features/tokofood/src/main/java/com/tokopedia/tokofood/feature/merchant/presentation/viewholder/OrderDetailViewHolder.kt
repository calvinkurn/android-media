package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokofood.R
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
    }

    private var context: Context? = null

    private var addOnInfoAdapter: AddOnInfoAdapter? = null

    init {
        context = binding.root.context
        binding.tpgEditButton.setOnClickListener {
            val customOrderDetail = binding.root.getTag(R.id.custom_order_detail) as CustomOrderDetail
            clickListener.onEditButtonClicked(cartId = customOrderDetail.cartId)
        }
        binding.removeFromCartButton.setOnClickListener {
            binding.root.getTag(R.id.dataset_position) as Int
            val customOrderDetail = binding.root.getTag(R.id.custom_order_detail) as CustomOrderDetail
            clickListener.onDeleteButtonClicked(adapterPosition =  adapterPosition, cartId = customOrderDetail.cartId)
        }
        binding.qeuProductQtyEditor.editText.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.qeuProductQtyEditor.editText.setOnEditorActionListener { view, actionId, _ ->
            if(actionId== EditorInfo.IME_ACTION_DONE) {
                view.clearFocus()
                val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                true
            } else false
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