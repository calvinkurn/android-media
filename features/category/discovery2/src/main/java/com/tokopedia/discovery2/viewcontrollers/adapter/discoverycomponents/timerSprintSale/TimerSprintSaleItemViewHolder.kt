package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifyprinciples.Typography

class TimerSprintSaleItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var timerSprintSaleItemViewModel: TimerSprintSaleItemViewModel
    private val tvHours: Typography = itemView.findViewById(R.id.tv_hours)
    private val tvMinutes: Typography = itemView.findViewById(R.id.tv_minutes)
    private val tvSeconds: Typography = itemView.findViewById(R.id.tv_seconds)
    private val backgroundView: View = itemView.findViewById(R.id.background_view)
    private val tvTitle: Typography = itemView.findViewById(R.id.tv_title)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        timerSprintSaleItemViewModel = discoveryBaseViewModel as TimerSprintSaleItemViewModel

        when {
            timerSprintSaleItemViewModel.isFutureSale() -> {
                backgroundView.background = MethodChecker.getDrawable(itemView.context, R.drawable.discovery_timer_sale_blue_background)
                tvTitle.text = itemView.context.getString(R.string.discovery_sale_begins_in)
            }
            timerSprintSaleItemViewModel.isSaleOver() -> {
                backgroundView.background = MethodChecker.getDrawable(itemView.context, R.drawable.discovery_timer_sale_grey_background)
                tvTitle.text = itemView.context.getString(R.string.discovery_sale_has_ended)
            }
            else -> {
                backgroundView.background = MethodChecker.getDrawable(itemView.context, R.drawable.discovery_timer_sale_red_background)
                tvTitle.text = itemView.context.getString(R.string.discovery_sale_ends_in)
            }
        }
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            timerSprintSaleItemViewModel.getComponentLiveData().observe(it, Observer { componentItem ->
                if (!componentItem.data.isNullOrEmpty()) {
                    timerSprintSaleItemViewModel.startTimer()
                    sendSprintSaleTimerTrack()
                }
            })

            timerSprintSaleItemViewModel.getTimerData().observe(it, Observer { timerData ->
                tvHours.text = String.format(TIME_DISPLAY_FORMAT, timerData.hours)
                tvMinutes.text = String.format(TIME_DISPLAY_FORMAT, timerData.minutes)
                tvSeconds.text = String.format(TIME_DISPLAY_FORMAT, timerData.seconds)
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            timerSprintSaleItemViewModel.getComponentLiveData().removeObservers(it)
            timerSprintSaleItemViewModel.getTimerData().removeObservers(it)
        }
    }

    private fun sendSprintSaleTimerTrack() {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackTimerSprintSale()
    }

    override fun onViewDetachedToWindow() {
        timerSprintSaleItemViewModel.stopTimer()
    }

    companion object {
        const val TIME_DISPLAY_FORMAT = "%1$02d"
    }
}
