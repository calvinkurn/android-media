package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.tokopedianow.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

class NoAddressEmptyStateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    private var changeAddressButton: UnifyButton? = null
    private var returnButton: UnifyButton? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_tokopedianow_empty_state_no_address, this, true)
        changeAddressButton = findViewById(R.id.tokonowEmptyStateButtonChangeAddress)
        returnButton = findViewById(R.id.tokonowEmptyStateButtonReturn)
        initRemoteView()
    }

    var actionListener: ActionListener? = null
        set(value) {
            field = value
            field?.let { listener ->
                changeAddressButton?.setOnClickListener { listener.onChangeAddressClicked() }
                returnButton?.setOnClickListener { listener.onReturnClick() }
            }
        }

    private fun initRemoteView() {
        val imgNoAddress = findViewById<ImageUnify>(R.id.tokonowEmptyStateIcon)
        imgNoAddress.setImageUrl(IMG_NO_ADDRESS)
    }

    interface ActionListener {
        fun onChangeAddressClicked()
        fun onReturnClick()
    }

    companion object {
        private const val IMG_NO_ADDRESS = "https://images.tokopedia.net/img/tokonow/tokonow_ic_empty_state_no_address_small.png"
    }
}