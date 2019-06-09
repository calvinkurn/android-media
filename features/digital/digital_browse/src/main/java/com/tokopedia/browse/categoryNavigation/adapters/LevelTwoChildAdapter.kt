package com.tokopedia.browse.categoryNavigation.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import kotlinx.android.synthetic.main.item_level_two_child.view.*

class LevelTwoChildAdapter(private val list: List<ChildItem>?) : RecyclerView.Adapter<LevelTwoChildAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_level_two_child, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = list!![position].name

        if(list[position].iconImageUrl == null){

            holder.productImage.setBackgroundResource(R.drawable.ic_see_more)

        }else{
            Glide.with(holder.itemView.context)
                    .load(list[position].iconImageUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .centerCrop()
                    .into(holder.productImage)
        }

        holder.productImage.setOnClickListener {
            RouteManager.route(holder.productImage.context, list[position].applinks)
        }
        holder.productName.setOnClickListener {
            RouteManager.route(holder.productImage.context, list[position].applinks)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val productImage = view.product_image
        val productName = view.product_name


    }
}