package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.view.View
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.popularkeyword.PopularKeywordAdapter
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by yoasfs on 2020-02-18
 */

class PopularKeywordViewHolder (val view: View,
                                val homeCategoryListener: HomeCategoryListener,
                                val popularKeywordListener: PopularKeywordListener)
    : AbstractViewHolder<PopularKeywordListViewModel>(view) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_popular_keyword
    }

    lateinit var adapter: PopularKeywordAdapter
    var channelTitle: Typography? = null
    var tvReload: Typography? = null
    var ivReload: SquareImageView? = null


    override fun bind(element: PopularKeywordListViewModel) {
        initStub(element)
        initAdapter(element)
    }

    override fun bind(element: PopularKeywordListViewModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        bind(element)
    }

    private fun initAdapter(element: PopularKeywordListViewModel) {
        adapter = PopularKeywordAdapter(element.popularKeywordList, popularKeywordListener, element.channel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_popular_keyword)
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.adapter = adapter
    }

    private fun initStub(element: PopularKeywordListViewModel) {
        try {
            val channelTitleStub: View? = itemView.findViewById(R.id.channel_title)
            val tvReloadStub: View? = itemView.findViewById(R.id.tv_reload)
            val ivReloadStub: View? = itemView.findViewById(R.id.iv_reload)
            channelTitleStub?.let {
                if (element.channel.header.name.isNotEmpty()) {
                    it.visibility = View.VISIBLE
                    channelTitle = if (channelTitleStub is ViewStub &&
                            !isViewStubHasBeenInflated(channelTitleStub)) {
                        val stubChannelView = channelTitleStub.inflate()
                        stubChannelView?.findViewById(R.id.channel_title)
                    } else {
                        itemView.findViewById(R.id.channel_title)
                    }
                    channelTitle?.text = element.channel.header.name
                    channelTitle?.visibility = View.VISIBLE
                    channelTitle?.setTextColor(
                            if(element.channel.header.textColor.isNotEmpty()) Color.parseColor(element.channel.header.textColor)
                            else ContextCompat.getColor(view.context, R.color.Neutral_N700)
                    )
                } else {
                    it.visibility = View.GONE
                }
            }
            tvReloadStub?.let {
                it.visibility = View.VISIBLE
                tvReload = if (tvReloadStub is ViewStub &&
                        !isViewStubHasBeenInflated(tvReloadStub)) {
                    val stubChannelView = tvReloadStub.inflate()
                    stubChannelView?.findViewById(R.id.tv_reload)
                } else {
                    itemView.findViewById(R.id.tv_reload)
                }
                tvReload?.setOnClickListener(reloadClickListener(element))
            }
            ivReloadStub?.let {
                it.visibility = View.VISIBLE
                ivReload = if (ivReloadStub is ViewStub &&
                        !isViewStubHasBeenInflated(ivReloadStub)) {
                    val stubChannelView = ivReloadStub.inflate()
                    stubChannelView?.findViewById(R.id.iv_reload)
                } else {
                    itemView.findViewById(R.id.iv_reload)
                }
                ivReload?.setOnClickListener(reloadClickListener(element))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }

    private fun reloadClickListener(element: PopularKeywordListViewModel): View.OnClickListener {
        return View.OnClickListener {
            popularKeywordListener.onPopularKeywordSectionReloadClicked(element.position, element.channel)
        }
    }

    interface PopularKeywordListener {
        fun onPopularKeywordSectionReloadClicked(position: Int, channel: DynamicHomeChannel.Channels)
        fun onPopularKeywordItemClicked(applink: String, channel: DynamicHomeChannel.Channels, position: Int, keyword: String)
        fun onPopularKeywordItemImpressed(channel: DynamicHomeChannel.Channels, position: Int, keyword: String)
    }
}