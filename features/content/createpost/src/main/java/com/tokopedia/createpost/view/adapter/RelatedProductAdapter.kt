package com.tokopedia.createpost.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.createpost.common.TYPE_AFFILIATE
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.createpost.R
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_af_related_product.view.*

/**
 * @author by milhamj on 21/02/19.
 */
class RelatedProductAdapter(val listener: RelatedProductListener? = null, val type: String = "") :
    RecyclerView.Adapter<RelatedProductAdapter.ViewHolder>() {

    private val emptyItem: RelatedProductItem = RelatedProductItem(EMPTY_ITEM_ID)
    private var list: MutableList<RelatedProductItem> = arrayListOf()

    init {
        setHasStableIds(true)
        if (shouldAddEmpty()) {
            list.add(emptyItem)
        }
    }

    companion object {
        private const val EMPTY_ITEM_ID = "-1"
        private const val IMAGE_RADIUS = 25f

        const val TYPE_PREVIEW = "preview"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_af_related_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long {
        return list[position].id.toLongOrNull() ?: RecyclerView.NO_ID
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]

        if (element.id == EMPTY_ITEM_ID) {
            holder.itemView.thumbnail.loadImageDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_addimage_grayscale_62)
            holder.itemView.delete.hide()
            holder.itemView.separatorBottom.hide()
            holder.itemView.separatorBottomEmpty.show()
            holder.itemView.setOnClickListener {
                listener?.onEmptyProductClick()
            }
        } else {
            holder.itemView.thumbnail.loadImageRounded(element.image, IMAGE_RADIUS)
            holder.itemView.delete.showWithCondition(type != TYPE_PREVIEW)
            holder.itemView.separatorBottom.show()
            holder.itemView.separatorBottomEmpty.hide()
            holder.itemView.setOnClickListener { }
        }
        holder.itemView.name.text = element.name
        holder.itemView.price.text = element.price
        holder.itemView.price.setTextColor(
            MethodChecker.getColor(
                holder.itemView.context,
                if (element.type == TYPE_AFFILIATE) com.tokopedia.unifyprinciples.R.color.Unify_BN500
                else com.tokopedia.unifyprinciples.R.color.Unify_YN500
            )
        )
        holder.itemView.delete.setOnClickListener {
            listener?.onItemDeleted(holder.adapterPosition)

            if (list.isEmpty() && shouldAddEmpty()) {
                list.add(emptyItem)
                notifyItemInserted(list.size)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<RelatedProductItem>) {
        if (list.isEmpty() && shouldAddEmpty()) {
            list.add(emptyItem)
        }
        this.list = list
        notifyDataSetChanged()
    }

    fun removeEmpty() {
        val position = this.list.indexOf(emptyItem)
        if (this.list.remove(emptyItem)) {
            notifyItemRemoved(position)
        }
    }

    private fun shouldAddEmpty() = type != TYPE_PREVIEW

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    interface RelatedProductListener {
        fun onEmptyProductClick()

        fun onItemDeleted(position: Int)
    }
}
