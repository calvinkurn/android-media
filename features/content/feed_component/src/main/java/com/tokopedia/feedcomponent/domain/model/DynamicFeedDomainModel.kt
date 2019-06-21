package com.tokopedia.feedcomponent.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by milhamj on 04/01/19.
 */
data class DynamicFeedDomainModel (
        var postList: MutableList<Visitable<*>> = ArrayList(),
        var cursor: String = "",
        var hasNext: Boolean = false
)