package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.constant.TopAdsRemoteImageUrl
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class InfoBottomSheet(
    private val bottomSheetType: Int
) : BottomSheetUnify() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(
            context,
            if (bottomSheetType == TYPE_DASAR)
                R.layout.layout_topads_edit_split_bid_info_bs
            else
                R.layout.topads_common_info_bs,
            null
        )
        setChild(contentView)
        showKnob = true
        isDragable = true
        isHideable = true
        showCloseIcon = false

        if (bottomSheetType == TYPE_DASAR) {
            setTitle(getString(R.string.topads_create_bs_title2))
        } else
            setTitle(getString(R.string.topads_create_bs_title1))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        context?.let {
            if (bottomSheetType == TYPE_DASAR) {
                view?.findViewById<Typography>(R.id.infoDesc)?.text =
                    getString(R.string.topads_create_bs_desc2)
                view?.findViewById<ImageUnify>(R.id.image)
                    ?.setImageUrl(TopAdsRemoteImageUrl.CREATE_TIPS2)
            } else {
                view?.findViewById<Typography>(R.id.infoDesc)?.text =
                    getString(R.string.topads_create_bs_desc1)
                view?.findViewById<ImageUnify>(R.id.image)
                    ?.setImageUrl(TopAdsRemoteImageUrl.CREATE_TIPS1)
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "ACTION_FILTER_BOTTOM_SHEET_TAG"
        const val TYPE_DASAR = 0
        const val TYPE_KATA_KUNCI = 1
    }
}