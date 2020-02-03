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
import com.tokopedia.home_page_banner.ext.isSame
import com.tokopedia.home_page_banner.presenter.adapter.HomePageBannerAdapter
import com.tokopedia.home_page_banner.presenter.handler.HomePageBannerActionHandler
import com.tokopedia.home_page_banner.presenter.handler.HomePageBannerListener
import com.tokopedia.home_page_banner.presenter.lifecycle.HomePageBannerLifecycleObserver
import com.tokopedia.home_page_banner.presenter.model.BannerModel
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
    private var currentItem = 0
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
        seeAllPromoView?.setOnClickListener { listener?.onPromoAllClick() }
        adapter = HomePageBannerAdapter {
            listener?.onPromoClick(it)
        }
    }

    fun showSeeAllPromo(isShow: Boolean){
        seeAllPromoView?.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    fun setListener(listener: HomePageBannerListener){
        this.listener = listener
    }

    fun buildView(banners: List<BannerModel>){
        if(viewPager?.adapter == null){
            viewPager?.adapter = adapter
        }
        if(!banners.isSame(adapter?.getList() ?: listOf())){
            buildIndicator(banners.size)
            adapter?.setItem(banners)
            resetImpressions()
            viewPager?.setCurrentItem(1, false)
            if (!impressionStatusList[0]) {
                impressionStatusList[0] = true
                listener?.onPromoScrolled(0)
            }
        }else {
            resetImpressions()
            adapter?.setItem(banners)
        }
        viewPager?.offscreenPageLimit = banners.size
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
        val realPosition = getRealPosition(currentPosition) - 1
        val realCount = adapter?.getRealCount() ?: 0
        if (realPosition < realCount && !impressionStatusList[realPosition]) {
            impressionStatusList[realPosition] = true
            listener?.onPromoScrolled(realPosition)
        }
    }

    private fun getRealPosition(positionWithOffset: Int): Int{
        val count = adapter?.itemCount ?: 0
        return when (positionWithOffset) {
            0 -> count - 2
            count - 1 -> 1
            else -> positionWithOffset
        }
    }

    private fun buildIndicator(size: Int){
        indicatorItems.clear()
        indicatorView?.removeAllViews()
        for (i in 0 until size){
            val imageView = ImageView(context)
            imageView.setPadding(0, 0, resources.getDimension(R.dimen.dp_5).toInt(), 0)
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
                    viewPager?.setCurrentItem(selectedPosition + 1, true)
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
            setImpression(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            listener?.onPageDragStateChanged(ViewPager2.SCROLL_STATE_DRAGGING == state && viewPager?.isInTouchMode == true)
            if (state == ViewPager2.SCROLL_STATE_DRAGGING && viewPager?.isInTouchMode == true) {
                disableSlider()
            } else if(state == ViewPager2.SCROLL_STATE_IDLE){
                val count = adapter?.itemCount ?: 0
                val position: Int
                if(count < 1) return
                when (val index = viewPager?.currentItem ?: 0) {
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