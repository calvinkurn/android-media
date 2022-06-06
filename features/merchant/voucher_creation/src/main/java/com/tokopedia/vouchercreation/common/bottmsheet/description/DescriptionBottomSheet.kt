package com.tokopedia.vouchercreation.common.bottmsheet.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcDescriptionBinding

/**
 * Created By @ilhamsuaib on 07/05/20
 */

class DescriptionBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(title: String): DescriptionBottomSheet = DescriptionBottomSheet().apply {
            setTitle(title)
        }

        const val TAG = "DescriptionBottomSheet"
    }

    private var binding by autoClearedNullable<BottomsheetMvcDescriptionBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet() {
        context?.run {
            binding = BottomsheetMvcDescriptionBinding.inflate(LayoutInflater.from(context))
            setChild(binding?.root)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }
    }

    fun show(content: String, fm: FragmentManager) {
        binding?.tvMvcDescription?.text = content.parseAsHtml()
        show(fm, TAG)
    }
}