package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomsheetEmptyProductItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-11-05.
 */
class SomBottomSheetStockEmptyAdapter(
    private val listener: ProductCheckChangedListener
) : RecyclerView.Adapter<SomBottomSheetStockEmptyAdapter.ViewHolder>() {
    var listProduct = mutableListOf<SomDetailOrder.GetSomDetail.Details.Product>()
    var listToBeEmptied = ArrayList<SomDetailOrder.GetSomDetail.Details.Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_empty_product_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.run {
            listProduct.getOrNull(position)?.let {
                ivProduct.loadImageRounded(
                    it.thumbnail,
                    root.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
                )
                tvProductName.text = it.name
                tvProductPrice.text = it.priceText
                cbProduct.setOnCheckedChangeListener(null)
                cbProduct.isChecked = listToBeEmptied.contains(it)
                labelEmptyStock.showWithCondition(cbProduct.isChecked)
                cbProduct.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        listToBeEmptied.add(it)
                        labelEmptyStock.visibility = View.VISIBLE
                    } else {
                        listToBeEmptied.remove(it)
                        labelEmptyStock.visibility = View.GONE
                    }
                    listener.onProductCheckChanged()
                }
                root.setOnClickListener {
                    cbProduct.isChecked = !cbProduct.isChecked
                }
            }
        }
    }

    fun getListProductEmptied(): ArrayList<SomDetailOrder.GetSomDetail.Details.Product> {
        return listToBeEmptied
    }

    fun reset() {
        listToBeEmptied.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding by viewBinding<BottomsheetEmptyProductItemBinding>()
    }

    interface ProductCheckChangedListener {
        fun onProductCheckChanged()
    }
}
