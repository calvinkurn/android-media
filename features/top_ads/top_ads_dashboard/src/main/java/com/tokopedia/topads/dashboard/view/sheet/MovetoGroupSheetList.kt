package com.tokopedia.topads.dashboard.view.sheet

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapter
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupItemModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupModel
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.android.synthetic.main.topads_dash_moveto_group_bottom_sheet.*

class MovetoGroupSheetList {

    private var dialog: BottomSheetDialog? = null
    private var adapter: MovetoGroupAdapter? = null
    var onItemClick: (() -> Unit)? = null
    var onItemSearch: ((search: String) -> Unit)? = null
    var groupId = 0
    private var searchTextField: EditText? = null

    private fun setupView(context: Context) {
        dialog?.let {
            adapter = MovetoGroupAdapter(MovetoGroupAdapterTypeFactoryImpl(::itemSelected))
            it.recyclerView.layoutManager = LinearLayoutManager(context)
            it.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            it.recyclerView.adapter = adapter
            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                if (frameLayout != null) {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.btn_close.setImageDrawable(context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_group_close))
            it.btn_close.setOnClickListener { dismissDialog() }
            it.submit_butt.setOnClickListener {
                if (groupId.toString() == "0") {
                    adapter?.items?.firstOrNull()?.let { model ->
                        groupId = (model as MovetoGroupItemModel).result.groupId.toInt()
                    }
                }
                onItemClick?.invoke()
                dismissDialog()
            }
            it.btnFilter.visibility = View.GONE
            it.filterCount.visibility = View.GONE
            dialog?.submit_butt?.isEnabled = true
            setSearchListener(it)
        }
    }

    private fun itemSelected(pos: Int) {
        groupId = (adapter?.items?.get(pos) as MovetoGroupItemModel).result.groupId.toInt()
        adapter?.setLastSelected(pos)
    }

    private fun setSearchListener(it: BottomSheetDialog) {
        searchTextField = it.searchBar?.searchBarTextField
        val searchClearButton = it.searchBar?.searchBarIcon
        searchTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(textView: TextView?, actionId: Int, even: KeyEvent?): Boolean {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onItemSearch?.invoke(textView?.text.toString())
                    return true
                }
                return false
            }
        })
        searchClearButton?.setOnClickListener {
            searchTextField?.text?.clear()
            onItemSearch?.invoke("")
            dialog?.submit_butt?.isEnabled = true
        }
    }

    fun show() {
        dialog?.show()
    }

    private fun dismissDialog() {
        dialog?.dismiss()
        searchTextField?.text?.clear()
    }

    fun updateData(data: MutableList<MovetoGroupModel>) {
        adapter?.updateData(data)
    }

    fun updateKeyCount(data: List<CountDataItem>) {
        adapter?.setItemCount(data)
    }

    fun getSelectedFilter(): String {
        return groupId.toString()
    }

    fun setButtonDisable() {
        dialog?.submit_butt?.isEnabled = false
    }

    companion object {

        fun newInstance(context: Context): MovetoGroupSheetList {
            val fragment = MovetoGroupSheetList()
            fragment.dialog = BottomSheetDialog(context, com.tokopedia.topads.common.R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_dash_moveto_group_bottom_sheet)
            fragment.setupView(context)
            return fragment
        }
    }
}
