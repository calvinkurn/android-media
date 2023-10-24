package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerorder.databinding.BottomsheetTransparencyFeeInfoBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TransparencyFeeInfoBottomSheet: BottomSheetUnify() {

    private val title by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(TITLE_KEY).orEmpty()
    }

    private val desc by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(DESC_KEY).orEmpty()
    }

    private var binding by autoClearedNullable<BottomsheetTransparencyFeeInfoBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetTransparencyFeeInfoBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(title)
        setDescription()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun setDescription() {
        binding?.tvTransparencyFeeInfo?.text = desc
    }

    companion object {

        private val TAG = TransparencyFeeInfoBottomSheet::class.java.simpleName

        private val TITLE_KEY = "transparencyInfoTitle"

        private val DESC_KEY = "transparencyInfoDesc"

        fun newInstance(title: String, desc: String): TransparencyFeeInfoBottomSheet {
            return TransparencyFeeInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(TITLE_KEY, title)
                    putString(DESC_KEY, desc)
                }
            }
        }
    }
}
