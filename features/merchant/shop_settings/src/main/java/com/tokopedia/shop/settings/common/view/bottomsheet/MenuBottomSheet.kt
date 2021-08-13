package com.tokopedia.shop.settings.common.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.view.adapter.MenuAdapter
import com.tokopedia.shop.settings.common.view.adapter.viewholder.MenuViewHolder
import com.tokopedia.shop.settings.etalase.data.PowerMerchantAccessModel
import com.tokopedia.shop.settings.etalase.view.bottomsheet.PowerMerchantAccessBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify

class MenuBottomSheet: BottomSheetUnify(){

    companion object {
        private const val LIST = "MENU_LIST"

        @JvmStatic
        fun newInstance(menuList: ArrayList<String>): MenuBottomSheet = MenuBottomSheet().apply {
            arguments = Bundle().apply {
                putStringArrayList(LIST, menuList)
            }
        }
    }

    private var listener: MenuViewHolder.ItemMenuListener? = null
    private var contentView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.fragment_menu, null)
        setChild(contentView)
    }

    private fun setData() {
        val menuList =  arguments?.getStringArrayList(LIST)
        val rvMenu = contentView?.findViewById<RecyclerView>(R.id.rv_menu)
        val menuAdapter = MenuAdapter(listener)
        rvMenu?.adapter = menuAdapter
        rvMenu?.layoutManager = LinearLayoutManager(context)
        menuList?.let {
            menuAdapter.setItemsAndAnimateChanges(it)
        }
    }

    fun setListener(listener: MenuViewHolder.ItemMenuListener?) {
        this.listener = listener
    }
}
