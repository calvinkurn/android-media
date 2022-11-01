package com.tokopedia.topchat.chatsetting.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.chatsetting.view.uimodel.BubbleActivationIntroUiModel
import com.tokopedia.topchat.databinding.ItemTopchatBubbleActivationIntroBinding

class BubbleActivationIntroAdapter: RecyclerView.Adapter<BubbleActivationIntroAdapter.BubbleActivationIntroViewHolder>() {

    private val introItemList = mutableListOf<BubbleActivationIntroUiModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setIntroItemData(items: List<BubbleActivationIntroUiModel>) {
        introItemList.clear()
        introItemList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BubbleActivationIntroViewHolder {
        val binding = ItemTopchatBubbleActivationIntroBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BubbleActivationIntroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BubbleActivationIntroViewHolder, position: Int) {
        introItemList.getOrNull(position)?.let { introItem ->
            holder.bind(introItem)
        }
    }

    override fun getItemCount(): Int = introItemList.size

    inner class BubbleActivationIntroViewHolder(
        private val binding: ItemTopchatBubbleActivationIntroBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(uiModel: BubbleActivationIntroUiModel) {
            setImageUrl(uiModel.imageUrl)
            setBenefitText(uiModel.benefitText)
            setDivider()
        }

        private fun setImageUrl(imageUrl: String) {
            binding.imgItemTopchatBubbleIntro.setImageUrl(imageUrl)
        }

        private fun setBenefitText(benefitText: String) {
            binding.tvItemTopchatBubbleIntro.text = MethodChecker.fromHtml(benefitText)
        }

        private fun setDivider() {
            binding.dividerItemTopchatBubbleIntro.showWithCondition(bindingAdapterPosition.isZero())
        }

    }

}
