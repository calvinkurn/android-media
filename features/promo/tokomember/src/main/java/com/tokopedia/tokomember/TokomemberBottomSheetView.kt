package com.tokopedia.tokomember

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokomember.TokomemberActivity.Companion.KEY_MEMBERSHIP
import com.tokopedia.tokomember.di.DaggerTokomemberComponent
import com.tokopedia.tokomember.model.BottomSheetContentItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography

class TokomemberBottomSheetView : BottomSheetUnify() {

    private var onFinishedListener: TokomemberBottomSheetView.OnFinishedListener? = null
    private var textTitle: Typography? = null
    private var textDesc: Typography? = null
    private var button: UnifyButton? = null
    private var bottomSheetData: BottomSheetContentItem? = null

    companion object {
        fun newInstance(bundle: Bundle): TokomemberBottomSheetView {
            val rbs = TokomemberBottomSheetView()
            rbs.arguments = bundle
            return rbs
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun init() {
        val view = View.inflate(context, R.layout.activity_tokomember, null)
        textTitle = view.findViewById(R.id.tvTitle)
        textDesc = view.findViewById(R.id.tvSubtitle)
        button = view?.findViewById(R.id.tokoButton)
        setUpBottomSheet()
        setBottomSheet()
        setChild(view)

        DaggerTokomemberComponent.builder()
            .build().inject(this)
    }

    private fun setUpBottomSheet() {
        this.apply {
            isDragable = true
            isHideable = true
            showKnob = false
            showCloseIcon = true
            clearContentPadding = true
            bottomSheet.isGestureInsetBottomIgnored = true
            customPeekHeight = (Resources.getSystem().displayMetrics.heightPixels / 2).toDp()

            setCloseClickListener {
                dismiss()
                onFinishedListener?.onFinish()
            }
        }
    }

    private fun setBottomSheet() {
        bottomSheetData = arguments?.getParcelable(KEY_MEMBERSHIP)

        textTitle?.text = bottomSheetData?.title
        textDesc?.text = bottomSheetData?.description
        button?.text = bottomSheetData?.cta?.text

        button?.setOnClickListener {
            RouteManager.route(context, bottomSheetData?.cta?.appLink)
        }
    }

    fun setOnFinishedListener(onFinishedListener: OnFinishedListener) {
        this.onFinishedListener = onFinishedListener
    }

    interface OnFinishedListener {
        fun onFinish()
    }
}