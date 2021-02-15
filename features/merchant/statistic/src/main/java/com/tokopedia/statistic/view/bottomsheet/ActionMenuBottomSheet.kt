package com.tokopedia.statistic.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.statistic.R
import com.tokopedia.statistic.view.adapter.ActionMenuAdapter
import com.tokopedia.statistic.view.model.ActionMenuUiModel
import kotlinx.android.synthetic.main.bottomsheet_stc_action_menu.view.*

/**
 * Created By @ilhamsuaib on 14/02/21
 */

class ActionMenuBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "StatisticActionMenuBottomSheet"
        private const val MENU_ITEMS = "menu_items"

        fun createInstance(menuItems: List<ActionMenuUiModel>): ActionMenuBottomSheet {
            return ActionMenuBottomSheet().apply {
                clearContentPadding = true
                showHeader = false
                arguments = Bundle().apply {
                    putParcelableArrayList(MENU_ITEMS, ArrayList(menuItems))
                }
            }
        }
    }

    override fun getResLayout(): Int = R.layout.bottomsheet_stc_action_menu

    override fun setupView() = childView?.run {
        setupAdapter(this)

        iconStcCloseActionMenu.setOnClickListener {
            dismiss()
        }
        tvStcActionMenuTitle.text = context.getString(R.string.stc_other_menu)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setupAdapter(view: View) = with(view) {
        val adapter = ActionMenuAdapter(::setOnMenuItemClick)
        rvStcActionMenu.layoutManager = LinearLayoutManager(context)
        rvStcActionMenu.adapter = adapter

        val actionMenus: ArrayList<ActionMenuUiModel>? = arguments?.getParcelableArrayList(MENU_ITEMS)
        actionMenus?.let {
            adapter.clearAllElements()
            adapter.addElement(it)
        }
    }

    private fun setOnMenuItemClick(menu: ActionMenuUiModel) {
        context?.let {
            RouteManager.route(it, menu.appLink)
            dismiss()
        }
    }
}