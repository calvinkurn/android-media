package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.SingleGlobalNavViewModel
import com.tokopedia.search.result.presentation.view.listener.GlobalNavListener
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.search_single_global_nav_view_holder.view.*

class SingleGlobalNavViewHolder(
        itemView: View,
        private val globalNavListener: GlobalNavListener?
) : AbstractViewHolder<SingleGlobalNavViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_single_global_nav_view_holder
    }

    override fun bind(element: SingleGlobalNavViewModel) {
        setRandomBackground()
        bindTitle(element.item)
        bindListener(element)

        if (element.navTemplate == SearchConstant.GlobalNav.NAV_TEMPLATE_PILL) setPillContent(element)
        else setCardContent(element)
    }

    private fun setRandomBackground() {
        val backgroundIndex = intArrayOf(
                ContainerUnify.RED,
                ContainerUnify.GREEN,
                ContainerUnify.BLUE,
                ContainerUnify.YELLOW,
                ContainerUnify.GREY
        ).indices.shuffled().first()
        itemView.searchSingleGlobalNavContainer?.setContainerColor(backgroundIndex)
    }

    private fun bindTitle(item: SingleGlobalNavViewModel.Item) {
        itemView.searchSingleGlobalNavTitle?.shouldShowWithAction(item.name.isNotEmpty()) {
            itemView.searchSingleGlobalNavTitle?.text = item.name
        }
    }

    private fun bindListener(element: SingleGlobalNavViewModel) {
        itemView.searchSingleGlobalNavCard?.setOnClickListener {
            globalNavListener?.onSingleGlobalNavClicked(element.item, element.keyword)
        }
    }

    private fun setPillContent(element: SingleGlobalNavViewModel) {
        setPillTypography()
        bindIconWithLogoUrl(element.item)
        hideContentForPill()
    }

    private fun setPillTypography() {
        itemView.searchSingleGlobalNavTitle?.setType(Typography.HEADING_5)
    }

    private fun bindIconWithLogoUrl(item: SingleGlobalNavViewModel.Item) {
        itemView.searchSingleGlobalNavIcon?.shouldShowWithAction(item.logoUrl.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(itemView.context, itemView.searchSingleGlobalNavIcon, item.logoUrl)
            itemView.searchSingleGlobalNavImage?.visibility = View.GONE
        }
    }

    private fun hideContentForPill() {
        itemView.searchSingleGlobalNavCategory?.visibility = View.GONE
        itemView.searchSingleGlobalNavSubtitleInfoLayout?.visibility = View.GONE
    }

    private fun setCardContent(element: SingleGlobalNavViewModel) {
        setCardTypography()

        val item = element.item
        bindIconCard(item)
        bindCategory(item)
        bindSubtitleInfo(item)
    }

    private fun setCardTypography() {
        itemView.searchSingleGlobalNavTitle?.setType(Typography.BODY_3)
        itemView.searchSingleGlobalNavTitle?.setWeight(Typography.BOLD)
    }

    private fun bindIconCard(item: SingleGlobalNavViewModel.Item) {
        if (item.imageUrl.isNotEmpty()) bindIconWithImageUrl(item)
        else bindIconWithLogoUrl(item)
    }

    private fun bindIconWithImageUrl(item: SingleGlobalNavViewModel.Item) {
        itemView.searchSingleGlobalNavImage?.shouldShowWithAction(item.imageUrl.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(itemView.context, itemView.searchSingleGlobalNavImage, item.imageUrl)
            itemView.searchSingleGlobalNavIcon?.visibility = View.GONE
        }
    }

    private fun AppCompatImageView.setHeightWidth(height: Int, width: Int) {
        this.layoutParams?.height = height.dpToPx(itemView.resources.displayMetrics)
        this.layoutParams?.width = width.dpToPx(itemView.resources.displayMetrics)
        this.requestLayout()
    }

    private fun bindCategory(item: SingleGlobalNavViewModel.Item) {
        itemView.searchSingleGlobalNavCategory?.shouldShowWithAction(item.categoryName.isNotEmpty()) {
            itemView.searchSingleGlobalNavCategory?.text = item.categoryName
        }
    }

    private fun bindSubtitleInfo(item: SingleGlobalNavViewModel.Item) {
        if (item.subtitle.isNotEmpty() && item.info.isNotEmpty()) {
            bindSubtitle(item)
            bindInfo(item)
        } else hideSubtitleInfoLayout()
    }

    private fun bindInfo(item: SingleGlobalNavViewModel.Item) {
        itemView.searchSingleGlobalNavInfo?.shouldShowWithAction(item.info.isNotEmpty()) {
            itemView.searchSingleGlobalNavInfo?.text = item.info
        }
    }

    private fun bindSubtitle(item: SingleGlobalNavViewModel.Item) {
        itemView.searchSingleGlobalNavSubtitle?.shouldShowWithAction(item.subtitle.isNotEmpty()) {
            itemView.searchSingleGlobalNavSubtitle?.text = item.subtitle
        }
    }

    private fun hideSubtitleInfoLayout() {
        itemView.searchSingleGlobalNavSubtitleInfoLayout?.visibility = View.GONE
    }
}
