package com.tokopedia.product.detail.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.product.detail.view.fragment.ProductFullDescriptionFragment
import com.tokopedia.product.detail.view.fragment.ProductSpecificationFragment

class ProductViewPagerAdapter(fragmentManager: FragmentManager,
                              private val titleList: List<String>,
                              private val descriptionData: DescriptionData,
                              private val listOfSpecification: ArrayList<Specification>) : FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ProductFullDescriptionFragment.createInstance(descriptionData)
            else -> ProductSpecificationFragment.createInstance(listOfSpecification)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    override fun getCount(): Int = titleList.size

}