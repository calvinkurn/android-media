package com.tokopedia.scp_rewards_widgets.cabinetHeader

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class CabinetHeader(
    val title: String? = null,
    val subTitle: String? = null,
    val background: String? = null,
    val backgroundColor: String? = null,
    val textColor: String? = null,
): Visitable<CabinetHeaderViewTypeFactory> {
    override fun type(typeFactory: CabinetHeaderViewTypeFactory): Int = typeFactory.type(this)
}
