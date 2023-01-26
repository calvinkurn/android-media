package com.tokopedia.sellerorder.orderextension.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.databinding.BottomSheetInfoPickTimeOrderExtentionBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.sellerorder.R
import com.tokopedia.unifycomponents.HtmlLinkHelper

class InfoPickTimeOrderExtentionBottomSheet(
    private val fm: FragmentManager? = null
) : BottomSheetUnify() {

    companion object {
        private val TAG: String = InfoPickTimeOrderExtentionBottomSheet::class.java.simpleName
    }

    private var binding by autoClearedNullable<BottomSheetInfoPickTimeOrderExtentionBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetInfoPickTimeOrderExtentionBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@InfoPickTimeOrderExtentionBottomSheet)?.commit()
        }
    }

    fun show() {
        fm?.let { show(it, TAG) }
    }

    private fun setupView() {
        val title =
            context?.getString(R.string.bottomsheet_order_extension_request_info_pick_time_title)
                .orEmpty()
        setTitle(title)

        context?.let {
            val description = HtmlLinkHelper(
                it,
                it.getString(R.string.bottomsheet_order_extension_request_info_pick_time_desc)
            ).spannedString ?: ""

            binding?.textDescription?.text = description
        }
        val padding =
            context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                .orZero()

        binding?.root?.setPadding(0, 0, 0, padding)
    }
}
