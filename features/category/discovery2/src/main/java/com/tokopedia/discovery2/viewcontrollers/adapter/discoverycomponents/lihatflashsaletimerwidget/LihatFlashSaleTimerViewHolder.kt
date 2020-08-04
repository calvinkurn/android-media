package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class LihatFlashSaleTimerViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), View.OnClickListener {

    private lateinit var lihatFlashSaleTimerViewModel: LihatFlashSaleTimerViewModel
    private var constraintLayout: ConstraintLayout
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
        constraintLayout = itemView.findViewById(R.id.parent_layout)
        lihatSemuaTextView = itemView.findViewById(R.id.lihat_semua_tv)
        context = constraintLayout.context
    }


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        lihatFlashSaleTimerViewModel = discoveryBaseViewModel as LihatFlashSaleTimerViewModel
        lihatSemuaTextView.setOnClickListener(this)

        lihatFlashSaleTimerViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer { componentItem ->
            if (!componentItem.data.isNullOrEmpty()) {
                setTimerUI(componentItem, HOURS)
                setTimerUI(componentItem, MINUTES)
                setTimerUI(componentItem, SECONDS)

                lihatFlashSaleTimerViewModel.startTimer()
            }
        })

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
                hoursTextView = itemView.findViewById(R.id.hours_layout)
                hoursTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                hoursTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
            MINUTES -> {
                minutesTextView = itemView.findViewById(R.id.minutes_layout)
                minutesTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                minutesTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
            SECONDS -> {
                secondsTextView = itemView.findViewById(R.id.seconds_layout)
                secondsTextView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
                secondsTextView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
            }
        }
        setSeparatorUI(componentItem)
    }

    private fun setSeparatorUI(componentItem: ComponentsItem?) {
        itemView.findViewById<TextView>(R.id.hours_separator_text_view).setTextColor(Color.parseColor(getTimerBoxColour(componentItem)))
        itemView.findViewById<TextView>(R.id.minutes_separator_text_view).setTextColor(Color.parseColor(getTimerBoxColour(componentItem)))
    }


    override fun onViewDetachedToWindow() {
        lihatFlashSaleTimerViewModel.stopTimer()
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        lihatFlashSaleTimerViewModel.startTimer()
    }

    override fun onClick(v: View?) {
        v?.context?.let { lihatFlashSaleTimerViewModel.onLihatSemuaClicked(it) }
    }

    @SuppressLint("ResourceType")
    private fun getTimerFontColour(componentItem: ComponentsItem?): String? {
        return if (componentItem?.data?.get(0)?.timerFontColor?.isEmpty() == true) {
            componentItem.data?.get(0)?.timerFontColor
        } else {
            context.resources.getString(R.color.clr_fd412b)
        }
    }

    @SuppressLint("ResourceType")
    private fun getTimerBoxColour(componentItem: ComponentsItem?): String? {
        return if (componentItem?.data?.get(0)?.timerBoxColor?.isEmpty() == true) {
            componentItem.data?.get(0)?.timerBoxColor
        } else {
            context.resources.getString(R.color.clr_fd412b)
        }
    }
}