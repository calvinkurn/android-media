package com.tokopedia.mvc.presentation.intro.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcIntroVouchersPagerViewholderBinding
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherIntroCarouselUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.binding.viewBinding

class VoucherIntroCarouselViewHolder(itemView: View?) : AbstractViewHolder<VoucherIntroCarouselUiModel>(
    itemView
) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.smvc_intro_vouchers_pager_viewholder
    }
    private var binding: SmvcIntroVouchersPagerViewholderBinding? by viewBinding()

    override fun bind(element: VoucherIntroCarouselUiModel?) {
        binding?.apply {
            headerText.text = element?.headerTitle
            viewPagerTitle.text = element?.tabsList?.get(0)?.tabHeader
            viewPagerDescription.text = element?.description

            setUpCarousel(this, 0)

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val selectedTab = tabLayout.selectedTabPosition
                    viewPagerTitle.text = element?.tabsList?.get(selectedTab)?.tabHeader
                    setUpCarousel(this@apply, selectedTab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    // Problems with Carousel
    private fun setUpCarousel(binding: SmvcIntroVouchersPagerViewholderBinding, position: Int) {
        val bannerArr: java.util.ArrayList<Any> = java.util.ArrayList()
        when (position) {
            0 -> {
//                bannerArr.add("https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_f563497d-22f9-4295-ae71-423a35af5476.jpg")
//                bannerArr.add("https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_df8a996c-b290-4a29-b780-f285c89dd720.jpg")
//                bannerArr.add("https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_9ce16553-f0fd-481e-a3e3-45363dbd8c70.jpg")
                bannerArr.add(
                    SampleData(
                    "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_4e29115b-cdf0-48d9-96fc-afe668269d12.jpg"
                ))
                bannerArr.add(
                    SampleData(
                    "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_4e29115b-cdf0-48d9-96fc-afe668269d12.jpg"
                ))
            }
            1 -> {
                bannerArr.add(
                    SampleData(
                    "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_9ce16553-f0fd-481e-a3e3-45363dbd8c70.jpg"
                ))
                bannerArr.add(
                    SampleData(
                    "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_4e29115b-cdf0-48d9-96fc-afe668269d12.jpg"
                ))
            }
            2 -> {
                bannerArr.add(
                    SampleData(
                    "https://ecs7.tokopedia.net/img/banner/2020/2/1/85531617/85531617_df8a996c-b290-4a29-b780-f285c89dd720.jpg"
                ))
            }
        }

        val itemParam = { view: View, data: Any ->
            val imageCarousel = view.findViewById<ImageUnify>(R.id.image)

            binding.containerCarousel.post {
                imageCarousel.initialWidth = binding.containerCarousel.measuredWidth
            }
            imageCarousel.setImageUrl((data as SampleData).title, 0.666f)
        }

        binding.apply {
            containerCarousel.apply {
                removePreviousItemViews()
                slideToShow = 1.2f
                indicatorPosition = CarouselUnify.INDICATOR_BC
                infinite = true
                //       setUpItemViews(context, bannerArr)
                //         addImages(bannerArr)
                addItems(R.layout.smvc_item_intro_carousel, bannerArr, itemParam)
            }
        }
    }

    private fun CarouselUnify.removePreviousItemViews() {
        stage.removeAllViews()
    }

    class SampleData(var title: String)

//    private fun CarouselUnify.setUpItemViews(context: Context, items: List<String>) {
//        items.forEach {
//            createItemView(context).apply{
//                bindItem(this,it)
//            }.run{
//                addItem(this)
//            }
//        }
//    }
//
//    private fun createItemView(context: Context): View {
//        return View.inflate(context, MvcIntroCarouselImageViewHolder.RES_LAYOUT, null).apply {
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//        }
//    }
//
//    private fun bindItem(view: View, data: String) {
//        val viewHolder = MvcIntroCarouselImageViewHolder(view)
//        viewHolder.bind(data)
//    }
}
