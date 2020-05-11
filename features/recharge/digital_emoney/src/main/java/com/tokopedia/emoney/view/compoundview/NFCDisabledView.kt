package com.tokopedia.emoney.view.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.emoney.R
import org.jetbrains.annotations.NotNull

/**
 * Created by Rizky on 16/05/18.
 */
class NFCDisabledView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var listener: OnActivateNFCClickListener

    fun setListener(listener: OnActivateNFCClickListener) {
        this.listener = listener
    }

    init {
        val view = View.inflate(context, R.layout.view_nfc_emoney_disabled, this)
        val buttonActivateNFC = view.findViewById<Button>(R.id.button_activate_nfc)

        if (::listener.isInitialized) {
            buttonActivateNFC.setOnClickListener { v -> listener.onClick() }
        }
    }

    interface OnActivateNFCClickListener {
        fun onClick()
    }
}