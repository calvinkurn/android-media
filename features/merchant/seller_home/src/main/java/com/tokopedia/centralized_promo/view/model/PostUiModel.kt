package com.tokopedia.centralized_promo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapterTypeFactory

data class PostListUiModel(
        val posts: List<PostUiModel>
): BaseUiModel()

data class PostUiModel(
        val title: String,
        val applink: String,
        val url: String,
        val featuredMediaUrl: String,
        val subtitle: String
): Visitable<CentralizedPromoAdapterTypeFactory> {
    override fun type(typeFactory: CentralizedPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}