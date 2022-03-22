package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_common_info_bs.*

class InfoBottomSheet(
    private val bottomSheetType: Int, private val isWhiteListUser: Boolean,
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
            if (isWhiteListUser && bottomSheetType == TYPE_DASAR)
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
        if (isWhiteListUser && bottomSheetType == TYPE_DASAR) {
            image?.setImageUrl(TopAdsCommonConstant.TOPADS_CREATE_TIPS2)
            return
        }
        context?.let {
            if (bottomSheetType == TYPE_DASAR) {
                infoDesc?.text = getString(R.string.topads_create_bs_desc2)
                image?.setImageUrl(TopAdsCommonConstant.TOPADS_CREATE_TIPS2)
            } else {
                infoDesc?.text = getString(R.string.topads_create_bs_desc1)
                image?.setImageUrl(TopAdsCommonConstant.TOPADS_CREATE_TIPS1)
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