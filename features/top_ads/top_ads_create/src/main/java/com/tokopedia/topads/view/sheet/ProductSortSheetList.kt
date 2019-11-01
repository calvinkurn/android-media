package com.tokopedia.topads.view.sheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout
import android.widget.RadioGroup
import com.tokopedia.topads.create.R
import kotlinx.android.synthetic.main.topads_create_fragment_product_list_sheet_sort.*

/**
 * Author errysuprayogi on 07,May,2019
 */
class ProductSortSheetList {

    private var dialog: BottomSheetDialog? = null
    private var actionListener: ActionListener? = null


    private fun setupView(context: Context) {
        dialog?.let {
            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                if (frameLayout != null) {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.btn_close.setOnClickListener {
                dismissDialog()
            }
            it.rb_group.setOnCheckedChangeListener ( object: RadioGroup.OnCheckedChangeListener{
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

                }
            })
        }
    }

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    fun show() {
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

    interface ActionListener {
        fun onSort(pos: Int)
    }

    companion object {

        fun newInstance(context: Context): ProductSortSheetList {
            val fragment = ProductSortSheetList()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.topads_create_fragment_product_list_sheet_sort)
            fragment.setupView(context)
            return fragment
        }
    }
}
