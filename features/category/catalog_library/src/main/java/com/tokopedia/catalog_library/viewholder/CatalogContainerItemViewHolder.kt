package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogContainerDataModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class CatalogContainerItemViewHolder(val view: View,  private val catalogLibraryListener: CatalogLibraryListener): AbstractViewHolder<CatalogContainerDataModel>(view) {

    private val title = view.findViewById<Typography>(R.id.container_titile)
    private val lihatSemuaText = view.findViewById<Typography>(R.id.lihat_semua_text)
    private val containerRV = view.findViewById<RecyclerView>(R.id.container_rv)

    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            catalogLibraryListener
        )
    }
    private val containerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDataModel> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    companion object {
        val LAYOUT = R.layout.catalog_container_view_item
    }

    override fun bind(element: CatalogContainerDataModel) {
        renderTitle(element.containerTitle)
        renderLihat(element)
        renderRecyclerView(element)
    }

    private fun renderLihat(element: CatalogContainerDataModel) {
        if(element.hasMoreButtonEnabled){
            lihatSemuaText.show()
            lihatSemuaText.setOnClickListener {
                catalogLibraryListener.onLihatSemuaTextClick(element.hasMoreButtonAppLink)
            }
        }else {
            lihatSemuaText.hide()
        }
    }

    private fun renderTitle(containerTitle: String) {
        title.displayTextOrHide(containerTitle)
    }

    private fun renderRecyclerView(element: CatalogContainerDataModel) {
        containerRV?.apply {
            layoutManager = if(element.isGrid){
                GridLayoutManager(view.context,element.columnCount)
            }else {
                LinearLayoutManager(view.context, element.orientationRecyclerView , false)
            }
            adapter = containerAdapter
        }
        containerAdapter.submitList(element.dataList)
    }
}
