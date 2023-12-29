package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcardemptystate

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.databinding.DiscoContentCardEmptyStateBinding
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.Toaster

class ContentCardEmptyStateViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: DiscoContentCardEmptyStateBinding = DiscoContentCardEmptyStateBinding.bind(itemView)
    private var emptyStateViewModel: ContentCardEmptyStateViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        emptyStateViewModel = discoveryBaseViewModel as ContentCardEmptyStateViewModel
        val discoItemCount = (fragment as? DiscoveryFragment)?.getItemCount()
        val emptyStateText = emptyStateViewModel?.getToastMessage(discoItemCount)
        binding.errorContentCardTitle.text = emptyStateText
        binding.contentEmptyCardImageContainer.setOnClickListener {
            if (discoItemCount != null) {
                if (emptyStateViewModel?.position == discoItemCount - 1) {
                    itemView.context?.getString(R.string.card_empty_toast_lc)?.let { it1 -> Toaster.build(itemView, it1, Toast.LENGTH_SHORT).show() }
                } else {
                    (fragment as? DiscoveryFragment)?.scrollToNextComponent(emptyStateViewModel?.position)
                }
            }
        }
    }
}
