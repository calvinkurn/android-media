package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcardemptystate

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.databinding.DiscoContentCardEmptyStateBinding
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class ContentCardEmptyStateViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: DiscoContentCardEmptyStateBinding = DiscoContentCardEmptyStateBinding.bind(itemView)
    private lateinit var emptyStateViewModel: ContentCardEmptyStateViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        emptyStateViewModel = discoveryBaseViewModel as ContentCardEmptyStateViewModel
        val emptyStateTexts = emptyStateViewModel.getToastMessage((fragment as? DiscoveryFragment)?.getItemCount())
        binding.errorContentCardTitle.text = emptyStateTexts.first
        binding.contentEmptyCardImageContainer.setOnClickListener {
            Toast.makeText(itemView.context, emptyStateTexts.second, Toast.LENGTH_SHORT).show()
        }
    }
}
