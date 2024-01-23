package com.tokopedia.shop.home.view.adapter.viewholder.advance_carousel_banner

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carousellayoutmanager.CarouselHorizontalFlingSwipeEffect
import com.tokopedia.carousellayoutmanager.CarouselLayoutManager
import com.tokopedia.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.tokopedia.carousellayoutmanager.CenterScrollListener
import com.tokopedia.carousellayoutmanager.DefaultChildSelectionListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ShopAdvanceCarouselBannerViewholderLayoutBinding
import com.tokopedia.shop.home.WidgetNameEnum
import com.tokopedia.shop.home.util.RecyclerviewPoolListener
import com.tokopedia.shop.home.view.adapter.ShopWidgetAdvanceCarouselBannerAdapter
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopHomeDisplayAdvanceCarouselBannerViewHolder(
    view: View?,
    private val listener: ShopHomeDisplayAdvanceCarouselBannerWidgetListener,
    private val recyclerviewPoolListener: RecyclerviewPoolListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.shop_advance_carousel_banner_viewholder_layout
        private const val DEFAULT_RATIO = "1:1"
        private const val AUTO_SCROLL_DURATION = 5000L
        private const val RV_HORIZONTAL_PADDING_FOR_MORE_THAT_ONE_DATA = 16
    }

    private val viewBinding: ShopAdvanceCarouselBannerViewholderLayoutBinding? by viewBinding()
    private val placeholderContainer: View? = viewBinding?.containerPlaceholder
    private val contentContainer: View? = viewBinding?.containerContent
    private val recyclerView: RecyclerView? = viewBinding?.recyclerViewBannerImage
    private val textViewTitle: Typography? = viewBinding?.textViewTitle
    private val bannerIndicator: PageControl? = viewBinding?.pageControl
    private var currentItem: Int = 0
    private var adapterShopWidgetAdvanceCarouselBanner: ShopWidgetAdvanceCarouselBannerAdapter? =
        null
    private var uiModel: ShopHomeDisplayWidgetUiModel = ShopHomeDisplayWidgetUiModel()
    private var timer: Timer? = Timer()
    private var currentSelectedItemPositionWhenUserTouchItem = 0
    private var carouselLayoutManager: CarouselLayoutManager? = null
    private val itemSelectionListener: CarouselLayoutManager.OnCenterItemSelectionListener =
        CarouselLayoutManager.OnCenterItemSelectionListener { position ->
            bannerIndicator?.setCurrentIndicator(position)
            setImpressionListener(position)
            currentItem = position
        }

    private val itemClickListener: DefaultChildSelectionListener.OnCenterItemClickListener =
        DefaultChildSelectionListener.OnCenterItemClickListener { recyclerView, _, view ->
            val position = recyclerView.getChildLayoutPosition(view)
            setItemClickListener(position)
        }

    private val itemTouchListener: RecyclerView.OnItemTouchListener =
        object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        currentSelectedItemPositionWhenUserTouchItem = carouselLayoutManager?.centerItemPosition.orZero()
                        setAutoScrollOff()
                    }

                    MotionEvent.ACTION_UP -> {
                        setAutoScrollOn()
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }

    private fun setItemClickListener(position: Int) {
        uiModel.data?.getOrNull(position)?.let {
            listener.onClickAdvanceCarouselBannerItem(
                uiModel,
                it,
                position
            )
            it.invoke()
        }
    }

    override fun bind(uiModel: ShopHomeDisplayWidgetUiModel) {
        this.uiModel = uiModel
        if (uiModel.isWidgetShowPlaceholder()) {
            showPlaceholderView()
        } else {
            showContentView()
            setHeaderSection()
            setBannerImageCarouselSection()
            setBannerIndicatorSection()
            setAutoScrollOn()
            configColorTheme()
        }
    }

    private fun configColorTheme() {
        if (uiModel.header.isOverrideTheme) {
            configReimaginedColor(uiModel.header.colorSchema)
        } else {
            configDefaultColor()
        }
    }

    private fun configReimaginedColor(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        textViewTitle?.setTextColor(titleColor)
    }

    private fun configDefaultColor() {
        val titleColor = MethodChecker.getColor(
            itemView.context,
            unifyprinciplesR.color.Unify_NN950_96
        )
        textViewTitle?.setTextColor(titleColor)
    }

    private fun setAutoScrollOn() {
        setAutoScrollOff()
        timer = Timer()
        timer?.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    Handler(Looper.getMainLooper()).post {
                        if (currentItem == recyclerView?.layoutManager?.itemCount.orZero() - 1) {
                            currentItem = Int.ZERO
                        } else {
                            currentItem++
                        }
                        recyclerView?.smoothScrollToPosition(currentItem)
                    }
                }
            },
            AUTO_SCROLL_DURATION, AUTO_SCROLL_DURATION
        )
    }

    private fun setAutoScrollOff() {
        timer?.cancel()
        timer = null
    }

    private fun setBannerIndicatorSection() {
        bannerIndicator?.setIndicator(uiModel.data?.size.orZero())
    }

    private fun setBannerImageCarouselSection() {
        setupAdapter(uiModel)
        initRecyclerView(uiModel)
    }

    private fun setupAdapter(uiModel: ShopHomeDisplayWidgetUiModel) {
        adapterShopWidgetAdvanceCarouselBanner = ShopWidgetAdvanceCarouselBannerAdapter(
            uiModel
        )
        adapterShopWidgetAdvanceCarouselBanner?.submit(uiModel.data.orEmpty())
    }

    private fun initRecyclerView(uiModel: ShopHomeDisplayWidgetUiModel) {
        val ratio = uiModel.header.ratio.takeIf { it.isNotEmpty() } ?: DEFAULT_RATIO
        carouselLayoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, isCircularRvLayout(uiModel), false)
        carouselLayoutManager?.setPostLayoutListener(CarouselZoomPostLayoutListener())
        carouselLayoutManager?.maxVisibleItems = Int.ONE
        carouselLayoutManager?.removeOnItemSelectionListener(itemSelectionListener)
        carouselLayoutManager?.addOnItemSelectionListener(itemSelectionListener)
        recyclerView?.apply {
            isNestedScrollingEnabled = false
            (this@apply.layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = ratio
            this.layoutManager = carouselLayoutManager
            this.setHasFixedSize(true)
            this.addOnScrollListener(CenterScrollListener())
            carouselLayoutManager?.let {
                DefaultChildSelectionListener.initCenterItemListener(
                    itemClickListener,
                    recyclerView,
                    it
                )
                setupFlingListener(this, it)
            }
            this.adapter = adapterShopWidgetAdvanceCarouselBanner
            this.removeOnItemTouchListener(itemTouchListener)
            this.addOnItemTouchListener(itemTouchListener)
            this.setRecycledViewPool(recyclerviewPoolListener.parentPool)
            if (uiModel.data?.size.orZero() > Int.ONE) {
                setPadding(
                    RV_HORIZONTAL_PADDING_FOR_MORE_THAT_ONE_DATA.toPx(),
                    Int.ZERO,
                    RV_HORIZONTAL_PADDING_FOR_MORE_THAT_ONE_DATA.toPx(),
                    Int.ZERO
                )
            } else {
                setPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
            }
        }
        updateRecyclerViewHeightBasedOnFirstChild()
    }

    private fun setupFlingListener(
        recyclerView: RecyclerView,
        carouselLayoutManager: CarouselLayoutManager
    ) {
        recyclerView.onFlingListener = null
        recyclerView.onFlingListener = CarouselHorizontalFlingSwipeEffect(
            recyclerView,
            carouselLayoutManager,
            uiModel.data?.size.orZero()
        ) { currentSelectedItemPositionWhenUserTouchItem }
    }

    private fun isCircularRvLayout(uiModel: ShopHomeDisplayWidgetUiModel): Boolean {
        return uiModel.data?.size.orZero() > 2
    }

    private fun updateRecyclerViewHeightBasedOnFirstChild() {
        recyclerView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val firstChildHeight = recyclerView.findViewHolderForAdapterPosition(
                        Int.ZERO
                    )?.itemView?.height.orZero()
                    val lp = recyclerView.layoutParams
                    lp?.height = firstChildHeight
                    recyclerView.layoutParams = lp
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
    }

    private fun setHeaderSection() {
        val title = uiModel.header.title
        textViewTitle?.shouldShowWithAction(title.isNotEmpty()) {
            textViewTitle.text = title
        }
    }

    private fun showContentView() {
        placeholderContainer?.hide()
        contentContainer?.show()
    }

    private fun showPlaceholderView() {
        placeholderContainer?.show()
        contentContainer?.hide()
    }

    private fun setImpressionListener(position: Int) {
        uiModel.data?.getOrNull(position)?.let {
            listener.onImpressionAdvanceCarouselBannerItem(
                uiModel,
                it,
                position
            )
            it.invoke()
        }
    }

    fun pauseTimer() {
        setAutoScrollOff()
    }

    fun resumeTimer() {
        currentItem = 0
        setAutoScrollOn()
    }
}
