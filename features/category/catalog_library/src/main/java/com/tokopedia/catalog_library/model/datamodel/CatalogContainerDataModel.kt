package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory

data class CatalogContainerDataModel(
    val name: String = "",
    val type: String = "",
    val containerTitle: String = "",
    val dataList: ArrayList<BaseCatalogLibraryDataModel>?,
    val orientationRecyclerView: Int = RecyclerView.HORIZONTAL,
    val hasMoreButtonAppLink: String = "",
    val hasMoreButtonEnabled: Boolean = false,
    val isGrid: Boolean = false,
    val columnCount: Int = 0
) : BaseCatalogLibraryDataModel {

    override fun type() = type

    override fun type(typeFactory: CatalogHomepageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name() = name

    override fun equalsWith(newData: BaseCatalogLibraryDataModel): Boolean {
        return newData == this
    }

    override fun getChangePayload(newData: BaseCatalogLibraryDataModel): Bundle? {
        return null
    }
}
