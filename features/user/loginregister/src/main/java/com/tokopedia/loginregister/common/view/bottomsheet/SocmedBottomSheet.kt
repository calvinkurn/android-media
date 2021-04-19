package com.tokopedia.loginregister.common.view.bottomsheet

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.loginregister.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by Yoris Prayogo on 15/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SocmedBottomSheet(mContext: Context?): BottomSheetUnify() {

    private var socmedButtonsContainer: LinearLayout? = null

    init {
        createBottomSheet(mContext)
    }

    private fun createBottomSheet(context: Context?): SocmedBottomSheet? {
        val viewBottomSheetDialog = View.inflate(context, R.layout.layout_socmed_bottomsheet, null)
        socmedButtonsContainer = viewBottomSheetDialog.findViewById(R.id.socmed_container)

        setTitle(context?.getString(R.string.choose_social_media) ?: "")
        setChild(viewBottomSheetDialog)
        return this
    }

    fun getSocmedButtonContainer(): LinearLayout? = socmedButtonsContainer
}