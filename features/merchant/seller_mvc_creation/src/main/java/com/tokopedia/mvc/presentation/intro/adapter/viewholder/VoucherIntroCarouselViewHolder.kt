package com.tokopedia.mvc.presentation.intro.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.material.tabs.TabLayout
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcIntroVouchersPagerViewholderBinding
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherIntroCarouselUiModel
import com.tokopedia.mvc.util.constant.FIRST_INDEX
import com.tokopedia.mvc.util.constant.SECOND_INDEX
import com.tokopedia.mvc.util.constant.ZEROTH_INDEX
import com.tokopedia.utils.view.binding.viewBinding

class VoucherIntroCarouselViewHolder(itemView: View?) :
    AbstractViewHolder<VoucherIntroCarouselUiModel>(
        itemView
    ),
    CarouselUnify.OnActiveIndexChangedListener {
    private var binding: SmvcIntroVouchersPagerViewholderBinding? by viewBinding()

    override fun bind(element: VoucherIntroCarouselUiModel?) {
        binding?.apply {
            headerText.text = element?.headerTitle
            viewPagerTitle.text = element?.tabsList?.get(ZEROTH_INDEX)?.tabHeader
            viewPagerDescription.text = element?.tabsList?.get(ZEROTH_INDEX)?.tabDescription

            setUpCarousel(this, ZEROTH_INDEX, element)

            element?.tabsList?.forEach {
                tabLayout.addTab(tabLayout.newTab().setText(it.tabHeader))
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val selectedTab = tabLayout.selectedTabPosition
                    viewPagerTitle.text = element?.tabsList?.get(selectedTab)?.tabHeader
                    viewPagerDescription.text = element?.tabsList?.get(selectedTab)?.tabDescription
                    setUpCarousel(this@apply, selectedTab, element)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

                override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            })
        }
    }

    private fun setUpCarousel(
        binding: SmvcIntroVouchersPagerViewholderBinding,
        position: Int,
        element: VoucherIntroCarouselUiModel?
    ) {
        val carouselArray: java.util.ArrayList<Any> = java.util.ArrayList()
        when (position) {
            ZEROTH_INDEX -> {
                val list: List<String>? = element?.tabsList?.get(ZEROTH_INDEX)?.listOfImages
                list?.let {
                    carouselArray.addAll(
                        it
                    )
                }
            }
            FIRST_INDEX -> {
                val list: List<String>? = element?.tabsList?.get(FIRST_INDEX)?.listOfImages
                list?.let {
                    carouselArray.addAll(
                        it
                    )
                }
            }
            SECOND_INDEX -> {
                val list: List<String>? = element?.tabsList?.get(SECOND_INDEX)?.listOfImages
                list?.let {
                    carouselArray.addAll(
                        it
                    )
                }
            }
        }

        binding.carouselPageControl.setIndicator(carouselArray.size)

        val itemParam = { view: View, data: Any ->
            val imageCarousel = view.findViewById<DeferredImageView>(R.id.mvc_intro_carousel_deferred_image)
            imageCarousel.loadRemoteImageDrawable(data as String, data)
        }

        binding.apply {
            containerCarousel.apply {
                removePreviousItemViews()
                slideToShow = SLIDE_TO_SHOW
                resetIndicatorPosition()
                indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
                infinite = true
                onActiveIndexChangedListener = this@VoucherIntroCarouselViewHolder
                addItems(R.layout.smvc_item_intro_carousel, carouselArray, itemParam)
            }
        }
    }

    private fun CarouselUnify.removePreviousItemViews() {
        stage.removeAllViews()
    }

    private fun CarouselUnify.resetIndicatorPosition() {
        activeIndex = DEFAULT_INDICATOR_POSITION
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.smvc_intro_vouchers_pager_viewholder
        private const val SLIDE_TO_SHOW = 1.2f
        private const val DEFAULT_INDICATOR_POSITION = 0
    }

    override fun onActiveIndexChanged(prev: Int, current: Int) {
        binding?.carouselPageControl?.setCurrentIndicator(current)
    }
}
