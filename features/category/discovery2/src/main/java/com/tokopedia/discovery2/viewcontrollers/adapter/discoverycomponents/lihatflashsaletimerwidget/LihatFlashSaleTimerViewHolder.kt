package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.BannerTimerViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class LihatFlashSaleTimerViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), View.OnClickListener {

    private lateinit var lihatFlashSaleTimerViewModel: LihatFlashSaleTimerViewModel
    private var linearLayout: LinearLayout
    private var context: Context
    private lateinit var hoursTextView: TextView
    private lateinit var minutesTextView: TextView
    private lateinit var secondsTextView: TextView

    private var lihatSemuaTextView: TextView
    private val TIME_DISPLAY_FORMAT = "%1$02d"

    companion object {
        const val HOURS = 1
        const val MINUTES = 2
        const val SECONDS = 3
    }

    init {
        linearLayout = itemView.findViewById(R.id.parent_layout)
        lihatSemuaTextView = itemView.findViewById(R.id.lihat_semua_tv)
        context = linearLayout.context
    }


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        lihatFlashSaleTimerViewModel = discoveryBaseViewModel as LihatFlashSaleTimerViewModel
        lihatSemuaTextView.setOnClickListener(this)

        // Observe Model Data
        lihatFlashSaleTimerViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer { componentItem ->
            if (!componentItem.data.isNullOrEmpty()) {
                setTimerUI(componentItem, BannerTimerViewHolder.DAYS)
                setTimerUI(componentItem, BannerTimerViewHolder.HOURS)
                setTimerUI(componentItem, BannerTimerViewHolder.MINUTES)
                setTimerUI(componentItem, BannerTimerViewHolder.SECONDS)

                lihatFlashSaleTimerViewModel.startTimer()
            }
        })

        // Observe Timer Data
        lihatFlashSaleTimerViewModel.getTimerData().observe(fragment.viewLifecycleOwner, Observer {
            hoursTextView.text = String.format(TIME_DISPLAY_FORMAT, it.hours)
            minutesTextView.text = String.format(TIME_DISPLAY_FORMAT, it.minutes)
            secondsTextView.text = String.format(TIME_DISPLAY_FORMAT, it.seconds)
        })
    }

    private fun setTimerUI(componentItem: ComponentsItem?, timeType: Int) {
        itemView.findViewById<TextView>(R.id.title_tv).text = componentItem?.data?.get(0)?.title
        lihatSemuaTextView.text = componentItem?.data?.get(0)?.buttonText

        when (timeType) {
            HOURS -> {
                val hoursViewLayout: View = itemView.findViewById(R.id.hours_layout)
                hoursTextView = hoursViewLayout.findViewById(R.id.time_text_view)
                hoursTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                hoursTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
            MINUTES -> {
                val minutesViewLayout: View = itemView.findViewById(R.id.minutes_layout)
                minutesTextView = minutesViewLayout.findViewById(R.id.time_text_view)
                minutesTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                minutesTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
            SECONDS -> {
                val secondsViewLayout: View = itemView.findViewById(R.id.seconds_layout)
                secondsTextView = secondsViewLayout.findViewById(R.id.time_text_view)
                secondsTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                secondsTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
        }
        setSeparatorUI(componentItem)
    }

    private fun setSeparatorUI(componentItem: ComponentsItem?) {
        itemView.findViewById<TextView>(R.id.hours_separator_text_view).setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
        itemView.findViewById<TextView>(R.id.minutes_separator_text_view).setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
    }


    override fun onViewDetachedToWindow() {
        lihatFlashSaleTimerViewModel.stopTimer()
    }

    override fun onClick(v: View?) {
        lihatFlashSaleTimerViewModel.onLihatSemuaClicked()
    }

    @SuppressLint("ResourceType")
    private fun getTimerFontColour(componentItem: ComponentsItem?): String? {
        return if (componentItem?.data?.get(0)?.timerFontColor != null && componentItem.data?.get(0)?.timerFontColor!!.isNotEmpty()) {
            componentItem.data?.get(0)?.timerFontColor
        } else {
            context.resources.getString(R.color.clr_fd412b)
        }
    }

    @SuppressLint("ResourceType")
    private fun getTimerBoxColour(componentItem: ComponentsItem?): String? {
        return if (componentItem?.data?.get(0)?.timerBoxColor != null && componentItem.data?.get(0)?.timerBoxColor!!.isNotEmpty()) {
            componentItem.data?.get(0)?.timerBoxColor
        } else {
            context.resources.getString(R.color.white)
        }
    }
}