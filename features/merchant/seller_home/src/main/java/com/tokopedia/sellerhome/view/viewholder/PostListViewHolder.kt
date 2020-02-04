package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeWidgetTooltipClickListener
import com.tokopedia.sellerhome.view.adapter.ListAdapterTypeFactory
import com.tokopedia.sellerhome.view.model.PostListWidgetUiModel
import com.tokopedia.sellerhome.view.model.PostUiModel
import com.tokopedia.sellerhome.view.model.TooltipUiModel
import kotlinx.android.synthetic.main.sah_partial_post_list_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_progress_widget_error.view.*
import kotlinx.android.synthetic.main.sah_partial_shimmering_post_list_widget.view.*

class PostListViewHolder(
        view: View?,
        private val listener: Listener,
        private val tooltipClickListener: SellerHomeWidgetTooltipClickListener
) : AbstractViewHolder<PostListWidgetUiModel>(view), BaseListAdapter.OnAdapterInteractionListener<PostUiModel> {

    companion object {
        val RES_LAYOUT = R.layout.sah_post_list_card_widget
    }

    private lateinit var adapter: BaseListAdapter<PostUiModel, ListAdapterTypeFactory>

    override fun bind(element: PostListWidgetUiModel) {
        val data = element.data
        when {
            data == null -> {
                showLoadingState()
                listener.getPostData()
            }
            data.error.isNotEmpty() -> {
                showErrorState(element)
            }
            else -> {
                if (data.items.isEmpty()) {
                    listener.removeWidget(adapterPosition, element)
                } else {
                    showSuccessState(element)
                }
            }
        }
    }

    private fun showLoadingState() {
        hideErrorLayout()
        hideListLayout()
        showShimmeringLayout()
    }

    private fun showErrorState(element: PostListWidgetUiModel) {
        hideListLayout()
        hideShimmeringLayout()
        itemView.tv_error_card_title.text = element.title
        showErrorLayout()
    }

    private fun showSuccessState(element: PostListWidgetUiModel) {
        element.data?.run {
            hideErrorLayout()
            hideShimmeringLayout()
            setupTooltip(element.tooltip)
            itemView.tv_card_title.text = element.title
            showCtaButtonIfNeeded(element.ctaText, element.appLink)
            setupPostList(items)
            showListLayout()
        }
    }

    private fun showShimmeringLayout() {
        itemView.sah_list_layout_shimmering.visible()
    }

    private fun hideShimmeringLayout() {
        itemView.sah_list_layout_shimmering.gone()
    }

    private fun showListLayout() {
        itemView.sah_list_layout.visible()
    }

    private fun hideListLayout() {
        itemView.sah_list_layout.gone()
    }

    private fun showErrorLayout() {
        itemView.sah_error_layout.visible()
    }

    private fun hideErrorLayout() {
        itemView.sah_error_layout.gone()
    }

    private fun setupTooltip(tooltip: TooltipUiModel?) {
        if (tooltip == null) {
            itemView.iv_info.gone()
        } else {
            itemView.iv_info.visible()
            itemView.iv_info.setOnClickListener { showBottomSheet(tooltip) }
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
        itemView.tv_see_details.text = ctaText
        itemView.tv_see_details.setOnClickListener { goToDetails(appLink) }
    }

    private fun toggleCtaButtonVisibility(isShow: Boolean) {
        when (isShow) {
            true -> {
                itemView.tv_see_details.visible()
                itemView.iv_see_details.visible()
            }
            false -> {
                itemView.tv_see_details.gone()
                itemView.iv_see_details.gone()
            }
        }
    }

    private fun showBottomSheet(tooltip: TooltipUiModel) {
        tooltipClickListener.onInfoTooltipClicked(tooltip)
    }

    private fun goToDetails(appLink: String) {
        val intent = RouteManager.getIntent(itemView.context, appLink)
        itemView.context.startActivity(intent)
    }

    private fun setupPostList(posts: List<PostUiModel>) {
        adapter = BaseListAdapter<PostUiModel, ListAdapterTypeFactory>(ListAdapterTypeFactory(), this@PostListViewHolder)
        itemView.rv_post.apply {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = this@PostListViewHolder.adapter
        }
        adapter.run {
            data.addAll(posts)
            notifyDataSetChanged()
        }
    }

    override fun onItemClicked(post: PostUiModel) {
        val intent = RouteManager.getIntent(itemView.context, post.appLink)
        itemView.context.startActivity(intent)
    }

    interface Listener {
        fun getPostData()
        fun removeWidget(position: Int, data: PostListWidgetUiModel)
    }
}