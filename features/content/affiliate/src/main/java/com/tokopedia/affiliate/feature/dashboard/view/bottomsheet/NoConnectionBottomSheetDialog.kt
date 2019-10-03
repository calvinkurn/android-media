package com.tokopedia.affiliate.feature.dashboard.view.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.tokopedia.affiliate.R
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.unifycomponents.EmptyState

/**
 * Created by jegul on 2019-09-23.
 */
class NoConnectionBottomSheetDialog {

    private lateinit var dialog: CloseableBottomSheetDialog
    private lateinit var dialogView: View
    private lateinit var esNoConnection: EmptyState

    companion object {
        fun createInstance(context: Context?): NoConnectionBottomSheetDialog {
            val noConnectionBottomSheetDialog = NoConnectionBottomSheetDialog()
            noConnectionBottomSheetDialog.dialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_no_connection, null)
            noConnectionBottomSheetDialog.dialog = CloseableBottomSheetDialog.createInstanceRounded(context)
            noConnectionBottomSheetDialog.apply {
                esNoConnection = dialogView.findViewById(R.id.es_no_connection)
                dialogView.findViewById<ImageView>(R.id.iv_close).setOnClickListener { dialog.dismiss() }
                dialog.setCustomContentView(dialogView, "", false)
                dialog.setCancelable(false)
            }
            return noConnectionBottomSheetDialog
        }
    }

    fun showWithListener(onTryAgainClicked: () -> Unit) {
        esNoConnection.setPrimaryCTAClickListener {
            onTryAgainClicked()
            dialog.dismiss()
        }
        dialog.show()
    }
}