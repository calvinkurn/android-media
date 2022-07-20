package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.SelectVariantMainAdapter
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class SelectVariantMainBottomSheet(
        private val listener: SelectVariantMainListener? = null
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Select Variant Main"
    }

    private var contentView: View? = null
    private var selectAdapter: SelectVariantMainAdapter? = null

    interface SelectVariantMainListener {
        fun onSelectVariantMainFinished(combination: List<Int>)
    }

    init {
        selectAdapter = SelectVariantMainAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    fun setData(items: VariantInputModel?) {
        items?.run {
            selectAdapter?.setData(this)
        }
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun initChildLayout() {
        setTitle(getString(R.string.label_variant_main_select_bottom_sheet_title))
        contentView = View.inflate(context,
                R.layout.add_edit_product_select_variant_main_bottom_sheet_content, null)
        val recyclerViewVariantMain: RecyclerView? = contentView?.findViewById(R.id.recyclerViewVariantMain)
        val buttonSave: UnifyButton? = contentView?.findViewById(R.id.buttonSave)

        recyclerViewVariantMain?.apply {
            setHasFixedSize(true)
            adapter = selectAdapter
            layoutManager = LinearLayoutManager(context)
        }
        buttonSave?.setOnClickListener {
            dismiss()
            listener?.onSelectVariantMainFinished(selectAdapter?.getSelectedData().orEmpty())
        }

        setChild(contentView)
        clearContentPadding = true
    }
}