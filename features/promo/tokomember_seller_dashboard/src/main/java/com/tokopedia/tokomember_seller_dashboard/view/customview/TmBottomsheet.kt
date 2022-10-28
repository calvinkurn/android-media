package com.tokopedia.tokomember_seller_dashboard.view.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class TokomemberBottomsheet : BottomSheetUnify() {

    private var secondaryCta: (() -> Unit)? = null
    private val childLayoutRes = R.layout.tm_dash_intro_bottomsheet
    private var mBottomSheetClickListener: BottomSheetClickListener? = null
    private lateinit var imgBottomsheet: ImageUnify
    private lateinit var tvHeading: Typography
    private lateinit var tvDesc: Typography
    private lateinit var btnProceed: UnifyButton
    private lateinit var btnSecondary: UnifyButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {

            initBottomSheet()
            return super.onCreateView(inflater, container, savedInstanceState)
        }
        catch (e: Exception){
            return null
        }
    }

    private fun initBottomSheet() {
        val view = View.inflate(context,childLayoutRes,null)
        imgBottomsheet = view.findViewById(R.id.img_bottom_sheet)
        tvHeading = view.findViewById(R.id.tv_heading)
        tvDesc = view.findViewById(R.id.tv_desc)
        btnProceed = view.findViewById(R.id.btn_proceed)
        btnSecondary = view.findViewById(R.id.btn_secondary)
        setDefaultParams()
        setChild(view)
        setData()
    }

   private fun setData() {
       val tmIntroBottomSheetModel = Gson().fromJson(
           arguments?.getString(ARG_BOTTOMSHEET, ""),
           TmIntroBottomsheetModel::class.java
       )
       if (tmIntroBottomSheetModel.image.isEmpty()) {
           imgBottomsheet.setImageResource(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500)
       } else {
           imgBottomsheet.loadImage(tmIntroBottomSheetModel.image)
       }
       if(tmIntroBottomSheetModel.showSecondaryCta) {
           btnSecondary.visibility = View.VISIBLE
           imgBottomsheet.setImageResource(com.tokopedia.globalerror.R.drawable.unify_globalerrors_404)
       }
       else{
           btnSecondary.visibility = View.GONE
       }
       btnSecondary.setOnClickListener {
           if(secondaryCta != null){
               secondaryCta?.invoke()
           }
           else {
               activity?.finish()
           }
       }
       tvHeading.text = tmIntroBottomSheetModel.title
       tvDesc.text = tmIntroBottomSheetModel.desc
       btnProceed.text = tmIntroBottomSheetModel.ctaName
       btnProceed.setOnClickListener {
           dismiss()
           mBottomSheetClickListener?.onButtonClick(tmIntroBottomSheetModel.errorCount)
       }
    }

    fun setSecondaryCta(action:() -> Unit){
        secondaryCta = action
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight()).toDp()
    }

    companion object {

        const val ARG_BOTTOMSHEET = "arg_bottomsheet"

        fun createInstance(bundle: Bundle): TokomemberBottomsheet {
            return TokomemberBottomsheet().apply {
                arguments = bundle
            }
        }
    }

    fun setUpBottomSheetListener(bottomSheetClickListener: BottomSheetClickListener) {
        mBottomSheetClickListener = bottomSheetClickListener
    }
}

interface BottomSheetClickListener {
    fun onButtonClick(errorCount: Int)
}