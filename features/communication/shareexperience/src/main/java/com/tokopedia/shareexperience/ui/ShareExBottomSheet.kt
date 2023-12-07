package com.tokopedia.shareexperience.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.shareexperience.databinding.ShareexperienceBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ShareExBottomSheet: BottomSheetUnify() {

    private var viewBinding by autoClearedNullable<ShareexperienceBottomSheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ShareexperienceBottomSheetBinding.inflate(inflater)
        setChild(viewBinding?.root)
        isFullpage = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
