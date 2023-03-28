package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory

/**
 * Created By : Muhammad Furqan on 08/03/23
 */
class FeedNoContentModel : Visitable<FeedAdapterTypeFactory> {
    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

}
