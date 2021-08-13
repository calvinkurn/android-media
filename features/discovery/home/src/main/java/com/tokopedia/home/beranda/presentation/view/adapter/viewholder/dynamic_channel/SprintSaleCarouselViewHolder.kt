package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.config.GlobalConfig
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.*
import com.tokopedia.home.beranda.listener.GridItemClickListener
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SpacingItemDecoration.Companion.HORIZONTAL
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * No further development for this viewholder
 * Backend possibly return this layout for version android  >= 2.19
 */
@Deprecated("")
class SprintSaleCarouselViewHolder(itemView: View, private val listener: HomeCategoryListener) : AbstractViewHolder<DynamicChannelDataModel>(itemView), GridItemClickListener {
    private val container: RelativeLayout
    private val recyclerView: RecyclerView
    private val itemAdapter: ItemAdapter
    private val context: Context = itemView.context
    private val title: Typography
    private val seeMore: TextView
    private val headerBg: ImageView
    private val countDownView: TimerUnifySingle
    private var channels: DynamicHomeChannel.Channels? = null
    override fun onGridItemClick(pos: Int, grid: DynamicHomeChannel.Grid) {
        val evenMap = channels!!.getEnhanceClickSprintSaleCarouselHomePage(pos, countDownView.targetDate?.toStringFormat(), grid.label)
        HomePageTracking.eventEnhancedClickSprintSaleProduct(
                evenMap)
        listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid))
        HomeTrackingUtils.homeSprintSaleClick(context,
                pos + 1, channels, grid, DynamicLinkHelper.getActionLink(grid))
    }

    override fun bind(element: DynamicChannelDataModel) {
        try {
            channels = element.channel
            title.text = channels!!.header.name
            Glide.with(context).load(channels!!.header.backImage).into(headerBg)
            channels?.grids?.let {
                itemAdapter.setList(it)
            }
            itemAdapter.setGridItemClickListener(this)
            HomeTrackingUtils.homeSprintSaleImpression(context,
                    channels!!.grids, channels!!.type)
            val expiredTime = DateHelper.getExpiredTime(channels!!.header.expiredTime)
            if (!DateHelper.isExpired(element.serverTimeOffset, expiredTime)) {
                countDownView.visibility = View.VISIBLE
            } else {
                countDownView.visibility = View.GONE
            }
            val currentDate = Date()
            val currentMillisecond: Long = currentDate.time + element.serverTimeOffset
            val serverTime = Date()
            serverTime.time = currentMillisecond
            val timeDiff = serverTime.getTimeDiff(expiredTime)
            countDownView.targetDate = timeDiff
            countDownView.onFinish = {
                listener.updateExpiredChannel(element, adapterPosition)
            }
            if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channels!!.header))) {
                seeMore.visibility = View.VISIBLE
            } else {
                seeMore.visibility = View.GONE
            }
            seeMore.setOnClickListener { onClickSeeAll() }
            val color = channels!!.header.backColor
            if (color.isNotEmpty()) {
                container.setBackgroundColor(Color.parseColor(color))
            }
        } catch (e: Exception) {
            if (!GlobalConfig.DEBUG) {
                FirebaseCrashlytics.getInstance().log("E/" + TAG + ":" + e.localizedMessage)
            }
        }
    }

    private fun onClickSeeAll() {
        listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channels!!.header))
        HomePageTracking.eventClickSeeAllProductSprintBackground(channels!!.id)
        HomeTrackingUtils.homeSprintSaleViewAll(context,
                DynamicLinkHelper.getActionLink(channels!!.header))
    }

    private class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>() {
        private var list: Array<DynamicHomeChannel.Grid> = arrayOf()
        private var gridItemClickListener: GridItemClickListener? = null
        fun setGridItemClickListener(gridItemClickListener: GridItemClickListener?) {
            this.gridItemClickListener = gridItemClickListener
        }

        fun setList(list: Array<DynamicHomeChannel.Grid>) {
            this.list = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_sprint_product_item, parent, false)
            return ItemViewHolder(v)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            try {
                val grid = list[position]
                ImageHandler.loadImageThumbs(holder.getContext(), holder.imageView, grid.imageUrl)
                holder.price1.text = grid.slashedPrice
                holder.price1.paintFlags = holder.price1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.price2.text = grid.price
                holder.stockStatus.text = grid.label
                if (grid.discount.isEmpty()) {
                    holder.channelDiscount.visibility = View.GONE
                } else {
                    holder.channelDiscount.visibility = View.VISIBLE
                    holder.channelDiscount.text = grid.discount
                }
                if (grid.label.equals(holder.getContext().getString(R.string.hampir_habis), ignoreCase = true)) {
                    holder.stockStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_flame, 0, 0, 0)
                } else {
                    holder.stockStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
                if (grid.soldPercentage >= 100) {
                    holder.stockProgress.visibility = View.INVISIBLE
                    holder.channelDiscount.isEnabled = false
                } else {
                    holder.stockProgress.progress = grid.soldPercentage
                    holder.stockProgress.visibility = View.VISIBLE
                    holder.channelDiscount.isEnabled = true
                }
                if (gridItemClickListener != null) {
                    holder.countainer.setOnClickListener { gridItemClickListener!!.onGridItemClick(position, grid) }
                } else {
                    holder.countainer.setOnClickListener(null)
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().log("E/" + TAG + ":" + e.localizedMessage)
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }

    private class ItemViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var countainer: CardView = view.findViewById(R.id.container)
        var imageView: ImageView = view.findViewById(R.id.image)
        var channelDiscount: TextView = view.findViewById(R.id.channel_discount)
        var price1: TextView = view.findViewById(R.id.price1)
        var price2: TextView = view.findViewById(R.id.price2)
        var stockStatus: Typography = view.findViewById(R.id.stock_status)
        var stockProgress: ProgressBar = view.findViewById(R.id.stock_progress)
        fun getContext(): Context {
            return view.context
        }

        init {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setBackgroundTintList(stockProgress,
                        ColorStateList.valueOf(ContextCompat.getColor(view.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_N100)))
            } else {
                stockProgress.backgroundTintList = ColorStateList.valueOf(ContextCompat
                        .getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_sprint_card_item
        private val TAG = SprintSaleCarouselViewHolder::class.java.simpleName
        const val ATTRIBUTION = "attribution"
        fun convertDpToPixel(dp: Float, context: Context): Int {
            val r = context.resources
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
        }
    }

    init {
        itemAdapter = ItemAdapter()
        countDownView = itemView.findViewById(R.id.count_down)
        container = itemView.findViewById(R.id.container)
        headerBg = itemView.findViewById(R.id.header_bg)
        title = itemView.findViewById(R.id.channel_title)
        title.isSelected = true
        seeMore = itemView.findViewById(R.id.see_all_button)
        recyclerView = itemView.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = itemAdapter
        recyclerView.addItemDecoration(SpacingItemDecoration(convertDpToPixel(16f, context), HORIZONTAL))
        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(recyclerView)
    }
}