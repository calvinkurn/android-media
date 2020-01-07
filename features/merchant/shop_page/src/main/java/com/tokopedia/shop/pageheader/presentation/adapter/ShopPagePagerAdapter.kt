package com.tokopedia.shop.pageheader.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LiveData
import com.google.android.material.tabs.TabLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.shop_page_tab_view.view.*
import java.lang.ref.WeakReference

internal class ShopPagePagerAdapter(
        ctx: Context?,
        fragmentManager: FragmentManager,
        val shopData: LiveData<Result<ShopInfo>>
) : FragmentStatePagerAdapter(fragmentManager) {

    private companion object {
        const val SHOP_PAGE_HOME = 0
        const val SHOP_PAGE_PRODUCTS = 1
        const val SHOP_PAGE_FEED = 2
        const val SHOP_PAGE_REVIEW = 3

        val tabViewLayout = R.layout.shop_page_tab_view
    }

    private val ctxRef = WeakReference(ctx)

    override fun getCount(): Int = 4

    override fun getItem(position: Int): Fragment {
        val fragment = ShopInfoFragment.createInstance()
        (shopData.value as? Success)?.also { result ->
            fragment.shopInfo = result.data
        }

        return fragment
    }

    @SuppressLint("InflateParams")
    fun getTabView(position: Int): View? = LayoutInflater.from(ctxRef.get())
            .inflate(tabViewLayout, null)?.apply {
                shop_page_tab_view_icon.setImageDrawable(getTabIconDrawable(position))
            }

    fun handleSelectedTab(tab: TabLayout.Tab, isActive: Boolean) {
        tab.customView?.apply {
            shop_page_tab_view_icon.setImageDrawable(getTabIconDrawable(tab.position, isActive))
        }
    }

    private fun getTabIconDrawable(position: Int, isActive: Boolean = false): Drawable? = ctxRef.get()?.run {
        ContextCompat.getDrawable(
                this,
                when (position) {
                    SHOP_PAGE_HOME -> R.drawable.ic_shop_tab_home_inactive
                    SHOP_PAGE_PRODUCTS -> R.drawable.ic_shop_tab_products_inactive
                    SHOP_PAGE_FEED -> R.drawable.ic_shop_tab_feed_inactive
                    SHOP_PAGE_REVIEW -> R.drawable.ic_shop_tab_review_inactive
                    else -> R.drawable.ic_shop_tab_home_inactive
                }
        )?.let { iconDrawable ->
            DrawableCompat.wrap(iconDrawable)
        }?.also { iconDrawable ->
            DrawableCompat.setTint(iconDrawable, ContextCompat.getColor(
                    this,
                    if (isActive) R.color.Green_G500 else R.color.Neutral_N200
            ))
        }
    }
}
