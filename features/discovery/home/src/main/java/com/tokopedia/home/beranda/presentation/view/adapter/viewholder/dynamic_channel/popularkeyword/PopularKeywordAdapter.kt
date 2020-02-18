package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.popularkeyword

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordViewModel
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by yoasfs on 2020-02-18
 */

class PopularKeywordAdapter(val dataList: List<PopularKeywordViewModel>)
    : RecyclerView.Adapter<PopularKeywordAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.layout_popular_keyword_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(dataList[position])
    }

    class Holder(view: View): RecyclerView.ViewHolder(view) {

        val ivImage = view.findViewById<SquareImageView>(R.id.iv_product)
        val tvProduct = view.findViewById<Typography>(R.id.tv_product)
        val tvCount = view.findViewById<Typography>(R.id.tv_count)

        fun bind(data : PopularKeywordViewModel) {
            ivImage.loadImage(data.imageUrl)
            tvProduct.text = data.title
            tvCount.text = data.productCount
        }
    }
}