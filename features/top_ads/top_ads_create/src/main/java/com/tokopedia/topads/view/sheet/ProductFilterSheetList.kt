package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseEtalase
import com.tokopedia.topads.view.adapter.etalase.EtalaseAdapter
import com.tokopedia.topads.view.adapter.etalase.EtalaseAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.etalase.viewmodel.EtalaseItemViewModel
import com.tokopedia.topads.view.adapter.etalase.viewmodel.EtalaseShimerViewModel
import com.tokopedia.topads.view.adapter.etalase.viewmodel.EtalaseViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_create_fragment_product_list_sheet_filter.*

/**
 * Author errysuprayogi on 07,May,2019
 */
class ProductFilterSheetList : BottomSheetUnify(){

    private var adapter: EtalaseAdapter? = null
    private var selectedItem: ResponseEtalase.Data.ShopShowcasesByShopID.Result = ResponseEtalase.Data.ShopShowcasesByShopID.Result()
    var onItemClick: ((ResponseEtalase.Data.ShopShowcasesByShopID.Result) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView = View.inflate(context, R.layout.topads_create_fragment_product_list_sheet_filter, null)
        setChild(contentView)
        setTitle(getString(R.string.filter))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView();
    }

    private fun initView() {
        val elementList = mutableListOf<EtalaseViewModel>(
                EtalaseShimerViewModel(),
                EtalaseShimerViewModel(),
                EtalaseShimerViewModel(),
                EtalaseShimerViewModel(),
                EtalaseShimerViewModel(),
                EtalaseShimerViewModel()
        )
        adapter = EtalaseAdapter(EtalaseAdapterTypeFactoryImpl(this::onItemClick), elementList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val fragment = manager.findFragmentByTag(tag)
        if (fragment != null) {
            val ft = manager.beginTransaction()
            ft.remove(fragment)
            ft.commit()
        }
        super.show(manager, tag)
    }

    fun updateData(data: MutableList<EtalaseViewModel>) {
        adapter?.updateData(data)
    }

    fun getSelectedFilter(): String {
        return selectedItem.id
    }

    private fun onItemClick(pos: Int) {
        adapter?.items?.forEachIndexed { index, result -> if(result is EtalaseItemViewModel) result.checked = index == pos }
        var item = adapter?.items?.get(pos)
        if(item is EtalaseItemViewModel) {
            selectedItem = item.result
            onItemClick?.invoke(selectedItem)
        }
        adapter?.notifyDataSetChanged()
        dismiss()
    }

    companion object {
        fun newInstance(): ProductFilterSheetList {
            return ProductFilterSheetList()
        }
    }
}
