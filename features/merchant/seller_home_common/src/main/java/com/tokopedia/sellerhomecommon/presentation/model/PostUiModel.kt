package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.presentation.adapter.ListAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created By @ilhamsuaib on 20/05/20
 */

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