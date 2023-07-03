package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.DiscoInfiniteShopBannerItemBinding
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage

private const val DEFAULT_DESIGN = 2

class ShopBannerInfiniteItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: DiscoInfiniteShopBannerItemBinding = DiscoInfiniteShopBannerItemBinding.bind(itemView)
    private var shopBannerInfiniteItemViewModel: ShopBannerInfiniteItemViewModel? = null
    private val displayMetrics = Utils.getDisplayMetric(fragment.context)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        shopBannerInfiniteItemViewModel = discoveryBaseViewModel as ShopBannerInfiniteItemViewModel
        binding.bannerImage.setOnClickListener {
            shopBannerInfiniteItemViewModel?.getNavigationUrl()?.let {
                RouteManager.route(fragment.activity, it)
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackShopBannerInfiniteClick(shopBannerInfiniteItemViewModel!!.components)
            }
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            shopBannerInfiniteItemViewModel?.getComponentLiveData()?.observe(
                fragment.viewLifecycleOwner,
                Observer { componentItem ->
                    componentItem.data?.let {
                        if (it.isNotEmpty()) {
                            setupImage(it.first())
                        }
                    }
                }
            )
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            shopBannerInfiniteItemViewModel?.getComponentLiveData()?.removeObservers(it)
        }
    }

    private fun setupImage(itemData: DataItem) {
        with(binding) {
            try {
                val layoutParams: ViewGroup.LayoutParams = bannerImage.layoutParams
                layoutParams.width = (
                    (displayMetrics.widthPixels - itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_24)) /
                        DEFAULT_DESIGN
                    )
                val height = Utils.extractDimension(itemData.imageUrlDynamicMobile, Constant.Dimensions.HEIGHT)
                val width = Utils.extractDimension(itemData.imageUrlDynamicMobile, Constant.Dimensions.WIDTH)
                if (width != null && height != null && width != 1 && height != 1) {
                    val aspectRatio = width.toFloat() / height.toFloat()
                    layoutParams.height = (layoutParams.width / aspectRatio).toInt()
                } else {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                bannerImage.layoutParams = layoutParams
                bannerImage.apply {
                    loadImage(itemData.imageUrlDynamicMobile)
                }
            } catch (exception: NumberFormatException) {
                bannerImage.hide()
                exception.printStackTrace()
            }
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        shopBannerInfiniteItemViewModel?.components?.let { (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackShopBannerInfiniteImpression(it) }
    }
}
