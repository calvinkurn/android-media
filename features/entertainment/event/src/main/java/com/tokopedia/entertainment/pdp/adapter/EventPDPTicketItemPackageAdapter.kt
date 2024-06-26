package com.tokopedia.entertainment.pdp.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntTicketAdapterItemBinding
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.common.util.EventConst
import com.tokopedia.entertainment.pdp.data.AvailabilityStatus
import com.tokopedia.entertainment.pdp.data.PackageItem
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getDigit
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.checkDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getDate
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class EventPDPTicketItemPackageAdapter(
    val onBindItemTicketListener: OnBindItemTicketListener
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

    inner class EventPDPTicketItemPackageViewHolder(val binding: EntTicketAdapterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isListenerRegistered = false
        private var textWatcher: TextWatcher? = null
        fun bind(items: PackageItem) {
            renderPackage(items)
        }

        private fun renderTicketStatus(
            items: PackageItem
        ) {
            binding.run {
                when (items.availabilityStatus.code) {
                    EventConst.EVENT_TICKET_STATUS_AVAILABLE -> {
                        val isAvailableOnSelectedDate = checkDate(items.dates, onBindItemTicketListener.getSelectedDate())
                        if (isRecommendationPackage) {
                            renderForRecommendationPackage(items)
                        } else if (isAvailableOnSelectedDate) {
                            renderGeneralPackage(items)
                        } else {
                            renderGeneralRecommendationPackage(items)
                        }
                        txtPriceTicket.show()
                        adjustContainerBottomMargin(true)
                        hideTicketStatus()
                    }
                    EventConst.EVENT_TICKET_STATUS_FULL -> {
                        showTicketStatus(items.availabilityStatus)
                        showTicketStatusRedBackground()
                        showSoldOut()
                        adjustContainerBottomMargin(false)
                    }
                    EventConst.EVENT_TICKET_STATUS_NOT_STARTED -> {
                        showTicketStatus(items.availabilityStatus)
                        showTicketStatusRedBackground()
                        showSoldOut()
                        adjustContainerBottomMargin(false)
                    }
                    else -> {
                        showTicketStatus(items.availabilityStatus)
                        showTicketStatusSoldOutBackground()
                        showSoldOut()
                        adjustContainerBottomMargin(false)
                    }
                }
            }
        }

        private fun showTicketStatusRedBackground() {
            binding.run {
                with(txtStatusTicket) {
                    setTextColor(getColorResource(unifyprinciplesR.color.Unify_RN500))
                    setBackgroundColor(getColorResource(unifyprinciplesR.color.Unify_RN50))
                }
                with(txtStatusDescTicket) {
                    setTextColor(getColorResource(unifyprinciplesR.color.Unify_NN950))
                    setBackgroundColor(getColorResource(unifyprinciplesR.color.Unify_RN50))
                }
            }
        }

        private fun showTicketStatusSoldOutBackground() {
            binding.run {
                with(txtStatusTicket) {
                    setTextColor(getColorResource(unifyprinciplesR.color.Unify_NN600))
                    setBackgroundColor(getColorResource(unifyprinciplesR.color.Unify_NN50))
                }
                with(txtStatusDescTicket) {
                    setTextColor(getColorResource(unifyprinciplesR.color.Unify_NN950))
                    setBackgroundColor(getColorResource(unifyprinciplesR.color.Unify_NN50))
                }
            }
        }

        private fun getColorResource(unifyResId: Int): Int {
            return MethodChecker.getColor(itemView.context, unifyResId)
        }

        private fun hideTicketStatus() {
            binding.run {
                txtStatusTicket.hide()
                txtStatusDescTicket.hide()
            }
        }

        private fun showTicketStatus(availabilityStatus: AvailabilityStatus) {
            binding.run {
                if (availabilityStatus.name.isNotEmpty()) {
                    txtStatusTicket.text = availabilityStatus.name
                    if (availabilityStatus.desc.isNotEmpty()) {
                        txtStatusDescTicket.text = " · ${availabilityStatus.desc}"
                    } else {
                        txtStatusDescTicket.text = ""
                    }
                    txtStatusTicket.show()
                    txtStatusDescTicket.show()
                } else {
                    hideTicketStatus()
                }
            }
        }

        private fun adjustContainerBottomMargin(shouldAddMargin: Boolean) {
            val marginValue = if (shouldAddMargin) {
                16.dpToPx(itemView.resources.displayMetrics)
            } else {
                0.dpToPx(itemView.resources.displayMetrics)
            }
            (binding.containerWrapperTicket.layoutParams as? MarginLayoutParams)?.bottomMargin = marginValue
        }

        private fun renderPackage(items: PackageItem) {
            with(binding) {
                root.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        val imm = root.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(root.rootView.windowToken, Int.ZERO)
                    }
                }

                txtTitleTicket.text = items.name
                txtSubtitleTicket.text = items.description
                txtSubtitleTicket.visibility = if (items.description.isNotEmpty()) View.VISIBLE else View.GONE
                txtPriceTicket.text = if (items.salesPrice.toIntSafely() != ZERO_PRICE) {
                    getRupiahFormat(items.salesPrice.toIntSafely())
                } else {
                    root.context.resources.getString(R.string.ent_free_price)
                }
                renderTicketStatus(items)
                if (!isListenerRegistered) {
                    quantityEditor.setValue(items.minQty.toIntSafely())
                    quantityEditor.minValue = items.minQty.toIntSafely() - Int.ONE
                    quantityEditor.maxValue = items.maxQty.toIntSafely()

                    quantityEditor.setValueChangedListener { newValue, _, _ ->
                        isError = !((quantityEditor.getValue() >= items.minQty.toIntSafely() || quantityEditor.getValue() >= EMPTY_QTY) && quantityEditor.getValue() <= items.maxQty.toIntSafely())
                        val total = if (newValue < items.minQty.toIntSafely()) EMPTY_QTY else newValue
                        onBindItemTicketListener.quantityEditorValueButtonClicked(
                            idPackage, items.id, items,
                            items.salesPrice.toIntSafely() * total, total.toString(),
                            isError, items.name, items.productId, items.salesPrice,
                            getDate(items.dates, onBindItemTicketListener.getSelectedDate()), packageName
                        )
                        eventPDPTracking.onClickQuantity()
                    }

                    textWatcher = object : TextWatcher {
                        override fun afterTextChanged(txtTotal: Editable?) {
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(txtTotal: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            if (txtTotal.toString().isNotBlank()) {
                                if (getDigit(txtTotal.toString()) > items.maxQty.toIntSafely()) {
                                    quantityEditor.editText.error = String.format(root.context.resources.getString(R.string.ent_error_value_exceeded), items.maxQty)
                                    isError = true
                                } else if (getDigit(txtTotal.toString()) < items.minQty.toIntSafely()) {
                                    txtPilihTicket.visibility = View.VISIBLE
                                    greenDivider.visibility = View.GONE
                                    quantityEditor.visibility = View.GONE
                                    bgTicket.background = ContextCompat.getDrawable(root.context, R.drawable.ent_pdp_ticket_normal_bg)
                                }
                                if (txtTotal.toString().length > Int.ONE) { // zero first checker example: 01 -> 1, 02 -> 2
                                    if (txtTotal.toString().startsWith("0")) {
                                        txtTotal.toString().replaceFirst("^0+(?!$)", "")
                                        quantityEditor.setValue(txtTotal.toString().toIntSafely())
                                        isError = false
                                    }
                                }
                                if (getDigit(txtTotal.toString()) > EMPTY_QTY &&
                                    getDigit(txtTotal.toString()) >= items.minQty.toIntSafely() &&
                                    getDigit(txtTotal.toString()) <= items.maxQty.toIntSafely()
                                ) {
                                    quantityEditor.editText.error = null
                                    isError = false
                                }

                                if (getDigit(txtTotal.toString()) == EMPTY_QTY && items.minQty.toIntSafely() == EMPTY_QTY) {
                                    txtPilihTicket.visibility = View.VISIBLE
                                    greenDivider.visibility = View.GONE
                                    quantityEditor.visibility = View.GONE
                                    bgTicket.background = ContextCompat.getDrawable(root.context, R.drawable.ent_pdp_ticket_normal_bg)
                                }
                            } else if (txtTotal.toString().isBlank()) {
                                isError = true
                            }

                            val total = if (getDigit(txtTotal.toString()) < items.minQty.toIntSafely()) EMPTY_QTY else getDigit(txtTotal.toString())
                            onBindItemTicketListener.quantityEditorValueButtonClicked(
                                idPackage, items.id, items, items.salesPrice.toIntSafely() * total,
                                total.toString(), isError, items.name, items.productId, items.salesPrice,
                                getDate(items.dates, onBindItemTicketListener.getSelectedDate()), packageName
                            )
                        }
                    }

                    quantityEditor.editText.addTextChangedListener(textWatcher)
                    isListenerRegistered = true
                }
            }
        }

        private fun renderGeneralPackage(items: PackageItem) {
            binding.run {
                txtPilihTicket.show()
                txtPilihTicket.setOnClickListener {
                    txtPilihTicket.visibility = View.GONE
                    greenDivider.visibility = View.VISIBLE
                    bgTicket.background = ContextCompat.getDrawable(root.context, R.drawable.ent_pdp_ticket_active_bg)
                    quantityEditor.visibility = View.VISIBLE
                    itemView.parent.clearChildFocus(itemView.findFocus())
                    quantityEditor.setValue(if (items.minQty.toIntSafely() < Int.ONE) MIN_QTY else items.minQty.toIntSafely())
                    quantityEditor.editText.visibility = View.VISIBLE
                    quantityEditor.addButton.visibility = View.VISIBLE
                    quantityEditor.subtractButton.visibility = View.VISIBLE
                    eventPDPTracking.onClickPackage(items, quantityEditor.getValue())
                }
            }
        }

        private fun renderForRecommendationPackage(items: PackageItem) {
            with(binding) {
                txtisRecommeded.show()
                txtisRecommeded.text = root.context.resources.getString(R.string.ent_pdp_ticket_change_date)
                txtisRecommeded.setOnClickListener {
                    onBindItemTicketListener.clickRecommendation(items.dates)
                }
            }
        }

        private fun renderGeneralRecommendationPackage(items: PackageItem) {
            with(binding) {
                txtisRecommeded.show()
                txtisRecommeded.text = root.context.resources.getString(R.string.ent_pdp_ticket_recommended)
                txtisRecommeded.setOnClickListener {
                    onBindItemTicketListener.clickRecommendation(items.dates)
                }
            }
        }

        private fun showSoldOut() {
            with(binding) {
                bgTicket.background = ContextCompat.getDrawable(root.context, R.drawable.ent_pdp_ticket_sold_out)
                txtPilihTicket.setTextColor(root.context.resources.getColor(unifyprinciplesR.color.Unify_NN950_44))
                txtTermurahTicket.setTextColor(root.context.resources.getColor(unifyprinciplesR.color.Unify_NN950_44))
                txtSubtitleTicket.setTextColor(root.context.resources.getColor(unifyprinciplesR.color.Unify_NN950_44))
                txtPriceTicket.hide()
            }
        }
    }

    override fun getItemCount(): Int = listItemPackage.size

    override fun onBindViewHolder(holder: EventPDPTicketItemPackageViewHolder, position: Int) {
        holder.bind(listItemPackage[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventPDPTicketItemPackageViewHolder {
        val binding = EntTicketAdapterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventPDPTicketItemPackageViewHolder(binding)
    }

    fun setList(list: List<PackageItem>, idPackage: String, packageName: String, isRecommendationPackage: Boolean) {
        listItemPackage = list
        this.idPackage = idPackage
        this.packageName = packageName
        this.isRecommendationPackage = isRecommendationPackage
        notifyDataSetChanged()
    }

    companion object {
        const val EMPTY_QTY = 0
        const val MIN_QTY = 1
        const val ZERO_PRICE = 0
    }
}
