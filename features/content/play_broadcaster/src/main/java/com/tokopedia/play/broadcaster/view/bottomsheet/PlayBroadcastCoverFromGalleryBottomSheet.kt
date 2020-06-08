package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by furqan on 08/06/2020
 */
class PlayBroadcastCoverFromGalleryBottomSheet : BottomSheetUnify() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isFullpage = false
        isDragable = true
        isHideable = true
        setChild(View.inflate(requireContext(), R.layout.bottom_sheet_play_cover_from_gallery, null))
    }

    private fun initView() {
        bottomSheetHeader.visibility = View.GONE
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)
    }

    private fun initChooseGallery() {

    }
}