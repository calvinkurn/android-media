package com.tokopedia.minicart

import android.content.Intent
import com.tokopedia.unifycomponents.BottomSheetUnify

class MiniCartBottomSheetUnify : BottomSheetUnify() {

    internal var listener: MiniCartBottomSheetUnifyListener? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        listener?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        listener = null
        super.onDestroy()
    }
}

interface MiniCartBottomSheetUnifyListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}
