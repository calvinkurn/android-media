package com.tokopedia.notifcenter.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.Visitable

class ViewHolderState(
        val visitable: Visitable<*>,
        val previouslyKnownPosition: Int,
        val payload: Any? = null
)