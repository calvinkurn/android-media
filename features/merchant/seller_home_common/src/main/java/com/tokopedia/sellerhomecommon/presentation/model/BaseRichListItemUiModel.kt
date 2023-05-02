package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomecommon.presentation.view.adapter.factory.RichListFactory

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

class BaseRichListItemUiModel(

) : Visitable<RichListFactory> {
    override fun type(typeFactory: RichListFactory): Int {
        return typeFactory.type(this)
    }
}