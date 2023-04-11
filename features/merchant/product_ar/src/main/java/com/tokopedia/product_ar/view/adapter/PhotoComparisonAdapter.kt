package com.tokopedia.product_ar.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_ar.model.ComparissonImageUiModel
import com.tokopedia.product_ar.view.fragment.ComparissonHelperListener
import com.tokopedia.product_ar.view.viewholder.FullImageViewHolder
import com.tokopedia.product_ar.view.viewholder.GridImageViewHolder

class PhotoComparisonAdapter(val listener: ComparissonHelperListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SINGLE_PHOTO_TYPE = 1
        const val MULTIPLE_PHOTO_TYPE = 2
    }

    var listBitmap: MutableList<ComparissonImageUiModel> = mutableListOf()

    fun setData(data: List<ComparissonImageUiModel>) {
        listBitmap = data.toMutableList()
        notifyDataSetChanged()
    }

    fun removeData(position: Int) {
        if (position != -1) {
            listBitmap.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SINGLE_PHOTO_TYPE -> {
                val v = LayoutInflater.from(parent.context).inflate(FullImageViewHolder.LAYOUT, parent, false)
                FullImageViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(GridImageViewHolder.LAYOUT, parent, false)
                GridImageViewHolder(v, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (listBitmap.size == 1) {
            SINGLE_PHOTO_TYPE
        } else {
            MULTIPLE_PHOTO_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FullImageViewHolder -> {
                holder.bind(listBitmap[position])
            }
            else -> {
                (holder as? GridImageViewHolder)?.bind(listBitmap[position])
            }
        }
    }

    override fun getItemCount(): Int = listBitmap.size

}