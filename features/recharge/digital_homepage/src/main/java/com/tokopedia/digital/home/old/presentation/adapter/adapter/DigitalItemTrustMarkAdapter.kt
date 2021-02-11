package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageSectionModel
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.layout_digital_home_trustmark_item.view.*

class DigitalItemTrustMarkAdapter(val items: List<DigitalHomePageSectionModel.Item>)
    : RecyclerView.Adapter<DigitalItemTrustMarkAdapter.DigitalItemTrustMarkViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemTrustMarkViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemTrustMarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_digital_home_trustmark_item, parent, false)
        return DigitalItemTrustMarkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemTrustMarkViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: DigitalHomePageSectionModel.Item) {
            itemView.trustmark_image.loadImage(element.mediaUrl)
            itemView.trustmark_name.text = element.title
        }

    }
}
