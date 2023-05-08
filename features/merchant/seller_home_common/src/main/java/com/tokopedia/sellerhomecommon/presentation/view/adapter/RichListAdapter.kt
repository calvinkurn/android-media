package com.tokopedia.sellerhomecommon.presentation.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItemUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.factory.RichListFactoryImpl

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

class RichListAdapter : BaseListAdapter<BaseRichListItemUiModel, RichListFactoryImpl>(
    RichListFactoryImpl()
)