package com.tokopedia.sellerhomecommon.presentation.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.factory.RichListFactoryImpl

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

class RichListAdapter(
    listener: Listener
) : BaseListAdapter<BaseRichListItem, RichListFactoryImpl>(
    RichListFactoryImpl(listener)
) {
    interface Listener {
        fun setOnCtaClicked(appLink: String)
        fun setOnTooltipClicked(tooltip: TooltipUiModel)
        fun setOnReloadClicked()
    }
}