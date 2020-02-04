package com.tokopedia.salam.umrah.travel.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.travel.data.UmrahTravelAgentBySlugNameEntity
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelAgentInfoFragment
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelAgentProductsFragment

class UmrahTravelAgentViewPagerAdapter(fm: FragmentManager, private val umrahTravelAgentBySlugNameEntity: UmrahTravelAgentBySlugNameEntity): FragmentStatePagerAdapter(fm),
        UmrahTravelAgentProductsFragment.UmrahTravelAgentProductListener,
        UmrahTravelAgentInfoFragment.UmrahTravelAgentInfoListener{

    override fun getCount(): Int  = SIZE_TAB

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> UmrahTravelAgentProductsFragment(this)
            1 -> UmrahTravelAgentInfoFragment(this)
            else -> UmrahTravelAgentProductsFragment(this)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> PRODUCTS_TITLE
            1 -> INFO_TITLE
            else -> PRODUCTS_TITLE
        }
    }

    override fun getDataProducts():List<UmrahProductModel.UmrahProduct> {
        return umrahTravelAgentBySlugNameEntity.umrahTravelAgentBySlug.products
    }

    override fun getDataInfo(): TravelAgent {
        return umrahTravelAgentBySlugNameEntity.umrahTravelAgentBySlug
    }

    companion object {
        const val SIZE_TAB = 2
        const val PRODUCTS_TITLE = "Paket Umroh"
        const val GALLERY_TITLE = "Galeri"
        const val INFO_TITLE = "Info"
    }
}