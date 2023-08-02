package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TextComponentViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val shimmerView: ImageUnify = itemView.findViewById(R.id.shimmer_view)
    private val titleTypography: Typography = itemView.findViewById(R.id.text_component_title)
    private val bodyTypography: Typography = itemView.findViewById(R.id.text_component_body)
    private var textComponentViewModel: TextComponentViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        textComponentViewModel = discoveryBaseViewModel as TextComponentViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        textComponentViewModel?.getTextComponentLiveData()?.observe(
            fragment.viewLifecycleOwner,
            Observer {
                showTitleInWebView(it.title)
                showBodyInWebView(it.body)
                shimmerView.hide()
            }
        )
    }

    private fun showBodyInWebView(textComponentBody: String) {
        bodyTypography.text = MethodChecker.fromHtmlWithoutExtraSpace(textComponentBody)
    }

    private fun showTitleInWebView(title: String?) {
        titleTypography.text = MethodChecker.fromHtmlWithoutExtraSpace(title)
    }
}
