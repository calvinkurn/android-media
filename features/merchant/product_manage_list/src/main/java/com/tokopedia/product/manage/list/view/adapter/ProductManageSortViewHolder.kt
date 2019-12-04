package com.tokopedia.product.manage.list.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.data.model.ProductManageSortModel

class ProductManageSortViewHolder(val view: View, val listener: ProductManageSortViewHolderListener) : BaseViewHolder<ProductManageSortModel>(view) {

    private val titleSort: TextView = view.findViewById(com.tokopedia.product.manage.list.R.id.txt_title_sort)
    private val imageCheckList: ImageView = view.findViewById(com.tokopedia.product.manage.list.R.id.img_view_sort)
    private var adapterListener: ProductManageSortViewHolderListener? = null

    fun setAdapterListener(adapterListener: ProductManageSortViewHolderListener) {
        this.adapterListener = adapterListener
    }

    override fun bindObject(data: ProductManageSortModel) {
        adapterListener?.let {
            it.isItemChecked(data.sortId)?.let { isChecked ->
                if (isChecked) {
                    imageCheckList.visibility = View.VISIBLE
                } else {
                    imageCheckList.visibility = View.INVISIBLE
                }
            }
        }
        view.setOnClickListener {
            listener.onClickItem(data)
        }
        titleSort.text = data.titleSort

    }

    interface ProductManageSortViewHolderListener {
        fun onClickItem(data: ProductManageSortModel)
        fun isItemChecked(id: String): Boolean?
    }
}