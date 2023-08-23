package com.tokopedia.officialstore.official.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.Benefit

class BenefitAdapter(private val context: Context, var benefitList: List<Benefit> = ArrayList()) :
        RecyclerView.Adapter<BenefitAdapter.BenefitViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): BenefitViewHolder {
        return BenefitViewHolder(LayoutInflater.from(context).
                inflate(R.layout.widget_official_benefit, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return benefitList.size
    }

    override fun onBindViewHolder(holder: BenefitViewHolder, position: Int) {
        val item = benefitList[position]
        ImageHandler.loadImage(
                context,
                holder.imageView,
                item.iconUrl,
                com.tokopedia.design.R.drawable.ic_loading_image
        )

        holder.textView?.text = item.label

        holder.itemView.setOnClickListener{
            onItemClickListener?.onItemClick(position, item)
        }
    }

    class BenefitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView? = null
        var textView: TextView? = null

        init {
            imageView = itemView.findViewById(R.id.image_icon_benefit)
            textView = itemView.findViewById(R.id.title_benefit)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: Benefit)
    }
}
