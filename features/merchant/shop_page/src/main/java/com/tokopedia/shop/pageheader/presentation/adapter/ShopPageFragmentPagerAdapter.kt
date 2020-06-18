package com.tokopedia.shop.pageheader.presentation.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment
import kotlinx.android.synthetic.main.shop_page_tab_view.view.*
import java.lang.ref.WeakReference

internal class ShopPageFragmentPagerAdapter(
        ctx: Context?,
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {
    private val registeredFragments = SparseArrayCompat<Fragment>()
    private var listTitleIcon = listOf<Int>()
    private var listFragment = listOf<Fragment>()

    private companion object {
        val tabViewLayout = R.layout.shop_page_tab_view
    }

    private val ctxRef = WeakReference(ctx)

    override fun getCount(): Int = listFragment.size

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    fun getTabView(position: Int, selectedPosition: Int): View? = LayoutInflater.from(ctxRef.get())
            .inflate(tabViewLayout, null)?.apply {
                shop_page_tab_view_icon.setImageDrawable(getTabIconDrawable(position,  position == selectedPosition))
            }

    fun handleSelectedTab(tab: TabLayout.Tab, isActive: Boolean) {
        tab.customView?.apply {
            shop_page_tab_view_icon.setImageDrawable(getTabIconDrawable(tab.position, isActive))
        }
    }

    private fun getTabIconDrawable(position: Int, isActive: Boolean = false): Drawable? = ctxRef.get()?.run {
        MethodChecker.getDrawable(
                this,
                listTitleIcon[position]
        )?.let { iconDrawable ->
            DrawableCompat.wrap(iconDrawable)
        }?.also { iconDrawable ->
            DrawableCompat.setTint(iconDrawable, ContextCompat.getColor(
                    this,
                    if (isActive) R.color.Green_G500 else R.color.Neutral_N200
            ))
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragments.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }

    fun setTabData(tabData: Pair<List<Int>, List<Fragment>>) {
        listTitleIcon = tabData.first
        listFragment = tabData.second
    }

    fun getFragmentPosition(classType: Class<*>): Int {
        return listFragment.filterIsInstance(classType).firstOrNull()?.let {
            return listFragment.indexOf(it).takeIf { index ->
                index >= 0
            } ?: 0
        } ?: 0
    }

    fun isFragmentObjectExists(classType: Class<*>): Boolean {
        return listFragment.filterIsInstance(classType).firstOrNull() != null
    }
}
