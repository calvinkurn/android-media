package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.timer.TimerUnifySingle

class TimerSprintSaleItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var timerSprintSaleItemViewModel: TimerSprintSaleItemViewModel
    private val timerUnify: TimerUnifySingle = itemView.findViewById(R.id.sprint_sale_timer)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        timerSprintSaleItemViewModel = discoveryBaseViewModel as TimerSprintSaleItemViewModel
        setTimerType()
    }


    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        timerUnify.onFinish = {
            timerSprintSaleItemViewModel.timerWithBannerCounter = null
            setTimerType()
            timerSprintSaleItemViewModel.handleSaleEndSates()
        }
        timerSprintSaleItemViewModel.startTimer(timerUnify)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            timerSprintSaleItemViewModel.getComponentLiveData().observe(it, Observer { componentItem ->
                if (!componentItem.data.isNullOrEmpty()) {
                    sendSprintSaleTimerTrack()
                }
            })
            timerSprintSaleItemViewModel.refreshPage().observe(it, Observer { refreshPage ->
                if (refreshPage) (fragment as DiscoveryFragment).onRefresh()
            })
            timerSprintSaleItemViewModel.getSyncPageLiveData().observe(it, Observer { needResync ->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            })
            timerSprintSaleItemViewModel.getRestartTimerAction().observe(it, { shouldStartTimer ->
                if (shouldStartTimer)
                    timerSprintSaleItemViewModel.startTimer(timerUnify)
            })
        }
    }

    private fun setTimerType() {
        timerUnify.timerVariant = timerSprintSaleItemViewModel.getTimerVariant()
        when {
            Utils.isFutureSale(timerSprintSaleItemViewModel.getStartDate()) -> {
                timerUnify.timerText = itemView.context.getString(R.string.discovery_sale_begins_in)
            }
            Utils.isSaleOver(timerSprintSaleItemViewModel.getEndDate()) -> {
                timerUnify.timerText = itemView.context.getString(R.string.discovery_sale_has_ended)
                timerSprintSaleItemViewModel.checkUpcomingSaleTimer()
            }
            else -> {
                timerUnify.timerText = itemView.context.getString(R.string.discovery_sale_ends_in)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            timerSprintSaleItemViewModel.stopTimer()
            timerUnify.pause()
            timerSprintSaleItemViewModel.getRestartTimerAction().removeObservers(it)
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
        timerUnify.pause()
    }
}
