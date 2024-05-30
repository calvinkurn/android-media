package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.R.dimen.festive_section_min_height
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section.model.NotifyPayload
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.ShopOfferHeroBrandViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.model.BmGmTierData
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.home_component.util.ImageLoaderStateListener
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifycomponents.toPx

class SectionViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    var viewModel: SectionViewModel? = null
    val shimmer: ConstraintLayout = itemView.findViewById(R.id.section_shimmer)
    private var carouselEmptyState: LocalLoad = itemView.findViewById(R.id.viewEmptyState)

    private val festiveContainer: LinearLayoutCompat = itemView.findViewById(R.id.festiveContainer)

    private val festiveBackground: AppCompatImageView =
        itemView.findViewById(R.id.festiveBackground)

    private val festiveForeground: AppCompatImageView =
        itemView.findViewById(R.id.festiveForeground)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as SectionViewModel
        viewModel?.let {
            getSubComponent().inject(it)
        }
        viewModel?.shouldShowShimmer()?.let { shimmer.showWithCondition(it) }
        viewModel?.shouldShowError()?.let { carouselEmptyState.showWithCondition(it) }

        resetFestiveSection()
        addChildComponent()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.hideSection?.observe(it) { shouldHideSection ->
                if (shouldHideSection) {
                    viewModel?.getSectionID()
                        ?.let { it1 -> (fragment as DiscoveryFragment).handleHideSection(it1) }
                }
            }
            viewModel?.getSyncPageLiveData()?.observe(it) { shouldSync ->
                if (shouldSync) {
                    (fragment as DiscoveryFragment).reSync()
                }
            }

            viewModel?.hideShimmerLD?.observe(it) { shouldHideShimmer ->
                if (shouldHideShimmer) {
                    shimmer.hide()
                    carouselEmptyState.hide()
                }
            }

            viewModel?.showErrorState?.observe(it) { shouldShowError ->
                if (shouldShowError) {
                    handleError()
                }
            }

            viewModel?.notifyChild?.observe(it) { payload ->
                notifyChildViewModel(payload)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)

        val lifecycle = lifecycleOwner ?: return
        viewModel?.run {
            hideSection.removeObservers(lifecycle)
            getSyncPageLiveData().removeObservers(lifecycle)
            hideShimmerLD.removeObservers(lifecycle)
            showErrorState.removeObservers(lifecycle)
            notifyChild.removeObservers(lifecycle)
        }
    }

    private fun addChildComponent() {
        val sectionComponent = viewModel?.components

        val items = sectionComponent?.getComponentsItem() ?: return

        val shouldSupportFestive = items.find { !it.isBackgroundPresent } == null

        if (!shouldSupportFestive || items.isEmpty()) return

        items.forEach { item ->
            addComponentView(item)
        }

        sectionComponent.properties?.backgroundImageUrl?.let {
            renderFestiveBackground(it)
        }

        sectionComponent.properties?.foregroundImageUrl?.let {
            renderFestiveForeground(it)
        }
    }

    private fun renderFestiveForeground(imageUrl: String) {
        festiveForeground.show()

        itemView.postDelayed({
            addHeightForeground(festiveForeground)
            festiveForeground.loadImageWithoutPlaceholder(
                imageUrl,
                listener = object : ImageLoaderStateListener {
                    override fun successLoad(view: ImageView) {
                        festiveForeground.minimumHeight = 0
                    }

                    override fun failedLoad(view: ImageView) {
                        view.hide()
                    }
                }
            )
        }, 100)
    }

    private fun renderFestiveBackground(imageUrl: String) {
        festiveBackground.show()

        itemView.postDelayed({
            addHeightBgOnce(festiveBackground)
            festiveBackground.loadImageWithoutPlaceholder(
                imageUrl,
                listener = object : ImageLoaderStateListener {
                    override fun successLoad(view: ImageView) {
                        festiveBackground.minimumHeight = 0
                    }

                    override fun failedLoad(view: ImageView) {
                        view.hide()
                    }
                }
            )
        }, 100)
    }

    private fun addHeightBgOnce(view: View) {
        if (!addHeightBackground && view.height > 0) {
            view.layoutParams.height = view.height + 8.toPx()
            addHeightBackground = true
        }
    }

    private fun addHeightForeground(view: View) {
        if (!addHeightForeground && view.height > 0) {
            view.layoutParams.height = view.height + 8.toPx()
            addHeightForeground = true
        }
    }

    var addHeightForeground = false
    var addHeightBackground = false

    private fun Context?.getMinHeight(): Int? {
        return this?.resources?.getDimensionPixelOffset(festive_section_min_height)
    }

    private fun resetFestiveSection() {
        festiveContainer.removeAllViews()

        val minHeight = itemView.context.getMinHeight()
        minHeight?.run {
            festiveBackground.resetLayout(this)
            festiveBackground.hide()

            festiveForeground.resetLayout(this)
            festiveForeground.hide()
        }
    }

    private fun AppCompatImageView.resetLayout(minHeight: Int) {
        apply {
            layout(0, 0, 0, 0)
            minimumHeight = minHeight
        }
    }

    private fun addComponentView(item: ComponentsItem) {
        val component = ComponentsList.values().find { it.componentName == item.name } ?: return

        component.let {
            festiveContainer.addView(
                CustomViewCreator.getCustomViewObject(itemView.context, it, item, fragment)
            )
        }
    }

    private fun handleError() {
        carouselEmptyState.apply {
            title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
            description?.text =
                context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
            refreshBtn?.setOnClickListener {
                hide()
                shimmer.show()
                viewModel?.reload()
            }
        }
        carouselEmptyState.visible()
        shimmer.hide()
    }

    private fun notifyChildViewModel(payload: NotifyPayload) {
        run loop@{
            festiveContainer.children.forEach { child ->
                val childViewModel = (child as? CustomViewCreator)?.viewModel
                childViewModel?.let { viewModel ->
                    when (payload.type) {
                        ComponentsList.ShopOfferHeroBrand -> {
                            val isFound = notifyShopHeroComponent(viewModel, payload)
                            if (isFound) return@loop
                        }

                        ComponentsList.FlashSaleTokoTab -> {
                            notifyFSTTargetedComponent()
                            return@loop
                        }

                        else -> return@loop
                    }
                }
            }
        }
    }

    private fun notifyFSTTargetedComponent() {
        val carouselView = festiveContainer.children
            .map { it as? CustomViewCreator }
            .filterNotNull()
            .find { it.viewModel is ProductCardCarouselViewModel }

        (carouselView?.viewModel as? ProductCardCarouselViewModel)?.fetchProductCarouselData()
    }

    @Suppress("UNCHECKED_CAST")
    private fun notifyShopHeroComponent(
        childViewModel: DiscoveryBaseViewModel,
        payload: NotifyPayload
    ): Boolean {
        if (childViewModel !is ShopOfferHeroBrandViewModel) return false

        val uniqueId = childViewModel.component.properties?.header?.offerId
        if (uniqueId == payload.identifier) {
            val bmGmTierData = payload.data as? BmGmTierData
            childViewModel.changeTier(
                false,
                bmGmTierData
            )

            return true
        }

        return false
    }
}
