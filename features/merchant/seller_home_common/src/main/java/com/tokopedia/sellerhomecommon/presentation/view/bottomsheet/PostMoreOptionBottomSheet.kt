package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetPostMoreOptionBinding

/**
 * Created by @ilhamsuaib on 23/08/22.
 */

class PostMoreOptionBottomSheet : BaseBottomSheet<ShcBottomSheetPostMoreOptionBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShcBottomSheetPostMoreOptionBinding.inflate(inflater, container, false).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() = binding?.run {

    }
}