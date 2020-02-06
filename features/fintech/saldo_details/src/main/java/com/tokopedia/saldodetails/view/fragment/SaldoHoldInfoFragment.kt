package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoHoldInfoAdapter
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.BuyerDataItem
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SellerDataItem
import kotlinx.android.synthetic.main.fragment_container_saldo_info.*
import kotlin.math.absoluteValue

class SaldoHoldInfoFragment : Fragment() {

    val SALDO_SELLER_AMOUNT = "SALDO_SELLER_AMOUNT"
    val SALDO_BUYER_AMOUNT = "SALDO_BUYER_AMOUNT"
    val RESULT_LIST = "RESULT_LIST"
    var sellerAmount: Double? = 0.0
    var buyerAmount: Double? = 0.0
    var resultList: ArrayList<Any>? = null
    var saveInstanceCacheManager: SaveInstanceCacheManager? = null
    var saveInstanceCacheManagerId: String? = null
    var type: Int? = 0
    val typeSeler = 1
    val typeBuyer = 2


    val saldoHoldInfoAdapter: SaldoHoldInfoAdapter by lazy { SaldoHoldInfoAdapter(ArrayList(), 0) }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val saldoHoldInfoFragment = SaldoHoldInfoFragment()
            saldoHoldInfoFragment.arguments = bundle
            return saldoHoldInfoFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_container_saldo_info, container, false)
        val bundle = arguments
        saveInstanceCacheManagerId = bundle?.getString("SAVE_INSTANCE_CACHEMANAGER_ID")
        saveInstanceCacheManager = context?.let { SaveInstanceCacheManager(it, saveInstanceCacheManagerId) }

        type = saveInstanceCacheManager?.get("KEY_TYPE", Int::class.java)
        if (type == typeSeler) {
            resultList = saveInstanceCacheManager?.get(RESULT_LIST, ArrayList<SellerDataItem>()::class.java)
        } else
            resultList = saveInstanceCacheManager?.get(RESULT_LIST, ArrayList<BuyerDataItem>()::class.java)

        sellerAmount = saveInstanceCacheManager?.get(SALDO_SELLER_AMOUNT, Double::class.java) ?: 0.0
        buyerAmount = saveInstanceCacheManager?.get(SALDO_BUYER_AMOUNT, Double::class.java) ?: 0.0

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        var type = 0
        var resultAmount: Double? = 0.0
        if (sellerAmount == 0.0) {
            type = 1
            resultAmount = buyerAmount
            title_saldo.text = resources.getString(R.string.saldo_total_balance_buyer)
        } else if (buyerAmount == 0.0) {
            type = 2
            resultAmount = sellerAmount
            title_saldo.text = resources.getString(R.string.saldo_total_balance_seller)
        }

        title_saldo_value.text = resultAmount?.let { CurrencyFormatUtil.convertPriceValueToIdrFormat(it, false) }
        rv_container.layoutManager = LinearLayoutManager(context)
        rv_container.adapter = saldoHoldInfoAdapter

        saldoHoldInfoAdapter.list.clear()
        saldoHoldInfoAdapter.list.addAll(resultList as ArrayList<Any>)
        saldoHoldInfoAdapter.type = type
        saldoHoldInfoAdapter.notifyDataSetChanged()

    }

}
