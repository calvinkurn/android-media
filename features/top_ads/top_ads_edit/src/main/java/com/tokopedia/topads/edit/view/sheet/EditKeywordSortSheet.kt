package com.tokopedia.topads.edit.view.sheet


import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout
import com.tokopedia.topads.edit.R
import kotlinx.android.synthetic.main.topads_edit_keyword_edit_sort_sheet.*

class EditKeywordSortSheet {

    private var dialog: BottomSheetDialog? = null
    var onItemClick: ((sortId: String) -> Unit)? = null

    private fun setupView() {
        dialog?.let {
            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                frameLayout?.let {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.btn_close.setOnClickListener {
                dismissDialog()
            }

            it.radio_group.setOnCheckedChangeListener { _, checkedId ->
                onItemClick?.invoke(checkedId.toString())
                dismissDialog()
            }
        }
    }

    fun getSelectedSortId(): String {
        return when (dialog?.radio_group?.checkedRadioButtonId) {
            R.id.title_1 -> TITLE_1
            R.id.title_2 -> TITLE_2
            else -> TITLE_1
        }
    }

    fun show() {
        dialog?.show()
    }

    private fun dismissDialog() {
        dialog?.dismiss()
    }

    fun setChecked(current: String) {
        if (current == TITLE_1)
            dialog?.radio_group?.check(R.id.title_1)
        else
            dialog?.radio_group?.check(R.id.title_2)
    }

    companion object {

        const val TITLE_1 = "Pencarian luas"
        const val TITLE_2 = "Pencarian Spesifik"

        fun newInstance(context: Context): EditKeywordSortSheet {
            val fragment = EditKeywordSortSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_edit_keyword_edit_sort_sheet)
            fragment.setupView()
            return fragment
        }
    }
}
