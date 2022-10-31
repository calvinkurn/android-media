package com.tokopedia.topchat.chatsetting.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topchat.chatsetting.view.uimodel.BubbleActivationGuideUiModel
import com.tokopedia.topchat.databinding.ItemTopchatBubbleActivationGuideBinding

class BubbleActivationGuideAdapter :
    RecyclerView.Adapter<BubbleActivationGuideAdapter.BubbleActivationGuideViewHolder>() {

    private val bubbleActivationGuideList = mutableListOf<BubbleActivationGuideUiModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDetailPerformanceData(newBubbleActivationGuideList: List<BubbleActivationGuideUiModel>) {
        bubbleActivationGuideList.clear()
        bubbleActivationGuideList.addAll(newBubbleActivationGuideList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BubbleActivationGuideViewHolder {
        val binding = ItemTopchatBubbleActivationGuideBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BubbleActivationGuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BubbleActivationGuideViewHolder, position: Int) {
        val bubbleActivationGuideItem = bubbleActivationGuideList.getOrNull(position)
        bubbleActivationGuideItem?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = bubbleActivationGuideList.size

    inner class BubbleActivationGuideViewHolder(
        private val binding: ItemTopchatBubbleActivationGuideBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BubbleActivationGuideUiModel) {
            with(binding) {
                setActivationGuideDetailInformation(item)
                setBubbleActivationContent(item)
            }
        }

        private fun ItemTopchatBubbleActivationGuideBinding.setBubbleActivationContent(item: BubbleActivationGuideUiModel) {
            topchatIvBubbleActivationGuide.setImageUrl(item.imageUrl, BUBBLE_ACTIVATION_GUIDE_RATIO)
            topchatTvBubbleActivationGuideDesc.text = MethodChecker.fromHtml(item.desc)
        }

        private fun ItemTopchatBubbleActivationGuideBinding.setActivationGuideDetailInformation(
            item: BubbleActivationGuideUiModel
        ) {
            if (bindingAdapterPosition.isZero()) {
                topchatIvBubbleActivationGuideDescSampleIcon.run {
                    show()
                    loadImage(item.detailInformationUrl)
                }
                topchatTvBubbleActivationGuideDescSampleIcon.run {
                    show()
                    text = item.descDetailInformation
                }
            } else {
                topchatTvBubbleActivationGuideDescSampleIcon.hide()
                topchatIvBubbleActivationGuideDescSampleIcon.hide()
            }
        }
    }

    companion object {
        private const val BUBBLE_ACTIVATION_GUIDE_RATIO = 0.4f
    }

}
