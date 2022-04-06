package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class TokomemberIntroBottomsheet: BottomSheetUnify() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_intro_bottomsheet, container, false)
    }

}