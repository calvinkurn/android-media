package com.tokopedia.product.manage.item.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.support.annotation.MenuRes
import android.support.v7.view.SupportMenuInflater
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import com.tokopedia.design.component.BottomSheets
import java.util.ArrayList
import com.tokopedia.product.manage.item.R

class ProductEditOptionMenuBottomSheets : BottomSheets() {
    private var title: String = ""
    private val optionMenus = ArrayList<ProductEditOptionMenu>()
    private var mode: Int = 0
    private var selectedId: Int = 0
    private lateinit var menuItemSelected: OnMenuItemSelected
    private lateinit var mContext: Context

    fun setTitle(title: String): ProductEditOptionMenuBottomSheets {
        this.title = title
        return this
    }

    fun setContext(context: Context): ProductEditOptionMenuBottomSheets {
        this.mContext = context
        return this
    }

    fun setMode(mode: Int): ProductEditOptionMenuBottomSheets {
        this.mode = mode
        return this
    }

    @SuppressLint("RestrictedApi")
    fun setMenu(@MenuRes menuId: Int): ProductEditOptionMenuBottomSheets {
        val menu = MenuBuilder(mContext)
        SupportMenuInflater(mContext).inflate(menuId, menu)

        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            addItem(item.itemId, item.title.toString(), false)
        }

        return this
    }

    fun setMenuItemSelected(menuItemSelected: OnMenuItemSelected): ProductEditOptionMenuBottomSheets {
        this.menuItemSelected = menuItemSelected
        return this
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_base_list
    }

    override fun initView(view: View) {
        val recyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView
        val adapter = ProductEditOptionMenuAdapter(mode, optionMenus)
        adapter.setOptionMenuItemSelected(menuItemSelected)
        if (mode == ProductEditOptionMenuAdapter.MODE_CHECKABLE) {
            adapter.setSelectedId(selectedId)
        }
        recyclerView.adapter = adapter

    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val btnClose = getDialog().findViewById<View>(com.tokopedia.design.R.id.btn_close)
        btnClose.setOnClickListener { dismiss() }
    }

    override fun title() =  title

    fun addItem(id: Int, title: String, isSelected: Boolean) {
        if (isSelected) {
            selectedId = id
        }
        optionMenus.add(ProductEditOptionMenu(title, id))
    }

    interface OnMenuItemSelected {
        fun onItemSelected(itemId: Int)
    }
}
