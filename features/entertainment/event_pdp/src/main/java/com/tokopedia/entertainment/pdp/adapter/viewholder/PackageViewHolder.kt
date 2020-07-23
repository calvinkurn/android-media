package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.R
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.data.Package
import com.tokopedia.kotlin.extensions.view.isVisible
import kotlinx.android.synthetic.main.ent_ticket_adapter_item.view.*
import timber.log.Timber
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject


class PackageViewHolder(view: View): AbstractViewHolder<Package>(view) {

    lateinit var quantityEditorValueButtonClicked: (String,String, String, Int, Boolean, String, String) -> Unit
    lateinit var pilihbuttonClicked: (String) -> Unit
    private var isError = false

    lateinit var eventPDPTracking: EventPDPTracking

    private fun getDigit(str: String): Int{
        if(str.isNotBlank()){
            val pattern = Pattern.compile("[^0-9]")
            val matcher: Matcher = pattern.matcher(str)
            return matcher.replaceAll("").toInt()
        }
        else return -1
    }

    override fun bind(items: Package) {
        with(itemView){
            onFocusChangeListener = View.OnFocusChangeListener{ _, hasFocus ->
                if(!hasFocus){
                    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(rootView.windowToken,0)
                }
            }

            txtTitle_ticket.text = items.name
            txtSubtitle_ticket.text = items.description
            txtPrice_ticket.text = CurrencyFormatter.getRupiahFormat(items.salesPrice.toLong())

            val areItemAvailable = items.scheduleStatusBahasa == items.isEmpty || items.sold.toInt()>=items.available.toInt()

            txtPilih_ticket.visibility = if(areItemAvailable) View.GONE else View.VISIBLE
            txtHabis_ticket.visibility = if(areItemAvailable) View.VISIBLE else View.GONE


            quantityEditor.minValue = items.minQty.toInt()
            quantityEditor.maxValue = items.maxQty.toInt()
            quantityEditor.setValueChangedListener { newValue, _, _ ->
                if(::quantityEditorValueButtonClicked.isInitialized){
                    isError = !(quantityEditor.getValue() >= items.minQty.toInt() && quantityEditor.getValue() <= items.maxQty.toInt())

                    quantityEditorValueButtonClicked.invoke(items.id,items.productGroupId,items.salesPrice,newValue, isError, items.name,items.productId)
                }
                eventPDPTracking.onClickQuantity()
            }
            quantityEditor.editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(txtTotal: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(txtTotal: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (txtTotal.toString().isNotBlank()) {
                        if (getDigit(txtTotal.toString()) > items.maxQty.toInt()) {
                            quantityEditor.editText.error = String.format(resources.getString(R.string.ent_error_value_exceeded), items.maxQty)
                            isError = true
                        } else if (getDigit(txtTotal.toString()) < items.minQty.toInt()) {
                            quantityEditor.editText.error = String.format(resources.getString(R.string.ent_error_value_under_min), items.minQty)
                            isError = true
                        }
                        if (txtTotal.toString().length > 1) { // zero first checker example: 01 -> 1, 02 -> 2
                            if (txtTotal.toString().startsWith("0")) {
                                txtTotal.toString().replaceFirst("^0+(?!$)", "")
                                quantityEditor.setValue(txtTotal.toString().toInt())
                                isError = false
                            }
                        }
                        if(getDigit(txtTotal.toString()) > EMPTY_QTY &&
                                getDigit(txtTotal.toString()) >= items.minQty.toInt() &&
                                getDigit(txtTotal.toString()) <= items.maxQty.toInt()) {
                            quantityEditor.editText.setError(null)
                            isError = false
                        }
                    } else if(txtTotal.toString().isBlank()){ isError = true }

                    if (::quantityEditorValueButtonClicked.isInitialized) {
                        quantityEditorValueButtonClicked.invoke(items.id,items.productGroupId, items.salesPrice, getDigit(txtTotal.toString()), isError, items.name, items.productId)
                    }
                }
            })


            txtPilih_ticket.setOnClickListener { if(::pilihbuttonClicked.isInitialized)pilihbuttonClicked.invoke(items.id) }

            if(items.isClicked){
                itemView.txtPilih_ticket.visibility = View.GONE
                itemView.greenDivider.visibility = View.VISIBLE
                itemView.bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_active_bg)
                itemView.quantityEditor.visibility = View.VISIBLE
                itemView.quantityEditor.setValue(MIN_QTY)
                itemView.quantityEditor.editText.visibility = View.VISIBLE
                itemView.quantityEditor.addButton.visibility = View.VISIBLE
                itemView.quantityEditor.subtractButton.visibility = View.VISIBLE

                eventPDPTracking.onClickPackage(items, itemView.quantityEditor.getValue())
            } else {
                itemView.clearFocus()
                txtPilih_ticket.visibility = if(areItemAvailable) View.GONE else View.VISIBLE
                txtHabis_ticket.visibility = if(areItemAvailable) View.VISIBLE else View.GONE
                itemView.greenDivider.visibility = View.GONE
                itemView.bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_normal_bg)
                itemView.quantityEditor.clearFocus()
                itemView.quantityEditor.visibility = View.GONE
                itemView.quantityEditor.editText.visibility = View.GONE
                itemView.quantityEditor.addButton.visibility = View.GONE
                itemView.quantityEditor.subtractButton.visibility = View.GONE
            }

        }
    }

    companion object{
        val LAYOUT = R.layout.ent_ticket_adapter_item
        val EMPTY_QTY = 0
        val MIN_QTY = 1
    }
}