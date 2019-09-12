package com.tokopedia.sellerorder.list.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import kotlinx.android.synthetic.main.fragment_som_filter.*

/**
 * Created by fwidjaja on 2019-09-11.
 */
class SomFilterFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): SomFilterFragment {
            return SomFilterFragment()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderFilterOrderType()
        renderFilterCourier()
    }

    private fun renderFilterOrderType() {
        val listOrderType = arrayListOf<QuickFilterItem>()

        val filterItem1 = CustomViewQuickFilterItem()
        filterItem1.name = "Pre-Order"
        filterItem1.type = "preorder"
        listOrderType.add(filterItem1)

        val filterItem2 = CustomViewQuickFilterItem()
        filterItem2.name = "Regular Order"
        filterItem2.type = "regular"
        listOrderType.add(filterItem2)

        val filterItem3 = CustomViewQuickFilterItem()
        filterItem3.name = "Pick-Up"
        filterItem3.type = "pickup"
        listOrderType.add(filterItem3)

        val filterItem4 = CustomViewQuickFilterItem()
        filterItem4.name = "Same-Day"
        filterItem4.type = "sameday"
        listOrderType.add(filterItem4)

        val filterItem5 = CustomViewQuickFilterItem()
        filterItem5.name = "Instant"
        filterItem5.type = "instant"
        listOrderType.add(filterItem5)

        quick_filter_order_type?.renderFilter(listOrderType)
    }

    private fun renderFilterCourier() {
        val listCourier = arrayListOf<QuickFilterItem>()

        val courierItem1 = CustomViewQuickFilterItem()
        courierItem1.name = "JNE"
        courierItem1.type = "jne"
        listCourier.add(courierItem1)

        val courierItem2 = CustomViewQuickFilterItem()
        courierItem2.name = "TIKI"
        courierItem2.type = "tiki"
        listCourier.add(courierItem2)

        val courierItem3 = CustomViewQuickFilterItem()
        courierItem3.name = "Go-Jek"
        courierItem3.type = "gojek"
        listCourier.add(courierItem3)

        val courierItem4 = CustomViewQuickFilterItem()
        courierItem4.name = "Grab"
        courierItem4.type = "grab"
        listCourier.add(courierItem4)

        val courierItem5 = CustomViewQuickFilterItem()
        courierItem5.name = "Wahana"
        courierItem5.type = "wahana"
        listCourier.add(courierItem5)

        quick_filter_order_courier?.renderFilter(listCourier)
    }
}