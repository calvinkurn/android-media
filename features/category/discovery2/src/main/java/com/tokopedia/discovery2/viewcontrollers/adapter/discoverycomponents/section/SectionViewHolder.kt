package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.home_component.util.ImageHandler
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LocalLoad

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
                    viewModel?.getSectionID()?.let { it1 -> (fragment as DiscoveryFragment).handleHideSection(it1) }
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
        }
    }

    private fun addChildComponent() {
        val sectionComponent = viewModel?.components

        val items = sectionComponent?.getComponentsItem() ?: return

        val shouldSupportFestive = items.find { !it.isBackgroundPresent } == null

        if (!shouldSupportFestive) return

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

        festiveForeground.loadImageWithoutPlaceholder(
            imageUrl,
            listener = object : ImageHandler.ImageLoaderStateListener {
                override fun successLoad(view: ImageView?) {
                    // no-op
                }

                override fun failedLoad(view: ImageView?) {
                    view?.hide()
                }
            }
        )
    }

    private fun renderFestiveBackground(imageUrl: String) {
        festiveBackground.show()

        festiveBackground.loadImageWithoutPlaceholder(
            imageUrl,
            listener = object : ImageHandler.ImageLoaderStateListener {
                override fun successLoad(view: ImageView?) {
                    // no-op
                }

                override fun failedLoad(view: ImageView?) {
                    view?.hide()
                }
            }
        )
    }

    private fun resetFestiveSection() {
        festiveContainer.removeAllViews()

        festiveBackground.layout(0, 0, 0, 0)
        festiveBackground.hide()

        festiveForeground.layout(0, 0, 0, 0)
        festiveForeground.hide()
    }

    private fun addComponentView(item: ComponentsItem) {
        val component = ComponentsList.values().find { it.componentName == item.name } ?: return

        component.let {
            festiveContainer.addView(
                CustomViewCreator.getCustomViewObject(itemView.context, it, item, fragment)
            )
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.hideSection?.removeObservers(it)
            viewModel?.getSyncPageLiveData()?.removeObservers(it)
            viewModel?.hideShimmerLD?.removeObservers(it)
            viewModel?.showErrorState?.removeObservers(it)
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
}
