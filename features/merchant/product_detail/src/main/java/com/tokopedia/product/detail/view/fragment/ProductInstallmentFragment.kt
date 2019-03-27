package com.tokopedia.product.detail.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.installment.InstallmentBank
import com.tokopedia.product.detail.data.util.InstallmentTypeDef
import com.tokopedia.product.detail.view.adapter.BankInstallmentAdapter

class ProductInstallmentFragment: BaseDaggerFragment() {
    var installmentType: Int = InstallmentTypeDef.MONTH_3
    var isOs: Boolean = false
    lateinit var adapter: BankInstallmentAdapter

    companion object {
        private const val INSTALLMENT_TYPE: String = "installment_type"
        private const val IS_OS_SHOP: String = "is_os"

        fun createInstance(installmentType: Int, isOsShop: Boolean): Fragment{
            return ProductInstallmentFragment().also {
                it.arguments = Bundle().apply { putInt(INSTALLMENT_TYPE, installmentType)
                                            putBoolean(IS_OS_SHOP, isOsShop)}
            }
        }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            installmentType = it.getInt(INSTALLMENT_TYPE, InstallmentTypeDef.MONTH_3)
            isOs = it.getBoolean(IS_OS_SHOP, false)
        }

        if (!::adapter.isInitialized)
            adapter = BankInstallmentAdapter(isOs)

        view.findViewById<VerticalRecyclerView>(R.id.recycler_view).adapter = adapter
    }

    fun updateInstallment(installments: List<InstallmentBank>, type: Int){
        if (type == installmentType){
            adapter.setInstallments(installments)
        }
    }
}