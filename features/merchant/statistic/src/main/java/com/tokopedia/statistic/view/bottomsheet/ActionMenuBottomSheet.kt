package com.tokopedia.statistic.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.statistic.R
import com.tokopedia.statistic.databinding.BottomsheetStcActionMenuBinding
import com.tokopedia.statistic.view.adapter.ActionMenuAdapter
import com.tokopedia.statistic.view.model.ActionMenuUiModel

/**
 * Created By @ilhamsuaib on 14/02/21
 */

class ActionMenuBottomSheet : BaseBottomSheet<BottomsheetStcActionMenuBinding>() {

    companion object {
        private const val TAG = "StatisticActionMenuBottomSheet"
        private const val MENU_ITEMS = "menu_items"
        private const val PAGE = "tab_page"
        private const val USER_ID = "user_id"

        fun createInstance(
            pageName: String,
            userId: String,
            menuItems: List<ActionMenuUiModel>
        ): ActionMenuBottomSheet {
            return ActionMenuBottomSheet().apply {
                clearContentPadding = true
                showHeader = false
                arguments = Bundle().apply {
                    putParcelableArrayList(MENU_ITEMS, ArrayList(menuItems))
                    putString(PAGE, pageName)
                    putString(USER_ID, userId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetStcActionMenuBinding.inflate(inflater).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() = binding?.run {
        setupAdapter()

        iconStcCloseActionMenu.setOnClickListener {
            dismiss()
        }
        tvStcActionMenuTitle.text = root.context.getString(R.string.stc_other_menu)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setupAdapter() = binding?.run {
        val userId = arguments?.getString(USER_ID).orEmpty()
        val pageName = arguments?.getString(PAGE).orEmpty()
        val adapter = ActionMenuAdapter(pageName, userId, ::setOnMenuItemClick)
        rvStcActionMenu.layoutManager = LinearLayoutManager(context)
        rvStcActionMenu.adapter = adapter

        val actionMenus: ArrayList<ActionMenuUiModel>? =
            arguments?.getParcelableArrayList(MENU_ITEMS)
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