package com.tokopedia.discovery2.viewcontrollers.customview

import android.os.Handler
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

private const val SLIDE_DELAY: Long = 5000

abstract class BannerPagerSnapHelper(private val bannerRecyclerView: RecyclerView) : PagerSnapHelper() {
    private var bannerHandler: Handler? = null
    private var runnableScrollBanner: Runnable? = null
    private var currentPosition = 0

    protected abstract fun setAutoScrollOnProgress(autoScrollOnProgress: Boolean)
    protected abstract fun isAutoScrollEnabled(): Boolean
    protected abstract fun isAutoScrollOnProgress(): Boolean

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        bannerRecyclerView.onFlingListener = null
        super.attachToRecyclerView(recyclerView)
        if (this.bannerHandler == null && this.runnableScrollBanner == null) {
            this.bannerHandler = Handler()

            this.runnableScrollBanner = object : Runnable {
                override fun run() {
                    if (bannerRecyclerView.adapter != null) {
                        if (currentPosition == bannerRecyclerView.adapter?.itemCount!! - 1) {
                            currentPosition = -1
                        }
                        bannerRecyclerView.smoothScrollToPosition(currentPosition + 1)
                        bannerHandler?.postDelayed(this, SLIDE_DELAY)
                    }
                }
            }
            startAutoScrollBanner()
        }
    }

    fun startAutoScrollBanner() {
        if (isAutoScrollEnabled() && bannerHandler != null && runnableScrollBanner != null && !isAutoScrollOnProgress()) {
            setAutoScrollOnProgress(true)
            bannerHandler?.postDelayed(runnableScrollBanner, SLIDE_DELAY)
        }
    }

    fun stopAutoScrollBanner() {
        if (isAutoScrollEnabled() && bannerHandler != null && runnableScrollBanner != null) {
            setAutoScrollOnProgress(false)
            bannerHandler?.removeCallbacks(runnableScrollBanner)
        }
    }

}