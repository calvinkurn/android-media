package com.tokopedia.tokomember_seller_dashboard.view.customview

import android.os.Bundle
import android.view.View
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet

class TmShareBottomSheet:UniversalShareBottomSheet() {
    private var shareBottomSheetTitle = ""
    private var imgOptionsTitle = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(shareBottomSheetTitle.isNotEmpty()) setTitle(shareBottomSheetTitle)
        if(imgOptionsTitle.isNotEmpty()){
            val imgOptionsTitleTv:Typography = view.findViewById(com.tokopedia.universal_sharing.R.id.img_options_heading)
            imgOptionsTitleTv.apply {
                text = imgOptionsTitle
                val color = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                setTextColor(color)
            }
        }
    }

    fun setTmShareBottomSheetTitle(title:String?){
        shareBottomSheetTitle = title.orEmpty()
    }

    fun setTmShareBottomImgOptionsTitle(title:String?){
        imgOptionsTitle = title.orEmpty()
    }
}
