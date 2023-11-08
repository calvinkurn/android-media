package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section

import android.util.Log
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class SectionViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    var viewModel: SectionViewModel? = null
    val shimmer: ConstraintLayout = itemView.findViewById(R.id.section_shimmer)
    private var carouselEmptyState: LocalLoad = itemView.findViewById(R.id.viewEmptyState)
    private val container = itemView.findViewById<LinearLayoutCompat>(R.id.section_container)

    private val festiveContainer: LinearLayoutCompat = itemView.findViewById(R.id.festiveContainer)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as SectionViewModel
        viewModel?.let {
            getSubComponent().inject(it)
        }
        viewModel?.shouldShowShimmer()?.let { shimmer.showWithCondition(it) }
        viewModel?.shouldShowError()?.let { carouselEmptyState.showWithCondition(it) }
    }

    private fun addChildComponent() {
        val items = viewModel?.components?.getComponentsItem()

        val backgroundColor = arrayOf(
            unifyprinciplesR.color.Unify_RN200_96,
            unifyprinciplesR.color.Unify_G200_96,
            unifyprinciplesR.color.Unify_B200_96,
            unifyprinciplesR.color.Unify_Y200_96
        ).random()

        container.setBackgroundColor(ContextCompat.getColor(itemView.context, backgroundColor))

        if (items != null) {
            for (item in items) {
                when (item.name) {
                    ComponentNames.ProductCardCarousel.componentName -> {
                        container.addView(
                            CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.ProductCardCarousel,
                                item,
                                fragment
                            )
                        )
                    }

                    ComponentNames.LihatSemua.componentName -> {
                        container.addView(
                            CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.LihatSemua,
                                item,
                                fragment
                            )
                        )
                    }

                    ComponentNames.ProductCardSingle.componentName -> {
                        container.addView(
                            CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.ProductCardSingle,
                                item,
                                fragment
                            )
                        )
                    }

                    ComponentNames.SingleBanner.componentName -> {
                        container.addView(
                            CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.SingleBanner,
                                item,
                                fragment
                            )
                        )
                    }

                    ComponentNames.DoubleBanner.componentName -> {
                        container.addView(
                            CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.DoubleBanner,
                                item,
                                fragment
                            )
                        )
                    }

                    ComponentNames.TripleBanner.componentName -> {
                        container.addView(
                            CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.TripleBanner,
                                item,
                                fragment
                            )
                        )
                    }

                    ComponentNames.QuadrupleBanner.componentName -> {
                        container.addView(
                            CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.QuadrupleBanner,
                                item,
                                fragment
                            )
                        )
                    }

                    ComponentNames.CalendarWidgetCarousel.componentName -> {
                        container.addView(
                            CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.CalendarWidgetCarousel,
                                item,
                                fragment
                            )
                        )
                    }

                    ComponentNames.ProductHighlight.componentName -> {
                        container.addView(
                            CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.ProductHighlight,
                                item,
                                fragment
                            )
                        )
                    }
                }
            }
        }
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
                    addChildComponent()
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
        val items = viewModel?.components?.getComponentsItem()

        val backgroundColor = arrayOf(
            unifyprinciplesR.color.Unify_RN200_96,
            unifyprinciplesR.color.Unify_G200_96,
            unifyprinciplesR.color.Unify_B200_96,
            unifyprinciplesR.color.Unify_Y200_96
        ).random()

        festiveContainer.setBackgroundColor(ContextCompat.getColor(itemView.context, backgroundColor))

        if (items == null) return

        for (item in items) {
            addComponentView(item)
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
