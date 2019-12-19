package com.tokopedia.product.manage.list.view.bottomsheets

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.tkpd.library.utils.legacy.MethodChecker
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.data.ConfirmationProductData
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType.Companion.STOCK_DELETED
import com.tokopedia.product.manage.list.view.adapter.ConfirmationProductAdapter

class ConfirmationUpdateProductBottomSheet : BottomSheets() {

    private var model = ArrayList<ConfirmationProductData>()
    private var listener: EditProductBottomSheet.EditProductInterface? = null
    private lateinit var btnCancel: Button
    private lateinit var btnConfirmation: Button
    private lateinit var btnClose: ImageView

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

    override fun title(): String {
        return if (model.first().statusStock != STOCK_DELETED) {
            if (model.size == 1) {
                MethodChecker.fromHtml(getString(R.string.product_confirmation_bs_title_change_single)).toString()
            } else {
                MethodChecker.fromHtml(getString(R.string.product_confirmation_bs_title_change, model.size.toString())).toString()
            }
        } else {
            if (model.size == 1) {
                MethodChecker.fromHtml(getString(R.string.product_confirmation_bs_title_delete_single)).toString()
            } else {
                MethodChecker.fromHtml(getString(R.string.product_confirmation_bs_title_delete, model.size.toString())).toString()
            }
        }
    }

    private lateinit var rvConfirmation: RecyclerView

    override fun state(): BottomSheetsState = BottomSheetsState.FULL

    override fun getLayoutResourceId(): Int = R.layout.confirmation_product_bottom_sheet

    override fun getBaseLayoutResourceId(): Int = R.layout.base_confirmation_bottom_sheet

    override fun getTheme(): Int = R.style.BaseBottomSheetDialog

    override fun configView(parentView: View) {
        getArgument()
        super.configView(parentView)
        btnClose = parentView.findViewById(R.id.btn_close_confirmation)
        btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun initView(view: View) {
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
            dismiss()
        }

        rvConfirmation.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = confirmationAdapter
        }
    }

    private fun getArgument() {
        model = arguments?.getParcelableArrayList(CONFIRMATION_DATA_PARAM)
                ?: arrayListOf()
    }
}