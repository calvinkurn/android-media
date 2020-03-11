package com.tokopedia.centralizedpromo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory

data class PostListUiModel(
        override val items: List<PostUiModel>,
        override val errorMessage: String
) : BaseUiListModel<PostUiModel>

data class PostUiModel(
        val title: String,
        val applink: String,
        val url: String,
        val featuredMediaUrl: String,
        val subtitle: String
) : Visitable<CentralizedPromoAdapterTypeFactory> {
    override fun type(typeFactory: CentralizedPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}