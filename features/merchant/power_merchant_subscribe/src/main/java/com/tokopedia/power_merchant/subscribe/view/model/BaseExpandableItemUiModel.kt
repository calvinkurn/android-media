package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactory

/**
 * Created By @ilhamsuaib on 04/03/21
 */

interface BaseExpandableItemUiModel : Visitable<ExpandableAdapterFactory> {
    val text: String
}