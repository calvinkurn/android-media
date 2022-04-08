package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.tm_dash_intro_container.view.*

class TokomemberIntroBottomsheet: BottomSheetUnify() {

    private val childLayoutRes = R.layout.tm_dash_intro_bottomsheet
    private lateinit var imgBottomsheet: ImageUnify
    private lateinit var tvHeading: Typography
    private lateinit var tvDesc: Typography
    private lateinit var btnProceed: UnifyButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgBottomsheet = view.findViewById(R.id.img_bottom_sheet)
        tvHeading = view.findViewById(R.id.tv_heading)
        tvDesc = view.findViewById(R.id.tv_desc)
        btnProceed = view.findViewById(R.id.btn_proceed)

        tvHeading.text = arguments?.getString(ARG_BOTTOMSHEET_TITLE, "")
        tvDesc.text = arguments?.getString(ARG_BOTTOMSHEET_DESC, "")
        imgBottomsheet.loadImage(arguments?.getString(ARG_BOTTOMSHEET_IMG, ""))
        btnProceed.setOnClickListener {

        }
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight()).toDp()
    }

    companion object {

        const val TAG = "PayLaterTokopediaGopayBottomsheet"
        const val ARG_BOTTOMSHEET_TITLE = "arg_bottomsheet_title"
        const val ARG_BOTTOMSHEET_DESC = "arg_bottomsheet_desc"
        const val ARG_BOTTOMSHEET_IMG = "arg_bottomsheet_img"

        fun show(
            bundle: Bundle,
            childFragmentManager: FragmentManager
        ) {
            val tokomemberIntroBottomsheet = TokomemberIntroBottomsheet().apply {
                arguments = bundle
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }
    }

}