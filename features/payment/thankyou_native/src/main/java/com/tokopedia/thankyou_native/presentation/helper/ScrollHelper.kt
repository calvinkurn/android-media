package com.tokopedia.thankyou_native.presentation.helper

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promotionstarget.presentation.dpToPx
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.fragment.InstantPaymentFragment
import com.tokopedia.thankyou_native.presentation.views.OnViewAddedListener
import com.tokopedia.thankyou_native.presentation.views.ThankYouPageLinearLayout

class ScrollHelper(private val fragment: InstantPaymentFragment) {

    private val scrollListenerMap = mutableMapOf<RecyclerView, RecyclerView.OnScrollListener>()

    fun detectHorizontalScroll(v: View) {
        val px3 = v.dpToPx(3)

        if (v.findViewById<ThankYouPageLinearLayout>(R.id.recommendationContainer) is ThankYouPageLinearLayout) {
            (v.findViewById<ThankYouPageLinearLayout>(R.id.recommendationContainer) as ThankYouPageLinearLayout).onViewAddedListener = object : OnViewAddedListener {
                override fun onViewAdded(view: View?) {
                    if (view is ViewGroup) {
                        val rvList = arrayListOf<RecyclerView>()
                        recursiveFindRecyclerView(view, rvList)
                        rvList.forEach {
                            val listener = object : RecyclerView.OnScrollListener() {
                                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                    super.onScrolled(recyclerView, dx, dy)
                                    if (dx > px3) {
                                        onHorizontalScroll(v)
                                    }
                                }
                            }
                            it.addOnScrollListener(listener)
                            scrollListenerMap[it] = listener
                        }
                    }
                }
            }
        }
    }

    private fun recursiveFindRecyclerView(viewGroup: ViewGroup, list: ArrayList<RecyclerView>) {
        val count = viewGroup.childCount
        var index = 0
        while (index < count) {
            val child = viewGroup.getChildAt(index)
            if (child is RecyclerView) {
                list.add(child)
            } else if (child is ViewGroup) {
                recursiveFindRecyclerView(child, list)
            }
            index += 1
        }
    }

    private fun onHorizontalScroll(v:View) {
        (v.findViewById<ThankYouPageLinearLayout>(R.id.recommendationContainer) as ThankYouPageLinearLayout).onViewAddedListener = null

        scrollListenerMap.forEach {
            it.key.removeOnScrollListener(it.value)
        }
        scrollListenerMap.clear()
        fragment.cancelGratifDialog()
    }
}