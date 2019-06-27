package com.tokopedia.product.detail.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup
import com.tokopedia.product.detail.data.util.InstallmentTypeDef
import com.tokopedia.product.detail.view.fragment.ProductInstallmentFragment

class ProductInstallmentAdapter(fm: FragmentManager, private val isOsShop: Boolean): FragmentStatePagerAdapter(fm) {
    private val installmentsType = intArrayOf(InstallmentTypeDef.MONTH_3, InstallmentTypeDef.MONTH_6, InstallmentTypeDef.MONTH_12)
    val registeredFragment = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int): Fragment{
        val type = installmentsType[position]
        return ProductInstallmentFragment.createInstance(type, isOsShop)
    }

    override fun getCount(): Int = installmentsType.size

    override fun getPageTitle(position: Int): CharSequence? {
        return "${installmentsType[position]} BULAN"
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragment.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragment.remove(position)
        super.destroyItem(container, position, `object`)
    }
}