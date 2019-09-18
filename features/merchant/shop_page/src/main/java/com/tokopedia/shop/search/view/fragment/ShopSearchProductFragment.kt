package com.tokopedia.shop.search.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.search.view.adapter.ShopSearchProductAdapterTypeFactory
import com.tokopedia.shop.search.view.model.ShopSearchProductDataModel
import com.tokopedia.shop.search.view.model.ShopSearchProductDynamicResultDataModel
import com.tokopedia.shop.search.view.model.ShopSearchProductFixedResultDataModel

class ShopSearchProductFragment: BaseSearchListFragment<ShopSearchProductDataModel, ShopSearchProductAdapterTypeFactory>() {

    companion object{
        fun createInstance() = ShopSearchProductFragment()
    }

    override fun getAdapterTypeFactory(): ShopSearchProductAdapterTypeFactory {
        return ShopSearchProductAdapterTypeFactory()
    }

    override fun onItemClicked(t: ShopSearchProductDataModel?) {
        Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
    }

    override fun onSearchSubmitted(text: String?) {
    }

    override fun loadData(page: Int) {
        adapter.clearAllElements()
        adapter.addElement(ShopSearchProductFixedResultDataModel())
        adapter.addElement(ShopSearchProductFixedResultDataModel())
        adapter.addElement(ShopSearchProductDynamicResultDataModel())
        adapter.addElement(ShopSearchProductDynamicResultDataModel())
        adapter.addElement(ShopSearchProductDynamicResultDataModel())
        adapter.addElement(ShopSearchProductDynamicResultDataModel())
        adapter.addElement(ShopSearchProductDynamicResultDataModel())
        adapter.addElement(ShopSearchProductDynamicResultDataModel())
        adapter.addElement(ShopSearchProductDynamicResultDataModel())

    }

    override fun onSearchTextChanged(text: String?) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view).addItemDecoration(DividerItemDecoration(context))

//        adapter.notifyDataSetChanged()
    }
}