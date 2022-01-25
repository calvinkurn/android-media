package com.tokopedia.tokomember

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.tokomember.di.DaggerTokomemberComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography

class TokomemberBottomSheetView : BottomSheetUnify() {

    private var textTitle: Typography? = null
    private var textDesc: Typography? = null
    private var button: UnifyButton? = null

    companion object{
        fun newInstance(bundle: Bundle):TokomemberBottomSheetView{
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


    private fun init(){
        val view = View.inflate(context, R.layout.activity_tokomember, null)
        textTitle = view.findViewById(R.id.tvTitle)
        textDesc = view.findViewById(R.id.tvSubtitle)
        button = view?.findViewById(R.id.tokoButton)
        setUpBottomSheet()
        setChild(view)

        DaggerTokomemberComponent.builder()
            .build().inject(this)
    }

    private fun setUpBottomSheet(){
        this.apply {
            isDragable = true
            isHideable = true
            showKnob = true
            showCloseIcon = false
            bottomSheet.isGestureInsetBottomIgnored = true
            customPeekHeight = (Resources.getSystem().displayMetrics.heightPixels / 2).toDp()
        }
    }

    private fun handleError(error: Throwable?) {

    }

    private fun handleLoading(b: Boolean) {

    }

    private fun handleSuccess(data: Any?) {

    }

    private fun setBottomSheet(){
  /*      setTitle(bottomSheetModel.bottomSheetTitle?:"")
        textTitle?.text = bottomSheetModel.contentTitle
        textDesc?.text = bottomSheetModel.contentDescription
        button?.text = bottomSheetModel.buttonText*/
    }
}