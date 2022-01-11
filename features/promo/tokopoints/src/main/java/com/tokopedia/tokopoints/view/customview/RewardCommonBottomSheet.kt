package com.tokopedia.tokopoints.view.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.remoteresourcerequest.view.ImageDensityType
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.BottomSheetModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class RewardCommonBottomSheet : BottomSheetUnify() {

    private var image: DeferredImageView? = null
    private var textTitle: Typography? = null
    private var textDesc: Typography? = null
    private var button: UnifyButton? = null

  companion object{
      fun newInstance(bundle: Bundle):RewardCommonBottomSheet{
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

    fun setBottomSheet(bottomSheetModel: BottomSheetModel){
        setTitle(bottomSheetModel?.bottomSheetTitle?:"")
        textTitle?.text = bottomSheetModel.contentTitle
        textDesc?.text = bottomSheetModel.contentDescription
        button?.text = bottomSheetModel.buttonText
//        image?.mRemoteFileName = "phone_verification.png"
//        image?.dpiSupportType = ImageDensityType.SUPPORT_SINGLE_DPI
        image?.loadRemoteImageDrawable("https://images.tokopedia.net/img/android/res/singleDpi/levelup_gold.png", ImageDensityType.SUPPORT_SINGLE_DPI)
      //  image?.dpiSupportType = ImageDensityType.SUPPORT_SINGLE_DPI
     //   image?.mCompleteUrl = "https://images.tokopedia.net/img/android/res/singleDpi/levelup_gold.png"
    }

}