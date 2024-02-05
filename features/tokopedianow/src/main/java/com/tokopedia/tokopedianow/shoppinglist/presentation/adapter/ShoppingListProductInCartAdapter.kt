package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListProductInCartItemBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListProductInCartItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.ShoppingListProductInCartItemViewHolder

class ShoppingListProductInCartAdapter(
    private var itemList: List<ShoppingListProductInCartItemUiModel>
) : RecyclerView.Adapter<ShoppingListProductInCartItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingListProductInCartItemViewHolder {
        return ShoppingListProductInCartItemViewHolder(
            ItemTokopedianowShoppingListProductInCartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ShoppingListProductInCartItemViewHolder,
        position: Int
    ) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size
}
