package com.tokopedia.homenav.util

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.AccountHeaderViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.InitialShimmeringDataViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.InitialShimmeringTransactionDataRevampViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.MainNavListAdapter
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerTransactionRevampDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ShimmerReviewDataModel

private const val NAME = "Recycler view has item idling resource"

internal class MainNavRecyclerViewIdlingResource(
    private val recyclerView: RecyclerView?,
    private val name: String? = NAME
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val recyclerView = recyclerView ?: return true

        val isIdle = recyclerView.noMoreShimmering()
        if (isIdle) resourceCallback?.onTransitionToIdle()

        return isIdle
    }

    private fun RecyclerView?.noMoreShimmering(): Boolean {
        (recyclerView?.adapter as? MainNavListAdapter)?.let {
            val isBuShimmeringExist = isModelExist(
                { it is InitialShimmerDataModel },
                { it is InitialShimmeringDataViewHolder }
            )

            val isTransactionShimmeringExist = isModelExist(
                { it is InitialShimmerTransactionRevampDataModel },
                { it is InitialShimmeringTransactionDataRevampViewHolder }
            )

            val isAccountTransactionShimmeringExist = isModelExist(
                {
                    it is AccountHeaderDataModel && (it as? AccountHeaderDataModel)?.let {
                        it.isCacheData
                    }?: true
                },
                { it is AccountHeaderViewHolder }
            )

            val isReviewShimmeringExist = isModelExist(
                { it is ShimmerReviewDataModel },
                { it is InitialShimmeringTransactionDataRevampViewHolder }
            )

            return !isBuShimmeringExist && !isTransactionShimmeringExist && !isAccountTransactionShimmeringExist && !isReviewShimmeringExist
        }

        return this != null
    }

    private fun isModelExist(predicateModel: (Visitable<*>) -> Boolean, predicateViewHolder: (holder: RecyclerView.ViewHolder?) -> Boolean): Boolean {
        (recyclerView?.adapter as? MainNavListAdapter)?.let {
            val shimmeringPos = it.currentList.indexOfFirst { predicateModel.invoke(it) }
            return (predicateViewHolder(recyclerView.findViewHolderForAdapterPosition(shimmeringPos)))
        }
        return false
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}
