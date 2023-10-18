package com.tokopedia.sellerpersona.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerpersona.view.adapter.factory.OptionAdapterFactoryImpl
import com.tokopedia.sellerpersona.view.adapter.viewholder.BaseOptionViewHolder
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class OptionsAdapter(
    listener: BaseOptionViewHolder.Listener
) : BaseListAdapter<BaseOptionUiModel, OptionAdapterFactoryImpl>(
    OptionAdapterFactoryImpl(listener)
)