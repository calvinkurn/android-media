package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemProductManageMenuBinding
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.utils.view.binding.viewBinding
import java.util.Date

class ProductMenuViewHolder(
    itemView: View,
    private val listener: ProductMenuListener
): AbstractViewHolder<ProductMenuUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_menu
    }

    private val binding by viewBinding<ItemProductManageMenuBinding>()


    override fun bind(menu: ProductMenuUiModel) {
        binding?.run {
            textMenu.text = itemView.context?.getString(menu.title).orEmpty()
            if (getString(menu.title) == getString(R.string.product_manage_create_broadcast_chat)) {
                itemView.context?.let {
                    icuPmlMoreMenu.setImageDrawable(
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.ic_bc_chat
                        )
                    )
                }
            } else {
                icuPmlMoreMenu.setImage(menu.icon)
            }
            labelProductManageMenuNew.showIfNew(menu.newTagEndMillis)
            if (menu.limitFromShopModerate) {
                icuPmlMoreMenu.isEnabled = false
                textMenu.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN400
                    )
                )
                itemView.setOnClickListener(null)
            } else {
                icuPmlMoreMenu.isEnabled = true
                textMenu.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950
                    )
                )
                itemView.setOnClickListener { listener.onClickOptionMenu(menu) }
            }

            if (menu.title == R.string.product_manage_stock_reminder_menu) {
                listener.onFinishBindMenuReminder(itemView)
            }
        }
    }

    private fun Label.showIfNew(newTagEndMillis: Long?) {
        val shouldShowLabel =
            newTagEndMillis?.let {
                val todayMillis = Date().time
                todayMillis < it
            }.orFalse()
        showWithCondition(shouldShowLabel)
    }

    interface ProductMenuListener {
        fun onClickOptionMenu(menu: ProductMenuUiModel)
        fun onFinishBindMenuReminder(view:View)
    }
}
