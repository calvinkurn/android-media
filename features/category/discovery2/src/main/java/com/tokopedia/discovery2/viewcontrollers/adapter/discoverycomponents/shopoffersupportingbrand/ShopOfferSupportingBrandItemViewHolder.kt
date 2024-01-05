package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.ItemDiscoveryShopOfferSupportingBrandLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.modeAware
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopOfferSupportingBrandItemViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding: ItemDiscoveryShopOfferSupportingBrandLayoutBinding?
        by viewBinding()

    private var viewModel: ShopOfferSupportingBrandItemViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? ShopOfferSupportingBrandItemViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let { lifeCycle ->
            viewModel?.getComponentData()?.observe(lifeCycle) {
                renderSupportingBrand(it)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        if (lifecycleOwner == null) return

        viewModel?.getComponentData()?.removeObservers(lifecycleOwner)
    }

    private fun renderSupportingBrand(item: DataItem) {
        binding?.run {
            setCardBackgroundColor(item)
            loadShopInfo(item.shopLogo, item.shopName)

            ctaText.text = item.buttonText
            offerTier.text = item.offerTiers?.firstOrNull()?.tierWording.orEmpty()

            firstProductImage.run {
                val product = item.products?.first()

                loadProductImage(product?.imageURL)
                handleClickAction(product?.redirectAppLink)
            }

            secondProductImage.run {
                val product = item.products?.get(SECOND_INDEX)

                loadProductImage(product?.imageURL)
                handleClickAction(product?.redirectAppLink)
            }

            item.fontColor?.let {
                renderSeeMoreButton(it)
                setTextColor(it)
            }

            handleClickAction(item.applinks)
        }
    }

    private fun ItemDiscoveryShopOfferSupportingBrandLayoutBinding.renderSeeMoreButton(
        mode: String
    ) {
        seeAllIcon.apply {
            setImageDrawable(getSeeMoreIcon(mode))
            background = getSeeMoreBackground(mode)
        }
    }

    private fun getSeeMoreBackground(mode: String): Drawable? {
        val drawable = ContextCompat.getDrawable(
            itemView.context,
            R.drawable.circular_see_product
        )

        drawable?.setTint(
            ContextCompat.getColor(
                getColorModeContext(mode),
                unifyprinciplesR.color.Unify_NN950_68
            )
        )
        return drawable
    }

    private fun getColorModeContext(mode: String): Context {
        val colorMode = when (mode) {
            TEXT_LIGHT_MODE -> ColorMode.DARK_MODE
            TEXT_DARK_MODE -> ColorMode.LIGHT_MODE
            else -> ColorMode.DEFAULT
        }

        return itemView.context.modeAware(colorMode) ?: itemView.context
    }

    private fun getSeeMoreIcon(mode: String): Drawable? {
        val drawable = ContextCompat.getDrawable(
            itemView.context,
            unifycomponentsR.drawable.iconunify_chevron_right
        )

        drawable?.setTint(
            ContextCompat.getColor(
                getColorModeContext(mode),
                unifyprinciplesR.color.Unify_NN950
            )
        )

        return drawable
    }

    private fun ItemDiscoveryShopOfferSupportingBrandLayoutBinding.handleClickAction(appLink: String?) {
        if (appLink.isNullOrBlank()) return

        container.setOnClickListener {
            viewModel?.component?.run {
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                    ?.trackSupportingBrandClick(this, ACTION_TYPE_BANNER)
            }

            RouteManager.route(itemView.context, appLink)
        }
    }

    private fun ImageUnify.loadProductImage(imageUrl: String?) {
        imageUrl?.let {
            loadImage(it)
        }
    }

    private fun ImageUnify.handleClickAction(appLink: String?) {
        if (appLink.isNullOrBlank()) return

        setOnClickListener {
            viewModel?.component?.run {
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                    ?.trackSupportingBrandClick(this, ACTION_TYPE_PRODUCT)
            }

            RouteManager.route(itemView.context, appLink)
        }
    }

    private fun ItemDiscoveryShopOfferSupportingBrandLayoutBinding.loadShopInfo(
        logoUrl: String?,
        name: String?
    ) {
        shopLogo.loadImage(logoUrl)
        shopName.text = name
    }

    @SuppressLint("Range")
    private fun ItemDiscoveryShopOfferSupportingBrandLayoutBinding.setCardBackgroundColor(
        item: DataItem
    ) {
        container.backgroundTintList = ColorStateList.valueOf(
            Color.parseColor(item.boxColor)
        )
    }

    private fun ItemDiscoveryShopOfferSupportingBrandLayoutBinding.setTextColor(mode: String) {
        val context = getColorModeContext(mode)

        val textColorWithOpacity = MethodChecker.getColor(
            context,
            unifyprinciplesR.color.Unify_NN950_68
        )

        shopName.setTextColor(textColorWithOpacity)

        val textColor = MethodChecker.getColor(
            context,
            unifyprinciplesR.color.Unify_NN950
        )

        offerTier.setTextColor(textColor)
        ctaText.setTextColor(textColor)
    }

    companion object {
        private const val SECOND_INDEX = 1
        private const val TEXT_LIGHT_MODE = "light"
        private const val TEXT_DARK_MODE = "dark"
        private const val ACTION_TYPE_BANNER = "banner"
        private const val ACTION_TYPE_PRODUCT = "product"
    }
}
