package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory

data class CatalogContainerDM(
    val name: String = "",
    val type: String = "",
    val containerTitle: String = "",
    val dataList: ArrayList<BaseCatalogLibraryDM>?,
    val orientationRecyclerView: Int = RecyclerView.HORIZONTAL,
    val hasMoreButtonAppLink: String = "",
    val hasMoreButtonEnabled: Boolean = false,
    val isGrid: Boolean = false,
    val columnCount: Int = 0,
    val marginForTitle: Margin = Margin(),
    val marginForRV: Margin = Margin()

) : BaseCatalogLibraryDM {

    override fun type() = type

    override fun type(typeFactory: CatalogHomepageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name() = name

    override fun equalsWith(newData: BaseCatalogLibraryDM): Boolean {
        return newData == this
    }

    override fun getChangePayload(newData: BaseCatalogLibraryDM): Bundle? {
        return null
    }
}
