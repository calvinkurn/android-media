package com.tokopedia.search.result.product.onboarding

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ChooseAddressViewHolder
import com.tokopedia.search.result.presentation.view.fragment.RecyclerViewUpdater
import com.tokopedia.search.result.product.similarsearch.SimilarSearchOnBoardingView
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import java.util.Collections.max
import javax.inject.Inject

@SearchScope
class OnBoardingListenerDelegate @Inject constructor(
    private val recyclerViewUpdater: RecyclerViewUpdater,
    @SearchContext context: Context,
) : SimilarSearchOnBoardingView, ContextProvider by WeakReferenceContextProvider(context) {

    companion object {
        private const val ON_BOARDING_DELAY_MS: Long = 200
    }

    private var coachMark: CoachMark2? = null
    private var shouldShowCoachmark = true
    private var firstProductPosition = -1

    private var shouldShowProductViewTypeCoachmark = false

    fun enableProductViewTypeCoachmark() {
        shouldShowProductViewTypeCoachmark = true
    }

    fun showProductViewTypeOnBoarding() {
        if (coachMark != null || !shouldShowProductViewTypeCoachmark) return

        buildCoachMark2()

        recyclerViewUpdater.recyclerView?.postDelayed({
            showProductViewTypeCoachmark(getChooseAddressView())
        }, ON_BOARDING_DELAY_MS)
    }

    private fun getChooseAddressView(): View? {
        val chooseAddressItemPosition = recyclerViewUpdater.itemList?.indexOfFirst {
            it is ChooseAddressDataView
        } ?: -1
        val viewHolder = recyclerViewUpdater
            .recyclerView
            ?.findViewHolderForAdapterPosition(chooseAddressItemPosition)
            ?: return null

        return if (viewHolder is ChooseAddressViewHolder)  {
            viewHolder.getChangeViewButton()
        } else null
    }

    private fun showProductViewTypeCoachmark(view: View?) {
        val coachMark2ItemList = createProductViewTypeCoachMark2ItemList(view)
        if (coachMark2ItemList.isEmpty()) return

        coachMark?.showCoachMark(coachMark2ItemList, null, 0)
    }

    private fun createProductViewTypeCoachMark2ItemList(
        changeViewTypeView: View?
    ): ArrayList<CoachMark2Item> {
        val coachMarkItemList = ArrayList<CoachMark2Item>()

        if (changeViewTypeView != null)
            coachMarkItemList.add(createViewTypeLabelCoachMark2Item(changeViewTypeView))

        return coachMarkItemList
    }

    private fun createViewTypeLabelCoachMark2Item(changeViewTypeView: View): CoachMark2Item {
        return CoachMark2Item(
            changeViewTypeView,
            context?.getString(R.string.search_product_view_type_onboarding_title) ?: "",
            context?.getString(R.string.search_product_view_type_onboarding_description)?: "",
            CoachMark2.POSITION_BOTTOM
        )
    }

    fun showProductWithBOEOnBoarding(firstProductPosition: Int) {
        if (coachMark != null || !shouldShowCoachmark) return

        this.firstProductPosition = firstProductPosition

        buildCoachMark2()

        val productWithBOELabel = getFirstProductWithBOELabel(firstProductPosition)
        recyclerViewUpdater.recyclerView?.postDelayed({
            showBOECoachmark(productWithBOELabel)
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
                shouldShowProductViewTypeCoachmark = false
                shouldShowCoachmark = false
            }
        }
    }

    private fun showBOECoachmark(view: View?) {
        val coachMark2ItemList = createBOECoachMark2ItemList(view)
        if (coachMark2ItemList.isEmpty()) return

        coachMark?.showCoachMark(coachMark2ItemList, null, 0)
    }

    private fun createBOECoachMark2ItemList(boeLabelProductCard: View?): ArrayList<CoachMark2Item> {
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

            private fun RecyclerView.LayoutManager?.findFirstVisibleItemPosition() : Int {
                return when(this) {
                    is GridLayoutManager -> findFirstVisibleItemPosition()
                    is StaggeredGridLayoutManager -> max(findFirstVisibleItemPositions(null).asList())
                    is LinearLayoutManager -> findFirstVisibleItemPosition()
                    else -> -1
                }
            }
            private fun RecyclerView.LayoutManager?.findLastVisibleItemPosition() : Int {
                return when(this) {
                    is GridLayoutManager -> findLastVisibleItemPosition()
                    is StaggeredGridLayoutManager -> max(findLastVisibleItemPositions(null).asList())
                    is LinearLayoutManager -> findLastVisibleItemPosition()
                    else -> -1
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItem = recyclerView.layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItem = recyclerView.layoutManager.findLastVisibleItemPosition()
                if (firstProductPosition in (visibleItem + 1)..lastVisibleItem) {
                    showProductWithBOEOnBoarding(firstProductPosition)
                } else {
                    coachMark?.hideCoachMark()
                }
            }
        }
    }

    override fun showSimilarSearchThreeDotsCoachmark(
        item: ProductItemDataView,
        adapterPosition: Int,
    ) {
        if (coachMark != null || !shouldShowCoachmark) return

        buildCoachMark2()

        val view = getProductThreeDotsView(adapterPosition)
        recyclerViewUpdater.recyclerView?.postDelayed({
            showThreeDotsCoachmark(view)
        }, ON_BOARDING_DELAY_MS)
    }

    private fun getProductThreeDotsView(adapterPosition: Int): View? {
        val viewHolder = recyclerViewUpdater.recyclerView
            ?.findViewHolderForAdapterPosition(adapterPosition)
            ?: return null
        return if (viewHolder.itemView is IProductCardView) (viewHolder.itemView as IProductCardView).getThreeDotsButton() else null
    }

    private fun showThreeDotsCoachmark(view: View?) {
        val coachMark2ItemList = createThreeDotsCoachMark2ItemList(view)
        if (coachMark2ItemList.isEmpty()) return

        coachMark?.showCoachMark(coachMark2ItemList, null, 0)
    }

    private fun createThreeDotsCoachMark2ItemList(productCard: View?): ArrayList<CoachMark2Item> {
        val coachMarkItemList = ArrayList<CoachMark2Item>()

        if (productCard != null)
            coachMarkItemList.add(createThreeDotsCoachMark2Item(productCard))

        return coachMarkItemList
    }

    private fun createThreeDotsCoachMark2Item(productCard: View): CoachMark2Item {
        return CoachMark2Item(
            productCard,
            "",
            context?.getString(R.string.search_product_similar_search_three_dots_onboarding_description)?: "",
            CoachMark2.POSITION_BOTTOM
        )
    }
}
