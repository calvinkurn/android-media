package com.tokopedia.globalnavwidget

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.globalnavwidget.databinding.GlobalNavWidgetCardItemLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

internal class GlobalNavWidgetCardViewHolder(
        itemView: View,
        private val listener: GlobalNavWidgetListener
): RecyclerView.ViewHolder(itemView) {

    private var binding: GlobalNavWidgetCardItemLayoutBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.global_nav_widget_card_item_layout
    }

    private val context: Context? = itemView.context

    internal fun bind(item: GlobalNavWidgetModel.Item) {
        bindOnClickListener(item)
        bindImageBackgroundAndLogo(item)
        bindCategoryName(item)
        bindTitle(item)
        bindSubtitleAndStrikethrough(item)
        bindInfo(item)
    }

    private fun bindOnClickListener(item: GlobalNavWidgetModel.Item) {
        binding?.globalNavCardItemCardView?.setOnClickListener { _ ->
            listener.onClickItem(item)
        }
    }

    private fun bindImageBackgroundAndLogo(item: GlobalNavWidgetModel.Item) {
        val backgroundUrl = if (item.imageUrl.isNotEmpty()) item.imageUrl else item.backgroundUrl

        binding?.globalNavCardItemBackground?.let {
            ImageHandler.loadImageFitCenter(context, it, backgroundUrl)
        }

        val shouldShowLogo = item.imageUrl.isEmpty() && item.logoUrl.isNotEmpty()

        binding?.globalNavCardItemLogo?.shouldShowWithAction(shouldShowLogo) {
            ImageHandler.loadImageFitCenter(context, it, item.logoUrl)
        }
    }

    private fun bindCategoryName(item: GlobalNavWidgetModel.Item) {
        binding?.globalNavCardCategoryName?.shouldShowWithAction(item.categoryName.isNotEmpty()) {
            binding?.globalNavCardCategoryName?.text = item.categoryName
        }
    }

    private fun bindTitle(item: GlobalNavWidgetModel.Item) {
        binding?.globalNavCardTitle.shouldShowWithAction(item.name.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(item.name)
        }
    }

    private fun bindSubtitleAndStrikethrough(item: GlobalNavWidgetModel.Item) {
        val shouldShowStrikethrough = item.strikethrough.isNotEmpty()

        binding?.globalNavCardStrikethrough.shouldShowWithAction(shouldShowStrikethrough) {
            it.text = MethodChecker.fromHtml(item.strikethrough)
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        val shouldShowSubtitle = !shouldShowStrikethrough && item.subtitle.isNotEmpty()

        binding?.globalNavCardSubtitle.shouldShowWithAction(shouldShowSubtitle) {
            it.text = MethodChecker.fromHtml(item.subtitle)
        }
    }

    private fun bindInfo(item: GlobalNavWidgetModel.Item) {
        binding?.globalNavCardPrice.shouldShowWithAction(item.info.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(item.info)
        }
    }
}