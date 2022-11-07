package com.tokopedia.deals.pdp.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.databinding.WidgetDealsPdpCarouselBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.deals.R.dimen as dimenDeals
import com.tokopedia.unifyprinciples.R.dimen as dimenUnify

class WidgetDealsPDPCarousel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {
    private val indicatorBannerContainer: LinearLayout
    private val vpBannerCategory: RecyclerView
    private var binding: WidgetDealsPdpCarouselBinding? = null

    private var indicatorItems: ArrayList<ImageView> = arrayListOf()
    var imageUrls: ArrayList<String> = arrayListOf()

    private var imageViewPagerAdapter: WidgetDealsPDPCarouselAdapter? = null
        get() = WidgetDealsPDPCarouselAdapter(imageUrls)

    var currentPosition: Int = Int.ZERO

    override fun onFinishInflate() {
        super.onFinishInflate()
        invalidate()
        requestLayout()
    }

    init {
        binding = WidgetDealsPdpCarouselBinding.inflate(LayoutInflater.from(context), this, true)
        indicatorBannerContainer = binding?.dealsPdpIndicatorBannerContainer as LinearLayout
        vpBannerCategory = binding?.viewpagerDealsPdp as RecyclerView
    }

    fun buildView() {
        binding?.apply {
            shimmeringImage.gone()
            dealsPdpIndicatorBannerContainer.show()
            viewpagerDealsPdp.show()
            indicatorBannerContainer.show()

            indicatorItems.clear()
            indicatorBannerContainer.removeAllViews()
            vpBannerCategory.setHasFixedSize(true)

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            vpBannerCategory.layoutManager = layoutManager

            vpBannerCategory.adapter = imageViewPagerAdapter
            for (count in Int.ZERO until imageUrls.size) {
                val pointView = ImageView(context)
                pointView.setPadding(
                    resources.getDimensionPixelSize(dimenDeals.deals_dp_5),
                    resources.getDimensionPixelSize(dimenUnify.layout_lvl0),
                    resources.getDimensionPixelSize(dimenDeals.deals_dp_5),
                    resources.getDimensionPixelSize(dimenUnify.layout_lvl0)
                )
                if (count == Int.ZERO) {
                    pointView.setImageDrawable(
                        resizeIndicator(
                            getIndicatorFocus(),
                            imageUrls.size
                        )
                    )
                } else {
                    pointView.setImageDrawable(resizeIndicator(getIndicator(), imageUrls.size))
                }

                indicatorItems.add(pointView)
                indicatorBannerContainer.addView(pointView)
            }

            vpBannerCategory.clearOnScrollListeners()
            vpBannerCategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    currentPosition =
                        (vpBannerCategory.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (currentPosition != -Int.ONE) vpBannerCategory.smoothScrollToPosition(
                        currentPosition
                    )
                    setCurrentIndicator()
                }
            })

            if (imageUrls.size == Int.ONE) indicatorBannerContainer.visibility = View.GONE

            val snapHelper = PagerSnapHelper()
            vpBannerCategory.onFlingListener = null
            snapHelper.attachToRecyclerView(vpBannerCategory)
        }
    }

    fun setCurrentIndicator() {
        for (i in Int.ZERO until indicatorItems.size) {
            if (currentPosition != i) {
                indicatorItems[i].setImageDrawable(
                    resizeIndicator(
                        getIndicator(),
                        indicatorItems.size
                    )
                )
            } else {
                indicatorItems.get(i)
                    .setImageDrawable(resizeIndicator(getIndicatorFocus(), indicatorItems.size))
            }
        }
    }

    fun setImages(images: List<String>) {
        imageUrls.clear()
        imageUrls.addAll(images)
        imageViewPagerAdapter?.addImages(images)
    }

    fun shimmering() {
        binding?.apply {
            shimmeringImage.show()
            dealsPdpIndicatorBannerContainer.invisible()
            viewpagerDealsPdp.invisible()
        }
    }

    fun resizeIndicator(id: Int, size: Int): GradientDrawable {
        val getDisplayWidth = Resources.getSystem().displayMetrics.widthPixels / (size + Int.ONE)
        val gradientDrawable: GradientDrawable =
            context.resources.getDrawable(id) as GradientDrawable
        gradientDrawable.setSize(getDisplayWidth, HEIGHT)
        return gradientDrawable
    }

    private fun getIndicatorFocus(): Int = R.drawable.widget_deals_pdp_indicator_focus
    private fun getIndicator(): Int = R.drawable.widget_deals_pdp_indicator

    companion object {
        private const val HEIGHT = 16
    }
}
