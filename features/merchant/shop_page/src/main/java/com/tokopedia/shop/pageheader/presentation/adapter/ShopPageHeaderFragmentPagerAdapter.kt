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
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.ShopUtil.isUrlJson
import com.tokopedia.shop.common.util.ShopUtil.isUrlPng
import com.tokopedia.shop.databinding.ShopPageDynamicTabViewBinding
import com.tokopedia.shop.databinding.ShopPageTabViewBinding
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderTabIconUrlModel
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderTabModel
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragmentTabContentWrapper
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import java.lang.ref.WeakReference

internal class ShopPageHeaderFragmentPagerAdapter(
    private val ctx: Context?,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    private var listShopPageTabModel = listOf<ShopPageHeaderTabModel>()
    private val ctxRef = WeakReference(ctx)
    private var isOverrideTheme: Boolean = false
    private var patternColorType: String = ""

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
            configTabViewBinding(it, tab, isActive) ?: configDynamicTabViewBinding(it, tab, isActive)
        }
    }

    private fun configTabViewBinding(
        view: View,
        tab: TabLayout.Tab,
        isActive: Boolean
    ): ShopPageTabViewBinding? {
        return getTabViewBinding(view)?.apply {
            val shopPageTabViewIcon: IconUnify = this.shopPageTabViewIcon
            setTabIcon(shopPageTabViewIcon, tab.position, isActive)
        }
    }

    private fun getTabViewBinding(view: View): ShopPageTabViewBinding? {
        return try {
            ShopPageTabViewBinding.bind(view)
        } catch (_: Exception) {
            null
        }
    }

    private fun configDynamicTabViewBinding(
        view: View,
        tab: TabLayout.Tab,
        isActive: Boolean
    ): ShopPageDynamicTabViewBinding? {
        return getDynamicTabViewBinding(view)?.apply {
            setupTabView(this, tab.position, isActive)
        }
    }

    private fun setupTabView(
        shopPageDynamicTabViewBinding: ShopPageDynamicTabViewBinding,
        position: Int,
        isActive: Boolean
    ) {
        setDynamicTabIcon(shopPageDynamicTabViewBinding, position, isActive)
        if (ShopUtil.isEnableShopPageReImagined()) {
            setTabName(shopPageDynamicTabViewBinding.textTabName, position, isActive)
        } else {
            shopPageDynamicTabViewBinding.textTabName.hide()
        }
    }

    private fun setTabName(textTabName: Typography, position: Int, active: Boolean) {
        ctx?.let {
            textTabName.apply {
                show()
                //TODO need to check colorSchema for this one
//                if (active) {
//                    setTextColor(ContextCompat.getColor(it, ICON_COLOR_LIGHT_ENABLE))
//                } else {
//                    setTextColor(ContextCompat.getColor(it, ICON_COLOR_LIGHT))
//                }
                text = listShopPageTabModel.getOrNull(position)?.tabText.orEmpty()
            }
        }
    }

    private fun getDynamicTabViewBinding(view: View): ShopPageDynamicTabViewBinding? {
        return try {
            ShopPageDynamicTabViewBinding.bind(view)
        } catch (_: Exception) {
            null
        }
    }

    fun getDynamicTabView(position: Int, selectedPosition: Int): View = ShopPageDynamicTabViewBinding.inflate(LayoutInflater.from(ctxRef.get())).apply {
        setupTabView(this, position, position == selectedPosition)
    }.root

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
            CommonUtil.fromJson<ShopPageHeaderTabIconUrlModel>(
                iconDataJsonString,
                ShopPageHeaderTabIconUrlModel::class.java
            ).run {
                if(isOverrideTheme){
                    configIconColorByBackgroundTheme(lightModeUrl, darkModeUrl)
                }else{
                    configIconColorByDeviceTheme(lightModeUrl, darkModeUrl)
                }
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun configIconColorByDeviceTheme(lightModeUrl: String, darkModeUrl: String): String {
        return if (ctx?.isDarkMode() == true) {
            darkModeUrl
        } else {
            lightModeUrl
        }
    }

    private fun configIconColorByBackgroundTheme(
        lightModeUrl: String,
        darkModeUrl: String
    ): String {
        return if (patternColorType == ShopPageHeaderLayoutUiModel.ColorType.DARK.value) {
            lightModeUrl
        } else {
            darkModeUrl
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

    override fun getItemCount(): Int = listShopPageTabModel.size

    override fun createFragment(position: Int): Fragment = listShopPageTabModel[position].tabFragment

    fun getRegisteredFragment(position: Int): Fragment? {
        return if (listShopPageTabModel.isNotEmpty()) {
            listShopPageTabModel.getOrNull(position)?.tabFragment
        } else {
            null
        }
    }

    fun setTabData(listShopPageHeaderTabModel: List<ShopPageHeaderTabModel>) {
        this.listShopPageTabModel = listShopPageHeaderTabModel
    }

    fun getFragmentPosition(classType: Class<*>): Int {
        var fragmentPosition = 0
        if(ShopUtil.isEnableShopPageReImagined()) {
            listShopPageTabModel.forEachIndexed { index, shopPageTabModel ->
                val tabFragmentInsideWrapper = (shopPageTabModel.tabFragment as? ShopPageHeaderFragmentTabContentWrapper)?.getTabFragment()
                tabFragmentInsideWrapper?.let {
                    if (it::class.java == classType) {
                        fragmentPosition = index
                    }
                }
            }
        } else {
            listShopPageTabModel.forEachIndexed { index, shopPageTabModel ->
                if (shopPageTabModel.tabFragment::class.java == classType) {
                    fragmentPosition = index
                }
            }
        }
        return fragmentPosition
    }

    fun isFragmentObjectExists(classType: Class<*>): Boolean {
        return if (ShopUtil.isEnableShopPageReImagined()) {
            listShopPageTabModel.firstOrNull {
                val tabFragmentInsideWrapper =
                    (it.tabFragment as? ShopPageHeaderFragmentTabContentWrapper)?.getTabFragment()
                tabFragmentInsideWrapper?.let { tabFragmentInsideWrapper ->
                    tabFragmentInsideWrapper::class.java == classType
                }.orFalse()
            } != null
        } else {
            listShopPageTabModel.firstOrNull {
                it.tabFragment::class.java == classType
            } != null
        }

    }

    fun setPageTheme(isOverrideTheme: Boolean, patternColorType: String) {
        this.isOverrideTheme = isOverrideTheme
        this.patternColorType = patternColorType
    }
}
