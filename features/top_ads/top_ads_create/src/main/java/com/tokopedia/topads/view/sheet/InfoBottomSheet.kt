package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.common.constant.TopAdsRemoteImageUrl
import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.topads.common.R as topadscommonR

const val TYPE_DASAR = 0
class InfoBottomSheet : BottomSheetUnify() {

    private var infoDesc: Typography ? = null
    private var image: ImageUnify ? = null
    private var bottomSheetType: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, com.tokopedia.topads.common.R.layout.topads_common_info_bs, null)
        setChild(contentView)
        infoDesc = contentView.findViewById(topadscommonR.id.infoDesc)
        image = contentView.findViewById(R.id.image)

        showKnob = true
        isDragable = true
        isHideable = true
        showCloseIcon = false
        if (bottomSheetType == TYPE_DASAR) {
            setTitle(getString(R.string.topads_create_bs_title2))
        } else {
            setTitle(getString(R.string.topads_create_bs_title1))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        context?.let {
            if (bottomSheetType == TYPE_DASAR) {
                infoDesc?.text = getString(R.string.topads_create_bs_desc2)
                image?.setImageUrl(TopAdsRemoteImageUrl.CREATE_TIPS2)
            } else {
                infoDesc?.text = getString(R.string.topads_create_bs_desc1)
                image?.setImageUrl(TopAdsRemoteImageUrl.CREATE_TIPS1)
            }
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        type: Int
    ) {
        this.bottomSheetType = type
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "ACTION_FILTER_BOTTOM_SHEET_TAG"
        fun newInstance(): InfoBottomSheet = InfoBottomSheet()
    }
}
