package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitableDiffUtil
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderOvoDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.OvoViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.OvoWidgetView
import com.tokopedia.home.beranda.presentation.view.listener.*
import com.tokopedia.kotlin.extensions.view.visible
import java.util.concurrent.Executors

class HomeHeaderOvoViewHolder(itemView: View, private val listener: HomeCategoryListener)
: AbstractViewHolder<HomeHeaderOvoDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_header_ovo
    }

    override fun bind(element: HomeHeaderOvoDataModel) {
        if (element.isLogin) {
            renderOvoLayout(element.headerDataModel)
        }
    }

    private fun renderOvoLayout(data: HeaderDataModel) {
        val ovoView = itemView.findViewById<OvoWidgetView>(R.id.view_ovo)
        if (data.isUserLogin) {
            ovoView.visible()
            ovoView.bind(data, listener)
        }

    }







}