package com.tokopedia.common_electronic_money.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common_electronic_money.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
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
        val buttonActivateNFC = view.findViewById<UnifyButton>(R.id.button_activate_nfc)

        if (::listener.isInitialized) {
            buttonActivateNFC.setOnClickListener { v -> listener.onClick() }
        }
    }

    interface OnActivateNFCClickListener {
        fun onClick()
    }
}