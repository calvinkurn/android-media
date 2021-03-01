package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.R.attr.path
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton




class ShopActionButtonWidgetChatButtonComponentViewHolder(
        itemView: View
) : AbstractViewHolder<ShopHeaderButtonComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_action_button_widget_chat_button_component
    }

    private val buttonChat: UnifyButton? = itemView.findViewById(R.id.button_shop_chat)

    override fun bind(model: ShopHeaderButtonComponentUiModel) {
        val lp = itemView.layoutParams
        if(lp is FlexboxLayoutManager.LayoutParams){
            val flexboxLp = lp as FlexboxLayoutManager.LayoutParams
            flexboxLp.flexGrow = 1.0f;
        }
//        buttonChat?.text = model.label
    }

}