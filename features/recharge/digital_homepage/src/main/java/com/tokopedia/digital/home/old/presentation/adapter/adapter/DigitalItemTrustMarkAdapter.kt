package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeTrustmarkItemBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageSectionModel
import com.tokopedia.kotlin.extensions.view.loadImage

class DigitalItemTrustMarkAdapter(val items: List<DigitalHomePageSectionModel.Item>)
    : RecyclerView.Adapter<DigitalItemTrustMarkAdapter.DigitalItemTrustMarkViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemTrustMarkViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemTrustMarkViewHolder {
        val view = LayoutDigitalHomeTrustmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigitalItemTrustMarkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemTrustMarkViewHolder(val binding: LayoutDigitalHomeTrustmarkItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: DigitalHomePageSectionModel.Item) {
            binding.trustmarkImage.loadImage(element.mediaUrl)
            binding.trustmarkName.text = element.title
        }

    }
}
