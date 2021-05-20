package com.tokopedia.tokomart.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.tokopedia.tokomart.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton

class NoAddressEmptyStateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    private var changeAddressButton: UnifyButton? = null
    private var returnButton: UnifyButton? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_tokomart_empty_state_no_address, this, true)
        changeAddressButton = findViewById<UnifyButton>(R.id.tokonowEmptyStateButton1)
        returnButton = findViewById<UnifyButton>(R.id.tokonowEmptyStateButton2)

        // Remove this later
        setDescriptionCityName("Jakarta Utara")
    }

    var actionListener: ActionListener? = null
        set(value) {
            field = value
            field?.let { listener ->
                changeAddressButton?.setOnClickListener { listener.onChangeAddressClicked() }
                returnButton?.setOnClickListener { listener.onReturnClick() }
            }
        }

    fun setDescriptionCityName(city: String) {
        val tvDescription = findViewById<TextView?>(R.id.tokonowEmptyStateDesc2)
        tvDescription?.text = context.getString(R.string.tokomart_search_category_empty_state_desc_2, city)
    }

    interface ActionListener {
        fun onChangeAddressClicked()
        fun onReturnClick()
    }
}