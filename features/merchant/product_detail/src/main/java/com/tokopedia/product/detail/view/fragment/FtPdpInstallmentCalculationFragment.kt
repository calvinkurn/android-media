package com.tokopedia.product.detail.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.product.detail.R

class FtPdpInstallmentCalculationFragment : TkpdBaseV4Fragment() {

    private lateinit var ftRecyclerView: RecyclerView
    private var mContext: Context? = null

    companion object {
        const val KEY_INSTALLMENT_CALCULATION_DATA = "keyInstallmentData"
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pdp_installment_calculation_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {

        ftRecyclerView = view.findViewById(R.id.ft_recycler_view)
        val linearLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        ftRecyclerView.layoutManager = linearLayoutManager
        /*arguments?.let {
            ftRecyclerView.adapter =
                    FtPDPInstallmentCalculationAdapter(
                            it.getParcelableArrayList<FtCalculationPartnerData>(KEY_INSTALLMENT_CALCULATION_DATA)
                                    ?: ArrayList())

        }*/
    }

    override fun onAttachActivity(context: Context) {
        this.mContext = context
        super.onAttachActivity(context)
    }
}