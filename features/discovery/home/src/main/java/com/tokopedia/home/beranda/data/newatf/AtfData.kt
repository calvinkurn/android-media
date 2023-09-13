package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class AtfData(
    val atfMetadata: AtfMetadata,
    val visitable: Visitable<*>
)
