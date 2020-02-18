package com.tokopedia.sellerhome.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhome.view.adapter.ListAdapterTypeFactory

class PostUiModel(
        val title: String = "",
        val appLink: String = "",
        val url: String = "",
        val featuredMediaURL: String = "",
        val subtitle: String = ""
) : Visitable<ListAdapterTypeFactory> {
    override fun type(typeFactory: ListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}