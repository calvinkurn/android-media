package com.tokopedia.product.detail.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.financing.FtCalculationPartnerData
import com.tokopedia.product.detail.data.model.financing.FtInstallmentTnc
import com.tokopedia.product.detail.data.model.financing.FtTncData
import com.tokopedia.product.detail.view.adapter.FtPDPInstallmentCalculationAdapter

class FtPdpInstallmentCalculationFragment : FtPDPInstallmentCalculationAdapter.GetTncDataFromFragment, TkpdBaseV4Fragment() {

    private var productPrice: Float? = 0f
    private var isOfficialStore: Boolean = false
    private lateinit var ftRecyclerView: RecyclerView
    private var tncDataListId: String = ""
    private var partnerDataItemListId: String = ""
    private lateinit var partnerDataItemList: ArrayList<FtCalculationPartnerData>
    private lateinit var tncDataList: ArrayList<FtInstallmentTnc>
    private var tncIdHashMap: HashMap<Int, ArrayList<FtTncData>> = HashMap()

    companion object {
        const val KEY_INSTALLMENT_CALCULATION_DATA = "keyInstallmentData"
        const val KEY_INSTALLMENT_PRODUCT_PRICE = "keyInstallmentProductPrice"
        const val KEY_INSTALLMENT_TNC_LIST = "keyInstallmentTncList"
        const val KEY_INSTALLMENT_IS_OFFICIAL_STORE = "keyInstallmentIsOfficialStore"

        fun createInstance(context: Context?, productPrice: Float, tncList: ArrayList<FtInstallmentTnc>, isOfficialStore: Boolean,
                           dataList: ArrayList<FtCalculationPartnerData>): FtPdpInstallmentCalculationFragment {
            val bundle = Bundle()
            bundle.putFloat(KEY_INSTALLMENT_PRODUCT_PRICE, productPrice)
            bundle.putBoolean(KEY_INSTALLMENT_IS_OFFICIAL_STORE, isOfficialStore)
            val lendingPartnerFragment = FtPdpInstallmentCalculationFragment()
            context?.let {

                val cacheManager = SaveInstanceCacheManager(it, true)

                cacheManager.put(FtInstallmentTnc::class.java.simpleName, ArrayList<FtInstallmentTnc>(tncList))
                cacheManager.put(FtCalculationPartnerData::class.java.simpleName, ArrayList<FtCalculationPartnerData>(dataList))

                bundle.putString(KEY_INSTALLMENT_TNC_LIST, cacheManager.id!!)
                lendingPartnerFragment.arguments = bundle
            }


            return lendingPartnerFragment
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productPrice = it.getFloat(KEY_INSTALLMENT_PRODUCT_PRICE)
            isOfficialStore = it.getBoolean(KEY_INSTALLMENT_IS_OFFICIAL_STORE)

            tncDataListId = it.getString(KEY_INSTALLMENT_TNC_LIST) ?: ""
            val cacheManager = SaveInstanceCacheManager(context!!, tncDataListId)

            tncDataList = cacheManager.get(FtInstallmentTnc::class.java.simpleName,
                    object : TypeToken<ArrayList<FtInstallmentTnc>>() {}.type, ArrayList())
                    ?: ArrayList()

            partnerDataItemList = cacheManager.get(FtCalculationPartnerData::class.java.simpleName,
                    object : TypeToken<ArrayList<FtCalculationPartnerData>>() {}.type, ArrayList())
                    ?: ArrayList()

        }
        prepareTncIDHashMap()
    }

    private fun prepareTncIDHashMap() {
        for (tncItem in tncDataList) {
            tncIdHashMap[tncItem.tncId] = tncItem.tncList
        }
    }

    override fun getTncData(id: Int): ArrayList<FtTncData>? {
        return tncIdHashMap[id]
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pdp_installment_calculation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        context?.let {
            ftRecyclerView = view.findViewById(R.id.ft_recycler_view)
            val linearLayoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            ftRecyclerView.layoutManager = linearLayoutManager
            ftRecyclerView.adapter = FtPDPInstallmentCalculationAdapter(productPrice,
                    isOfficialStore, partnerDataItemList, this)
        }
    }

}