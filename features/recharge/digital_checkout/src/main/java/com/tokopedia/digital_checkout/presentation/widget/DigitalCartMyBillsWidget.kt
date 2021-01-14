package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.digital_checkout.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.item_digital_checkout_my_bills_section.view.*
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 14/01/21
 */

class DigitalCartMyBillsWidget @JvmOverloads constructor(@NotNull context: Context,
                                                         attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_digital_checkout_my_bills_section, this, true)
    }

    var actionListener: ActionListener? = null
        set(value) {
            field = value
            field?.let { listener ->
                icCheckoutMyBillsInfo.setOnClickListener { listener.onMoreInfoClicked() }
                checkBoxCheckoutMyBills.setOnCheckedChangeListener() { _, isChecked ->
                    listener.onCheckChanged(isChecked)
                }
            }
        }

    fun setTitle(title: String) {
        tvCheckoutMyBillsHeaderTitle.text = title
    }

    fun setDescription(description: String) {
        tvCheckoutMyBillsDescription.text = description
    }

    fun setChecked(isChecked: Boolean) {
        checkBoxCheckoutMyBills.isChecked = isChecked
    }

    fun isChecked(): Boolean = checkBoxCheckoutMyBills.isChecked

    fun disableCheckBox() {
        checkBoxCheckoutMyBills.visibility = View.GONE
    }

    fun hasMoreInfo(state: Boolean) {
        icCheckoutMyBillsInfo.visibility = if (state) View.VISIBLE else View.GONE
    }

    interface ActionListener {
        fun onMoreInfoClicked()
        fun onCheckChanged(isChecked: Boolean)
    }

}