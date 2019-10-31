package com.tokopedia.product.detail.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.financing.FtCalculationPartnerData
import com.tokopedia.product.detail.view.adapter.FtPDPInstallmentCalculationAdapter

class FtPdpInstallmentCalculationFragment : TkpdBaseV4Fragment() {

    private var productPrice: Float? = 0f
    private lateinit var ftRecyclerView: RecyclerView
    private var mContext: Context? = null
    private lateinit var partnerDataItemList: ArrayList<FtCalculationPartnerData>


    companion object {
        const val KEY_INSTALLMENT_CALCULATION_DATA = "keyInstallmentData"
        const val KEY_INSTALLMENT_PRODUCT_PRICE = "keyInstallmentProductPrice"

        fun createInstance(productPrice: Float, dataList: ArrayList<FtCalculationPartnerData>): FtPdpInstallmentCalculationFragment {
            val bundle = Bundle()
            bundle.putFloat(KEY_INSTALLMENT_PRODUCT_PRICE, productPrice)
            bundle.putParcelableArrayList(KEY_INSTALLMENT_CALCULATION_DATA, dataList)
            val lendingPartnerFragment = FtPdpInstallmentCalculationFragment()
            lendingPartnerFragment.arguments = bundle
            return lendingPartnerFragment
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productPrice = arguments?.getFloat(KEY_INSTALLMENT_PRODUCT_PRICE)
        partnerDataItemList = arguments?.getParcelableArrayList<FtCalculationPartnerData>(KEY_INSTALLMENT_CALCULATION_DATA)
                ?: ArrayList()
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
        ftRecyclerView = view.findViewById(R.id.ft_recycler_view)
        val linearLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        ftRecyclerView.layoutManager = linearLayoutManager
        ftRecyclerView.adapter = FtPDPInstallmentCalculationAdapter(context, productPrice, partnerDataItemList)
    }

    override fun onAttachActivity(context: Context) {
        this.mContext = context
        super.onAttachActivity(context)
    }
}