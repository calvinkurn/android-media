package com.tokopedia.vouchercreation.create.view.fragment.vouchertype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.data.source.PromotionTypeUiListStaticDataSource
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory

class FreeDeliveryVoucherCreateFragment: BaseListFragment<Visitable<*>, PromotionTypeItemAdapterFactory>() {

    companion object {
        @JvmStatic
        fun createInstance() = FreeDeliveryVoucherCreateFragment()
    }

    override fun getAdapterTypeFactory(): PromotionTypeItemAdapterFactory = PromotionTypeItemAdapterFactory()

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun loadData(page: Int) {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_free_delivery_voucher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        adapter.notifyDataSetChanged()
        renderList(PromotionTypeUiListStaticDataSource.getFreeDeliveryTypeUiList())
    }
}