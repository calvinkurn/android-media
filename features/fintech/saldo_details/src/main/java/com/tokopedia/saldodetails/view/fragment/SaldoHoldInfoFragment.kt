package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoHoldInfoAdapter
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldInfoItem
import com.tokopedia.saldodetails.utils.CurrencyUtils
import com.tokopedia.saldodetails.view.activity.SaldoHoldInfoActivity.Companion.TAG
import kotlinx.android.synthetic.main.fragment_container_saldo_info.*

class SaldoHoldInfoFragment : Fragment() {

    val SALDO_SELLER_AMOUNT = "SALDO_SELLER_AMOUNT"
    val SALDO_BUYER_AMOUNT = "SALDO_BUYER_AMOUNT"
    var sellerAmount: Long = 0
    var buyerAmount: Long = 0
    var resultList: ArrayList<SaldoHoldInfoItem>? = null
    var saveInstanceCacheManager: SaveInstanceCacheManager? = null
    var saveInstanceCacheManagerId: String? = null
    var type: Int? = 0
    var transactionType = ""

    val saldoHoldInfoAdapter: SaldoHoldInfoAdapter by lazy { SaldoHoldInfoAdapter(ArrayList()) }

    companion object {
        val TRANSACTION_TYPE = "type"
        val FOR_SELLER = "for_seller"
        val FOR_BUYER = "for_buyer"
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

        val turnsType = object : TypeToken<List<SaldoHoldInfoItem>>() {}.type
        resultList = saveInstanceCacheManager?.get(TAG, turnsType)

        sellerAmount = saveInstanceCacheManager?.get(SALDO_SELLER_AMOUNT, Long::class.java) ?: 0
        buyerAmount = saveInstanceCacheManager?.get(SALDO_BUYER_AMOUNT, Long::class.java) ?: 0
        transactionType = saveInstanceCacheManager?.get(TRANSACTION_TYPE, String::class.java) ?: ""

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {

        var resultAmount: Long? = null
        if (transactionType.equals(FOR_BUYER)) {
            resultAmount = buyerAmount
            title_saldo.text = resources.getString(R.string.saldo_total_balance_buyer)
        } else if (transactionType.equals(FOR_SELLER)) {
            resultAmount = sellerAmount
            title_saldo.text = resources.getString(R.string.saldo_total_balance_seller)
        }

        title_saldo_value.text = resultAmount?.let { CurrencyUtils.convertToCurrencyString(it) }
        rv_container.layoutManager = LinearLayoutManager(context)
        rv_container.adapter = saldoHoldInfoAdapter

        saldoHoldInfoAdapter.list.clear()
        resultList?.let { saldoHoldInfoAdapter.list.addAll(it) }
        saldoHoldInfoAdapter.notifyDataSetChanged()

    }

}
