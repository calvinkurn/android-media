package com.tokopedia.statistic.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.statistic.view.adapter.factory.ActionMenuAdapterFactory
import com.tokopedia.statistic.view.model.ActionMenuUiModel

/**
 * Created By @ilhamsuaib on 14/02/21
 */

class ActionMenuAdapter(
        pageName: String, userId: String,
        onItemClick: (menu: ActionMenuUiModel) -> Unit
) : BaseAdapter<ActionMenuAdapterFactory>(ActionMenuAdapterFactory(pageName, userId, onItemClick))