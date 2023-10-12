package com.tokopedia.productcard.options.onboarding

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.productcard.options.R
import java.lang.ref.WeakReference

class OnBoardingListenerDelegate(
    context: Context
) : LifecycleEventObserver {
    private val contextReference = WeakReference(context)
    private val context: Context?
        get() = contextReference.get()

    private var coachMark: CoachMark2? = null

    fun observeLifeCycle(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun buildCoachMark(recyclerView: RecyclerView, adapterPosition: Int) {
        if (coachMark != null) return

        buildCoachMark2()

        val view = getProductView(recyclerView, adapterPosition)
        recyclerView.postDelayed({
            showCoachmark(view)
        }, ON_BOARDING_DELAY_MS)
    }

    private fun getProductView(recyclerView: RecyclerView, adapterPosition: Int): View? {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(adapterPosition)
            ?: return null
        return viewHolder.itemView
    }

    private fun showCoachmark(view: View?) {
        val coachMark2ItemList = createCoachMark2ItemList(view)
        if (coachMark2ItemList.isEmpty()) return

        coachMark?.showCoachMark(coachMark2ItemList, null, 0)
    }

    private fun createCoachMark2ItemList(similarSearchOptionView: View?): ArrayList<CoachMark2Item> {
        val coachMarkItemList = ArrayList<CoachMark2Item>()

        if (similarSearchOptionView != null)
            coachMarkItemList.add(createThreeDotsCoachMark2Item(similarSearchOptionView))

        return coachMarkItemList
    }

    private fun createThreeDotsCoachMark2Item(similarSearchOptionView: View): CoachMark2Item {
        return CoachMark2Item(
            similarSearchOptionView,
            "",
            context?.getString(R.string.productcard_options_similar_search_coachmark_description)?: "",
            CoachMark2.POSITION_TOP
        )
    }

    private fun buildCoachMark2() {
        val context = context ?: return

        coachMark = CoachMark2(context).apply {
            setOnDismissListener {
                coachMark = null
            }
            onDismissListener = {
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            onViewDestroyed()
        }
    }

    private fun onViewDestroyed() {
        coachMark?.dismiss()
        coachMark = null
    }

    companion object {
        private const val ON_BOARDING_DELAY_MS: Long = 200
    }
}
