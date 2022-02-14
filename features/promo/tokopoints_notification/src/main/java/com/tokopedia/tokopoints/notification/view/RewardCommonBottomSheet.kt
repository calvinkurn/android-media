package com.tokopedia.tokopoints.notification.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tokopoints.notification.R
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.tokopoints.notification.model.BottomSheetModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class RewardCommonBottomSheet : BottomSheetUnify() {

    private var image: ImageUnify? = null
    private var textTitle: Typography? = null
    private var textDesc: Typography? = null
    private var button: UnifyButton? = null

  companion object{
      fun newInstance(bundle: Bundle): RewardCommonBottomSheet {
          val rbs = RewardCommonBottomSheet()
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
        val view = View.inflate(context, R.layout.tp_membership_bs, null)
        image = view?.findViewById(R.id.iv_bs_membership)
        textTitle = view?.findViewById(R.id.tv_title_membership)
        textDesc = view?.findViewById(R.id.tv_desc_membership)
        button = view?.findViewById(R.id.button_membership)
        setChild(view)
        setBottomSheet(arguments?.getParcelable<BottomSheetModel>("bsm") as BottomSheetModel)
    }

    private fun setBottomSheet(bottomSheetModel: BottomSheetModel){
        setTitle(bottomSheetModel.bottomSheetTitle?:"")
        textTitle?.text = bottomSheetModel.contentTitle
        textDesc?.text = bottomSheetModel.contentDescription
        button?.text = bottomSheetModel.buttonText
        image?.loadImage(bottomSheetModel.imageUrl?:"")
    }
}