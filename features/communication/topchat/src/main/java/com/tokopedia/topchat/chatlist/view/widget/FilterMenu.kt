package com.tokopedia.topchat.chatlist.view.widget

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.view.adapter.FilterMenuAdapter
import com.tokopedia.topchat.common.data.TopchatItemMenu
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_menu_list.view.rvMenu

class FilterMenu : BottomSheetUnify() {

    private lateinit var menuView: View
    private val menuAdapter = FilterMenuAdapter()

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeCloseButtonColour()
    }

    private fun changeCloseButtonColour() {
        context?.let { ctx ->
            val color =
                MethodChecker.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
            bottomSheetClose.drawable?.apply {
                mutate()
                setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
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

    fun setOnItemMenuClickListener(onClick: (TopchatItemMenu, Int) -> Unit) {
        menuAdapter.setOnItemMenuClickListener(onClick)
    }

    fun setItemMenuList(menus: MutableList<TopchatItemMenu>) {
        menuAdapter.menus = menus
        menuAdapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "Tag filter menu"
    }
}