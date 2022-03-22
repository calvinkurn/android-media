package com.tokopedia.home_component.viewholders.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.FeaturedBrandListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.FeaturedBrandTabletConfiguration
import com.tokopedia.home_component.util.loadImageNoRounded
import com.tokopedia.home_component.visitable.FeaturedBrandDataModel
import com.tokopedia.home_component.visitable.Lego4AutoItem
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by yoasfs on 31/05/21
 */
class FeaturedBrandAdapter(
    private val listener: FeaturedBrandListener?,
    private val positionInWidget: Int,
    private val isCacheData: Boolean
) : RecyclerView.Adapter<FeaturedBrandAdapter.Holder>() {

    companion object {
        private const val MAX_ITEM = 4
        private const val ROUNDED_12F = 12F
        private const val PRODUCT_IMAGE_CORNER = 0
        private const val DESC_MAXIMUM_CHAR = 12
    }

    private lateinit var dataModel: FeaturedBrandDataModel
    private var itemList: MutableList<Lego4AutoItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layout = FeaturedBrandTabletConfiguration.getLayout(parent.context)
        return Holder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(
            itemList[position],
            positionInWidget,
            listener,
            dataModel.channelModel,
            isCacheData
        )
    }

    fun addData(dataModel: FeaturedBrandDataModel) {
        this.dataModel = dataModel
        itemList.clear()
        dataModel.channelModel.channelGrids.forEachIndexed { index, data ->
            if (index < MAX_ITEM) {
                itemList.add(Lego4AutoItem(grid = data))
            }
        }
    }

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        private val itemLayout: CardUnify = v.findViewById(R.id.featured_brand_layout)
        private val itemImage: ImageUnify = v.findViewById(R.id.featured_brand_image)
        private val itemLogo: ImageUnify = v.findViewById(R.id.featured_brand_logo)
        private val itemDesc: Typography = v.findViewById(R.id.featured_brand_desc)
        private val itemValue: Typography = v.findViewById(R.id.featured_brand_value)

        fun bind(
            item: Lego4AutoItem,
            parentPosition: Int,
            listener: FeaturedBrandListener?,
            channelModel: ChannelModel,
            isCacheData: Boolean
        ) {
            itemImage.apply {
                cornerRadius = PRODUCT_IMAGE_CORNER
                loadImageNoRounded(item.grid.productImageUrl, R.drawable.placeholder_grey)
            }
            if (item.grid.imageUrl.isNotEmpty()) {
                itemLogo.loadImageRounded(item.grid.imageUrl, ROUNDED_12F) {
                    setErrorDrawable(R.drawable.featured_brand_border_image_shimmer)
                    setPlaceHolder(R.drawable.featured_brand_border_image_shimmer)
                }
            }
            if (item.grid.benefit.value.isNotEmpty() && item.grid.benefit.type.isNotEmpty()) {
                itemDesc.text = item.grid.benefit.type
                itemValue.text = item.grid.benefit.value
                itemValue.show()
            } else {
                itemDesc.text = item.grid.benefit.type.ifEmpty {
                    item.grid.benefit.value
                }.apply {
                    if (length > DESC_MAXIMUM_CHAR) StringBuilder(this).apply { insert(DESC_MAXIMUM_CHAR, "\n") }.toString()
                }
                itemValue.hide()
            }
            if (item.grid.textColor.isNotEmpty()) {
                itemDesc.setTextColor(Color.parseColor(item.grid.textColor))
                itemValue.setTextColor(Color.parseColor(item.grid.textColor))
            }
            itemLayout.addOnImpressionListener(item.impressHolder) {
                if (!isCacheData) {
                    listener?.onLegoItemImpressed(
                        channelModel,
                        item.grid,
                        adapterPosition,
                        parentPosition
                    )
                }
            }
            itemLayout.setOnClickListener {
                listener?.onLegoItemClicked(
                    channelModel,
                    item.grid,
                    adapterPosition,
                    parentPosition,
                    item.grid.applink
                )
            }
            itemLogo.setOnClickListener {
                listener?.onLegoItemClicked(
                    channelModel,
                    item.grid,
                    adapterPosition,
                    parentPosition,
                    item.grid.applink
                )
            }
            itemImage.setOnClickListener {
                listener?.onLegoItemClicked(
                    channelModel,
                    item.grid,
                    adapterPosition,
                    parentPosition,
                    item.grid.applink
                )
            }
        }
    }
}