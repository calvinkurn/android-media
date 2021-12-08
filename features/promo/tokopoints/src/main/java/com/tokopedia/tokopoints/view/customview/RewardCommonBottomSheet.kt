package com.tokopedia.tokopoints.view.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.BottomSheetModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class RewardCommonBottomSheet : BottomSheetUnify() {

    private var image: com.tkpd.remoteresourcerequest.view.DeferredImageView? = null
    private var textTitle: Typography? = null
    private var textDesc: Typography? = null
    private var button: UnifyButton? = null

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
    }

    fun setBottomSheet(bottomSheetModel: BottomSheetModel){
        textTitle?.text = bottomSheetModel.contentTitle
        textDesc?.text = bottomSheetModel.contentDescription
        button?.text = bottomSheetModel.buttonText
        image?.mRemoteFileName = bottomSheetModel.remoteImage
    }

}