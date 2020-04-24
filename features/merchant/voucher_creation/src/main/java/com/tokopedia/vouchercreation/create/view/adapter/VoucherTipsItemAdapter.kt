package com.tokopedia.vouchercreation.create.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTipsItemAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTipsItemTypeFactory

class VoucherTipsItemAdapter(adapterTypeFactory: VoucherTipsItemAdapterTypeFactory)
    : BaseListAdapter<Visitable<VoucherTipsItemTypeFactory>, VoucherTipsItemAdapterTypeFactory>(adapterTypeFactory) {

}