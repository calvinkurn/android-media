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
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.data.PackageItem
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getDigit
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.checkDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.checkNotEndSale
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.checkStartSale
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getDate
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.entertainment.pdp.listener.OnCoachmarkListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import kotlinx.android.synthetic.main.ent_ticket_adapter_item.view.*
import java.util.*

class EventPDPTicketItemPackageAdapter(
        val onBindItemTicketListener: OnBindItemTicketListener,
        private val onCoachmarkListener: OnCoachmarkListener
) : RecyclerView.Adapter<EventPDPTicketItemPackageAdapter.EventPDPTicketItemPackageViewHolder>() {

    private var listItemPackage = emptyList<PackageItem>()
    private var isError = false
    private var isRecommendationPackage = false
    private var idPackage = ""
    private var packageName = ""
    lateinit var eventPDPTracking: EventPDPTracking

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class EventPDPTicketItemPackageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var isListenerRegistered = false
        private var textWatcher: TextWatcher? = null
        fun bind(items: PackageItem) {
            if (isRecommendationPackage) {
                renderForRecommendationPackage(items)
            } else {
                renderForMainPackage(items)
            }
        }

        private fun renderForMainPackage(items: PackageItem) {
            with(itemView) {

                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(rootView.windowToken, 0)
                    }
                }

                txtTitle_ticket.text = items.name
                txtSubtitle_ticket.text = items.description
                txtSubtitle_ticket.visibility = if (items.description.isNotEmpty()) View.VISIBLE else View.GONE
                txtPrice_ticket.text = if(items.salesPrice.toIntSafely() != ZERO_PRICE) getRupiahFormat(items.salesPrice.toIntSafely())
                else resources.getString(R.string.ent_free_price)

                val isSaleStarted = checkStartSale(items.startDate, Calendar.getInstance().time)
                val isNotEnded = checkNotEndSale(items.endDate, Calendar.getInstance().time)
                val itemIsAvailable = (checkDate(items.dates, onBindItemTicketListener.getSelectedDate()) && items.available.toIntSafely() >= 1)
                val isRecomended = checkDate(items.dates, onBindItemTicketListener.getSelectedDate())
                if (isRecomended) {
                    if (isSaleStarted && isNotEnded) {
                        if (itemIsAvailable) {
                            txtPilih_ticket.show()
                        } else {
                            txtHabis_ticket.show()
                            showSoldOut(itemView)
                        }
                    } else if (!isSaleStarted && isNotEnded) {
                        txtNotStarted.show()
                    } else if (isSaleStarted && !isNotEnded) {
                        txtAlreadyEnd.show()
                    }
                } else {
                    txtisRecommeded.show()
                }

                if (!isListenerRegistered) {
                    quantityEditor.setValue(items.minQty.toIntSafely())
                    quantityEditor.minValue = items.minQty.toIntSafely() - 1
                    quantityEditor.maxValue = items.maxQty.toIntSafely()

                    quantityEditor.setValueChangedListener { newValue, _, _ ->
                        isError = !((quantityEditor.getValue() >= items.minQty.toIntSafely() || quantityEditor.getValue() >= EMPTY_QTY) && quantityEditor.getValue() <= items.maxQty.toIntSafely())
                        val total = if (newValue < items.minQty.toIntSafely()) EMPTY_QTY else newValue
                        onBindItemTicketListener.quantityEditorValueButtonClicked(idPackage, items.id, items,
                                items.salesPrice.toIntSafely() * total, total.toString(),
                                isError, items.name, items.productId, items.salesPrice,
                                getDate(items.dates, onBindItemTicketListener.getSelectedDate()), packageName)
                        eventPDPTracking.onClickQuantity()
                    }

                    textWatcher = object : TextWatcher {
                        override fun afterTextChanged(txtTotal: Editable?) {
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(txtTotal: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            if (txtTotal.toString().isNotBlank()) {
                                if (getDigit(txtTotal.toString()) > items.maxQty.toIntSafely()) {
                                    quantityEditor.editText.error = String.format(resources.getString(R.string.ent_error_value_exceeded), items.maxQty)
                                    isError = true
                                } else if (getDigit(txtTotal.toString()) < items.minQty.toIntSafely()) {
                                    itemView.txtPilih_ticket.visibility = View.VISIBLE
                                    itemView.greenDivider.visibility = View.GONE
                                    itemView.quantityEditor.visibility = View.GONE
                                    itemView.bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_normal_bg)
                                }
                                if (txtTotal.toString().length > 1) { // zero first checker example: 01 -> 1, 02 -> 2
                                    if (txtTotal.toString().startsWith("0")) {
                                        txtTotal.toString().replaceFirst("^0+(?!$)", "")
                                        quantityEditor.setValue(txtTotal.toString().toIntSafely())
                                        isError = false
                                    }
                                }
                                if (getDigit(txtTotal.toString()) > EMPTY_QTY &&
                                        getDigit(txtTotal.toString()) >= items.minQty.toIntSafely() &&
                                        getDigit(txtTotal.toString()) <= items.maxQty.toIntSafely()) {
                                    quantityEditor.editText.error = null
                                    isError = false
                                }

                                if (getDigit(txtTotal.toString()) == EMPTY_QTY && items.minQty.toIntSafely() == EMPTY_QTY) {
                                    itemView.txtPilih_ticket.visibility = View.VISIBLE
                                    itemView.greenDivider.visibility = View.GONE
                                    itemView.quantityEditor.visibility = View.GONE
                                    itemView.bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_normal_bg)
                                }
                            } else if (txtTotal.toString().isBlank()) {
                                isError = true
                            }

                            val total = if (getDigit(txtTotal.toString()) < items.minQty.toIntSafely()) EMPTY_QTY else getDigit(txtTotal.toString())
                            onBindItemTicketListener.quantityEditorValueButtonClicked(idPackage, items.id, items, items.salesPrice.toIntSafely() * total,
                                    total.toString(), isError, items.name, items.productId, items.salesPrice,
                                    getDate(items.dates, onBindItemTicketListener.getSelectedDate()), packageName)
                        }
                    }

                    quantityEditor.editText.addTextChangedListener(textWatcher)
                    isListenerRegistered = true
                }

                txtPilih_ticket.setOnClickListener {
                    itemView.txtPilih_ticket.visibility = View.GONE
                    itemView.greenDivider.visibility = View.VISIBLE
                    itemView.bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_active_bg)
                    itemView.quantityEditor.visibility = View.VISIBLE
                    itemView.quantityEditor.setValue(if (items.minQty.toIntSafely() < 1) MIN_QTY else items.minQty.toIntSafely())
                    itemView.quantityEditor.editText.visibility = View.VISIBLE
                    itemView.quantityEditor.addButton.visibility = View.VISIBLE
                    itemView.quantityEditor.subtractButton.visibility = View.VISIBLE
                    eventPDPTracking.onClickPackage(items, itemView.quantityEditor.getValue())
                }

                txtisRecommeded.setOnClickListener {
                    onBindItemTicketListener.clickRecommendation(items.dates)
                }
            }
        }

        private fun renderForRecommendationPackage(items: PackageItem) {
            with(itemView) {
                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(rootView.windowToken, 0)
                    }
                }

                txtTitle_ticket.text = items.name
                txtSubtitle_ticket.text = items.description
                txtSubtitle_ticket.visibility = if (items.description.isNotEmpty()) View.VISIBLE else View.GONE
                txtPrice_ticket.text = getRupiahFormat(items.salesPrice.toIntSafely())

                txtisRecommeded.show()
                txtisRecommeded.text = context.getString(R.string.ent_pdp_ticket_change_date)
                txtisRecommeded.setOnClickListener {
                    onBindItemTicketListener.clickRecommendation(items.dates)
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

    fun setList(list: List<PackageItem>, idPackage: String, packageName: String, isRecommendationPackage: Boolean) {
        listItemPackage = list
        this.idPackage = idPackage
        this.packageName = packageName
        this.isRecommendationPackage = isRecommendationPackage
        notifyDataSetChanged()
    }

    private fun showSoldOut(itemView: View) {
        with(itemView) {
            bgTicket.background = ContextCompat.getDrawable(context, R.drawable.ent_pdp_ticket_sold_out)
            txtTitle_ticket.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            txtTermurah_ticket.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            txtSubtitle_ticket.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            txtPrice_ticket.text = resources.getString(R.string.ent_pdp_ticket_sold_out)
            txtPrice_ticket.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        }
    }

    companion object {
        const val EMPTY_QTY = 0
        const val MIN_QTY = 1
        const val ZERO_PRICE = 0
    }
}
