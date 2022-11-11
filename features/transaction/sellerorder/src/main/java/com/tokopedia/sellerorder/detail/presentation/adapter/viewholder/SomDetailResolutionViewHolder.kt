package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.databinding.DetailResolutionItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailResolution
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.utils.view.binding.viewBinding

class SomDetailResolutionViewHolder(
    itemView: View?,
    val actionListener: SomDetailAdapterFactoryImpl.ActionListener,
) : AbstractViewHolder<SomDetailData>(itemView) {

    companion object {
        val LAYOUT = R.layout.detail_resolution_item
    }

    private val binding by viewBinding<DetailResolutionItemBinding>()

    override fun bind(element: SomDetailData) {
        binding?.run {
            val uiModel: SomDetailResolution = element.dataObject as SomDetailResolution
            bindResolutionIcon(uiModel.picture)
            bindResolutionTitle(uiModel.title)
            bindResolutionStatus(uiModel.status, uiModel.resolutionStatusFontColor)
            bindResolutionDescription(uiModel.description.parseAsHtml())
            bindResolutionDeadline(uiModel.deadlineDateTime, uiModel.backgroundColor, uiModel.showDeadline)
            bindListener(uiModel.redirectPath)
        }
    }

    private fun DetailResolutionItemBinding.bindResolutionIcon(url: String) {
        ivDisplay.loadImage(url)
    }

    private fun DetailResolutionItemBinding.bindResolutionTitle(title: String) {
        tvTitle.text = title
    }

    private fun DetailResolutionItemBinding.bindResolutionStatus(status: String, fontColor: String) {
        val color = Utils.parseUnifyColorHex(
            context = tvStatus.context,
            colorHex = fontColor,
            defaultColor = com.tokopedia.unifyprinciples.R.color.Unify_TN600
        )
        tvStatus.text = status
        tvStatus.setTextColor(color)
    }

    private fun DetailResolutionItemBinding.bindResolutionDescription(description: CharSequence) {
        tvDescription.text = description
    }

    private fun DetailResolutionItemBinding.bindResolutionDeadline(
        deadlineDateTime: String, backgroundColor: String, showDeadline: Boolean
    ) {
        if (showDeadline) {
            tvDueResponseTitle.show()
            tvDueResponse.show()
            tvDueResponseValue.text = deadlineDateTime
            val deadlineBackground = Utils.getColoredResoDeadlineBackground(
                context = root.context,
                colorHex = backgroundColor,
                defaultColor = com.tokopedia.unifyprinciples.R.color.Unify_YN600
            )
            tvDueResponse.background = deadlineBackground
        } else {
            tvDueResponseTitle.gone()
            tvDueResponse.gone()
        }
    }

    private fun DetailResolutionItemBinding.bindListener(redirectPath: String) {
        root.setOnClickListener { actionListener.onResoClicked(redirectPath) }
    }
}
