package com.tokopedia.play.ui.productsheet.viewholder

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.R
import com.tokopedia.play.data.Section
import com.tokopedia.play.ui.productsheet.adapter.ProductLineAdapter
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.PlayProductSectionUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author by astidhiyaa on 27/01/22
 */
class ProductSectionViewHolder(
    itemView: View, private val listener: Listener
) : BaseViewHolder(itemView) {

    private val tvSectionTitle: TextView = itemView.findViewById(R.id.tv_header_title)
    private val ivBg: ImageView = itemView.findViewById(R.id.iv_bg)
    private val tvTimerInfo: TextView = itemView.findViewById(R.id.tv_header_info)
    private val timerSection: TimerUnifySingle = itemView.findViewById(R.id.section_timer)
    private val rvProducts: RecyclerView = itemView.findViewById(R.id.rv_product)

    private var timerTime = ""

    private val adapter: ProductLineAdapter =
        ProductLineAdapter(object : ProductLineViewHolder.Listener {
            override fun onBuyProduct(product: PlayProductUiModel.Product) {
                listener.onBuyProduct(product)
            }
            override fun onAtcProduct(product: PlayProductUiModel.Product) {
                listener.onATCProduct(product)
            }
            override fun onClickProductCard(product: PlayProductUiModel.Product, position: Int) {
                listener.onClickProductCard(product, position)
            }
        })

    init {
        rvProducts.layoutManager = LinearLayoutManager(itemView.context)
        rvProducts.adapter = adapter
    }

    fun bind(item: PlayProductSectionUiModel.ProductSection) {
        tvSectionTitle.shouldShowWithAction(item.title.isNotEmpty()){
            tvSectionTitle.text = item.title
        }
        tvTimerInfo.text = item.timerInfo

        when (item.type) {
            ProductSectionType.Active -> {
                tvTimerInfo.show()
                timerSection.show()
                timerSection.timerVariant = TimerUnifySingle.VARIANT_MAIN
                setupTimer(item)
            }
            ProductSectionType.Upcoming -> {
                tvTimerInfo.show()
                timerSection.show()
                timerSection.timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE
                setupTimer(item)
            }
            ProductSectionType.Other -> {
                tvTimerInfo.hide()
                timerSection.hide()
            }
        }
        setupBackground(item.background)
        adapter.setItemsAndAnimateChanges(itemList = item.productList)
        if (isProductCountChanged(item.productList.size)) listener.onProductChanged()
    }

    private fun setupBackground(background: Section.Background) {
        if (background.gradientList.isNotEmpty()) {
            try {
                val bgArray = IntArray(background.gradientList.size)
                background.gradientList.forEachIndexed { index, s ->
                    bgArray[index] = android.graphics.Color.parseColor(s)
                }
                val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, bgArray)
                itemView.background = gradient
            } catch (e: Exception) { }
        } else {
            ivBg.loadImage(background.imageUrl)
        }
    }

    private fun setupTimer(item : PlayProductSectionUiModel.ProductSection) {
        if(item.type == ProductSectionType.Active) timerTime = item.endTime else item.startTime

        val convertedServerTime = item.serverTime.toDate(format = DateUtil.YYYY_MM_DD_T_HH_MM_SS)
        val convertedExpiredTime = timerTime.toDate(format = DateUtil.YYYY_MM_DD_T_HH_MM_SS)

        if(!isExpired(serverTime = convertedServerTime, expiredTime = convertedExpiredTime)){
            val dt = DateUtil.getCurrentCalendar().apply {
                val diff = timerTime.toDate(
                    DateUtil.YYYY_MM_DD_T_HH_MM_SS
                ).time - convertedServerTime.time
                val seconds = TimeUnit.MILLISECONDS.toSeconds(diff).toInt()
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff).toInt()
                val hours = TimeUnit.MILLISECONDS.toHours(diff).toInt()
                val days = TimeUnit.MILLISECONDS.toDays(diff).toInt()
                add(Calendar.SECOND, seconds)
                add(Calendar.MINUTE, minutes)
                add(Calendar.HOUR, hours)
                add(Calendar.DAY_OF_MONTH, days)
            }
            timerSection.pause()
            timerSection.targetDate = dt
            timerSection.onFinish = {
                listener.onTimerExpired(product = item)
            }
            timerSection.resume()
        }else{
            tvTimerInfo.hide()
            timerSection.hide()
        }
    }

    private fun isExpired(serverTime: Date, expiredTime: Date): Boolean {
        return serverTime.after(expiredTime)
    }

    private fun isProductCountChanged(productSize: Int): Boolean {
        return adapter.getItems().isNotEmpty() &&
                adapter.getItems().first() is PlayProductUiModel.Product &&
                adapter.itemCount != productSize
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_play_product_section_header
    }

    interface Listener {
        fun onBuyProduct(product: PlayProductUiModel.Product)
        fun onATCProduct(product: PlayProductUiModel.Product)
        fun onClickProductCard(product: PlayProductUiModel.Product, position: Int)
        fun onProductChanged()
        fun onTimerExpired(product: PlayProductSectionUiModel.ProductSection)
    }
}

