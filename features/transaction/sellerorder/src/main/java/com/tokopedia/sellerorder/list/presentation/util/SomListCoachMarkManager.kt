package com.tokopedia.sellerorder.list.presentation.util

import android.os.Build
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.databinding.FragmentSomListBinding

class SomListCoachMarkManager(
    private val somListBinding: FragmentSomListBinding?,
    private val userId: String
) {

    companion object {
        private const val SHARED_PREF_SOM_LIST_COACH_MARK = "somListCoachMarkV1"
        private const val COACH_MARK_ITEM_ORDER_ITEM_POS = 1
        private const val EXPECTED_COACH_MARK_ITEM_COUNT = 2
    }

    private val coachMark: CoachMark2? by lazy {
        somListBinding?.root?.context?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                CoachMark2(it)
            } else {
                null
            }
        }
    }

    private val scrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (currentCoachMarkPosition == COACH_MARK_ITEM_ORDER_ITEM_POS) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        showCoachMark()
                    } else {
                        dismissCoachMark()
                    }
                }
            }
        }
    }

    private val somListLayoutManager: LinearLayoutManager?
        get() = somListBinding?.rvSomList?.layoutManager as? LinearLayoutManager

    private var currentCoachMarkPosition = Int.ZERO

    init {
        initScrollListener()
    }

    private fun initScrollListener() {
        somListBinding?.root?.context?.let { context ->
            if (!CoachMarkPreference.hasShown(context, SHARED_PREF_SOM_LIST_COACH_MARK) && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                somListBinding.rvSomList.addOnScrollListener(scrollListener)
            }
        }
    }

    private fun createCoachMarkItems(): ArrayList<CoachMark2Item> {
        return arrayListOf<CoachMark2Item>().apply {
            somListBinding?.sortFilterSomList?.let {
                if (it.isVisible) {
                    add(
                        CoachMark2Item(
                            it,
                            it.context?.resources?.getString(R.string.som_list_coachmark_sort_filter_title)
                                .orEmpty(),
                            it.context?.resources?.getString(R.string.som_list_coachmark_sort_filter_description)
                                .orEmpty()
                        )
                    )
                }
            }
            findFirstVisibleOrderView()?.let {
                add(
                    CoachMark2Item(
                        it,
                        it.context?.resources?.getString(R.string.som_list_coachmark_order_card_title).orEmpty(),
                        it.context?.resources?.getString(R.string.som_list_coachmark_order_card_description).orEmpty()
                    )
                )
            }
        }
    }

    private fun findFirstVisibleOrderView(): View? {
        val somListIsNotVisible = somListBinding?.rvSomList?.isVisible == false
        if (somListIsNotVisible) return null
        val firstVisibleIndex = somListLayoutManager?.findFirstCompletelyVisibleItemPosition().orZero()
        val lastVisibleIndex = somListLayoutManager?.findLastCompletelyVisibleItemPosition().orZero()
        val rangeVisibleIndex = firstVisibleIndex..lastVisibleIndex
        for (i in rangeVisibleIndex) {
            val viewHolderView = somListLayoutManager?.findViewByPosition(i)
            if (viewHolderView?.findViewById<View>(R.id.cardSomOrder) != null) {
                return viewHolderView
            }
        }
        return null
    }

    private fun setupCoachMark() {
        coachMark?.onFinishListener = {
            somListBinding?.root?.context?.let { context ->
                SomAnalytics.eventClickOnboardInfoOk(userId)
                CoachMarkPreference.setShown(context, SHARED_PREF_SOM_LIST_COACH_MARK, true)
                somListBinding.rvSomList.removeOnScrollListener(scrollListener)
            }
        }
        coachMark?.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                val isMovingToNextStep = currentIndex > currentCoachMarkPosition
                val isMovingToPreviousStep = currentIndex < currentCoachMarkPosition
                if (isMovingToNextStep) {
                    SomAnalytics.eventClickOnboardInfoContinue(userId)
                } else if (isMovingToPreviousStep) {
                    SomAnalytics.eventClickOnboardInfoBack(userId)
                }
                currentCoachMarkPosition = currentIndex
                if (currentCoachMarkPosition == COACH_MARK_ITEM_ORDER_ITEM_POS) {
                    coachMark?.isDismissed = true
                    somListBinding?.rvSomList?.post {
                        showCoachMark()
                    }
                }
            }
        })
    }

    fun showCoachMark() {
        somListBinding?.root?.context?.let { context ->
            if (!CoachMarkPreference.hasShown(context, SHARED_PREF_SOM_LIST_COACH_MARK) && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val coachMarkItems = createCoachMarkItems()
                if (coachMarkItems.size == EXPECTED_COACH_MARK_ITEM_COUNT) {
                    setupCoachMark()
                    coachMark?.isDismissed = false
                    coachMark?.showCoachMark(step = coachMarkItems, index = currentCoachMarkPosition)
                }
            }
        }
    }

    fun dismissCoachMark() {
        coachMark?.dismissCoachMark()
        coachMark?.isDismissed = true
    }
}
