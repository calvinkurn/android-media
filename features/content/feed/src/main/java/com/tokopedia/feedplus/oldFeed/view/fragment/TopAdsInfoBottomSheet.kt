package com.tokopedia.feedplus.oldFeed.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.feedplus.R

/**
 * @author by astidhiyaa on 30/08/22
 */
class TopAdsInfoBottomSheet {
    private var dialog: BottomSheetDialog? = null
    private var closeButton: View? = null
    private var moreButton: View? = null

    fun setView(context: Context) {
        dialog?.setOnShowListener { dialogInterface: DialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val frameLayout = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            frameLayout?.let {
                val behavior =
                    BottomSheetBehavior.from(frameLayout)
                behavior.isHideable = false
            }
        }
        moreButton?.setOnClickListener {
            dismissDialog()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(ADS_URL)
            context.startActivity(intent)
        }
        closeButton?.setOnClickListener { dismissDialog() }
    }

    fun show() {
        dialog?.show()
    }

    private fun dismissDialog() {
        dialog?.dismiss()
    }

    companion object {
        private const val ADS_URL =
            "https://seller.tokopedia.com/edu/about-topads/iklan/?source=tooltip&medium=android"

        @SuppressLint("UnifyComponentUsage")
        fun newInstance(context: Context): TopAdsInfoBottomSheet {
            val frag = TopAdsInfoBottomSheet()
            frag.dialog = BottomSheetDialog(context)
            frag.dialog?.setContentView(R.layout.promoted_info_dialog)
            frag.closeButton = frag.dialog?.findViewById(R.id.close_but)
            frag.moreButton = frag.dialog?.findViewById(R.id.more)
            frag.setView(context)
            return frag
        }
    }
}
