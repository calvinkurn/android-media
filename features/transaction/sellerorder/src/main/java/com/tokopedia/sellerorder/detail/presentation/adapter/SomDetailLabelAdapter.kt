package com.tokopedia.sellerorder.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemLabelBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.utils.view.binding.viewBinding

class SomDetailLabelAdapter(private var labels: List<SomDetailOrder.GetSomDetail.LabelInfo>) :
    RecyclerView.Adapter<SomDetailLabelAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_label, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = labels.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(labels.getOrNull(position))
    }

    fun setLabels(labels: List<SomDetailOrder.GetSomDetail.LabelInfo>) {
        this.labels = labels
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding<ItemLabelBinding>()

        fun bind(label: SomDetailOrder.GetSomDetail.LabelInfo?) {
            binding?.root?.run {
                if (label != null) {
                    setLabel(label.flagName)
                    setLabelType(label.flagBg)
                    show()
                } else {
                    gone()
                }
            }
        }
    }
}
