package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListProductInCartItemBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductCartItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListProductCartItemViewHolder

class ShoppingListProductInCartAdapter(
    private var itemList: List<ShoppingListProductCartItemUiModel>
) : RecyclerView.Adapter<ShoppingListProductCartItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingListProductCartItemViewHolder = ShoppingListProductCartItemViewHolder(
        ItemTokopedianowShoppingListProductInCartItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: ShoppingListProductCartItemViewHolder,
        position: Int
    ) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size
}
