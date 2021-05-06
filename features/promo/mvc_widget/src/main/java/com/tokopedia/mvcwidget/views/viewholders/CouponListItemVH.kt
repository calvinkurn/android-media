package com.tokopedia.mvcwidget.views.viewholders

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.mvcwidget.MvcCouponListItem
import com.tokopedia.mvcwidget.R
import com.tokopedia.unifyprinciples.Typography

class CouponListItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val rvImage: RecyclerView = itemView.findViewById(R.id.rvImage)
    val tv1: Typography = itemView.findViewById(R.id.tv1)
    val tv2: Typography = itemView.findViewById(R.id.tv2)
    val tv3: Typography = itemView.findViewById(R.id.tv3)
    val divider: View = itemView.findViewById(R.id.divider)

    init {
        rvImage.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setData(data: MvcCouponListItem) {
        tv1.text = data.title1
        tv2.text = data.title2
        tv3.text = data.title3

        toggleView(data.title2,tv2)
        toggleView(data.title3,tv3)

        if (!data.urlList.isNullOrEmpty()) {
            rvImage.adapter = ImageAdapter(data.urlList)
            (rvImage.adapter as ImageAdapter).notifyDataSetChanged()
        }
    }

    fun toggleView(text:String?,view:View){
        if(text.isNullOrEmpty()){
            view.visibility = View.GONE
        }else{
            view.visibility = View.VISIBLE
        }
    }

    fun toggleView(text:SpannableString?,view:View){
        if(text.isNullOrEmpty()){
            view.visibility = View.GONE
        }else{
            view.visibility = View.VISIBLE
        }
    }

    class ImageAdapter(val urlList: List<String?>) : RecyclerView.Adapter<ListItemImageVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemImageVH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.mvc_list_item_images, parent, false)
            return ListItemImageVH(v)
        }

        override fun getItemCount() = urlList.size

        override fun onBindViewHolder(holder: ListItemImageVH, position: Int) {
            if (!urlList[position].isNullOrEmpty()) {
                Glide.with(holder.image)
                        .load(urlList[position])
                        .dontAnimate()
                        .into(holder.image)
            }
        }

    }

    class ListItemImageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: AppCompatImageView = itemView.findViewById(R.id.image)
    }
}