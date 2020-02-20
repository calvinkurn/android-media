package com.tokopedia.browse.categoryNavigation.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.item_level_two_child.view.*

class LevelTwoChildAdapter(private val list: List<ChildItem>?,
                           private val trackingQueue: TrackingQueue?) : RecyclerView.Adapter<LevelTwoChildAdapter.ViewHolder>() {

    val viewMap = HashMap<Int, Boolean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_level_two_child, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.let {list->
            holder.itemView.addOnImpressionListener(list[position], object: ViewHintListener {
                override fun onViewHint() {
                    if (!viewMap.containsKey(position) && list.size > position) {
                        viewMap[position] = true
                        trackingQueue?.let {trackingQueue->
                            CategoryAnalytics.createInstance().eventBannerInsideLevelTwoView(trackingQueue, list[position], position)
                        }
                    }
                }
            })
        }

        holder.productName.text = list!![position].name

        val marginThirty = holder.itemView.resources.getDimensionPixelOffset(R.dimen.dp_10)
        val marginZero = holder.itemView.resources.getDimensionPixelOffset(R.dimen.dp_0)

        if (list[position].iconImageUrl == null) {
            holder.productImage.setImageResource(R.drawable.ic_see_more)
            holder.productImage.setPadding(marginThirty, marginThirty, marginThirty, marginThirty)
        } else {
            holder.productImage.setPadding(marginZero, marginZero, marginZero, marginZero)
            ImageHandler.loadImage(holder.itemView.context, holder.productImage, list[position].iconImageUrl, R.drawable.loading_page)
        }

        holder.productImage.setOnClickListener {
            CategoryAnalytics.createInstance().eventBannerInsideLevelTwoClick(list[position], position)
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
