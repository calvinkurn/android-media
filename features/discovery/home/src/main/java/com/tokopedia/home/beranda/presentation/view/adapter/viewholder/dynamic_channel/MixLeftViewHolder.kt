package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mixleft.model.MixLeftAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.EmptyDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.SeeMorePdpDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory.FlashSaleCardViewTypeFactoryImpl
import com.tokopedia.productcard.v2.BlankSpaceConfig

/**
 * @author by yoasfs on 2020-03-05
 */
class MixLeftViewHolder (itemView: View, val homeCategoryListener: HomeCategoryListener,
                         val countDownListener: CountDownView.CountDownListener,
                         val flashSaleCardListener: FlashSaleCardListener,
                         private val parentRecycledViewPool: RecyclerView.RecycledViewPool)
    : DynamicChannelViewHolder(itemView, homeCategoryListener, countDownListener) {

    private lateinit var adapter: MixLeftAdapter

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.rv_product)
    private val image: ImageView = itemView.findViewById(R.id.parallax_image)
    private val parallaxBackground: View = itemView.findViewById(R.id.parallax_background)
    private val parallaxView: View = itemView.findViewById(R.id.parallax_view)

    private lateinit var layoutManager: LinearLayoutManager


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_mix_left
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        setupBackground(channel)
        setupList(channel)
        setSnapEffect()
        setParallaxEffect()
    }

    override fun getViewHolderClassName(): String {
        return ""
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        
    }

    private fun setupBackground(channel: DynamicHomeChannel.Channels) {
        parallaxBackground.setBackgroundColor(Color.parseColor(channel.header.backColor))
        image.loadImage("https://upload.wikimedia.org/wikipedia/commons/6/6b/Reynhard_Sinaga.jpg")
    }

    private fun setupList(channel: DynamicHomeChannel.Channels) {
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val typeFactoryImpl = FlashSaleCardViewTypeFactoryImpl(flashSaleCardListener)
        val listData = mutableListOf<Visitable<*>>()
        listData.add(EmptyDataModel())
        listData.addAll(convertDataToProductData(channel))
        listData.add(SeeMorePdpDataModel())
        adapter = MixLeftAdapter(listData,typeFactoryImpl, homeCategoryListener)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setSnapEffect() {
        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(recyclerView)
    }

    private fun setParallaxEffect() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    val firstView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())
                    firstView?.let {
                        val distanceFromLeft = it.left
                        val translateX = distanceFromLeft * 0.2f
                        parallaxView.translationX = translateX

                        if (distanceFromLeft <= 0) {
                            val itemSize = it.width.toFloat()
                            val alpha = (Math.abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                            image.alpha = 1 - alpha
                        }
                    }
                }
            }
        })
    }

    private fun convertDataToProductData(channel: DynamicHomeChannel.Channels): List<Visitable<*>> {
        val list :MutableList<Visitable<*>> = mutableListOf()
        for (grid in channel.grids) {
            list.add(FlashSaleDataModel(
                    grid,
                    blankSpaceConfig = BlankSpaceConfig()
            ))
        }
        return list
    }

}
