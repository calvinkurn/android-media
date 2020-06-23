package com.tokopedia.vouchercreation.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.voucherlist.model.ui.MoreMenuUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.MenuAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 18/04/20
 */

class MoreMenuAdapter(
        callback: (MoreMenuUiModel) -> Unit
) : BaseAdapter<MenuAdapterFactoryImpl>(MenuAdapterFactoryImpl(callback))