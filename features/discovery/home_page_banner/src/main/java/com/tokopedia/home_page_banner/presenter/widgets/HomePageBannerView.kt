package com.tokopedia.home_page_banner.presenter.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.home_page_banner.R
import com.tokopedia.home_page_banner.presenter.adapter.HomePageBannerAdapter
import com.tokopedia.home_page_banner.presenter.handler.HomePageBannerActionHandler
import com.tokopedia.home_page_banner.presenter.handler.HomePageBannerListener
import com.tokopedia.home_page_banner.presenter.lifecycle.HomePageBannerLifecycleObserver
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


@SuppressLint("SyntheticAccessor")
class HomePageBannerView : FrameLayout, CoroutineScope, HomePageBannerActionHandler{

    private val masterJob = Job()

    companion object{
        private const val SLIDE_DELAY: Long = 5000
    }

    private var adapter: HomePageBannerAdapter? = null
    private var viewPager: ViewPager2? = null
    private var indicatorView: LinearLayout? = null
    private var seeAllPromoView: TextView? = null
    private var isAutoSlideActive = false
    private var jumpPosition: Int = -1
    private var currentPosition = -1

    // Current ViewPager Display Page
    private var currentItem = 0
    // Are you sliding to the left?
    private var isDraggingToLeft = false
    private var lastItem = 0
    private var listener: HomePageBannerListener? = null
    private val indicatorItems = mutableListOf<ImageView>()
    private val impressionStatusList = mutableListOf<Boolean>()

    constructor(context: Context): super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }

    private fun init(){
        HomePageBannerLifecycleObserver.registerActionHandler(this)
        val view = View.inflate(context, R.layout.home_page_banner_layout, this)
        indicatorView = view.findViewById(R.id.indicator_banner_container)
        seeAllPromoView = view.findViewById(R.id.see_all_promo)
        viewPager = view.findViewById(R.id.viewpager_banner)
        viewPager?.setCurrentItem(1, false)
    }

    fun showSeeAllPromo(isShow: Boolean){
        seeAllPromoView?.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    fun setListener(listener: HomePageBannerListener){
        this.listener = listener
    }

    fun buildView(imagesUrl: List<String>){
        adapter = HomePageBannerAdapter()
        viewPager?.offscreenPageLimit = 5
        currentPosition = -1
        buildIndicator(imagesUrl.size)
        adapter?.setItem(imagesUrl)
        resetImpressions()
        viewPager?.adapter = adapter
        viewPager?.setCurrentItem(1, false)
        setPageListener()
        runSlider()
    }

    internal fun setCurrentIndicator(currentPosition: Int) {
        launch(Dispatchers.Main) {
            for (i in indicatorItems.indices) {
                if (currentPosition != i) {
                    indicatorItems[i].setImageResource(getIndicator())
                } else {
                    indicatorItems[i].setImageResource(getIndicatorFocus())
                }
            }
        }
    }

    private fun setPageListener(){
        viewPager?.unregisterOnPageChangeCallback(onPageChangeCallbackListener)
        viewPager?.registerOnPageChangeCallback(onPageChangeCallbackListener)
    }

    private fun resetImpressions(){
        impressionStatusList.clear()
        for (i in 0..(adapter?.getRealCount() ?: 0)) {
            impressionStatusList.add(false)
        }
    }

    private fun setImpression(currentPosition: Int){
        if (!impressionStatusList[currentPosition]) {
            impressionStatusList[currentPosition] = true
            listener?.onPromoScrolled(currentPosition)
        }
    }

    private fun buildIndicator(size: Int){
        indicatorItems.clear()
        indicatorView?.removeAllViews()
        for (i in 0 until size){
            val imageView = ImageView(context)
            imageView.setPadding(5, 0, 5, 0)
            if (i == 0) {
                imageView.setImageResource(getIndicatorFocus())
            } else {
                imageView.setImageResource(getIndicator())
            }
            indicatorItems.add(imageView)
            indicatorView?.addView(imageView)
        }
    }

    private fun runSlider(){
        if(isAutoSlideActive || adapter == null) return
        isAutoSlideActive = true
        launch(coroutineContext){
            while (isAutoSlideActive) {
                delay(SLIDE_DELAY)
                var selectedPosition = (viewPager?.currentItem ?: 0)
                if(selectedPosition == (adapter?.getRealCount() ?: 0)){
                    selectedPosition = 0 // reset to first image
                }
                launch(Dispatchers.Main){
                    viewPager?.setCurrentItem(selectedPosition + 1, selectedPosition != 0)
                }
            }
        }
    }

    private fun disableSlider(){
        isAutoSlideActive = false
        masterJob.cancelChildren()
    }

    private fun getIndicatorFocus(): Int {
        return R.drawable.banner_dynamic_indicator_focus
    }

    private fun getIndicator(): Int {
        return R.drawable.banner_dynamic_indicator
    }

    private val onPageChangeCallbackListener = object: ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            lastItem = currentItem
            currentItem = position
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager2.SCROLL_STATE_DRAGGING && viewPager?.isInTouchMode == true) {
                disableSlider()
            } else if(state == ViewPager2.SCROLL_STATE_IDLE){
                val count = adapter?.itemCount ?: 0
                val position: Int
                if(count < 2) return
                val index = viewPager?.currentItem ?: 0
                when (index) {
                    0 -> {
                        position = count - 2
                        viewPager?.setCurrentItem(position, false)
                    }
                    count - 1 -> {
                        position = 1
                        viewPager?.setCurrentItem(position, false)
                    }
                    else -> position = index

                }
                setCurrentIndicator(position - 1)
                runSlider()
            }
        }
    }

    override fun onStart() {
        runSlider()
    }

    override fun onStop() {
        isAutoSlideActive = false
        masterJob.cancelChildren()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + masterJob
}