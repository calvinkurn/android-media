package com.tokopedia.browse.categoryNavigation.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import kotlinx.android.synthetic.main.item_level_two_child.view.*

class LevelTwoChildAdapter(private val list: List<ChildItem>?) : RecyclerView.Adapter<LevelTwoChildAdapter.ViewHolder>() {

    val viewMap = HashMap<Int, Boolean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_level_two_child, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = list!![position].name

        val marginThirty = holder.itemView.resources.getDimensionPixelOffset(R.dimen.dp_30)
        val marginZero = holder.itemView.resources.getDimensionPixelOffset(R.dimen.dp_0)

        if (list[position].iconImageUrl == null) {
            holder.productImage.setImageResource(R.drawable.ic_see_more)
            holder.productImage.setPadding(marginThirty, marginThirty, marginThirty, marginThirty)
        } else {
            holder.productImage.setPadding(marginZero, marginZero, marginZero, marginZero)
            ImageHandler.loadImage(holder.itemView.context, holder.productImage, list[position].iconImageUrl, R.drawable.loading_page)
        }

        holder.productImage.setOnClickListener {
            CategoryAnalytics.createInstance().eventBannerInsideLevelTwoClick(holder.itemView.context,list[position], position)
            RouteManager.route(holder.productImage.context, list[position].applinks)
        }
        holder.productName.setOnClickListener {
            RouteManager.route(holder.productImage.context, list[position].applinks)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap.containsKey(position)) {
            viewMap[position] = true
            CategoryAnalytics.createInstance().eventBannerInsideLevelTwoView(holder.itemView.context,list?.get(position)!!, position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage = view.product_image
        val productName = view.product_name
    }
}