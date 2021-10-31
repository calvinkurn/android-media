package com.tokopedia.buyerorder.recharge.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailDividerModel
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailModel
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailTopSectionModel

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailAdapter(private val typeFactory: RechargeOrderDetailTypeFactory) :
        BaseAdapter<RechargeOrderDetailTypeFactory>(typeFactory) {

    fun updateItems(data: RechargeOrderDetailModel) {
        val newItems = setupItems(data)
        visitables.clear()
        visitables.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun setupItems(
            data: RechargeOrderDetailModel
    ): List<Visitable<RechargeOrderDetailTypeFactory>> {
        return mutableListOf<Visitable<RechargeOrderDetailTypeFactory>>().apply {
            setupTopSection(data.topSectionModel)
        }
    }

    private fun MutableList<Visitable<RechargeOrderDetailTypeFactory>>.setupTopSection(topModel: RechargeOrderDetailTopSectionModel) {
        add(topModel)
        addDivider()
    }

    private fun MutableList<Visitable<RechargeOrderDetailTypeFactory>>.addDivider() {
        add(RechargeOrderDetailDividerModel())
    }

}