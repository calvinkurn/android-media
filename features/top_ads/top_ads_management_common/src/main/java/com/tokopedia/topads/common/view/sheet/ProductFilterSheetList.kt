package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.view.adapter.etalase.EtalaseAdapter
import com.tokopedia.topads.common.view.adapter.etalase.EtalaseAdapterTypeFactoryImpl
import com.tokopedia.topads.common.view.adapter.etalase.uimodel.EtalaseItemUiModel
import com.tokopedia.topads.common.view.adapter.etalase.uimodel.EtalaseShimerUiModel
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductFilterSheetList : BottomSheetUnify() {

    private var recyclerView : RecyclerView ?= null

    var elementList = mutableListOf<EtalaseUiModel>()
    private var adapter: EtalaseAdapter? = null
    private var selectedItem: ResponseEtalase.Data.ShopShowcasesByShopID.Result =
        ResponseEtalase.Data.ShopShowcasesByShopID.Result()
    var onItemClick: ((ResponseEtalase.Data.ShopShowcasesByShopID.Result) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val contentView = View.inflate(context,
            R.layout.topads_edit_select_fragment_product_list_sheet_filter, null)
        recyclerView = contentView.findViewById(R.id.recyclerView)
        setChild(contentView)
        setTitle(getString(R.string.filter))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView();
    }

    private fun initView() {
        if (elementList.isEmpty()) {
            elementList = mutableListOf<EtalaseUiModel>(
                EtalaseShimerUiModel(),
                EtalaseShimerUiModel(),
                EtalaseShimerUiModel(),
                EtalaseShimerUiModel(),
                EtalaseShimerUiModel(),
                EtalaseShimerUiModel()
            )
        }
        adapter = EtalaseAdapter(EtalaseAdapterTypeFactoryImpl(this::onItemClick), elementList)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL))
        recyclerView?.adapter = adapter
    }

    fun updateData(data: MutableList<EtalaseUiModel>) {
        elementList = data
        adapter?.updateData(data)
    }

    fun getSelectedFilter(): String {
        return selectedItem.id
    }

    private fun onItemClick(pos: Int) {
        adapter?.items?.forEachIndexed { index, result ->
            if (result is EtalaseItemUiModel) result.checked = index == pos
        }
        val item = adapter?.items?.get(pos)
        if (item is EtalaseItemUiModel) {
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
