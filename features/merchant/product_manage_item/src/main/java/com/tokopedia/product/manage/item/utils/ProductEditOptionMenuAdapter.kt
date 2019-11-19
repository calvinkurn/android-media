package com.tokopedia.product.manage.item.utils

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.product.manage.item.R

class ProductEditOptionMenuAdapter(mode: Int, private val optionMenus: List<ProductEditOptionMenu>) : RecyclerView.Adapter<ProductEditOptionMenuViewHolder>() {
    private val isCheckEnable: Boolean
    private var selectedId: Int = 0

    private var onMenuItemSelected: ProductEditOptionMenuBottomSheets.OnMenuItemSelected? = null

    init {
        isCheckEnable = mode == MODE_CHECKABLE
        if (optionMenus.isNotEmpty()) {
            selectedId = optionMenus[0].id
        }
    }

    fun setSelectedId(selectedId: Int) {
        this.selectedId = selectedId
    }

    fun setOptionMenuItemSelected(onMenuItemSelected: ProductEditOptionMenuBottomSheets.OnMenuItemSelected) {
        this.onMenuItemSelected = onMenuItemSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductEditOptionMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_edit_menu_option, parent, false)
        return ProductEditOptionMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductEditOptionMenuViewHolder, position: Int) {
        val optionMenu = optionMenus[position]
        holder.onBind(optionMenu, isCheckEnable && selectedId == optionMenu.id)
        holder.itemView.setOnClickListener({
            if (isCheckEnable) {
                selectedId = position
                notifyDataSetChanged()
            }
            onMenuItemSelected?.onItemSelected(optionMenu.id)
        })
    }

    override fun getItemCount(): Int {
        return optionMenus.size
    }

    companion object {
        const val MODE_DEFAULT = 0
        const val MODE_CHECKABLE = 1
    }
}
