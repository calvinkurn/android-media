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

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BenefitViewHolder {
        return BenefitViewHolder(LayoutInflater.from(context).
                inflate(R.layout.widget_official_benefit, p0, false))
    }

    override fun getItemCount(): Int {
        return benefitList.size
    }

    override fun onBindViewHolder(p0: BenefitViewHolder, p1: Int) {
        val item = benefitList[p1]
        ImageHandler.loadImage(
                context,
                p0.imageView,
                item.iconUrl,
                R.drawable.ic_loading_image
        )

        p0.textView?.text = item.label
    }

    class BenefitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView? = null
        var textView: TextView? = null

        init {
            imageView = itemView.findViewById(R.id.image_icon_benefit)
            textView = itemView.findViewById(R.id.title_benefit)
        }
    }
}