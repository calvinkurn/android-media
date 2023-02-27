package com.tokopedia.play.ui.view.taggedproductlabel

import com.tokopedia.content.common.databinding.ViewTaggedProductLabelBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.content.common.R as commonR

/**
 * @author by astidhiyaa on 27/02/23
 */
class TaggedProductLabelUiView(
    private val binding: ViewTaggedProductLabelBinding,
    listener: Listener
) {

    init {
        binding.root.setOnClickListener {
            listener.onTaggedProductLabelClicked(this)
        }
    }

    fun show(isShown: Boolean) {
        binding.root.showWithCondition(isShown)
    }

    fun setProductSize(value: Int) {
        binding.tvTaggedProduct.text = binding.root.resources.getString(commonR.string.content_comment_tagged_product_label, value)
    }

    interface Listener {
        fun onTaggedProductLabelClicked(view: TaggedProductLabelUiView)
    }
}
