package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.TIME_DISPLAY_FORMAT
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel
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
    }


    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        setTimerType()
        timerSprintSaleItemViewModel.startTimer()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            timerSprintSaleItemViewModel.getComponentLiveData().observe(it, Observer { componentItem ->
                if (!componentItem.data.isNullOrEmpty()) {
                    sendSprintSaleTimerTrack()
                }
            })
            timerSprintSaleItemViewModel.getTimerData().observe(it, Observer { timerData ->
                if (timerData.timeFinish) {
                    timerSprintSaleItemViewModel.stopTimer()
                    setTimerType()
                    timerSprintSaleItemViewModel.handleSaleEndSates()
                }
                setTimerTime(timerData)
            })
            timerSprintSaleItemViewModel.refreshPage().observe(it, Observer { refreshPage ->
                if (refreshPage) (fragment as DiscoveryFragment).onRefresh()
            })
            timerSprintSaleItemViewModel.getSyncPageLiveData().observe(it, Observer { needResync ->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            })
        }
    }

    private fun setTimerType() {
        when {
            Utils.isFutureSale(timerSprintSaleItemViewModel.getStartDate()) -> {
                backgroundView.background = MethodChecker.getDrawable(itemView.context, R.drawable.discovery_timer_sale_blue_background)
                tvTitle.text = itemView.context.getString(R.string.discovery_sale_begins_in)
            }
            Utils.isSaleOver(timerSprintSaleItemViewModel.getEndDate()) -> {
                backgroundView.background = MethodChecker.getDrawable(itemView.context, R.drawable.discovery_timer_sale_grey_background)
                tvTitle.text = itemView.context.getString(R.string.discovery_sale_has_ended)
                setTimerTime(null)
                timerSprintSaleItemViewModel.checkUpcomingSaleTimer()
            }
            else -> {
                backgroundView.background = MethodChecker.getDrawable(itemView.context, R.drawable.discovery_timer_sale_red_background)
                tvTitle.text = itemView.context.getString(R.string.discovery_sale_ends_in)
            }
        }
    }

    private fun setTimerTime(timerData: TimerDataModel?) {
        if (timerData == null) {
            tvHours.text = String.format(TIME_DISPLAY_FORMAT, 0)
            tvMinutes.text = String.format(TIME_DISPLAY_FORMAT, 0)
            tvSeconds.text = String.format(TIME_DISPLAY_FORMAT, 0)
        } else {
            tvHours.text = String.format(TIME_DISPLAY_FORMAT, timerData.hours)
            tvMinutes.text = String.format(TIME_DISPLAY_FORMAT, timerData.minutes)
            tvSeconds.text = String.format(TIME_DISPLAY_FORMAT, timerData.seconds)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            timerSprintSaleItemViewModel.stopTimer()
            timerSprintSaleItemViewModel.getComponentLiveData().removeObservers(it)
            timerSprintSaleItemViewModel.getTimerData().removeObservers(it)
            timerSprintSaleItemViewModel.refreshPage().removeObservers(it)
            timerSprintSaleItemViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }

    private fun sendSprintSaleTimerTrack() {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackTimerSprintSale()
    }

    override fun onViewDetachedToWindow() {
        timerSprintSaleItemViewModel.stopTimer()
    }
}
