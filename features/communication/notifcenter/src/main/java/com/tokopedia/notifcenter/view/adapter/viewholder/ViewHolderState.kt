package com.tokopedia.notifcenter.view.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.Visitable

class ViewHolderState(
        val visitable: Visitable<*>,
        val previouslyKnownPosition: Int,
        val payload: Any? = null
)
