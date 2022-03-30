package com.tokopedia.play.ui.variantsheet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.play_common.R as commonR

/**
 * Created by kenny.hadisaputra on 16/03/22
 */
class VariantLabelAdapter : BaseDiffUtilAdapter<String>() {

    init {
        delegatesManager
            .addDelegate(Delegate())
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    private class Delegate : TypedAdapterDelegate<String, String, ViewHolder>(
        commonR.layout.view_play_empty
    ) {

        override fun onBindViewHolder(item: String, holder: ViewHolder) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return ViewHolder.create(parent)
        }
    }

    private class ViewHolder private constructor(itemView: View) : BaseViewHolder(itemView) {

        private val labelVariant: Label = itemView as Label

        fun bind(item: String) {
            labelVariant.setLabel(item)
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_play_variant_label, parent, false)
                )
            }
        }
    }
}