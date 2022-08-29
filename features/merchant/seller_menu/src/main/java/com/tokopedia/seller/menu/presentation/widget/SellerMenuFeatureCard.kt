package com.tokopedia.seller.menu.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.databinding.LayoutSellerMenuFeatureCardBinding
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_PLAY_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_PROMO_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_FINANCE_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_STATISTICS_ICON
import com.tokopedia.unifycomponents.CardUnify

class SellerMenuFeatureCard(context: Context, attrs: AttributeSet) : CardUnify(context, attrs) {

    private val iconURLs = listOf(
        URL_PROMO_ICON,
        URL_PLAY_ICON,
        URL_STATISTICS_ICON,
        URL_FINANCE_ICON
    )

    private var binding: LayoutSellerMenuFeatureCardBinding? = null

    init {
        inflateView()
        initView(attrs)
    }

    private fun inflateView() {
        binding =
            LayoutSellerMenuFeatureCardBinding.inflate(
                LayoutInflater.from(context), this, true)
    }

    private fun initView(attrs: AttributeSet) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.SellerMenuFeatureCard)

        val title = styledAttrs.getString(R.styleable.SellerMenuFeatureCard_menuTitle)
        val description = styledAttrs.getString(R.styleable.SellerMenuFeatureCard_menuDescription)
        val iconAttr = styledAttrs.getInt(R.styleable.SellerMenuFeatureCard_menuIcon, 0)

        binding?.textTitle?.text = title
        binding?.textDescription?.text = description

        iconURLs.getOrNull(iconAttr)?.let {
            binding?.imageFeature?.setImageUrl(it)
        }

        styledAttrs.recycle()
    }
}