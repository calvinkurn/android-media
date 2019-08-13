package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetaiItemViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailHeaderViewModel

/**
 * @author by yoasfs on 2019-08-12
 */

class CommissionDetailTypeFactoryImpl:
        BaseAdapterTypeFactory(), CommissionDetailTypeFactory {

    override fun type(commissionDetailHeaderViewModel: CommissionDetailHeaderViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(commissionDetaiItemViewModel: CommissionDetaiItemViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when (type) {

            else -> super.createViewHolder(parent, type)

        }
    }
}