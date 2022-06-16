package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.MoreMenuUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.MenuAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 18/04/20
 */

class MoreMenuAdapter(
        callback: (MoreMenuUiModel) -> Unit
) : BaseAdapter<MenuAdapterFactoryImpl>(MenuAdapterFactoryImpl(callback))