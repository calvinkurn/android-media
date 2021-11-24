package com.tokopedia.shop.pageheader.presentation.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.databinding.ShopPageTabViewBinding
import com.tokopedia.shop.pageheader.data.model.ShopPageTabModel
import java.lang.ref.WeakReference

internal class ShopPageFragmentPagerAdapter(
        ctx: Context?,
        fragment: Fragment
) : FragmentStateAdapter(fragment) {
    private var listShopPageTabModel = listOf<ShopPageTabModel>()
    private val ctxRef = WeakReference(ctx)

    fun getTabView(position: Int, selectedPosition: Int): View = ShopPageTabViewBinding.inflate(LayoutInflater.from(ctxRef.get())).apply {
        val shopPageTabViewIcon: ImageView = this.shopPageTabViewIcon
        shopPageTabViewIcon.setImageDrawable(getTabIconDrawable(position, position == selectedPosition))
    }.root

    fun handleSelectedTab(tab: TabLayout.Tab, isActive: Boolean) {
        tab.customView?.let {
            ShopPageTabViewBinding.bind(it).apply {
                val shopPageTabViewIcon: ImageView = this.shopPageTabViewIcon
                shopPageTabViewIcon.setImageDrawable(getTabIconDrawable(tab.position, isActive))
            }
        }
    }

    private fun getTabIconDrawable(position: Int, isActive: Boolean = false): Drawable? = ctxRef.get()?.run {
        val tabIconActiveSrc = listShopPageTabModel[position].tabIconActive
        val tabIconInactiveSrc = listShopPageTabModel[position].tabIconInactive
        if (isActive) {
            MethodChecker.getDrawable(this, tabIconActiveSrc.takeIf { it != -1 }
                    ?: tabIconInactiveSrc)
        } else {
            MethodChecker.getDrawable(this, tabIconInactiveSrc)
        }
    }

    override fun getItemId(position: Int): Long {
        return listShopPageTabModel[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return listShopPageTabModel.any {
            it.hashCode().toLong() == itemId
        }
    }

    override fun getItemCount(): Int =  listShopPageTabModel.size

    override fun createFragment(position: Int): Fragment = listShopPageTabModel[position].tabFragment

    fun getRegisteredFragment(position: Int): Fragment? {
        return if (listShopPageTabModel.isNotEmpty())
            listShopPageTabModel.getOrNull(position)?.tabFragment
        else
            null
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
