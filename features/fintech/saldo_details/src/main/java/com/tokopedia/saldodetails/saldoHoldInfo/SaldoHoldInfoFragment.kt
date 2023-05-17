package com.tokopedia.saldodetails.saldoHoldInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.utils.CurrencyUtils
import com.tokopedia.saldodetails.saldoHoldInfo.SaldoHoldInfoActivity.Companion.TAG
import com.tokopedia.saldodetails.saldoHoldInfo.response.SaldoHoldInfoItem
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

    private fun initView() {

        var resultAmount: Long? = null
        context?.let { context ->
            when (transactionType) {
                FOR_BUYER -> {
                    resultAmount = buyerAmount
                    title_saldo.text = context.resources.getString(R.string.saldo_total_balance_buyer)
                }
                FOR_SELLER -> {
                    resultAmount = sellerAmount
                    title_saldo.text = context.resources.getString(R.string.saldo_total_balance_seller)
                }
                else -> {
                    title_saldo.gone()
                    title_saldo_value.gone()
                }
            }
        }

        title_saldo_value.text = resultAmount?.let { CurrencyUtils.convertToCurrencyString(it) }
        rv_container.layoutManager = LinearLayoutManager(context)
        rv_container.adapter = saldoHoldInfoAdapter

        saldoHoldInfoAdapter.list.clear()
        resultList?.let { saldoHoldInfoAdapter.list.addAll(it) }
        saldoHoldInfoAdapter.notifyDataSetChanged()

    }

}
