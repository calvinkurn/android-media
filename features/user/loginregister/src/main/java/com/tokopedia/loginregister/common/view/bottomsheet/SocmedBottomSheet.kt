package com.tokopedia.loginregister.common.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.LayoutSocmedBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by Yoris Prayogo on 15/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SocmedBottomSheet: BottomSheetUnify() {

    private var socmedButtonsContainer: LinearLayout? = null
    private var viewBinding by autoClearedNullable<LayoutSocmedBottomsheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet() {
        viewBinding = LayoutSocmedBottomsheetBinding.inflate(LayoutInflater.from(context))
        setTitle(context?.getString(R.string.choose_social_media) ?: "")
        setChild(viewBinding?.root)
    }

    fun getSocmedButtonContainer(): LinearLayout? = socmedButtonsContainer
}