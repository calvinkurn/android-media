package com.tokopedia.seller.menu.common.view.widget

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_PLAY_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_PROMO_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_FINANCE_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_STATISTICS_ICON
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.layout_seller_menu_feature_card.view.*

class SellerMenuFeatureCard(context: Context, attrs: AttributeSet) : CardUnify(context, attrs) {

    private val iconURLs = listOf(
        URL_PROMO_ICON,
        URL_PLAY_ICON,
        URL_STATISTICS_ICON,
        URL_FINANCE_ICON
    )

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
        val iconAttr = styledAttrs.getInt(R.styleable.SellerMenuFeatureCard_menuIcon, 0)

        textTitle.text = title
        textDescription.text = description

        iconURLs.getOrNull(iconAttr)?.let {
            imageFeature.setImageUrl(it)
        }

        styledAttrs.recycle()
    }
}