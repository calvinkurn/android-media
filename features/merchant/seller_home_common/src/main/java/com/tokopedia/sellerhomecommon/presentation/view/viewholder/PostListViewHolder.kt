package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.ListAdapterTypeFactory
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_post_list_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_post_list_widget_error.view.*
import kotlinx.android.synthetic.main.shc_partial_shimmering_post_list_widget.view.*

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class PostListViewHolder(
        view: View?,
        private val listener: Listener
) : AbstractViewHolder<PostListWidgetUiModel>(view), BaseListAdapter.OnAdapterInteractionListener<PostUiModel> {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_post_list_card_widget
    }

    private val postAdapter = BaseListAdapter(ListAdapterTypeFactory(), this)

    private var dataKey: String = ""

    override fun bind(element: PostListWidgetUiModel) {
        itemView.rvPostList.isNestedScrollingEnabled = false
        observeState(element)
    }

    private fun observeState(postListWidgetUiModel: PostListWidgetUiModel) {
        val data = postListWidgetUiModel.data
        when {
            data == null -> onLoading()
            data.error.isNotEmpty() -> {
                onError(postListWidgetUiModel.title)
                listener.setOnErrorWidget(adapterPosition, postListWidgetUiModel)
            }
            else -> onSuccessLoadData(postListWidgetUiModel)
        }
    }

    private fun onLoading() {
        showLoadingState()
    }

    private fun onError(cardTitle: String) {
        hideListLayout()
        hideShimmeringLayout()
        with(itemView) {
            tvPostListTitleOnError.text = cardTitle
            imgWidgetOnError.loadImageDrawable(R.drawable.unify_globalerrors_connection)
            showErrorLayout()
        }
    }

    private fun onSuccessLoadData(postListWidgetUiModel: PostListWidgetUiModel) {
        if (postListWidgetUiModel.data?.items.isNullOrEmpty()) {
            listener.removeWidget(adapterPosition, postListWidgetUiModel)
        } else {
            showSuccessState(postListWidgetUiModel)
        }
    }

    private fun showLoadingState() {
        hideErrorLayout()
        hideListLayout()
        showShimmeringLayout()
    }

    private fun showSuccessState(element: PostListWidgetUiModel) {
        element.data?.run {
            hideErrorLayout()
            hideShimmeringLayout()
            setupTooltip(element.tooltip)
            itemView.tvPostListTitle.text = element.title
            showCtaButtonIfNeeded(element.ctaText, element.appLink)
            setupPostList(items)
            showListLayout()
            addImpressionTracker(element.dataKey, element.impressHolder)
        }
    }

    private fun addImpressionTracker(dataKey: String, impressHolder: ImpressHolder) {
        this@PostListViewHolder.dataKey = dataKey
        itemView.addOnImpressionListener(impressHolder) {
            listener.sendPostListImpressionEvent(dataKey)
        }
    }

    private fun showShimmeringLayout() {
        itemView.sahPostListOnLoadingLayout.visible()
    }

    private fun hideShimmeringLayout() {
        itemView.sahPostListOnLoadingLayout.gone()
    }

    private fun showListLayout() {
        itemView.sahPostListOnSuccessLayout.visible()
    }

    private fun hideListLayout() {
        itemView.sahPostListOnSuccessLayout.gone()
    }

    private fun hideErrorLayout() {
        itemView.sahPostListOnErrorLayout.gone()
    }

    private fun showErrorLayout() {
        itemView.sahPostListOnErrorLayout.visible()
    }

    private fun setupTooltip(tooltip: TooltipUiModel?) = with(itemView) {
        tooltip?.run {
            val shouldShowTooltip = shouldShow && (content.isNotBlank() || list.isNotEmpty())
            if (shouldShowTooltip) {
                imgInfo.visible()
                imgInfo.setOnClickListener { showBottomSheet(tooltip) }
                tvPostListTitle.setOnClickListener { showBottomSheet(tooltip) }
            } else {
                imgInfo.gone()
            }
        }
    }

    private fun showCtaButtonIfNeeded(ctaText: String, appLink: String) {
        val isCtaVisible = ctaText.isNotEmpty() && appLink.isNotEmpty()
        if (isCtaVisible) {
            setupCtaButton(ctaText, appLink)
        }
        toggleCtaButtonVisibility(isCtaVisible)
    }

    private fun setupCtaButton(ctaText: String, appLink: String) {
        itemView.tvPostListSeeDetails.text = ctaText
        itemView.tvPostListSeeDetails.setOnClickListener { goToDetails(appLink) }
    }

    private fun toggleCtaButtonVisibility(isShow: Boolean) = with(itemView) {
        when {
            isShow -> {
                tvPostListSeeDetails.visible()
                icPostListSeeDetails.visible()
            }
            else -> {
                tvPostListSeeDetails.gone()
                icPostListSeeDetails.gone()
            }
        }
    }

    private fun showBottomSheet(tooltip: TooltipUiModel) {
        listener.onTooltipClicked(tooltip)
    }

    private fun goToDetails(appLink: String) {
        if (RouteManager.route(itemView.context, appLink)) {
            listener.sendPostListCtaClickEvent(dataKey)
        }
    }

    private fun setupPostList(posts: List<PostUiModel>) {
        itemView.rvPostList.apply {
            layoutManager = object : LinearLayoutManager(itemView.context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = this@PostListViewHolder.postAdapter
            isNestedScrollingEnabled = true
        }
        postAdapter.run {
            data.clear()
            data.addAll(posts)
            notifyDataSetChanged()
        }
    }

    override fun onItemClicked(post: PostUiModel) {
        if (RouteManager.route(itemView.context, post.appLink)) {
            listener.sendPosListItemClickEvent(dataKey, post.title)
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendPosListItemClickEvent(dataKey: String, title: String) {}

        fun sendPostListCtaClickEvent(dataKey: String) {}

        fun sendPostListImpressionEvent(dataKey: String) {}
    }
}
