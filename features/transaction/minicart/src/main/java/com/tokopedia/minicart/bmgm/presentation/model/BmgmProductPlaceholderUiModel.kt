package com.tokopedia.minicart.bmgm.presentation.model

import com.tokopedia.minicart.bmgm.presentation.adapter.factory.BmgmMiniCartAdapterFactory

/**
 * Created by @ilhamsuaib on 03/08/23.
 */

class BmgmProductPlaceholderUiModel : BaseMiniCartUiModel {

    override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
        return typeFactory.type(this)
    }
}