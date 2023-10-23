package com.tokopedia.createpost.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.createpost.common.TYPE_AFFILIATE
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.createpost.R
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.resources.common.R as resourcescommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by milhamj on 21/02/19.
 */
class RelatedProductAdapter(
    private val listener: RelatedProductListener? = null,
    val type: String = ""
) : RecyclerView.Adapter<RelatedProductAdapter.ViewHolder>() {

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
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long {
        return list[position].id.toLongOrNull() ?: RecyclerView.NO_ID
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position], type)
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

    class ViewHolder(
        private val view: View,
        private val listener: RelatedProductListener?
    ) : RecyclerView.ViewHolder(view) {

        private val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        private val delete: ImageView = view.findViewById(R.id.delete)
        private val separatorBottom: View = view.findViewById(R.id.separatorBottom)
        private val separatorBottomEmpty: View = view.findViewById(R.id.separatorBottomEmpty)
        private val name: Typography = view.findViewById(R.id.name)
        private val price: Typography = view.findViewById(R.id.price)

        fun bindData(
            element: RelatedProductItem,
            type: String,
        ) {
            if (element.id == EMPTY_ITEM_ID) {
                thumbnail.loadImageDrawable(resourcescommonR.drawable.ic_system_action_addimage_grayscale_62)
                delete.hide()
                separatorBottom.hide()
                separatorBottomEmpty.show()
                view.setOnClickListener {
                    listener?.onEmptyProductClick()
                }
            } else {
                thumbnail.loadImageRounded(element.image, IMAGE_RADIUS)
                delete.showWithCondition(type != TYPE_PREVIEW)
                separatorBottom.show()
                separatorBottomEmpty.hide()
            }
            name.text = element.name
            price.text = element.price
            price.setTextColor(
                MethodChecker.getColor(
                    view.context,
                    if (element.type == TYPE_AFFILIATE) unifyprinciplesR.color.Unify_BN500
                    else unifyprinciplesR.color.Unify_YN500
                )
            )
            delete.setOnClickListener {
                listener?.onItemDeleted(adapterPosition)
            }
        }
    }

    interface RelatedProductListener {
        fun onEmptyProductClick()

        fun onItemDeleted(position: Int)
    }
}
