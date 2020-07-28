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

    fun trimTrailingWhitespace(source: CharSequence?): CharSequence? {
        if (source == null) return ""
        var i = source.length

        // loop back to the first non-whitespace character
        do {
            --i
        } while (i >= 0 && Character.isWhitespace(source[i]))

        return source.subSequence(0, i + 1)
    }

    private fun showBodyInWebView(textComponentBody: String) {
        bodyTypography.text = trimTrailingWhitespace(MethodChecker.fromHtml(textComponentBody))
    }

    private fun showTitleInWebView(title: String?) {
        titleTypography.text = trimTrailingWhitespace(MethodChecker.fromHtml(title))
    }
}