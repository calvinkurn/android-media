package com.tokopedia.topchat.chatlist.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.design.component.Menus
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.FilterMenuAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_menu_list.view.*

class FilterMenu : BottomSheetUnify() {

    private lateinit var menuView: View
    private val menuAdapter = FilterMenuAdapter()

    init {
        setFullPage(false)
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        menuView = View.inflate(context, R.layout.fragment_menu_list, null)
        menuView.rvMenu?.apply {
            setHasFixedSize(true)
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context))
        }
        setChild(menuView)
    }

    fun setOnItemMenuClickListener(onClick: (Menus.ItemMenus, Int) -> Unit) {
        menuAdapter.setOnItemMenuClickListener(onClick)
    }

    fun setItemMenuList(menus: MutableList<Menus.ItemMenus>) {
        menuAdapter.menus = menus
        menuAdapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "Tag filter menu"
    }
}