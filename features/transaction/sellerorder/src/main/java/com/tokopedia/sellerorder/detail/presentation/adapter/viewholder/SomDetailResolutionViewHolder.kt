package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.databinding.DetailResolutionItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailResolution
import com.tokopedia.sellerorder.detail.di.SomDetailScope
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.utils.view.binding.viewBinding

@SomDetailScope
class SomDetailResolutionViewHolder(
    itemView: View?,
    val actionListener: SomDetailAdapterFactoryImpl.ActionListener,
) : AbstractViewHolder<SomDetailData>(itemView) {

    companion object {
        val LAYOUT = R.layout.detail_resolution_item
    }

    private val binding by viewBinding<DetailResolutionItemBinding>()

    override fun bind(element: SomDetailData?) {

        itemView.context?.let { context ->
            element?.let {
                val uiModel: SomDetailResolution = it.dataObject as SomDetailResolution
                binding?.tvTitle?.text = uiModel.title
                binding?.tvStatus?.text = uiModel.status
                binding?.tvDescription?.text = uiModel.description
                binding?.ivDisplay?.loadImage(uiModel.picture)
                showDeadline(context, uiModel)
                itemView.setOnClickListener {
                    actionListener.onResoClicked(uiModel.redirectPath.orEmpty())
                }
            }
        }
    }

    private fun showDeadline(context: Context, uiModel: SomDetailResolution) {
        if (!uiModel.showDeadline.orFalse()) {
            binding?.tvDueResponseTitle?.visibility = View.GONE
            binding?.tvDueResponse?.visibility = View.GONE
        } else {
            binding?.tvDueResponseTitle?.visibility = View.VISIBLE
            binding?.tvDueResponse?.visibility = View.VISIBLE
            binding?.tvDueResponseValue?.text = uiModel.deadlineDateTime
            val deadlineBackground = Utils.getColoredResoDeadlineBackground(
                context = context,
                colorHex = uiModel.backgroundColor.orEmpty(),
                defaultColor = com.tokopedia.unifyprinciples.R.color.Unify_YN600
            )
            binding?.tvDueResponse?.background = deadlineBackground
        }
    }

}