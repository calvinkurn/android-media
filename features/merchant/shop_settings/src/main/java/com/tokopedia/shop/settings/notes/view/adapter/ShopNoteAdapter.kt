package com.tokopedia.shop.settings.notes.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteFactory

/**
 * Created by hendry on 16/08/18.
 */
class ShopNoteAdapter(baseListAdapterTypeFactory: ShopNoteFactory,
                      onAdapterInteractionListener: BaseListAdapter.OnAdapterInteractionListener<ShopNoteViewModel>)
    : BaseListAdapter<ShopNoteViewModel, ShopNoteFactory>(
        baseListAdapterTypeFactory,
        onAdapterInteractionListener)
