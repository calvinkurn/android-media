package com.tokopedia.privacycenter.dsar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.dsar.domain.SubmitRequestUseCase
import com.tokopedia.privacycenter.dsar.model.CreateRequestResponse
import com.tokopedia.privacycenter.dsar.model.ItemRangeModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DsarViewModel @Inject constructor(
    val submitRequestUseCase: SubmitRequestUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val itemRangeData = MutableLiveData<ArrayList<ItemRangeModel>>()
    val _itemRangeData: LiveData<ArrayList<ItemRangeModel>> = itemRangeData

    private val filterItems = MutableLiveData<ArrayList<String>>(arrayListOf())
    val _filterItems: LiveData<ArrayList<String>> = filterItems

    private val startDate = MutableLiveData<String>()
    val _startDate: LiveData<String> = startDate

    private val showSummary = MutableLiveData<String>()
    val _showSummary: LiveData<String> = showSummary

    private val endDate = MutableLiveData<String>()
    val _endDate: LiveData<String> = endDate

    private val submitRequest = MutableLiveData<CreateRequestResponse>()
    val _submitRequest: LiveData<CreateRequestResponse> = submitRequest

    fun setRangeItems(items: ArrayList<ItemRangeModel>) {
        itemRangeData.value = items
    }

    fun getSelectedRangeItems(): ItemRangeModel? {
        return itemRangeData.value?.first { it.selected }
    }

    fun setSelectedRangeItems(id: Int) {
        itemRangeData.value?.forEach {
            it.selected = it.id == id
            if(it.id == 5) {
                it.transactionDate = calculateTransactionDate(id)
            }
        }
        itemRangeData.value = itemRangeData.value
    }

    fun populateRangeItems() {
        itemRangeData.value?.clear()
        val items = arrayListOf(
            ItemRangeModel(0, "Selama Tahun Ini", transactionDate = calculateTransactionDate(0)),
            ItemRangeModel(1, "3 Tahun Terakhir", transactionDate = calculateTransactionDate(1)),
            ItemRangeModel(2, "3 Bulan Terakhir", transactionDate = calculateTransactionDate(2)),
            ItemRangeModel(3, "30 Hari Terakhir", transactionDate = calculateTransactionDate(3)),
            ItemRangeModel(4, "7 Hari Terakhir", transactionDate = calculateTransactionDate(4)),
            ItemRangeModel(5, "Pilih Tanggal Sendiri", transactionDate = calculateTransactionDate(5))
        )
        itemRangeData.value = items
    }

    fun addFilter(filter: String) {
        if((filterItems.value?.count() ?: 0) < 3) {
            val newVal = filterItems.value?.apply {
                add(filter)
            }
            filterItems.value = newVal
        }
    }

    fun removeFilter(filter: String) {
        val newVal = filterItems.value?.apply {
            remove(filter)
        }
        filterItems.value = newVal
    }

    fun submitRequest() {
        launch {
            try {
                val requests = arrayListOf<String>()
                filterItems.value?.forEach {
                    if(it == "personal") {
                        requests.add("full_name")
                        requests.add("mailing_address")
                        requests.add("phone_number")
                        requests.add("email")
                        requests.add("dob")
                        requests.add("gender")
                    }
                    if(it == "payment") {
                        requests.add("bank_account")
                        requests.add("payment")
                    }
                    if(it == "transaction") {
                        requests.add(getSelectedRangeItems()?.transactionDate ?: "")
                    }
                }
                val param = submitRequestUseCase.constructParams(requests)
                submitRequest.value = submitRequestUseCase(param)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun applyTransactionHistoryFilter() {
        if(itemRangeData.value?.isNotEmpty() == true) {
            println("selected_item ${itemRangeData.value!!.first { it.selected }}")
        } else {

        }
    }

    fun calculateTransactionDate(selectedId: Int): String {
        val maxDate = GregorianCalendar(Locale.getDefault())
        val minDate = GregorianCalendar(Locale.getDefault())
        when(selectedId) {
            0 -> {
                minDate.apply {
                    set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY)
                    set(GregorianCalendar.DAY_OF_MONTH, 1)
                }
            }
            1 -> {
                minDate.apply {
                    add(Calendar.YEAR, -3)
                }
            }
            2 -> {
                minDate.apply {
                    add(Calendar.MONTH, -3)
                }
            }
            3 -> {
                minDate.apply {
                    add(Calendar.DAY_OF_MONTH, -30)
                }
            }
            4 -> {
                minDate.apply {
                    add(Calendar.DAY_OF_MONTH, -7)
                }
            }
            5 -> {
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                if(endDate.value != null && startDate.value != null) {
                    maxDate.time = sdf.parse(endDate.value ?: "")
                    minDate.time = sdf.parse(startDate.value ?: "")
                }
            }
        }
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        val formattedMaxDate = dateFormat.format(maxDate.time)
        val formattedMinDate = dateFormat.format(minDate.time)
        return "transaction_history_${formattedMinDate}_${formattedMaxDate}"
    }

    fun setStartDate(date: Date) {
        startDate.value = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date).toString()
    }

    fun setEndDate(date: Date) {
        endDate.value = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date).toString()
        setSelectedRangeItems(5)
    }

    fun showSummary() {
        var text = ""
        filterItems.value?.forEach {
            if(it == "personal") {
                text += "Informasi Pribadi:\nUser ID, Nama Lengkap, Nomor HP, E-mail, Tanggal Lahir, Jenis Kelamin, Daftar Alamat Pengiriman\n\n"
            }
            if(it == "payment") {
                text += "Informasi Pembayaran:\nDaftar Rekening Bank, Daftar Kartu Kredit, Daftar Kartu Debit\n\n"
            }
            if(it == "transaction") {
                val transData = getSelectedRangeItems()
                var transText = transData?.title
                if(transData?.id == 5) {
                    transText = "${startDate.value} - ${endDate.value}"
                }
                text += "Riwayat Transaksi:\n${transText}\n\n"
            }
        }
        showSummary.value = text
    }
}
