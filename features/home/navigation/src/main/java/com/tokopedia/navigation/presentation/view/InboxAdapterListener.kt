package com.tokopedia.navigation.presentation.view

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Author errysuprayogi on 13,March,2019
 */
interface InboxAdapterListener {
    fun onItemClickListener(item: Visitable<*>, position: Int)
}
