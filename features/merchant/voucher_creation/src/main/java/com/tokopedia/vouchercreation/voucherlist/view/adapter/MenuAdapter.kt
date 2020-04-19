package com.tokopedia.vouchercreation.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.MenuAdapterFactoryImpl
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.MenuViewHolder

/**
 * Created By @ilhamsuaib on 18/04/20
 */

class MenuAdapter(listener: MenuViewHolder.Listener) : BaseAdapter<MenuAdapterFactoryImpl>(MenuAdapterFactoryImpl(listener))