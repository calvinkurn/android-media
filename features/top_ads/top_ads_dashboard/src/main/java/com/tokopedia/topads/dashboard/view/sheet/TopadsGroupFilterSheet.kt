package com.tokopedia.topads.dashboard.view.sheet

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_dash_filter_bottomsheet.*

/**
 * Created by Pika on 3/6/20.
 */
class TopadsGroupFilterSheet : BottomSheetUnify() {
    private var dialog: BottomSheetDialog? = null
    var onSubmitClick: (() -> Unit)? = null
    private var filterCount = 0

    private fun setupView(context: Context) {
        dialog?.let {
            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                frameLayout?.let {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.status?.visibility = View.VISIBLE
            it.status_title?.visibility = View.VISIBLE
            it.btn_close.setOnClickListener {
                dismissDialog()
            }
            it.submit.setOnClickListener { _ ->
                filterCount = 0
                if (it.status?.checkedRadioButtonId != -1)
                    filterCount++
                if (it.sortFilter?.checkedRadioButtonId != -1)
                    filterCount++
                onSubmitClick?.invoke()
                dismissDialog()
            }
        }
    }

    fun getFilterCount(): Int {
        return filterCount
    }

    fun removeStatusFilter() {
        dialog.let {
            it?.status?.visibility = View.GONE
            it?.status_title?.visibility = View.GONE
        }
    }

    fun getSelectedSortId(): String {
        return when (dialog?.sortFilter?.checkedRadioButtonId) {
            R.id.filter1 -> list[0]
            R.id.filter2 -> list[1]
            R.id.filter3 -> list[2]
            R.id.filter4 -> list[3]
            R.id.filter5 -> list[4]
            else -> ""
        }
    }

    fun getSelectedStatusId(): Int? {
        return when (dialog?.status?.checkedRadioButtonId) {
            R.id.active -> 1
            R.id.tidak_tampil -> 2
            R.id.tidak_active -> 3
            else -> null
        }
    }

    fun show() {
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    companion object {
        lateinit var list: Array<String>
        fun newInstance(context: Context): TopadsGroupFilterSheet {
            val fragment = TopadsGroupFilterSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_dash_filter_bottomsheet)
            list = context.resources.getStringArray(R.array.top_ads_sort_value)
            fragment.setupView(context)
            return fragment
        }
    }
}