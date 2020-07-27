package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify

class TextComponentViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val shimmerView: ImageUnify = itemView.findViewById(R.id.shimmer_view)
    private val titleTypography: TextView = itemView.findViewById(R.id.text_component_title)
    private val bodyTypography: TextView = itemView.findViewById(R.id.text_component_body)
    private lateinit var textComponentViewModel: TextComponentViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        textComponentViewModel = discoveryBaseViewModel as TextComponentViewModel
        setUpObserver()
    }

    private fun setUpObserver() {
        textComponentViewModel.getTextComponentLiveData().observe(fragment.viewLifecycleOwner, Observer {
            showTitleInWebView(it.title)
            showBodyInWebView(it.textComponentBody)
            shimmerView.hide()
        })
    }

    private fun showBodyInWebView(textComponentBody: String) {
        bodyTypography.text = MethodChecker.fromHtmlPreserveLineBreak(textComponentBody)
    }

    private fun showTitleInWebView(title: String?) {
        titleTypography.text = MethodChecker.fromHtmlPreserveLineBreak(title)
    }
}