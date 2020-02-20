package com.tokopedia.product.manage.list.view.bottomsheets

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.list.view.adapter.EditProductBottomSheetAdapter

class EditProductBottomSheet(context: Context, val listener: EditProductInterface, val fragmentManager: FragmentManager) : FrameLayout(context) {

    interface EditProductInterface {
        fun goToEtalasePicker(etalaseId: Int)
        fun goToEditStock()
        fun goToConfirmationBottomSheet(isActionDelete: Boolean)
        fun updateProduct()
    }

    var rvBulk: RecyclerView? = null

    private val editAdapter = EditProductBottomSheetAdapter(listener)
    private var btnNext: Button? = null

    init {
        LayoutInflater.from(context).inflate(com.tokopedia.product.manage.list.R.layout.fragment_bs_bulk_edit, this, true).apply {
            rvBulk = findViewById(com.tokopedia.product.manage.list.R.id.rv_bulk_edit)
            btnNext = findViewById(com.tokopedia.product.manage.list.R.id.btn_next_bs)
            rvBulk?.run {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = editAdapter
            }
        }
        btnNext?.setOnClickListener {
            listener.goToConfirmationBottomSheet(false)
        }
        setInitialData(createDefaultObject())
    }

    fun setResultValue(etalaseValue: BulkBottomSheetType.EtalaseType?, stockValue: BulkBottomSheetType.StockType?) {
        btnNext?.isEnabled = true
        editAdapter.setDataResult(etalaseValue, stockValue)
    }

    private fun setInitialData(initialData: List<BulkBottomSheetType>) {
        editAdapter.loadInitialData(initialData)
    }

    private fun createDefaultObject(): List<BulkBottomSheetType> {
        return arrayListOf(
                BulkBottomSheetType.EtalaseType(),
                BulkBottomSheetType.StockType(),
                BulkBottomSheetType.DeleteType())
    }

    fun clearAllData() {
        btnNext?.isEnabled = false
        editAdapter.clearData(createDefaultObject())
    }
}