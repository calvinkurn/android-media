package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.R.color.Unify_GN100
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class AnchorTabsItemViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val anchorImage: ImageUnify = itemView.findViewById(R.id.anchor_icon)
    private val anchorText: Typography = itemView.findViewById(R.id.anchor_text)
    private val anchorParent: ConstraintLayout = itemView.findViewById(R.id.anchor_parent)

    private lateinit var viewModel: AnchorTabsItemViewModel

    init {
        itemView.setOnClickListener {
            if (::viewModel.isInitialized) {
                val sectionID = viewModel.getSectionID()
                if (sectionID.isNotEmpty())
                    (fragment as DiscoveryFragment).scrollToSection(
                        sectionID,
                        viewModel.parentPosition()
                    )
            }
        }
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as AnchorTabsItemViewModel
        setupView()
    }

    private fun setupView() {
        anchorText.text = viewModel.getTitle()

        val imageUrl = viewModel.getImageUrl()
        if (imageUrl.isNotEmpty())
            anchorImage.loadImage(imageUrl)
        else {
//            Todo:: Add handling for else case
        }

        if (viewModel.isSelected())
            anchorParent.setBackgroundColor(
                MethodChecker.getColor(
                    anchorParent.context,
                    Unify_GN100
                )
            )
        else
            anchorParent.setBackgroundColor(
                (MethodChecker.getColor(
                    anchorParent.context,
                    R.color.discovery2_dms_anchor_bg
                ))
            )
    }

}