package com.tokopedia.home_account.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.home_account.view.adapter.factory.FundsAndInvestmentItemFactory

internal open class HomeAccountFundsAndInvestmentAdapter(
    factory: FundsAndInvestmentItemFactory
) : BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(factory) {


}