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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapter
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupItemModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifyprinciples.Typography

class MovetoGroupSheetList {

    private var dialog: BottomSheetDialog? = null
    private var adapter: MovetoGroupAdapter? = null
    var onItemClick: (() -> Unit)? = null
    var onItemSearch: ((search: String) -> Unit)? = null
    private var groupId = 0
    private var searchTextField: EditText? = null

    private fun setupView(context: Context) {
        dialog?.let {
            adapter = MovetoGroupAdapter(MovetoGroupAdapterTypeFactoryImpl(::itemSelected))
            it.findViewById<RecyclerView>(R.id.recyclerView)?.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context,
                    DividerItemDecoration.VERTICAL))
                this.adapter = this@MovetoGroupSheetList.adapter
            }

            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout =
                    dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                if (frameLayout != null) {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.findViewById<ImageUnify>(R.id.btn_close)?.apply {
                setImageDrawable(context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_create_ic_group_close))
                setOnClickListener { dismissDialog() }
            }
            it.findViewById<UnifyButton>(R.id.submit_butt)?.setOnClickListener {
                if (groupId.toString() == "0") {
                    adapter?.items?.firstOrNull()?.let { model ->
                        groupId = (model as MovetoGroupItemModel).result.groupId.toInt()
                    }
                }
                onItemClick?.invoke()
                dismissDialog()
            }
            it.findViewById<UnifyImageButton>(R.id.btnFilter)?.visibility = View.GONE
            it.findViewById<Typography>(R.id.filterCount)?.visibility = View.GONE
            it.findViewById<UnifyButton>(R.id.submit_butt)?.isEnabled = true
            setSearchListener(it)
        }
    }

    private fun itemSelected(pos: Int) {
        groupId = (adapter?.items?.get(pos) as MovetoGroupItemModel).result.groupId.toInt()
        adapter?.setLastSelected(pos)
    }

    private fun setSearchListener(it: BottomSheetDialog) {
        val searchBar = it.findViewById<SearchBarUnify>(R.id.searchBar)

        searchTextField = searchBar?.searchBarTextField
        val searchClearButton = searchBar?.searchBarIcon
        searchTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(
                textView: TextView?,
                actionId: Int,
                even: KeyEvent?,
            ): Boolean {

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
            it.findViewById<UnifyButton>(R.id.submit_butt)?.isEnabled = true
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
        dialog?.findViewById<UnifyButton>(R.id.submit_butt)?.isEnabled = false
    }

    companion object {

        fun newInstance(context: Context): MovetoGroupSheetList {
            val fragment = MovetoGroupSheetList()
            fragment.dialog = BottomSheetDialog(context,
                com.tokopedia.topads.common.R.style.CreateAdsBottomSheetDialogTheme)
            fragment.dialog?.setContentView(R.layout.topads_dash_moveto_group_bottom_sheet)
            fragment.setupView(context)
            return fragment
        }
    }
}
