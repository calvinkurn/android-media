package com.tokopedia.oneclickcheckout.order.view.card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.databinding.LayoutOccOnboardingNewBinding
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding

class OrderOnboardingCard(private val binding: LayoutOccOnboardingNewBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = 1
    }

    fun bind(onboarding: OccMainOnboarding) {
        if (onboarding.isShowOnboardingTicker) {
            binding.ivNewOccOnboarding.setImageUrl(onboarding.onboardingTicker.image)
            binding.tvNewOccOnboardingMessage.text = onboarding.onboardingTicker.message
            binding.containerOccOnboarding.visible()
            binding.dividerOccOnboarding.visible()
        } else {
            binding.containerOccOnboarding.gone()
            binding.dividerOccOnboarding.gone()
        }
    }
}