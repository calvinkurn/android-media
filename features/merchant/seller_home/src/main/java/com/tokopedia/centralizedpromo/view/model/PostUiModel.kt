package com.tokopedia.centralizedpromo.view.model

import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class PostListUiModel(
        override val items: List<PostUiModel>,
        override val errorMessage: String
) : BaseUiListModel<PostUiModel>

data class PostUiModel(
        val title: String,
        val applink: String,
        val url: String,
        val featuredMediaUrl: String = "",
        val subtitle: String = ""
) : BaseUiListItemModel<CentralizedPromoAdapterTypeFactory> {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: CentralizedPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}