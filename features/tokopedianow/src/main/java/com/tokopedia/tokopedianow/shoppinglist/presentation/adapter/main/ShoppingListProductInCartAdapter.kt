package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListProductInCartItemBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListCartProductItemViewHolder

class ShoppingListProductInCartAdapter(
    private var itemList: List<ShoppingListCartProductItemUiModel>
) : RecyclerView.Adapter<ShoppingListCartProductItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingListCartProductItemViewHolder = ShoppingListCartProductItemViewHolder(
        ItemTokopedianowShoppingListProductInCartItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: ShoppingListCartProductItemViewHolder,
        position: Int
    ) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size
}
