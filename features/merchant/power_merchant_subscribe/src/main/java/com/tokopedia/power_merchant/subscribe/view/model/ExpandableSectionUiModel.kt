package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactory

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class ExpandableSectionUiModel(
        override val text: String
) : BaseExpandableItemUiModel {

    override fun type(typeFactory: ExpandableAdapterFactory): Int {
        return typeFactory.type(this)
    }
}