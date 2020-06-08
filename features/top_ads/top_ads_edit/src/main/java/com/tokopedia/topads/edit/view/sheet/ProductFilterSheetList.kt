package com.tokopedia.topads.edit.view.sheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.etalase.EtalaseAdapter
import com.tokopedia.topads.edit.view.adapter.etalase.EtalaseAdapterTypeFactoryImpl
import com.tokopedia.topads.edit.view.adapter.etalase.viewmodel.EtalaseItemViewModel
import com.tokopedia.topads.edit.view.adapter.etalase.viewmodel.EtalaseShimerViewModel
import com.tokopedia.topads.edit.view.adapter.etalase.viewmodel.EtalaseViewModel
import kotlinx.android.synthetic.main.topads_edit_select_fragment_product_list_sheet_filter.*

class ProductFilterSheetList {

    private var dialog: BottomSheetDialog? = null
    private var adapter: EtalaseAdapter? = null
    private var selectedItem: ResponseEtalase.Data.ShopShowcasesByShopID.Result = ResponseEtalase.Data.ShopShowcasesByShopID.Result()
    var onItemClick: ((ResponseEtalase.Data.ShopShowcasesByShopID.Result) -> Unit)? = null

    private fun setupView(context: Context) {
        dialog?.let {
            val elementList = mutableListOf<EtalaseViewModel>(
                    EtalaseShimerViewModel(),
                    EtalaseShimerViewModel(),
                    EtalaseShimerViewModel(),
                    EtalaseShimerViewModel(),
                    EtalaseShimerViewModel(),
                    EtalaseShimerViewModel()
            )
            adapter = EtalaseAdapter(EtalaseAdapterTypeFactoryImpl(this::onItemClick), elementList)
            it.recyclerView.setLayoutManager(LinearLayoutManager(context))
            it.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            it.recyclerView.adapter = adapter
            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                if (frameLayout != null) {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.btn_close.setOnClickListener { dismissDialog() }
        }
    }

    fun show() {
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    fun updateData(data: MutableList<EtalaseViewModel>) {
        adapter?.updateData(data)
    }

    fun getSelectedFilter(): String {
        return selectedItem.id
    }

    private fun onItemClick(pos: Int) {
        adapter?.items?.forEachIndexed { index, result -> if(result is EtalaseItemViewModel) result.checked = index == pos }
        val item = adapter?.items?.get(pos)
        if(item is EtalaseItemViewModel) {
            selectedItem = item.result
            onItemClick?.invoke(selectedItem)
        }
        adapter?.notifyDataSetChanged()
        dismissDialog()
    }

    companion object {

        fun newInstance(context: Context): ProductFilterSheetList {
            val fragment = ProductFilterSheetList()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_edit_select_fragment_product_list_sheet_filter)
            fragment.setupView(context)
            return fragment
        }
    }
}
