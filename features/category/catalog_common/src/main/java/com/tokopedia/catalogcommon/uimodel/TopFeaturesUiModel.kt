package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class TopFeaturesUiModel(
    override val idWidget: String,
    val items: List<ItemTopFeatureUiModel>
) : BaseCatalogUiModel(idWidget) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemTopFeatureUiModel(
        val id: String,
        val icon: String,
        val name: String,
        val backgroundColor: Int = R.color.Unify_N0,
        val textColor: Int = R.color.Unify_NN600,
        val borderColor: Int? = null
    )


    companion object{
        fun dummyTopFeatures() = listOf(
            ItemTopFeatureUiModel(
                "",
                "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                "Lorep Ipsum"
            ),
            ItemTopFeatureUiModel(
                "",
                "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                "Lorep Ipsum"
            ),
            ItemTopFeatureUiModel(
                "",
                "https://images.tokopedia.net/ta/icon/badge/OS-Badge-80.png",
                "Lorep Ipsum"
            )
        )
    }
}
