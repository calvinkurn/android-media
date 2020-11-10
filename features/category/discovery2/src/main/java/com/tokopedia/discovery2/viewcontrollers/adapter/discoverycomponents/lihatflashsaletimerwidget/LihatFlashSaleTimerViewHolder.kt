package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.TIME_DISPLAY_FORMAT
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class LihatFlashSaleTimerViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), View.OnClickListener {

    private var lihatFlashSaleTimerViewModel: LihatFlashSaleTimerViewModel? = null
    private var constraintLayout: ConstraintLayout = itemView.findViewById(R.id.parent_layout)
    private var lihatSemuaTextView: TextView = itemView.findViewById(R.id.lihat_semua_tv)
    private var context: Context
    private lateinit var hoursTextView: TextView
    private lateinit var minutesTextView: TextView
    private lateinit var secondsTextView: TextView

    companion object {
        const val HOURS = 1
        const val MINUTES = 2
        const val SECONDS = 3
    }

    init {
        context = constraintLayout.context
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        lihatFlashSaleTimerViewModel = discoveryBaseViewModel as LihatFlashSaleTimerViewModel
        lihatSemuaTextView.setOnClickListener(this)
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        lihatFlashSaleTimerViewModel?.startTimer()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            lihatFlashSaleTimerViewModel?.getComponentData()?.observe(it, Observer { componentItem ->
                if (!componentItem.data.isNullOrEmpty()) {
                    setTimerUI(componentItem, HOURS)
                    setTimerUI(componentItem, MINUTES)
                    setTimerUI(componentItem, SECONDS)
                }
            })
            lihatFlashSaleTimerViewModel?.getTimerData()?.observe(it, Observer { timerData ->
                hoursTextView.text = String.format(TIME_DISPLAY_FORMAT, timerData.hours)
                minutesTextView.text = String.format(TIME_DISPLAY_FORMAT, timerData.minutes)
                secondsTextView.text = String.format(TIME_DISPLAY_FORMAT, timerData.seconds)
                if (timerData.timeFinish) (fragment as DiscoveryFragment).reSync()
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            lihatFlashSaleTimerViewModel?.stopTimer()
            lihatFlashSaleTimerViewModel?.getComponentData()?.removeObservers(it)
            lihatFlashSaleTimerViewModel?.getTimerData()?.removeObservers(it)
        }
    }

    private fun setTimerUI(componentItem: ComponentsItem?, timeType: Int) {
        itemView.findViewById<TextView>(R.id.title_tv).text = componentItem?.data?.get(0)?.title
        lihatSemuaTextView.text = componentItem?.data?.get(0)?.buttonText

        when (timeType) {
            HOURS -> {
                hoursTextView = itemView.findViewById(R.id.hours_layout)
                componentItem?.let {
                    setTextCustomColor(hoursTextView, it)
                    setBackgroundCustomColor(hoursTextView, it)
                }
            }
            MINUTES -> {
                minutesTextView = itemView.findViewById(R.id.minutes_layout)
                componentItem?.let {
                    setTextCustomColor(minutesTextView, it)
                    setBackgroundCustomColor(minutesTextView, it)
                }
            }
            SECONDS -> {
                secondsTextView = itemView.findViewById(R.id.seconds_layout)
                componentItem?.let {
                    setTextCustomColor(secondsTextView, it)
                    setBackgroundCustomColor(secondsTextView, it)
                }
            }
        }
        setSeparatorUI(componentItem)
    }

    private fun setSeparatorUI(componentItem: ComponentsItem?) {
        itemView.findViewById<TextView>(R.id.hours_separator_text_view).setTextColor(Color.parseColor(getTimerBoxColour(componentItem)))
        itemView.findViewById<TextView>(R.id.minutes_separator_text_view).setTextColor(Color.parseColor(getTimerBoxColour(componentItem)))
    }

    private fun setTextCustomColor(textView: TextView, componentItem: ComponentsItem) {
        try {
            textView.setTextColor(Color.parseColor(getTimerFontColour(componentItem)))
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun setBackgroundCustomColor(textView: TextView, componentItem: ComponentsItem) {
        try {
            textView.background.setColorFilter(Color.parseColor(getTimerBoxColour(componentItem)), PorterDuff.Mode.SRC_ATOP)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    override fun onViewDetachedToWindow() {
        lihatFlashSaleTimerViewModel?.stopTimer()
    }

    override fun onClick(v: View?) {
        v?.context?.let { lihatFlashSaleTimerViewModel?.onLihatSemuaClicked(it) }
    }

    @SuppressLint("ResourceType")
    private fun getTimerFontColour(componentItem: ComponentsItem?): String? {
        return if (componentItem?.data?.firstOrNull()?.timerFontColor?.isEmpty() == true) {
            componentItem.data!![0].timerFontColor
        } else {
            context.resources.getString(R.color.white)
        }
    }

    @SuppressLint("ResourceType")
    private fun getTimerBoxColour(componentItem: ComponentsItem?): String? {
        return if (componentItem?.data?.firstOrNull()?.timerBoxColor?.isEmpty() == true) {
            componentItem.data!![0].timerBoxColor
        } else {
            context.resources.getString(R.color.clr_fd412b)
        }
    }
}