package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.R

private const val SAVED = "instance state BannerView.class"
private const val SAVE_STATE_AUTO_SCROLL_ON_PROGRESS = "auto_scroll_on_progress"

class DiscoveryBannerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var autoScrollEnabled: Boolean = false
    private var autoScrollOnProgressLiveData = MutableLiveData<Boolean>()
    private var discoveryBannerViewInteraction: DiscoveryBannerViewInteraction? = null

    init {
        View.inflate(context, R.layout.widget_recycler_view, this)
        getDataFromAttrs(attrs)
    }

    private fun getDataFromAttrs(attrs: AttributeSet?) {
        attrs?.let {
            context.theme.obtainStyledAttributes(attrs, R.styleable.DiscoveryBannerView, 0, 0).apply {
                try {
                    autoScrollEnabled = getBoolean(R.styleable.DiscoveryBannerView_autoScroll, true)
                } finally {
                    recycle()
                }
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        invalidate()
        requestLayout()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var savedState = state
        if (state is Bundle) {
            setAutoScrollOnProgressValue(state.getBoolean(SAVE_STATE_AUTO_SCROLL_ON_PROGRESS))
            if (isAutoScrollOnProgress()) {
                discoveryBannerViewInteraction?.startScrolling()
            } else {
                discoveryBannerViewInteraction?.stopScrolling()
            }
            savedState = state.getParcelable(SAVED)
        }
        super.onRestoreInstanceState(savedState)
    }

    private fun isAutoScrollOnProgress(): Boolean {
        return autoScrollOnProgressLiveData.value ?: false
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(SAVED, super.onSaveInstanceState())
        bundle.putBoolean(SAVE_STATE_AUTO_SCROLL_ON_PROGRESS, isAutoScrollOnProgress())
        return bundle
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        discoveryBannerViewInteraction?.stopScrolling()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        discoveryBannerViewInteraction?.startScrolling()
    }

    interface DiscoveryBannerViewInteraction {
        fun attachRecyclerView()
        fun startScrolling()
        fun stopScrolling()
    }

    fun setCarouselBannerViewInteraction(discoveryBannerViewInteraction: DiscoveryBannerViewInteraction) {
        this.discoveryBannerViewInteraction = discoveryBannerViewInteraction
    }

    fun setAutoScrollOnProgressValue(value: Boolean) {
        this.autoScrollOnProgressLiveData.postValue(value)
    }

    fun getAutoScrollOnProgressLiveData(): LiveData<Boolean> {
        return autoScrollOnProgressLiveData
    }

    fun getAutoScrollEnabled(): Boolean {
        return autoScrollEnabled
    }
}