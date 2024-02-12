package com.tokopedia.buy_more_get_more.minicart.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory.BmgmMiniCartAdapterFactory

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

interface BaseMiniCartVisitable : Visitable<BmgmMiniCartAdapterFactory> {

    fun getItemId(): String
}