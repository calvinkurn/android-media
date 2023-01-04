package com.tokopedia.mvc.presentation.intro.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcIntroVouchersPagerViewholderBinding
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherIntroCarouselUiModel
import com.tokopedia.mvc.presentation.intro.util.FIRST_INDEX
import com.tokopedia.mvc.presentation.intro.util.SECOND_INDEX
import com.tokopedia.mvc.presentation.intro.util.ZEROTH_INDEX
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.utils.view.binding.viewBinding

class VoucherIntroCarouselViewHolder(itemView: View?) : AbstractViewHolder<VoucherIntroCarouselUiModel>(
    itemView
) {
    private var binding: SmvcIntroVouchersPagerViewholderBinding? by viewBinding()

    override fun bind(element: VoucherIntroCarouselUiModel?) {
        binding?.apply {
            headerText.text = element?.headerTitle
            viewPagerTitle.text = element?.tabsList?.get(ZEROTH_INDEX)?.tabHeader
            viewPagerDescription.text = element?.description
//            val viewPagerAdapter = MvcIntroViewPager()
//            viewPager.adapter = viewPagerAdapter

            setUpCarousel(this, ZEROTH_INDEX, element)

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val selectedTab = tabLayout.selectedTabPosition
                    viewPagerTitle.text = element?.tabsList?.get(selectedTab)?.tabHeader
                    setUpCarousel(this@apply, selectedTab, element)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun setUpCarousel(
        binding: SmvcIntroVouchersPagerViewholderBinding,
        position: Int,
        element: VoucherIntroCarouselUiModel?
    ) {
        val bannerArr: java.util.ArrayList<Any> = java.util.ArrayList()
        when (position) {
            ZEROTH_INDEX -> {
                element?.tabsList?.get(ZEROTH_INDEX)?.listOfImages?.let {
                    bannerArr.addAll(
                        it
                    )
                }
            }
            FIRST_INDEX -> {
                element?.tabsList?.get(FIRST_INDEX)?.listOfImages?.let {
                    bannerArr.addAll(
                        it
                    )
                }
            }
            SECOND_INDEX -> {
                element?.tabsList?.get(SECOND_INDEX)?.listOfImages?.let {
                    bannerArr.addAll(
                        it
                    )
                }
            }
        }

        val itemParam = { view: View, data: Any ->
            val imageCarousel = view.findViewById<ImageUnify>(R.id.image)
            val pageControl = view.findViewById<PageControl>(R.id.carousel_page_control)
            pageControl.setIndicator(10)
            binding.containerCarousel.post {
                imageCarousel.initialWidth = binding.containerCarousel.measuredWidth
            }
            imageCarousel.setImageUrl(data as String)
        }

        binding.apply {
            containerCarousel.apply {
                removePreviousItemViews()
                slideToShow = SLIDE_TO_SHOW
                resetIndicatorPosition()
                indicatorPosition = CarouselUnify.INDICATOR_BC
                infinite = true
                addItems(R.layout.smvc_item_intro_carousel, bannerArr, itemParam)
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
}
