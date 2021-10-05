package com.tokopedia.unifyorderhistory.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyorderhistory.data.model.UohFilterBundle
import com.tokopedia.unifyorderhistory.databinding.BottomsheetOptionUohItemBinding
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohBottomSheetOptionItemViewHolder
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment

/**
 * Created by fwidjaja on 05/07/20.
 */
class UohBottomSheetOptionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    var filterBundleList = arrayListOf<UohFilterBundle>()
    var filterType = -1
    private var actionListener: ActionListener? = null

    interface ActionListener {
        fun onOptionItemClick(label: String, value: String, filterType: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = BottomsheetOptionUohItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
        return UohBottomSheetOptionItemViewHolder(binding, actionListener)
    }

    override fun getItemCount(): Int {
        return filterBundleList.size
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UohBottomSheetOptionItemViewHolder -> {
                holder.bind(holder.adapterPosition, filterBundleList, filterType)
            }
        }
    }
}