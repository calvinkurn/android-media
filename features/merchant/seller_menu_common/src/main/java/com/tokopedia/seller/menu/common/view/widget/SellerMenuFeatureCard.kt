package com.tokopedia.seller.menu.common.view.widget

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.seller.menu.common.R
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.layout_seller_menu_feature_card.view.*

class SellerMenuFeatureCard(context: Context, attrs: AttributeSet) : CardUnify(context, attrs) {

    init {
        inflateView()
        initView(attrs)
    }

    private fun inflateView() {
        inflate(context, R.layout.layout_seller_menu_feature_card, this)
    }

    private fun initView(attrs: AttributeSet) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.SellerMenuFeatureCard)

        val title = styledAttrs.getString(R.styleable.SellerMenuFeatureCard_menuTitle)
        val description = styledAttrs.getString(R.styleable.SellerMenuFeatureCard_menuDescription)
        val icon = styledAttrs.getDrawable(R.styleable.SellerMenuFeatureCard_menuIcon)

        textTitle.text = title
        textDescription.text = description

        icon?.let {
            textTitle.setCompoundDrawablesWithIntrinsicBounds(it, null, null, null)
        }

        styledAttrs.recycle()
    }
}