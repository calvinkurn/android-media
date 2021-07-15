package com.tokopedia.power_merchant.subscribe.view.adapter

import com.tokopedia.power_merchant.subscribe.view.model.ExpandableItemUiModel
import com.tokopedia.power_merchant.subscribe.view.model.ExpandableSectionUiModel

/**
 * Created By @ilhamsuaib on 04/03/21
 */

interface ExpandableAdapterFactory {

    fun type(model: ExpandableSectionUiModel): Int

    fun type(model: ExpandableItemUiModel): Int
}