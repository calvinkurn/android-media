package com.tokopedia.product.manage.list.view.bottomsheets

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.list.view.adapter.EditProductBottomSheetAdapter

class EditProductBottomSheet(context: Context, val listener: EditProductInterface) : FrameLayout(context) {

    interface EditProductInterface {
        fun goToEtalasePicker(etalaseId: Int)
        fun goToEditStock()
    }

    var rvBulk: RecyclerView? = null
    var bulkData = listOf(
            BulkBottomSheetType.EtalaseType(""),
            BulkBottomSheetType.StockType("",null),
            BulkBottomSheetType.DeleteType())
    private val editAdapter = EditProductBottomSheetAdapter(bulkData, listener)
    private var btnNext: Button? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.fragment_bs_bulk_edit, this, true).apply {
            rvBulk = findViewById(R.id.rv_bulk_edit)
            btnNext = findViewById(R.id.btn_next_bs)
            rvBulk?.run {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = editAdapter
            }
        }
    }

    fun setResultValue(etalaseValue: BulkBottomSheetType.EtalaseType?, stockValue: BulkBottomSheetType.StockType?) {
        btnNext?.isEnabled = true
        if (etalaseValue != null) {
            bulkData[0].editValue = etalaseValue.editValue
            (bulkData[0] as BulkBottomSheetType.EtalaseType).etalaseId = etalaseValue.etalaseId
        } else if (stockValue != null) {
            bulkData[1].editValue = stockValue.editValue
        }
        editAdapter.notifyDataSetChanged()
    }

    fun clearAllData() {
        btnNext?.isEnabled = false
        editAdapter.clearData()
    }

}