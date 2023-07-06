package com.tokopedia.catalog_library.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.CatalogLibraryGridSpacingItemDecoration
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDM
import com.tokopedia.catalog_library.model.datamodel.CatalogContainerDM
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class CatalogContainerItemVH(
    val view: View,
    private val catalogLibraryListener: CatalogLibraryListener
) : CatalogLibraryAbstractViewHolder<CatalogContainerDM>(view) {

    private val title: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.container_titile)
    }

    private val lihatSemuaText: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.lihat_semua_text)
    }

    private val containerRV: RecyclerView by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.container_rv)
    }

    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            catalogLibraryListener
        )
    }
    private val containerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDM> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    companion object {
        val LAYOUT = R.layout.catalog_container_view_item
    }

    override fun bind(element: CatalogContainerDM) {
        renderTitle(element)
        renderLihat(element)
        renderRecyclerView(element)
    }

    private fun renderLihat(element: CatalogContainerDM) {
        if (element.hasMoreButtonEnabled) {
            lihatSemuaText.show()
            lihatSemuaText.setOnClickListener {
                catalogLibraryListener.onLihatSemuaTextClick(element.hasMoreButtonAppLink)
            }
        } else {
            lihatSemuaText.hide()
        }
    }

    private fun renderTitle(element: CatalogContainerDM) {
        title.displayTextOrHide(element.containerTitle)

        val params = LinearLayout.LayoutParams(title.layoutParams)
        params.setMargins(
            element.marginForTitle.start.toPx(),
            element.marginForTitle.top.toPx(),
            element.marginForTitle.end.toPx(),
            element.marginForTitle.bottom.toPx()
        )
        title.layoutParams = params
        title.requestLayout()
    }

    private fun renderRecyclerView(element: CatalogContainerDM) {
        containerRV.apply {
            layoutManager = if (element.isGrid) {
                addItemDecoration(CatalogLibraryGridSpacingItemDecoration(CatalogLihatVH.COLUMN_COUNT, 0.toPx(), false))
                GridLayoutManager(itemView.context, element.columnCount)
            } else {
                LinearLayoutManager(itemView.context, element.orientationRecyclerView, false)
            }
            adapter = containerAdapter
        }
        containerAdapter.submitList(element.dataList)

        if (!element.isGrid) {
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(containerRV.layoutParams)
            params.setMargins(
                element.marginForRV.start.toPx(),
                element.marginForRV.top.toPx(),
                element.marginForRV.end.toPx(),
                element.marginForRV.bottom.toPx()
            )
            containerRV.layoutParams = params
            containerRV.requestLayout()
        }
    }
}
