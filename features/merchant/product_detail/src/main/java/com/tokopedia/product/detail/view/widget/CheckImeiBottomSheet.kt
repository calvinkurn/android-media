package com.tokopedia.product.detail.view.widget

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tokopedia.product.detail.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.dialog_check_imei.view.*

/**
 * Created by Yoris Prayogo on 2020-03-17.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class CheckImeiBottomSheet(val mActivity: FragmentActivity?, val onUnderstandClicked: () -> Unit) : BottomSheetUnify() {

    companion object {
        const val TAG = "ImeiCheck"
        fun showPermissionDialog(context: FragmentActivity, onUnderstandClicked: () -> Unit) {
            CheckImeiBottomSheet(context, onUnderstandClicked).showDialog()
        }
    }

    init {
        val contentView = View.inflate(mActivity, R.layout.dialog_check_imei, null)
        setChild(contentView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.dialog_check_imei_btn.setOnClickListener {
            onUnderstandClicked()
            dismiss()
        }
    }


    fun showDialog() {
        mActivity?.supportFragmentManager?.run {
            show(this, TAG)
        }
    }
}