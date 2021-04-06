package com.tokopedia.product.addedit.productlimitation.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.productlimitation.presentation.adapter.ProductLimitationItemAdapter
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class ProductLimitationBottomSheet(private val actionItems: List<ProductLimitationModel>) : BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Product Limitation Bottom Sheet"
    }

    init {
        setTitle("Tidak bisa menambahkan produk")
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDismissButton()
    }

    private fun setupDismissButton() {
        requireView().findViewById<UnifyButton>(R.id.btn_dismiss).setOnClickListener {
            dismiss()
        }
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        val contentView: View? = View.inflate(context,
                R.layout.add_edit_product_product_limitation_bottom_sheet_content, null)
        val rvItems = contentView?.findViewById<RecyclerView>(R.id.rv_product_limitation)
        val adapter = ProductLimitationItemAdapter()

        adapter.setData(actionItems)
        rvItems?.adapter = adapter
        rvItems?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvItems?.addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)))
        setChild(contentView)
    }
}