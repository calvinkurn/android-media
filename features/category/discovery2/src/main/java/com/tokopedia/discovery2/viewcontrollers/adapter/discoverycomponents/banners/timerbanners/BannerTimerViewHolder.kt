package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

private const val TIME_DISPLAY_FORMAT = "%1$02d"

class BannerTimerViewHolder(private val customItemView: View, val fragment: Fragment) : AbstractViewHolder(customItemView) {

    private lateinit var bannerTimerViewModel: BannerTimerViewModel
    private var constraintLayout: ConstraintLayout = customItemView.findViewById(R.id.banner_timer_container_layout)
    private var context: Context
    private var bannerImageView: ImageView = customItemView.findViewById(R.id.banner_image_view)

    private lateinit var daysTextView: TextView
    private lateinit var hoursTextView: TextView
    private lateinit var minutesTextView: TextView
    private lateinit var secondsTextView: TextView

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
        val viewHeight = bannerTimerViewModel.getBannerUrlHeight()
        val viewWidth = bannerTimerViewModel.getBannerUrlWidth()
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setDimensionRatio(bannerImageView.id, "H, $viewWidth : $viewHeight")
        constraintSet.applyTo(constraintLayout)

        bannerTimerViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer { componentItem ->
            if (!componentItem.data.isNullOrEmpty()) {
                ImageHandler.LoadImage(bannerImageView, componentItem.data?.get(0)?.backgroundUrlMobile)
                setTimerUI(componentItem, DAYS)
                setTimerUI(componentItem, HOURS)
                setTimerUI(componentItem, MINUTES)
                setTimerUI(componentItem, SECONDS)

                bannerTimerViewModel.startTimer()
            }
        })

        bannerTimerViewModel.getTimerData().observe(fragment.viewLifecycleOwner, Observer {
            daysTextView.text = String.format(TIME_DISPLAY_FORMAT, it.days)
            hoursTextView.text = String.format(TIME_DISPLAY_FORMAT, it.hours)
            minutesTextView.text = String.format(TIME_DISPLAY_FORMAT, it.minutes)
            secondsTextView.text = String.format(TIME_DISPLAY_FORMAT, it.seconds)
        })

        constraintLayout.setOnClickListener {
            bannerTimerViewModel.onBannerClicked(it.context)
        }
    }

    override fun onViewDetachedToWindow() {
        bannerTimerViewModel.stopTimer()
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        bannerTimerViewModel.startTimer()
    }

    private fun setTimerUI(componentItem: ComponentsItem?, timeType: Int) {
        when (timeType) {
            DAYS -> {
                val daysViewLayout: View = customItemView.findViewById(R.id.day_layout)
                val daysTitleTextView: TextView = daysViewLayout.findViewById(R.id.time_title_text_view)
                daysTitleTextView.text = context.resources.getString(R.string.hari)
                daysTitleTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                daysTextView = daysViewLayout.findViewById(R.id.time_text_view)
                daysTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                daysTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
            HOURS -> {
                val hoursViewLayout: View = customItemView.findViewById(R.id.hours_layout)
                val hoursTitleTextView: TextView = hoursViewLayout.findViewById(R.id.time_title_text_view)
                hoursTitleTextView.text = context.resources.getString(R.string.jam)
                hoursTitleTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                hoursTextView = hoursViewLayout.findViewById(R.id.time_text_view)
                hoursTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                hoursTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
            MINUTES -> {
                val minutesViewLayout: View = customItemView.findViewById(R.id.minutes_layout)
                val minutesTitleTextView: TextView = minutesViewLayout.findViewById(R.id.time_title_text_view)
                minutesTitleTextView.text = context.resources.getString(R.string.menit)
                minutesTitleTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                minutesTextView = minutesViewLayout.findViewById(R.id.time_text_view)
                minutesTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                minutesTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
            SECONDS -> {
                val secondsViewLayout: View = customItemView.findViewById(R.id.seconds_layout)
                val secondsTitleTextView: TextView = secondsViewLayout.findViewById(R.id.time_title_text_view)
                secondsTitleTextView.text = context.resources.getString(R.string.detik)
                secondsTitleTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                secondsTextView = secondsViewLayout.findViewById(R.id.time_text_view)
                secondsTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                secondsTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
        }
        setSeparatorUI(componentItem)
    }

    private fun setSeparatorUI(componentItem: ComponentsItem?) {
        customItemView.findViewById<TextView>(R.id.day_separator_text_view).setTextColor(Color.parseColor(getTimerBoxColour(componentItem)))
        customItemView.findViewById<TextView>(R.id.hours_separator_text_view).setTextColor(Color.parseColor(getTimerBoxColour(componentItem)))
        customItemView.findViewById<TextView>(R.id.minutes_separator_text_view).setTextColor(Color.parseColor(getTimerBoxColour(componentItem)))
    }

    @SuppressLint("ResourceType")
    private fun getTimerFontColour(componentItem: ComponentsItem?): String? {
        return if (componentItem?.data?.get(0)?.fontColor?.isEmpty() == true) {
            componentItem.data?.get(0)?.fontColor
        } else {
            context.resources.getString(R.color.clr_ff8000)
        }
    }

    @SuppressLint("ResourceType")
    private fun getTimerBoxColour(componentItem: ComponentsItem?): String? {
        return if (componentItem?.data?.get(0)?.boxColor?.isEmpty() == true) {
            componentItem.data?.get(0)?.boxColor
        } else {
            context.resources.getString(R.color.white)
        }
    }
}