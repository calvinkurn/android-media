package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_OVO_VIEWHOLDER
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.kotlin.extensions.view.visible

/**
 * Created by Lukas on 2019-08-20
 */
class OvoViewHolder(itemView: View, val listener: HomeCategoryListener?) : AbstractViewHolder<HeaderDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_ovo_widget
        val NON_LOGIN_LAYOUT = R.layout.layout_ovo_widget
    }

    override fun bind(element: HeaderDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_OVO_VIEWHOLDER)
        val ovoView = itemView.findViewById<OvoWidgetView>(R.id.widget_ovo)
        if (element.isUserLogin) {
            ovoView.visible()
            ovoView.bind(element, listener)
        }
        BenchmarkHelper.endSystraceSection()
    }
}
