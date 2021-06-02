package com.tokopedia.home_component.viewholders.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.FeaturedBrandListener
import com.tokopedia.home_component.listener.Lego4AutoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.visitable.FeaturedBrandDataModel
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.home_component.visitable.Lego4AutoItem
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by yoasfs on 31/05/21
 */
class FeaturedBrandAdapter(
        private val listener: FeaturedBrandListener?,
        private val positionInWidget: Int,
        private val isCacheData: Boolean
) : RecyclerView.Adapter<FeaturedBrandAdapter.Holder>() {

    private lateinit var dataModel: FeaturedBrandDataModel
    private var itemList: MutableList<Lego4AutoItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.layout_featured_brand_item, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position], positionInWidget, listener, dataModel.channelModel, isCacheData)
    }

    fun addData(dataModel: FeaturedBrandDataModel) {
        this.dataModel = dataModel
        itemList.clear()
        dataModel.channelModel.channelGrids.forEachIndexed { index, data ->
            if (index < 4) {
                itemList.add(Lego4AutoItem(grid = data))
            }
        }
    }

    class Holder(v: View): RecyclerView.ViewHolder(v) {
        private val itemLayout: ConstraintLayout = v.findViewById(R.id.item_lego_auto)
        private val itemImage: ImageView = v.findViewById(R.id.item_image)
        private val itemLogo: ImageView = v.findViewById(R.id.item_logo)
        private val itemDesc: Typography = v.findViewById(R.id.item_desc)
        private val template = "%s %s"


        fun bind(item: Lego4AutoItem, parentPosition: Int, listener: FeaturedBrandListener?, channelModel: ChannelModel, isCacheData: Boolean) {
            itemImage.loadImageRounded(item.grid.productImageUrl, 10f)
            if (item.grid.imageUrl.isNotEmpty()) {
                itemLogo.loadImageRounded(item.grid.imageUrl, 10f)
            }
            itemDesc.text = constructBoldFont(item.grid.benefit.type, item.grid.benefit.value)
            if (item.grid.textColor.isNotEmpty()) {
                itemDesc.setTextColor(Color.parseColor(item.grid.textColor))
            }
            if (item.grid.backColor.isNotEmpty()) {
                itemLayout.setGradientBackground(arrayListOf(item.grid.backColor))
            }
            itemLayout.addOnImpressionListener(item.impressHolder){
                if (!isCacheData) {
                    listener?.onLegoItemImpressed(channelModel, item.grid, adapterPosition, parentPosition)
                }
            }
            itemLayout.setOnClickListener {
                listener?.onLegoItemClicked(channelModel, item.grid, adapterPosition, parentPosition)
            }
        }

        private fun constructBoldFont(type: String, value : String): SpannableStringBuilder {
            val text = SpannableStringBuilder(String.format(template, type, value))
            val startIndexBold = text.indexOf(value)
            val endIndexBold = startIndexBold + value.length
            val spanStyle = StyleSpan(Typeface.BOLD)
            text.setSpan(spanStyle, startIndexBold, endIndexBold, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            return text
        }
    }
}