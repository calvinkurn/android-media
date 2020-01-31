package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlin.collections.ArrayList

private const val SLIDE_DELAY: Long = 5000
private const val SAVED = "instance state BannerView.class"
private const val SAVE_STATE_AUTO_SCROLL_ON_PROGRESS = "auto_scroll_on_progress"

class CarouselBannerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var bannerHandler: Handler? = null
    private var runnableScrollBanner: Runnable? = null
    private var bannerRecyclerView: RecyclerView? = null
    private var bannerIndicator: ViewGroup? = null
    private var bannerSeeAll: TextView? = null
    private var autoScrollOnProgress = false

    private var onPromoAllClickListener: OnPromoAllClickListener? = null

    private var indicatorItems = ArrayList<ImageView>()
    private var impressionStatusList = ArrayList<Boolean>()
    private var carouselDataItems: ArrayList<ComponentsItem> = ArrayList()
    private var currentPosition = 0
    private var carouselBannerAdapter: DiscoveryRecycleAdapter? = null

    init {
        val view = View.inflate(context, R.layout.widget_carousel_banner, this)
        bannerRecyclerView = view.findViewById(R.id.banner_carousel_recyclerview)
        bannerIndicator = view.findViewById(R.id.indicator_banner_container)
        bannerSeeAll = view.findViewById(R.id.promo_link)
        indicatorItems = ArrayList()
        impressionStatusList = ArrayList()
        carouselDataItems = ArrayList()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        bannerSeeAll.let {
            it?.setOnClickListener { onPromoAllClickListener?.onPromoAllClick() }
        }
        invalidate()
        requestLayout()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var savedState = state
        if (state is Bundle) {
            setAutoScrollOnProgress(state.getBoolean(SAVE_STATE_AUTO_SCROLL_ON_PROGRESS))
            if (isAutoScrollOnProgress()) {
                startAutoScrollBanner()
            } else {
                stopAutoScrollBanner()
            }
            savedState = state.getParcelable(SAVED)
        }
        super.onRestoreInstanceState(savedState)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(SAVED, super.onSaveInstanceState())
        bundle.putBoolean(SAVE_STATE_AUTO_SCROLL_ON_PROGRESS, isAutoScrollOnProgress())
        return bundle
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoScrollBanner()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAutoScrollBanner()
    }

    fun buildView() {
        this.show()
        this.resetImpressionStatus()
        this.bannerIndicator?.show()
        this.indicatorItems.clear()
        this.bannerIndicator?.removeAllViews()
        this.bannerRecyclerView?.setHasFixedSize(true)
        this.indicatorItems.clear()
        this.bannerIndicator?.removeAllViews()
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        this.bannerRecyclerView?.layoutManager = layoutManager
        this.bannerRecyclerView?.adapter = carouselBannerAdapter
        for (item in carouselDataItems) {
            val pointView = ImageView(this.context)
            pointView.setPadding(5, 0, 5, 0)
            if (carouselDataItems.indexOf(item) == 0) {
                pointView.setImageResource(this.getIndicatorFocus())
            } else {
                pointView.setImageResource(this.getIndicator())
            }
            this.indicatorItems.add(pointView)
            this.bannerIndicator?.addView(pointView)
        }
        setTextAndCheckShow(carouselDataItems[0].data?.get(0)?.buttonApplink)
        this.bannerRecyclerView?.clearOnScrollListeners()
        this.bannerRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.layoutManager != null) {
                    this@CarouselBannerView.currentPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
                }

                this@CarouselBannerView.setCurrentIndicator()
                if (!this@CarouselBannerView.isCurrentPositionHasImpression(this@CarouselBannerView.currentPosition)) {
                    this@CarouselBannerView.impressionStatusList[this@CarouselBannerView.currentPosition] = true
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 1 && recyclerView.isInTouchMode) {
                    this@CarouselBannerView.stopAutoScrollBanner()
                }
            }
        })
        if (this.carouselDataItems.size == 1) {
            this.bannerIndicator?.hide()
        }

        val snapHelper = PagerSnapHelper()
        this.bannerRecyclerView?.onFlingListener = null
        snapHelper.attachToRecyclerView(this.bannerRecyclerView)
        if (this.bannerHandler == null && this.runnableScrollBanner == null) {
            this.bannerHandler = Handler()

            this.runnableScrollBanner = object : Runnable {
                override fun run() {
                    if (this@CarouselBannerView.bannerRecyclerView != null && this@CarouselBannerView.bannerRecyclerView?.adapter != null) {
                        if (this@CarouselBannerView.currentPosition == this@CarouselBannerView.bannerRecyclerView?.adapter?.itemCount!! - 1) {
                            this@CarouselBannerView.currentPosition = -1
                        }
                        this@CarouselBannerView.bannerRecyclerView?.smoothScrollToPosition(this@CarouselBannerView.currentPosition + 1)
                        this@CarouselBannerView.bannerHandler?.postDelayed(this, SLIDE_DELAY)
                    }
                }
            }

            this.startAutoScrollBanner()
        }

    }

    private fun setTextAndCheckShow(buttonApplink: String?) {
        buttonApplink?.let {
            if (it.isNotEmpty()) {
                bannerSeeAll?.show()
            } else {
                bannerSeeAll?.hide()
            }
        }
    }

    private fun startAutoScrollBanner() {
        if (bannerHandler != null && runnableScrollBanner != null && !isAutoScrollOnProgress()) {
            setAutoScrollOnProgress(true)
            bannerHandler?.postDelayed(runnableScrollBanner, SLIDE_DELAY)
        }
    }

    private fun stopAutoScrollBanner() {
        if (bannerHandler != null && runnableScrollBanner != null) {
            setAutoScrollOnProgress(false)
            bannerHandler?.removeCallbacks(runnableScrollBanner)
        }
    }

    private fun setAutoScrollOnProgress(autoScrollOnProgress: Boolean) {
        this.autoScrollOnProgress = autoScrollOnProgress
    }

    private fun isAutoScrollOnProgress(): Boolean {
        return autoScrollOnProgress
    }

    fun setCurrentIndicator() {
        for (i in indicatorItems.indices) {
            if (currentPosition != i) {
                indicatorItems[i].setImageResource(getIndicator())
            } else {
                indicatorItems[i].setImageResource(getIndicatorFocus())
            }
        }
    }

    fun isCurrentPositionHasImpression(currentPosition: Int): Boolean {
        return if (currentPosition >= 0 && currentPosition < impressionStatusList.size) {
            impressionStatusList[currentPosition]
        } else {
            true
        }
    }

    private fun resetImpressionStatus() {
        impressionStatusList.clear()
        for (i in carouselDataItems.indices) {
            impressionStatusList.add(false)
        }
    }

    private fun getIndicatorFocus(): Int {
        return R.drawable.indicator_focus
    }

    private fun getIndicator(): Int {
        return R.drawable.indicator

    }

    fun setAdapter(recycleAdapter: DiscoveryRecycleAdapter) {
        carouselBannerAdapter = recycleAdapter
    }

    fun setOnAllPromoClickListener(onPromoAllClickListener: OnPromoAllClickListener) {
        this.onPromoAllClickListener = onPromoAllClickListener

    }

    fun setDataItems(item: ArrayList<ComponentsItem>?) {
        item?.let {
            carouselDataItems = item
        }

    }

    interface OnPromoAllClickListener {
        fun onPromoAllClick()
    }

}