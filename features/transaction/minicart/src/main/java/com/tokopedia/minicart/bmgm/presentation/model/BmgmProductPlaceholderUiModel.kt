package com.tokopedia.minicart.bmgm.presentation.model

import com.tokopedia.purchase_platform.common.feature.bmgm.adapter.BmgmMiniCartAdapterFactory
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 03/08/23.
 */

class BmgmProductPlaceholderUiModel : BmgmMiniCartVisitable {

    override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
        return typeFactory.type(this)
    }
}