package com.tokopedia.layanan_finansial.view.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.Analytics
import com.tokopedia.layanan_finansial.view.Analytics.EVENT_PROMO_CLICK
import com.tokopedia.layanan_finansial.view.Analytics.EVENT_PROMO_VIEW
import com.tokopedia.layanan_finansial.view.Analytics.LAYANAN_FINANSIAL_CATEGORY
import com.tokopedia.layanan_finansial.view.Analytics.LAYANAN_FINANSILA_VIEW_ACTION
import com.tokopedia.layanan_finansial.view.Analytics.LAYANAN_FINANSILA_click_ACTION
import com.tokopedia.layanan_finansial.view.models.LayananListItem
import kotlinx.android.synthetic.main.layanan_card_item.view.*
import java.util.*
import kotlin.collections.HashMap

class LayananAdapter(private val list: List<LayananListItem>) : RecyclerView.Adapter<LayananAdapter.LayananViewHolder>() {


    inner class LayananViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setData(layananListItem: LayananListItem) {
            itemView.apply {
                ImageLoader.LoadImage(icon,layananListItem.iconUrl)
                name.text = layananListItem.name
                category.text = layananListItem.categrory
                desc_1.text = layananListItem.desc1
                if(!layananListItem.desc2.isNullOrEmpty()){
                    desc_1.setLines(1)
                    desc_2.text = layananListItem.desc2
                    desc_2.show()
                } else {
                    desc_1.setLines(2)
                    desc_2.hide()
                }
                if (!layananListItem.cta.isNullOrEmpty()) {
                    cta.show()
                    view.show()
                    arrow.show()
                    cta.text = layananListItem.cta
                } else {
                    cta.hide()
                    view.hide()
                    arrow.hide()
                }
                if(!layananListItem.status.isNullOrEmpty()){
                    status.text = layananListItem.status
                    (status.background as GradientDrawable).setColor(Color.parseColor(layananListItem.statusBackgroundColor))
                    status.setTextColor(Color.parseColor(layananListItem.statusTextColor))
                    status.show()
                } else {
                    status.hide()
                }
                setOnClickListener{
                    RouteManager.route(context, String.format("%s?url=%s",ApplinkConst.WEBVIEW,layananListItem.url))
                    val label = "product: ${layananListItem.name}, status: ${layananListItem.datalayerStatus}"
                    Analytics.sendEcomerceEvent(EVENT_PROMO_CLICK,LAYANAN_FINANSIAL_CATEGORY, LAYANAN_FINANSILA_click_ACTION,label,createEcommerceMap(position = layoutPosition,item =  layananListItem))
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayananViewHolder {
        return LayananViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layanan_card_item,parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LayananViewHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun onViewAttachedToWindow(holder: LayananViewHolder) {
        super.onViewAttachedToWindow(holder)
        val layananListItem = list[holder.layoutPosition]
        if (!layananListItem.isVisited) {
            val label = "product: ${layananListItem.name}, status: ${layananListItem.datalayerStatus}"
            Analytics.sendEcomerceEvent(EVENT_PROMO_VIEW, LAYANAN_FINANSIAL_CATEGORY, LAYANAN_FINANSILA_VIEW_ACTION, label,createEcommerceMap(holder.layoutPosition,item = layananListItem))
            layananListItem.isVisited = true
        }
    }

    private fun createEcommerceMap(position: Int, item: LayananListItem) : Map<String, Any?>{
        val map = mutableMapOf<String,Any?>()
        map["id"] = "${item.name} _ ${item.datalayerStatus}"
        map["name"] = "/layanan finansial"
        map["position"] = position
        map["creative"] = item.name
        map["creative_url"] = item.iconUrl

        val promotions = HashMap<String, List<Map<String, Any?>>>()
        promotions["promotions"] = Arrays.asList<Map<String, Any?>>(map)

        val promoView = HashMap<String, Map<String, List<Map<String, Any?>>>>()
        promoView["promoView"] = promotions
        return promoView
    }
}