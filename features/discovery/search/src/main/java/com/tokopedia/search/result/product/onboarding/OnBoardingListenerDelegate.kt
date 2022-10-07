package com.tokopedia.search.result.product.onboarding

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.result.presentation.view.fragment.RecyclerViewUpdater
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import java.util.Collections.max
import javax.inject.Inject

class OnBoardingListenerDelegate @Inject constructor(
    private val recyclerViewUpdater: RecyclerViewUpdater,
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager,
    @SearchContext context: Context,
) : ContextProvider by WeakReferenceContextProvider(context) {

    companion object {
        private const val ON_BOARDING_DELAY_MS: Long = 200
    }

    private var coachMark: CoachMark2? = null
    private var shouldShowCoachmark = true
    private var firstProductPosition = -1

    fun showOnBoarding(firstProductPosition: Int) {
        if (coachMark != null || !shouldShowCoachmark) return

        this.firstProductPosition = firstProductPosition

        buildCoachMark2()

        val productWithBOELabel = getFirstProductWithBOELabel(firstProductPosition)
        recyclerViewUpdater.recyclerView?.postDelayed({
            showCoachmark(productWithBOELabel)
        }, ON_BOARDING_DELAY_MS)
    }

    fun dismissCoachmark() {
        coachMark?.dismissCoachMark()
    }

    private fun getFirstProductWithBOELabel(firstProductPositionWithBOELabel: Int): View? {
        val viewHolder = recyclerViewUpdater
            .recyclerView
            ?.findViewHolderForAdapterPosition(firstProductPositionWithBOELabel)
            ?: return null

        return if (viewHolder.itemView is IProductCardView) viewHolder.itemView
        else null
    }

    private fun buildCoachMark2() {
        val context = context ?: return

        coachMark = CoachMark2(context).apply {
            setOnDismissListener {
                coachMark = null
            }
            onDismissListener = {
                shouldShowCoachmark = false
            }
        }
    }

    private fun showCoachmark(view: View?) {
        val coachMark2ItemList = createCoachMark2ItemList(view)
        if (coachMark2ItemList.isEmpty()) return

        coachMark?.showCoachMark(coachMark2ItemList, null, 0)
    }

    private fun createCoachMark2ItemList(boeLabelProductCard: View?): ArrayList<CoachMark2Item> {
        val coachMarkItemList = ArrayList<CoachMark2Item>()

        if (boeLabelProductCard != null)
            coachMarkItemList.add(createBOELabelCoachMark2Item(boeLabelProductCard))

        return coachMarkItemList
    }

    private fun createBOELabelCoachMark2Item(boeLabelProductCard: View): CoachMark2Item {
        return CoachMark2Item(
            boeLabelProductCard,
            context?.getString(R.string.search_product_boe_label_onboarding_title) ?: "",
            context?.getString(R.string.search_product_boe_label_onboarding_description)?: "",
            CoachMark2.POSITION_TOP
        )
    }

    fun createScrollListener(): RecyclerView.OnScrollListener {
        return object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(null)
                val lastVisibleItems = staggeredGridLayoutManager.findLastVisibleItemPositions(null)
                if (max(lastVisibleItems.asList()) >= firstProductPosition
                    && max(visibleItems.asList()) < firstProductPosition) {
                    showOnBoarding(firstProductPosition)
                } else {
                    coachMark?.hideCoachMark()
                }
            }
        }
    }
}
