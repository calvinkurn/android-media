package com.tokopedia.entertainment.pdp.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.data.PackageItem
import kotlinx.android.synthetic.main.ent_ticket_adapter_item.view.*
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getDigit
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.checkDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getDate
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener

class EventPDPTicketItemPackageAdapter(
        val onBindItemTicketListener: OnBindItemTicketListener
) : RecyclerView.Adapter<EventPDPTicketItemPackageAdapter.EventPDPTicketItemPackageViewHolder>(){

    private var listItemPackage = emptyList<PackageItem>()
    private var isError = false
    private var idPackage = ""

    inner class EventPDPTicketItemPackageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(items: PackageItem) {
            with(itemView) {

                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(rootView.windowToken, 0)
                    }
                }

                txtTitle_ticket.text = items.name
                txtSubtitle_ticket.text = items.description
                txtPrice_ticket.text = getRupiahFormat(items.salesPrice.toInt())

                txtPilih_ticket.visibility = if(checkDate(items.dates,onBindItemTicketListener.getSelectedDate())) View.VISIBLE else View.GONE
                txtHabis_ticket.visibility = if(!checkDate(items.dates,onBindItemTicketListener.getSelectedDate())) View.VISIBLE else View.GONE

                quantityEditor.minValue = items.minQty.toInt()
                quantityEditor.maxValue = items.maxQty.toInt()

                quantityEditor.setValueChangedListener { newValue, _, _ ->
                    isError = !(quantityEditor.getValue() >= items.minQty.toInt() && quantityEditor.getValue() <= items.maxQty.toInt())
                    onBindItemTicketListener.quantityEditorValueButtonClicked(idPackage,items.id, items,
                            items.salesPrice.toInt()*newValue, newValue.toString(),
                            isError, items.name, items.productId,items.salesPrice,
                            getDate(items.dates, onBindItemTicketListener.getSelectedDate()))
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
                            if (getDigit(txtTotal.toString()) > EMPTY_QTY &&
                                    getDigit(txtTotal.toString()) >= items.minQty.toInt() &&
                                    getDigit(txtTotal.toString()) <= items.maxQty.toInt()) {
                                quantityEditor.editText.error = null
                                isError = false
                            }
                        } else if (txtTotal.toString().isBlank()) {
                            isError = true
                        }

                        onBindItemTicketListener.quantityEditorValueButtonClicked(idPackage,items.id,items,items.salesPrice.toInt()*getDigit(txtTotal.toString()),
                                getDigit(txtTotal.toString()).toString(), isError, items.name, items.productId,items.salesPrice,
                                getDate(items.dates, onBindItemTicketListener.getSelectedDate()))
                    }
                })

                txtPilih_ticket.setOnClickListener {
                    itemView.txtPilih_ticket.visibility = View.GONE
                    itemView.greenDivider.visibility = View.VISIBLE
                    itemView.bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_active_bg)
                    itemView.quantityEditor.visibility = View.VISIBLE
                    itemView.quantityEditor.setValue(MIN_QTY)
                    itemView.quantityEditor.editText.visibility = View.VISIBLE
                    itemView.quantityEditor.addButton.visibility = View.VISIBLE
                    itemView.quantityEditor.subtractButton.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int = listItemPackage.size
    override fun onBindViewHolder(holder: EventPDPTicketItemPackageViewHolder, position: Int) {
        holder.bind(listItemPackage[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPTicketItemPackageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ent_ticket_adapter_item, parent, false)
        return EventPDPTicketItemPackageViewHolder(itemView)
    }

    fun setList(list: List<PackageItem>, idPackage : String) {
        listItemPackage = list
        this.idPackage = idPackage
        notifyDataSetChanged()
    }

    companion object {
        const val EMPTY_QTY = 0
        const val MIN_QTY = 1
    }
}