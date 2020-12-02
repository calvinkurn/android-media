package com.tokopedia.topchat.chatlist.widget

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.Menus
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.LongClickMenuAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_menu_list.view.*

class LongClickMenu : BottomSheetUnify() {

    private lateinit var menuView: View
    private val menuAdapter = LongClickMenuAdapter()

    init {
        setFullPage(false)
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeCloseButtonColour()
    }

    private fun changeCloseButtonColour() {
        context?.let { ctx ->
            val color = MethodChecker.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_N400)
            bottomSheetClose.drawable?.apply {
                mutate()
                setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
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
        const val TAG = "Tag long click menu"
    }
}