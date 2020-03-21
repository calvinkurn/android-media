package com.tokopedia.thankyou_native.presentation.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.DetailedInvoiceAdapter
import com.tokopedia.thankyou_native.presentation.adapter.InvoiceTypeFactory

class InvoiceDetailBottomSheet(val context: Context) : CloseableBottomSheetDialog.CloseClickedListener {

    lateinit var visitables: List<Visitable<*>>
    lateinit var dialog: CloseableBottomSheetDialog

    fun show(visitables: List<Visitable<*>>) {
        this.visitables = visitables
        val view = createBottomSheetView()
        if (!::dialog.isInitialized)
            dialog = CloseableBottomSheetDialog.createInstanceCloseableRounded(context, this)
        dialog.setCustomContentView(view, context.resources.getString(R.string.thank_payment_detail), true)
        dialog.show()
    }

    private fun createBottomSheetView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.thank_payment_invoice_bsheet, null)
        if (::visitables.isInitialized) {
            val adapter = DetailedInvoiceAdapter(visitables, InvoiceTypeFactory())
            val recycleListView = view.findViewById<RecyclerView>(R.id.recyclerView)
            recycleListView.layoutManager = LinearLayoutManager(context)
            recycleListView.adapter = adapter
        }
        return view
    }

    override fun onCloseDialog() {
    }

}