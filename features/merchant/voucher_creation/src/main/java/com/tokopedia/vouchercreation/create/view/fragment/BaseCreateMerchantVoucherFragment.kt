package com.tokopedia.vouchercreation.create.view.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory

abstract class BaseCreateMerchantVoucherFragment<F : CreateVoucherTypeFactory, WTF : BaseAdapterTypeFactory>(val onNext: () -> Unit) : BaseListFragment<Visitable<F>, WTF>()