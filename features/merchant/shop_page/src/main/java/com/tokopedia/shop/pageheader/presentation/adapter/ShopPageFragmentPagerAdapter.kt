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
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.pageheader.data.model.ShopPageTabModel
import kotlinx.android.synthetic.main.shop_page_tab_view.view.*
import java.lang.ref.WeakReference

internal class ShopPageFragmentPagerAdapter(
        ctx: Context?,
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {
    private val registeredFragments = SparseArrayCompat<Fragment>()
    private var listShopPageTabModel = listOf<ShopPageTabModel>()

    private companion object {
        val tabViewLayout = R.layout.shop_page_tab_view
    }

    private val ctxRef = WeakReference(ctx)

    override fun getCount(): Int = listShopPageTabModel.size

    override fun getItem(position: Int): Fragment {
        return listShopPageTabModel[position].tabFragment
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
        if (ShopUtil.isUsingNewNavigation()) {
            val tabIconActiveSrc = listShopPageTabModel[position].tabIconActive
            val tabIconInactiveSrc = listShopPageTabModel[position].tabIconInactive
            if (isActive) {
                MethodChecker.getDrawable(this, tabIconActiveSrc.takeIf { it != -1 }?:tabIconInactiveSrc)
            } else {
                MethodChecker.getDrawable(this, tabIconInactiveSrc)
            }
        } else {
            MethodChecker.getDrawable(
                    this,
                    listShopPageTabModel[position].tabIconInactive
            )?.let { iconDrawable ->
                DrawableCompat.wrap(iconDrawable)
            }?.also { iconDrawable ->
                DrawableCompat.setTint(iconDrawable, ContextCompat.getColor(
                        this,
                        if (isActive) getTabActivateColor() else getTabInactiveColor()
                ))
            }
        }
    }

    private fun getTabInactiveColor(): Int {
        return if (ShopUtil.isUsingNewNavigation())
            R.color.color_gray_shop_tab_new
        else
            com.tokopedia.unifyprinciples.R.color.Unify_N200
    }

    private fun getTabActivateColor(): Int {
        return if (ShopUtil.isUsingNewNavigation())
            R.color.color_green_shop_tab_new
        else
            com.tokopedia.unifyprinciples.R.color.Unify_G500
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

    fun setTabData(listShopPageTabModel: List<ShopPageTabModel>) {
        this.listShopPageTabModel = listShopPageTabModel
    }

    fun getFragmentPosition(classType: Class<*>): Int {
        var fragmentPosition = 0
        listShopPageTabModel.forEachIndexed { index, shopPageTabModel ->
            if(shopPageTabModel.tabFragment::class.java == classType){
                fragmentPosition = index
            }
        }
        return fragmentPosition
    }

    fun isFragmentObjectExists(classType: Class<*>): Boolean {
        return listShopPageTabModel.firstOrNull {
            it.tabFragment::class.java == classType
        } != null
    }
}
