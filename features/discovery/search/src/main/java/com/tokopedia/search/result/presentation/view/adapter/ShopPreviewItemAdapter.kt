package com.tokopedia.search.result.presentation.view.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.view.adapter.viewholder.shop.ShopPreviewItemViewHolder
import kotlinx.android.synthetic.main.item_preview_shop.view.*

class ShopPreviewItemAdapter(val context : Context,
                             val imgSize : Int) : RecyclerView.Adapter<ShopPreviewItemViewHolder>() {

    private val DEFAULT_CORNER_RADIUS = 4
    private val DEFAULT_STROKE_WIDTH = 2

    private var item: List<String> = arrayListOf()

    fun setData(item : List<String>){
        this.item = item
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPreviewItemViewHolder {
        val view : View = LayoutInflater.from(context).inflate(
            R.layout.item_preview_shop,
            parent,
            false)
        view.img_preview.layoutParams.width = imgSize
        view.img_preview.layoutParams.height = imgSize

        return ShopPreviewItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ShopPreviewItemViewHolder, position: Int) {
        val imgUrl = item[position]
        ImageHandler.loadImageRoundedWithBorder(
            holder.img_preview,
            context,
            imgUrl,
            DEFAULT_CORNER_RADIUS,
            DEFAULT_STROKE_WIDTH,
            ContextCompat.getColor(context, R.color.color_item_preview_border),
            imgSize,
            imgSize
        )
    }

}