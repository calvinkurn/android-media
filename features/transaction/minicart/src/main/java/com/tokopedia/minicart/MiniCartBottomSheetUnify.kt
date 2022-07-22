package com.tokopedia.minicart

import android.content.DialogInterface
import android.content.Intent
import com.tokopedia.unifycomponents.BottomSheetUnify

class MiniCartBottomSheetUnify(private var listener: MiniCartBottomSheetUnifyListener?): BottomSheetUnify() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        listener?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onDismiss()
    }

    override fun onDestroy() {
        listener = null
        super.onDestroy()
    }
}

interface MiniCartBottomSheetUnifyListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun onDismiss()
}