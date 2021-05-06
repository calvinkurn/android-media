package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.TIME_DISPLAY_FORMAT
import com.tokopedia.discovery2.Utils.Companion.parsedColor
import com.tokopedia.discovery2.Utils.Companion.setTimerBoxDynamicBackground
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.media.loader.loadImageFitCenter

class BannerTimerViewHolder(private val customItemView: View, val fragment: Fragment) : AbstractViewHolder(customItemView, fragment.viewLifecycleOwner) {

    private lateinit var bannerTimerViewModel: BannerTimerViewModel
    private var constraintLayout: ConstraintLayout = customItemView.findViewById(R.id.banner_timer_container_layout)
    private var context: Context
    private var bannerImageView: ImageView = customItemView.findViewById(R.id.banner_image_view)
    private lateinit var daysTextView: TextView
    private lateinit var hoursTextView: TextView
    private lateinit var minutesTextView: TextView
    private lateinit var secondsTextView: TextView
    private var timeTextFontColour : Int = 0
    private var timeBoxColour : Int = 0

    companion object {
        const val DAYS = 0
        const val HOURS = 1
        const val MINUTES = 2
        const val SECONDS = 3
    }

    init {
        context = constraintLayout.context
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        bannerTimerViewModel = discoveryBaseViewModel as BannerTimerViewModel
        initView()
    }

    private fun initView() {
        val viewHeight = bannerTimerViewModel.getBannerUrlHeight()
        val viewWidth = bannerTimerViewModel.getBannerUrlWidth()
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setDimensionRatio(bannerImageView.id, "H, $viewWidth : $viewHeight")
        constraintSet.applyTo(constraintLayout)
        configureTimerUI()
        constraintLayout.setOnClickListener {
            bannerTimerViewModel.onBannerClicked(it.context)
        }
    }

    private fun configureTimerUI() {
        bannerTimerViewModel.getComponent().let {
            if (!it.data.isNullOrEmpty()) {
                bannerImageView.loadImageFitCenter(it.data?.firstOrNull()?.backgroundUrlMobile ?: "")
                timeTextFontColour = getTimerFontColour(it)
                timeBoxColour = getTimerBoxColour(it)
                setTimerUI(DAYS)
                setTimerUI(HOURS)
                setTimerUI(MINUTES)
                setTimerUI(SECONDS)
                setSeparatorUI()
            }
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {lifecycle ->
            bannerTimerViewModel.getTimerData().observe(lifecycle, {
                daysTextView.text = String.format(TIME_DISPLAY_FORMAT, it.days)
                hoursTextView.text = String.format(TIME_DISPLAY_FORMAT, it.hours)
                minutesTextView.text = String.format(TIME_DISPLAY_FORMAT, it.minutes)
                secondsTextView.text = String.format(TIME_DISPLAY_FORMAT, it.seconds)
                bannerTimerViewModel.checkTimerEnd(it)
            })
            bannerTimerViewModel.getSyncPageLiveData().observe(lifecycle, { needResync ->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            })
        }
    }


    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            bannerTimerViewModel.stopTimer()
            bannerTimerViewModel.getTimerData().removeObservers(it)
            bannerTimerViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }

    override fun onViewDetachedToWindow() {
        bannerTimerViewModel.stopTimer()
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        bannerTimerViewModel.startTimer()
    }

    private fun setTimerUI(timeType: Int) {
        when (timeType) {
            DAYS -> {
                val daysViewLayout: View = customItemView.findViewById(R.id.day_layout)
                val daysTitleTextView: TextView = daysViewLayout.findViewById(R.id.time_title_text_view)
                daysTitleTextView.text = context.resources.getString(R.string.hari)
                daysTitleTextView.setTextColor(timeBoxColour)
                daysTextView = daysViewLayout.findViewById(R.id.time_text_view)
                daysTextView.setTextColor(timeTextFontColour)
                setTimerBoxDynamicBackground(daysTextView, timeBoxColour)
            }
            HOURS -> {
                val hoursViewLayout: View = customItemView.findViewById(R.id.hours_layout)
                val hoursTitleTextView: TextView = hoursViewLayout.findViewById(R.id.time_title_text_view)
                hoursTitleTextView.text = context.resources.getString(R.string.jam)
                hoursTitleTextView.setTextColor(timeBoxColour)
                hoursTextView = hoursViewLayout.findViewById(R.id.time_text_view)
                hoursTextView.setTextColor(timeTextFontColour)
                setTimerBoxDynamicBackground(hoursTextView, timeBoxColour)
            }
            MINUTES -> {
                val minutesViewLayout: View = customItemView.findViewById(R.id.minutes_layout)
                val minutesTitleTextView: TextView = minutesViewLayout.findViewById(R.id.time_title_text_view)
                minutesTitleTextView.text = context.resources.getString(R.string.menit)
                minutesTitleTextView.setTextColor(timeBoxColour)
                minutesTextView = minutesViewLayout.findViewById(R.id.time_text_view)
                minutesTextView.setTextColor(timeTextFontColour)
                setTimerBoxDynamicBackground(minutesTextView, timeBoxColour)
            }
            SECONDS -> {
                val secondsViewLayout: View = customItemView.findViewById(R.id.seconds_layout)
                val secondsTitleTextView: TextView = secondsViewLayout.findViewById(R.id.time_title_text_view)
                secondsTitleTextView.text = context.resources.getString(R.string.detik)
                secondsTitleTextView.setTextColor(timeBoxColour)
                secondsTextView = secondsViewLayout.findViewById(R.id.time_text_view)
                secondsTextView.setTextColor(timeTextFontColour)
                setTimerBoxDynamicBackground(secondsTextView, timeBoxColour)
            }
        }
    }

    private fun setSeparatorUI() {
        customItemView.findViewById<TextView>(R.id.day_separator_text_view).setTextColor(timeBoxColour)
        customItemView.findViewById<TextView>(R.id.hours_separator_text_view).setTextColor(timeBoxColour)
        customItemView.findViewById<TextView>(R.id.minutes_separator_text_view).setTextColor(timeBoxColour)
    }

    private fun getTimerFontColour(componentItem: ComponentsItem?): Int {
        val fontColour = componentItem?.data?.firstOrNull()?.fontColor
        return if (fontColour.isNullOrEmpty()) {
            MethodChecker.getColor(context, R.color.clr_ff8000)
        } else {
            parsedColor(context, fontColour, R.color.clr_ff8000)
        }
    }

    private fun getTimerBoxColour(componentItem: ComponentsItem?): Int {
        val boxColor = componentItem?.data?.firstOrNull()?.boxColor
        return if (boxColor.isNullOrEmpty()) {
            MethodChecker.getColor(context, R.color.white)
        } else {
            parsedColor(context, boxColor, R.color.white)
        }
    }
}