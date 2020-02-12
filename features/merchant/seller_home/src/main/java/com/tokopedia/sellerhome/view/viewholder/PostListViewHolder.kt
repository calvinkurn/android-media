package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.SellerHomeTracking
import com.tokopedia.sellerhome.view.adapter.ListAdapterTypeFactory
import com.tokopedia.sellerhome.view.model.PostListWidgetUiModel
import com.tokopedia.sellerhome.view.model.PostUiModel
import com.tokopedia.sellerhome.view.model.TooltipUiModel
import kotlinx.android.synthetic.main.sah_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.sah_partial_post_list_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_post_list_widget_error.view.*
import kotlinx.android.synthetic.main.sah_partial_shimmering_post_list_widget.view.*

class PostListViewHolder(
        view: View?,
        private val listener: Listener
) : AbstractViewHolder<PostListWidgetUiModel>(view), BaseListAdapter.OnAdapterInteractionListener<PostUiModel> {

    companion object {
        val RES_LAYOUT = R.layout.sah_post_list_card_widget
    }

    private lateinit var adapter: BaseListAdapter<PostUiModel, ListAdapterTypeFactory>

    private var dataKey: String = ""

    override fun bind(element: PostListWidgetUiModel) {
        observeState(element)
    }

    private fun observeState(postListWidgetUiModel: PostListWidgetUiModel) {
        val data = postListWidgetUiModel.data
        when {
            data == null -> onLoading()
            data.error.isNotEmpty() -> onError(postListWidgetUiModel.title)
            else -> onSuccessLoadData(postListWidgetUiModel)
        }
    }

    private fun onLoading() {
        showLoadingState()
        listener.getPostData()
    }

    private fun onError(cardTitle: String) {
        showErrorState(cardTitle)
    }

    private fun onSuccessLoadData(postListWidgetUiModel: PostListWidgetUiModel) {
        with(postListWidgetUiModel) {
            if (data?.items.isNullOrEmpty()) {
                listener.removeWidget(adapterPosition, postListWidgetUiModel)
            } else {
                showSuccessState(postListWidgetUiModel)
            }
        }
    }

    private fun showLoadingState() {
        hideErrorLayout()
        hideListLayout()
        showShimmeringLayout()
    }

    private fun showErrorState(cardTitle: String) {
        hideListLayout()
        hideShimmeringLayout()
        with(itemView) {
            tvPostListTitleOnError.text = cardTitle
            ImageHandler.loadImageWithId(imgWidgetOnError, R.drawable.unify_globalerrors_connection)
            sahPostListOnErrorLayout.visible()
        }
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
            SellerHomeTracking.sendImpressionPostEvent(dataKey)
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

    private fun setupTooltip(tooltip: TooltipUiModel?) = with(itemView) {
        tooltip?.run {
            if (content.isNotBlank() || list.isNotEmpty()) {
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
            SellerHomeTracking.sendClickPostSeeMoreEvent(dataKey)
        }
    }

    private fun setupPostList(posts: List<PostUiModel>) {
        adapter = BaseListAdapter<PostUiModel, ListAdapterTypeFactory>(ListAdapterTypeFactory(), this@PostListViewHolder)
        itemView.rvPostList.apply {
            layoutManager = object : LinearLayoutManager(itemView.context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = this@PostListViewHolder.adapter
            isNestedScrollingEnabled = true
        }
        adapter.run {
            data.addAll(posts)
            notifyDataSetChanged()
        }
    }

    override fun onItemClicked(post: PostUiModel) {
        if (RouteManager.route(itemView.context, post.appLink)) {
            SellerHomeTracking.sendClickPostItemEvent(dataKey, post.title)
        }
    }

    interface Listener : BaseViewHolderListener {
        fun getPostData()
    }
}