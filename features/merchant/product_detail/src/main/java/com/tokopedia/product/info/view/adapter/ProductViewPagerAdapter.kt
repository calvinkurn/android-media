package com.tokopedia.product.info.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tokopedia.product.info.model.description.DescriptionData
import com.tokopedia.product.info.view.fragment.ProductFullDescriptionFragment
import com.tokopedia.product.info.view.fragment.ProductSpecificationFragment
import com.tokopedia.product.info.model.specification.Specification

class ProductViewPagerAdapter(fragmentManager: FragmentManager,
                              private val titleList: List<String>,
                              private val descriptionData: DescriptionData) : FragmentPagerAdapter(fragmentManager) {

    private var listOfSpecification: ArrayList<Specification> = arrayListOf()

    fun setSpecificationData(listOfData: ArrayList<Specification>) {
        this.listOfSpecification = listOfData
    }

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