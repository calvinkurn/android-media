package com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.presentation.detail.PayLaterPaymentOptionsFragment
import com.tokopedia.pdpsimulation.paylater.presentation.detail.PayLaterPaymentOptionsFragment.Companion.PAYLATER_PARTNER_POSITION

class PayLaterOfferPagerAdapter(fm: FragmentManager, behaviour: Int) :
    FragmentStatePagerAdapter(fm, behaviour) {
    private val paymentOptionsFragmentArray = SparseArrayCompat<Fragment>()
    private var paymentProductList: List<Detail> = ArrayList()

    override fun getItem(position: Int): Fragment {
        val bundle = setBundleData(position)
        return PayLaterPaymentOptionsFragment.newInstance(bundle)
    }

    private fun setBundleData(position: Int): Bundle {
        val paymentOption = paymentProductList[position]
        return Bundle().apply {
            putParcelable(PayLaterPaymentOptionsFragment.PAY_LATER_PARTNER_DATA, paymentOption)
            putInt(PAYLATER_PARTNER_POSITION, position)
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

    fun setPaymentData(paymentProductList: List<Detail>) {
        this.paymentProductList = paymentProductList

        notifyDataSetChanged()
    }

    fun getPaymentDetailByPosition(position: Int): Detail? {
        return if (position < count) {
            paymentProductList[position]
        } else
            null
    }

    override fun getPageWidth(position: Int): Float {
        return 0.9f
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE

    }

}