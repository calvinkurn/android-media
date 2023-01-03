package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.view.View
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.LayoutShopActionButtonWidgetChatButtonComponentBinding
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class ShopActionButtonWidgetChatButtonComponentViewHolder(
    itemView: View,
    private val shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel,
    private val shopActionButtonWidgetChatButtonComponentListener: Listener
) : AbstractViewHolder<ShopHeaderButtonComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_action_button_widget_chat_button_component
    }

    interface Listener {
        fun onButtonChatClicked(
            componentModel: ShopHeaderButtonComponentUiModel,
            shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel
        )

        fun onImpressionButtonChat(
            componentModel: ShopHeaderButtonComponentUiModel,
            shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel
        )
    }

    private val viewBinding: LayoutShopActionButtonWidgetChatButtonComponentBinding? by viewBinding()
    private val buttonChat: UnifyButton? = viewBinding?.buttonShopChat

    override fun bind(model: ShopHeaderButtonComponentUiModel) {
        val lp = itemView.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            lp.flexGrow = 1.0f
        }
        buttonChat?.apply {
            text = model.label
            setOnClickListener {
                shopActionButtonWidgetChatButtonComponentListener.onButtonChatClicked(
                    model,
                    shopHeaderWidgetUiModel
                )
            }
            addOnImpressionListener(model) {
                shopActionButtonWidgetChatButtonComponentListener.onImpressionButtonChat(
                    model,
                    shopHeaderWidgetUiModel
                )
            }
        }
    }
}
