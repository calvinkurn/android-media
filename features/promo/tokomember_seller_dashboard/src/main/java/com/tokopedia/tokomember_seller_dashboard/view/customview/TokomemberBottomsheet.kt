package com.tokopedia.tokomember_seller_dashboard.view.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography

class TokomemberBottomsheet(): BottomSheetUnify() {

    private val childLayoutRes = R.layout.tm_dash_intro_bottomsheet
    private lateinit var imgBottomsheet: ImageUnify
    private lateinit var tvHeading: Typography
    private lateinit var tvDesc: Typography
    private lateinit var btnProceed: UnifyButton
    private var mBottomSheetClickListener:BottomSheetClickListener?=null

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

        val tmIntroBottomsheetModel = Gson().fromJson(arguments?.getString(ARG_BOTTOMSHEET, ""), TmIntroBottomsheetModel::class.java)

        tvHeading.text = tmIntroBottomsheetModel.title
        tvDesc.text = tmIntroBottomsheetModel.desc
        imgBottomsheet.loadImage(tmIntroBottomsheetModel.image)
        btnProceed.text = tmIntroBottomsheetModel.ctaName
        btnProceed.setOnClickListener {
            mBottomSheetClickListener?.onButtonClick(tmIntroBottomsheetModel.errorCount)
        }
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight()).toDp()
    }

    fun setUpBottomSheetListener(bottomSheetClickListener:BottomSheetClickListener){
        mBottomSheetClickListener = bottomSheetClickListener
    }

    companion object {

        const val TAG = "PayLaterTokopediaGopayBottomsheet"
        const val ARG_BOTTOMSHEET = "arg_bottomsheet"

        fun show(
            bundle: Bundle,
            childFragmentManager: FragmentManager
        ) {
            val tokomemberIntroBottomsheet = TokomemberBottomsheet().apply {
                arguments = bundle
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }
    }

}

interface BottomSheetClickListener{
    fun onButtonClick(errorCount:Int)
}