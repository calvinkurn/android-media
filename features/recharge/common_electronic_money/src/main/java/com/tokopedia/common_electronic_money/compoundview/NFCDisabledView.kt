package com.tokopedia.common_electronic_money.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.common_electronic_money.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import org.jetbrains.annotations.NotNull

/**
 * Created by Rizky on 16/05/18.
 */
class NFCDisabledView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var buttonActivateNFC: UnifyButton

    init {
        inflateNFCDisableView()
    }

    fun inflateNFCDisableView(){
        val view = View.inflate(context, R.layout.view_nfc_emoney_disabled, this)
        buttonActivateNFC = view.findViewById<UnifyButton>(R.id.button_activate_nfc)
        val imageInactiveNFC = view.findViewById<ImageView>(R.id.img_inactive_nfc)
        imageInactiveNFC.loadImage(resources.getString(R.string.emoney_nfc_inactive_link)){
            setPlaceHolder(R.drawable.emoney_ic_nfc_inactive_placeholder)
        }
    }
}