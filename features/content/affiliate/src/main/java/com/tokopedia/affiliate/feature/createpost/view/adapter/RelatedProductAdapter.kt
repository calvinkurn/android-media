package com.tokopedia.affiliate.feature.createpost.view.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.RelatedProductItem
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_af_related_product.view.*

/**
 * @author by milhamj on 21/02/19.
 */
class RelatedProductAdapter : RecyclerView.Adapter<RelatedProductAdapter.ViewHolder>() {

    private val list : MutableList<RelatedProductItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_af_related_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addAll(list: MutableList<RelatedProductItem>) {
        val diffResult = DiffUtil.calculateDiff(Callback(this.list, list))
        diffResult.dispatchUpdatesTo(this)

        this.list.clear()
        this.list.addAll(list)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(element: RelatedProductItem) {
            itemView.thumbnail.loadImage(element.image)
            itemView.name.text = element.name
            itemView.price.text = element.price
        }
    }

    class Callback(
            private val oldList: List<RelatedProductItem>,
            private val newList: List<RelatedProductItem>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem == newItem
        }
    }
}