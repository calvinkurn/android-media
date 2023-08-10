package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeHeaderBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.utils.view.binding.viewBinding

class HomeHeaderViewHolder(
    itemView: View
) : AbstractViewHolder<HomeHeaderUiModel>(itemView) {

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_header
    }

    private var binding: ItemTokopedianowHomeHeaderBinding? by viewBinding()

    override fun bind(header: HomeHeaderUiModel) {
        when(header.state) {
            HomeLayoutItemState.LOADED -> {
                showHeaderContent(header)
                hideLoading()
            }
            HomeLayoutItemState.ERROR -> {
                showDefaultContent()
                hideLoading()
            }
            else -> {
                showLoading()
                hideContent()
            }
        }
    }

    private fun showHeaderContent(header: HomeHeaderUiModel) {
        binding?.apply {
            val shopStatus = header.shopStatus
            val shippingHint = header.shippingHint
            val subTitle = itemView.context
                .getString(R.string.tokopedianow_home_header_subtitle, shopStatus, shippingHint)

            textTitle.text = header.title
            textSubtitle.text = MethodChecker.fromHtml(subTitle)
            imageIcon.loadImage(header.logoUrl)

            imageIcon.show()
            textTitle.show()
            textSubtitle.show()
            imageChevronDown.show()
        }
    }

    private fun showDefaultContent() {
        binding?.apply {
            textTitle.text = itemView.context
                .getString(R.string.tokopedianow_home_title)
            imageIcon.loadImage(R.drawable.tokopedianow_icon)
            textTitle.show()
            imageIcon.show()
            textSubtitle.hide()
            imageChevronDown.hide()
        }
    }

    private fun showLoading() {
        binding?.apply {
            val color = ContextCompat.getColor(itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White)

            shimmerIcon.setBackgroundColor(color)
            shimmerTitle.setBackgroundColor(color)
            shimmerSubtitle.setBackgroundColor(color)

            shimmerIcon.show()
            shimmerTitle.show()
            shimmerSubtitle.show()
        }
    }

    private fun hideLoading() {
        binding?.apply {
            shimmerIcon.hide()
            shimmerTitle.hide()
            shimmerSubtitle.hide()
        }
    }

    private fun hideContent() {
        binding?.apply {
            textTitle.hide()
            textSubtitle.hide()
            imageIcon.hide()
            imageChevronDown.hide()
        }
    }
}
