package com.tokopedia.mvc.presentation.bottomsheet.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.mvc.presentation.bottomsheet.adapter.factory.MenuAdapterFactoryImpl
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel

class MoreMenuAdapter(
    callback: (MoreMenuUiModel) -> Unit
) : BaseAdapter<MenuAdapterFactoryImpl>(MenuAdapterFactoryImpl(callback))
