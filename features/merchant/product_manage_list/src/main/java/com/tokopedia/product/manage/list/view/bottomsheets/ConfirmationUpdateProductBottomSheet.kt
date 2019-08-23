package com.tokopedia.product.manage.list.view.bottomsheets

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.data.ConfirmationProductData
import com.tokopedia.product.manage.list.view.adapter.ConfirmationProductAdapter

class ConfirmationUpdateProductBottomSheet : BottomSheets() {

    private var model = ArrayList<ConfirmationProductData>()
    private var listener: EditProductBottomSheet.EditProductInterface? = null
    lateinit var btnCancel: Button
    lateinit var btnConfirmation: Button
    private val confirmationAdapter by lazy {
        ConfirmationProductAdapter(model)
    }

    companion object {

        const val CONFIRMATION_DATA_PARAM = "confirmation_data"

        @JvmStatic
        fun newInstance(model: ArrayList<ConfirmationProductData>): ConfirmationUpdateProductBottomSheet {
            return ConfirmationUpdateProductBottomSheet().apply {
                val bundle = Bundle()
                bundle.putParcelableArrayList(CONFIRMATION_DATA_PARAM, model)
                arguments = bundle
            }
        }
    }

    fun setListener(listener: EditProductBottomSheet.EditProductInterface) {
        this.listener = listener
    }

    private lateinit var rvConfirmation: RecyclerView

    override fun state(): BottomSheetsState = BottomSheetsState.FULL

    override fun getLayoutResourceId(): Int = R.layout.confirmation_product_bottom_sheet

    override fun getBaseLayoutResourceId(): Int = R.layout.base_confirmation_bottom_sheet

    override fun getTheme(): Int = R.style.BaseBottomSheetDialog

    override fun initView(view: View) {
        getArgument()

        view.apply {
            rvConfirmation = findViewById(R.id.rv_confirmation_product)
            btnCancel = findViewById(R.id.btn_cancel_confirmation)
            btnConfirmation = findViewById(R.id.btn_confirmation)
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnConfirmation.setOnClickListener {
            listener?.updateProduct()
        }

        rvConfirmation?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = confirmationAdapter
        }
    }

    private fun getArgument() {
        model = arguments?.getParcelableArrayList(CONFIRMATION_DATA_PARAM)
                ?: arrayListOf()
    }
}