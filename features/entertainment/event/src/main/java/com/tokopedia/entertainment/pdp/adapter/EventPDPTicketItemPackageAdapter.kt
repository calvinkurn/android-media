package com.tokopedia.entertainment.pdp.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.data.PackageItem
import kotlinx.android.synthetic.main.ent_ticket_adapter_item.view.*
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getDigit
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.checkDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.checkNotEndSale
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.checkStartSale
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getDate
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.entertainment.pdp.listener.OnCoachmarkListener
import com.tokopedia.unifycomponents.toDp
import java.util.*

class EventPDPTicketItemPackageAdapter(
        val onBindItemTicketListener: OnBindItemTicketListener,
        private val onCoachmarkListener: OnCoachmarkListener
) : RecyclerView.Adapter<EventPDPTicketItemPackageAdapter.EventPDPTicketItemPackageViewHolder>(){

    private var listItemPackage = emptyList<PackageItem>()
    private var isError = false
    private var idPackage = ""
    private var packageName = ""
    private var heightItemView = 0
    lateinit var eventPDPTracking: EventPDPTracking



    inner class EventPDPTicketItemPackageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val linear = LinearLayout(itemView.context)
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

                val isSaleStarted = checkStartSale(items.startDate, Calendar.getInstance().time)
                val isNotEnded = checkNotEndSale(items.endDate, Calendar.getInstance().time)
                val itemIsAvailable = (checkDate(items.dates,onBindItemTicketListener.getSelectedDate()) && items.available.toInt()>=1)
                if(isSaleStarted && isNotEnded) {
                    txtPilih_ticket.visibility = if (itemIsAvailable) View.VISIBLE else View.GONE
                    txtHabis_ticket.visibility = if (!itemIsAvailable) View.VISIBLE else View.GONE
                } else if(!isSaleStarted && isNotEnded) {
                    txtPilih_ticket.visibility = View.GONE
                    txtHabis_ticket.visibility = View.GONE
                    txtAlreadyEnd.visibility = View.GONE
                    txtNotStarted.visibility = View.VISIBLE
                } else if(isSaleStarted && !isNotEnded){
                    txtPilih_ticket.visibility = View.GONE
                    txtHabis_ticket.visibility = View.GONE
                    txtAlreadyEnd.visibility = View.VISIBLE
                    txtNotStarted.visibility = View.GONE
                }

                itemView.post {
                    if(position == 0) heightItemView += itemView.height.toDp()
                    if(onCoachmarkListener.getLocalCache()) {
                        if (listItemPackage.size == 1 && position == 0) {
                            onCoachmarkListener.showCoachMark(itemView, 0)
                        } else if (listItemPackage.size >= 2 && position == 1) {
                            onCoachmarkListener.showCoachMark(itemView, heightItemView)
                        }
                    }
                }

                quantityEditor.setValue(items.minQty.toInt())
                quantityEditor.minValue = items.minQty.toInt() - 1
                quantityEditor.maxValue = items.maxQty.toInt()

                quantityEditor.setValueChangedListener { newValue, _, _ ->
                    isError = !(quantityEditor.getValue() >= items.minQty.toInt() && quantityEditor.getValue() <= items.maxQty.toInt())
                    onBindItemTicketListener.quantityEditorValueButtonClicked(idPackage,items.id, items,
                            items.salesPrice.toInt()*newValue, newValue.toString(),
                            isError, items.name, items.productId,items.salesPrice,
                            getDate(items.dates, onBindItemTicketListener.getSelectedDate()), packageName)
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
                                itemView.txtPilih_ticket.visibility = View.VISIBLE
                                itemView.greenDivider.visibility = View.GONE
                                itemView.quantityEditor.visibility = View.GONE
                                itemView.bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_normal_bg)
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

                            if(getDigit(txtTotal.toString())== EMPTY_QTY && items.minQty.toInt() == EMPTY_QTY){
                                itemView.txtPilih_ticket.visibility = View.VISIBLE
                                itemView.greenDivider.visibility = View.GONE
                                itemView.quantityEditor.visibility = View.GONE
                                itemView.bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_normal_bg)
                            }
                        } else if (txtTotal.toString().isBlank()) {
                            isError = true
                        }

                        onBindItemTicketListener.quantityEditorValueButtonClicked(idPackage,items.id,items,items.salesPrice.toInt()*getDigit(txtTotal.toString()),
                                getDigit(txtTotal.toString()).toString(), isError, items.name, items.productId,items.salesPrice,
                                getDate(items.dates, onBindItemTicketListener.getSelectedDate()), packageName)
                    }
                })

                txtPilih_ticket.setOnClickListener {
                    itemView.txtPilih_ticket.visibility = View.GONE
                    itemView.greenDivider.visibility = View.VISIBLE
                    itemView.bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_active_bg)
                    itemView.quantityEditor.visibility = View.VISIBLE
                    itemView.quantityEditor.setValue(if (items.minQty.toInt() < 1) MIN_QTY else items.minQty.toInt())
                    itemView.quantityEditor.editText.visibility = View.VISIBLE
                    itemView.quantityEditor.addButton.visibility = View.VISIBLE
                    itemView.quantityEditor.subtractButton.visibility = View.VISIBLE
                    eventPDPTracking.onClickPackage(items, itemView.quantityEditor.getValue())
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

    fun setList(list: List<PackageItem>, idPackage : String, packageName:String) {
        listItemPackage = list
        this.idPackage = idPackage
        this.packageName = packageName
        notifyDataSetChanged()
    }

    companion object {
        const val EMPTY_QTY = 0
        const val MIN_QTY = 1
    }
}