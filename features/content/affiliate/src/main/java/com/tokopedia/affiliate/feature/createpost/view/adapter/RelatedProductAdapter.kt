package com.tokopedia.affiliate.feature.createpost.view.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.RelatedProductItem
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadDrawable
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_af_related_product.view.*

/**
 * @author by milhamj on 21/02/19.
 */
class RelatedProductAdapter : RecyclerView.Adapter<RelatedProductAdapter.ViewHolder>() {

    private val emptyItem: RelatedProductItem = RelatedProductItem(EMPTY_ITEM_ID)
    private val list: MutableList<RelatedProductItem> = arrayListOf(emptyItem)

    companion object {
        private const val EMPTY_ITEM_ID = "-1"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_af_related_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]

        if (element.id == EMPTY_ITEM_ID) {
            holder.itemView.thumbnail.loadDrawable(R.drawable.ic_system_action_addimage_grayscale_62)
            holder.itemView.delete.hide()
        } else {
            holder.itemView.thumbnail.loadImage(element.image)
            holder.itemView.delete.show()
        }
        holder.itemView.name.text = element.name
        holder.itemView.price.text = element.price
        holder.itemView.delete.setOnClickListener {
            val newList= ArrayList(list)
            newList.removeAt(holder.adapterPosition)
            if (newList.isEmpty()) {
                newList.add(emptyItem)
            }
            addAll(newList)
        }
    }

    fun addAll(list: MutableList<RelatedProductItem>) {
        if (list.isEmpty()) {
            list.add(emptyItem)
        }

        val diffResult = DiffUtil.calculateDiff(Callback(this.list, list))
        diffResult.dispatchUpdatesTo(this)

        this.list.clear()
        this.list.addAll(list)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

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