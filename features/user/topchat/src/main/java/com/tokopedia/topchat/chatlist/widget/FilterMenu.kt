package com.tokopedia.topchat.chatlist.widget

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.design.component.Menus
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.FilterMenuAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeCloseButtonColour()
        removeContainerPadding()
        addMarginCloseButton()
    }

    private fun changeCloseButtonColour() {
        context?.let { ctx ->
            val color = ContextCompat.getColor(ctx, R.color.Neutral_N400)
            bottomSheetClose.drawable?.apply {
                mutate()
                setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    private fun removeContainerPadding() {
        bottomSheetWrapper.setPadding(0, 0, 0, 0)
    }

    private fun addMarginCloseButton() {
        val horizontalMargin = 16.toPx()
        (bottomSheetClose.layoutParams as RelativeLayout.LayoutParams).apply {
            setMargins(horizontalMargin, 0, horizontalMargin, 0)
            addRule(RelativeLayout.CENTER_VERTICAL)
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