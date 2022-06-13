package com.tokopedia.buyerorderdetail.presentation.coachmark

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.scroller.BuyerOrderDetailRecyclerViewScroller
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference

class CoachMarkManager(
    private val rootView: View,
    private val smoothScroller: BuyerOrderDetailRecyclerViewScroller?
) {

    private val coachMark by lazy {
        CoachMark2(rootView.context).apply {
            onDismissListener = ::onDismiss
            setStepListener(createStepHandler())
        }
    }

    private val coachMarkItemsManager by lazy {
        CoachMarkItemsManager()
    }
    private val scrollListener = createScrollListener()

    private val rvBuyerOrderDetail = rootView.findViewById<RecyclerView>(R.id.rvBuyerOrderDetail)
    private val adapter: BuyerOrderDetailAdapter?
        get() = rvBuyerOrderDetail?.adapter as? BuyerOrderDetailAdapter
    private val layoutManager: LinearLayoutManager?
        get() = rvBuyerOrderDetail?.layoutManager as? LinearLayoutManager

    init {
        setupCoachMarkManager()
    }

    private fun createScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!tryShowCoachMark() && coachMarkItemsManager.shouldHideCoachMarkItem()) {
                    coachMark.hideCoachMark()
                }
            }
        }
    }

    private fun onDismiss() {
        coachMarkItemsManager.updateHasShownStatus(rootView.context)
        resetCoachMarkState()
    }

    private fun createStepHandler(): CoachMark2.OnStepListener {
        return object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                coachMarkItemsManager.currentCoachMarkPosition = currentIndex
                coachMark.isDismissed = true
                tryScrollToSectionWithCoachMark()
            }
        }
    }

    private fun setupCoachMarkManager() {
        attachScrollListenerToRecyclerView()
    }

    private fun attachScrollListenerToRecyclerView() {
        rvBuyerOrderDetail?.addOnScrollListener(scrollListener)
    }

    private fun tryShowCoachMark(): Boolean {
        var coachMarkShowed = false
        if (!coachMark.isShowing) {
            val currentCoachMarkItem = coachMarkItemsManager.getCurrentCoachMarkItemManager()
            val itemPosition = getAdapterPositionForCurrentCoachMark()
            if (itemPosition != RecyclerView.NO_POSITION) {
                val viewHolderRootView = layoutManager?.findViewByPosition(itemPosition)
                if (currentCoachMarkItem != null && viewHolderRootView != null) {
                    currentCoachMarkItem.viewHolderRootView = viewHolderRootView
                    if (currentCoachMarkItem.shouldShowCoachMark()) {
                        coachMark.isDismissed = false
                        coachMark.showCoachMark(
                            step = coachMarkItemsManager.getUnifyCoachMarkItems(),
                            index = coachMarkItemsManager.getCoachMarkPosition()
                        )
                        coachMarkShowed = true
                    }
                }
            }
        }
        return coachMarkShowed
    }

    private fun tryScrollToSectionWithCoachMark() {
        smoothScroller?.scrollToPosition(getAdapterPositionForCurrentCoachMark())
    }

    private fun getAdapterPositionForCurrentCoachMark(): Int {
        val currentCoachMarkItem = coachMarkItemsManager.getCurrentCoachMarkItemManager()
        return adapter?.getItemPosition(
            currentCoachMarkItem?.uiModel
        ) ?: RecyclerView.NO_POSITION
    }

    fun notifyUpdatedAdapter() {
        val coachMarkItemManagers = adapter?.getBaseVisitableUiModels()?.mapNotNull { visitable ->
            visitable.getCoachMarkItemManager()?.takeIf { coachMarkItemManager ->
                CoachMarkPreference.hasShown(rootView.context, coachMarkItemManager.key).not()
            }
        }?.toSet().orEmpty()
        coachMarkItemsManager.coachMarkItemManagers = ArrayList(coachMarkItemManagers)
        tryScrollToSectionWithCoachMark()
    }

    fun resetCoachMarkState() {
        coachMarkItemsManager.reset()
        coachMark.hideCoachMark()
    }

    fun dismissCoachMark() {
        coachMark.dismissCoachMark()
    }
}