package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tkpd.remoteresourcerequest.view.ImageDensityType
import com.tokopedia.discovery2.Constant.EmptyStateTexts.EMPTY_IMAGE
import com.tokopedia.discovery2.Constant.EmptyStateTexts.FILTER_EMPTY_IMAGE
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.emptystate.EmptyStateModel
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class EmptyStateViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var emptyStateViewModel: EmptyStateViewModel
    private val horizontalView: ConstraintLayout = itemView.findViewById(R.id.horizontal_view)
    private val verticalView: ConstraintLayout = itemView.findViewById(R.id.vertical_view)
    private val verticalTitle: Typography = itemView.findViewById(R.id.vertical_title_tv)
    private val verticalDecription: Typography = itemView.findViewById(R.id.vertical_decription_tv)
    private val horizontalTitle: Typography = itemView.findViewById(R.id.horizontal_title_tv)
    private val horizontalDecription: Typography = itemView.findViewById(R.id.horizontal_decription_tv)
    private val verticalButton: UnifyButton = itemView.findViewById(R.id.vertical_button)
    private val verticalImageView: DeferredImageView = itemView.findViewById(R.id.vertical_image_empty_state)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        emptyStateViewModel = discoveryBaseViewModel as EmptyStateViewModel
        getSubComponent().inject(emptyStateViewModel)
        init()
    }

    private fun init() {
        emptyStateViewModel.getEmptyStateData().let {
            if(it.isHorizontal){
                horizontalView.show()
                verticalView.hide()
                horizontalTitle.text = it.title
                horizontalDecription.text = it.description
            } else {
                setVerticalState(it)
            }
        }
    }

    private fun setVerticalState(emptyStateModel: EmptyStateModel) {
        horizontalView.hide()
        verticalView.show()
        verticalTitle.text = emptyStateModel.title
        verticalDecription.text = emptyStateModel.description
        if (emptyStateModel.isFilterState) {
            verticalButton.show()
            verticalImageView.loadRemoteImageDrawable(FILTER_EMPTY_IMAGE,  ImageDensityType.SUPPORT_SINGLE_DPI)
            verticalButton.setOnClickListener {
                emptyStateViewModel.handleEmptyStateReset()
            }
        }else{
            verticalButton.hide()
            verticalImageView.loadRemoteImageDrawable(EMPTY_IMAGE)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            emptyStateViewModel.getSyncPageLiveData().observe(lifecycle, {
                if (it) (fragment as DiscoveryFragment).reSync()
            })
        }
    }
}