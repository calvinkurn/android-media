package com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.pdpsimulation.paylater.presentation.detail.PayLaterPaymentOptionsFragment

class PayLaterOfferPagerAdapter(fm: FragmentManager, behaviour: Int) : FragmentStatePagerAdapter(fm, behaviour) {
    private val paymentOptionsFragmentArray = SparseArrayCompat<Fragment>()
    private var paymentProductList: ArrayList<PayLaterItemProductData> = ArrayList()
    private var applicationStatusList: ArrayList<PayLaterApplicationDetail> = ArrayList()

    override fun getItem(position: Int): Fragment {
        val bundle = setBundleData(position)
        return PayLaterPaymentOptionsFragment.newInstance(bundle)
    }

    private fun setBundleData(position: Int): Bundle {
        val paymentOption = paymentProductList[position]
        val applicationStatus: PayLaterApplicationDetail? = PayLaterPartnerTypeMapper.getPayLaterApplicationDataForPartner(paymentOption, applicationStatusList)
        return Bundle().apply {
            putParcelable(PayLaterPaymentOptionsFragment.PAY_LATER_PARTNER_DATA, paymentProductList[position])
            applicationStatus?.let {
                putParcelable(PayLaterPaymentOptionsFragment.PAY_LATER_APPLICATION_DATA, applicationStatus)
            }
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Fragment {
        val fragment = super.instantiateItem(container, position) as Fragment
        paymentOptionsFragmentArray[position, fragment]
        return fragment
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        paymentOptionsFragmentArray.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return paymentProductList.size
    }

    fun setPaymentData(paymentProductList: ArrayList<PayLaterItemProductData>, applicationStatusList: ArrayList<PayLaterApplicationDetail>) {
        this.paymentProductList = paymentProductList
        this.applicationStatusList = applicationStatusList
        notifyDataSetChanged()
    }

    override fun getPageWidth(position: Int): Float {
        return 0.9f
    }

}