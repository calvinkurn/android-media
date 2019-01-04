package com.tokopedia.feedcomponent.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by milhamj on 04/01/19.
 */
data class DynamicFeedsDomainModel (
        val postList: MutableList<Visitable<*>> = ArrayList(),
        val cursor: String = ""
)