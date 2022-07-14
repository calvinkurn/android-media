package com.tokopedia.shop.pageheader.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.util.ShopUtil.isUrlJson
import com.tokopedia.shop.common.util.ShopUtil.isUrlPng
import com.tokopedia.shop.databinding.ShopPageDynamicTabViewBinding
import com.tokopedia.shop.databinding.ShopPageTabViewBinding
import com.tokopedia.shop.pageheader.data.model.ShopPageTabModel
import com.tokopedia.shop.pageheader.data.model.ShopTabIconUrlModel
import com.tokopedia.utils.resources.isDarkMode
import java.lang.Exception
import java.lang.ref.WeakReference

internal class ShopPageFragmentPagerAdapter(
        private val ctx: Context?,
        fragment: Fragment
) : FragmentStateAdapter(fragment) {
    private var listShopPageTabModel = listOf<ShopPageTabModel>()
    private val ctxRef = WeakReference(ctx)

    companion object {
        @ColorRes
        private val ICON_COLOR_LIGHT_ENABLE = com.tokopedia.unifyprinciples.R.color.Unify_GN500
        @ColorRes
        private val ICON_COLOR_LIGHT = com.tokopedia.unifyprinciples.R.color.Unify_NN900
    }

    fun getTabView(position: Int, selectedPosition: Int): View = ShopPageTabViewBinding.inflate(LayoutInflater.from(ctxRef.get())).apply {
        val shopPageTabViewIcon: IconUnify = this.shopPageTabViewIcon
        setTabIcon(shopPageTabViewIcon, position, position == selectedPosition)
    }.root

    fun handleSelectedTab(tab: TabLayout.Tab, isActive: Boolean) {
        tab.customView?.let {
            ShopPageTabViewBinding.bind(it).apply {
                val shopPageTabViewIcon: IconUnify = this.shopPageTabViewIcon
                setTabIcon(shopPageTabViewIcon, tab.position, isActive)
            }
        }
    }

    fun getDynamicTabView(position: Int, selectedPosition: Int): View = ShopPageDynamicTabViewBinding.inflate(LayoutInflater.from(ctxRef.get())).apply {
        setDynamicTabIcon(this, position, position == selectedPosition)
    }.root

    fun handleSelectedDynamicTab(tab: TabLayout.Tab, isActive: Boolean) {
        tab.customView?.let {
            ShopPageDynamicTabViewBinding.bind(it).apply {
                setDynamicTabIcon(this, tab.position, isActive)
            }
        }
    }

    private fun getTabIconDrawable(position: Int, isActive: Boolean): Int? = ctxRef.get()?.run {
        return if (isActive) {
            listShopPageTabModel[position].tabIconActive
        } else {
            listShopPageTabModel[position].tabIconInactive
        }
    }

    private fun setTabIcon(tabIconUnify: IconUnify, position: Int, isActive: Boolean) {
        ctx?.let {
            tabIconUnify.apply {
                if (isActive) {
                    val iconId = getTabIconDrawable(position, true)
                    val iconColor = ContextCompat.getColor(it, ICON_COLOR_LIGHT_ENABLE)
                    setImage(newIconId = iconId, newLightEnable = iconColor)
                    isEnabled = true
                } else {
                    val iconId = getTabIconDrawable(position, false)
                    val iconColor = ContextCompat.getColor(it, ICON_COLOR_LIGHT)
                    setImage(newIconId = iconId, newLightEnable = iconColor)
                }
            }
        }
    }

    private fun setDynamicTabIcon(binding: ShopPageDynamicTabViewBinding, position: Int, isActive: Boolean) {
        binding.shopPageDynamicTabViewIcon.hide()
        binding.shopPageDynamicTabLottieView.hide()
        ctx?.let {
            val iconDataJsonString: String = if (isActive) {
                listShopPageTabModel.getOrNull(position)?.iconActiveUrl.orEmpty()
            } else {
                listShopPageTabModel.getOrNull(position)?.iconUrl.orEmpty()
            }
            val iconUrl = getIconUrlFromJsonString(iconDataJsonString)
            when {
                iconUrl.isUrlPng() -> {
                    binding.shopPageDynamicTabViewIcon.apply {
                        show()
                        setImageUrl(iconUrl)
                        isEnabled = true
                    }
                }
                iconUrl.isUrlJson() -> {
                    binding.shopPageDynamicTabLottieView.apply {
                        show()
                        setupLottieAnimation(iconUrl, this)
                        isEnabled = true
                    }
                }
                else -> {}
            }
        }
    }

    private fun getIconUrlFromJsonString(iconDataJsonString: String): String {
        return try {
            Gson().fromJson(
                JsonParser.parseString(iconDataJsonString),
                ShopTabIconUrlModel::class.java
            ).run {
                if (ctx?.isDarkMode() == true) {
                    darkModeUrl
                } else {
                    lightModeUrl
                }
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun setupLottieAnimation(lottieIconUrl: String, lottieIcon: LottieAnimationView) {
        ctx?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, lottieIconUrl)
            lottieCompositionLottieTask.addListener { result ->
                lottieIcon.setComposition(result)
                lottieIcon.visibility = View.VISIBLE
                lottieIcon.playAnimation()
                lottieIcon.repeatCount = LottieDrawable.INFINITE
            }
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
