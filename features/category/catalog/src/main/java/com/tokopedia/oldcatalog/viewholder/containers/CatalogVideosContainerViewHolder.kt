package com.tokopedia.oldcatalog.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.adapter.components.CatalogVideosAdapter
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.datamodel.CatalogVideoDataModel

class CatalogVideosContainerViewHolder(private val view : View,
                                       private val catalogDetailListener: CatalogDetailListener): AbstractViewHolder<CatalogVideoDataModel>(view) {

    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    companion object {
        val LAYOUT = R.layout.item_catalog_videos_container
    }

    override fun bind(element: CatalogVideoDataModel) {
        val videosRV = view.findViewById<RecyclerView>(R.id.catalog_videos_rv)
        videosRV.layoutManager = layoutManager
        videosRV.adapter = CatalogVideosAdapter(element.videosList, catalogDetailListener)
    }
}
