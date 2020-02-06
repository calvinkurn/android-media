package com.tokopedia.salam.umrah.travel.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.salam.umrah.travel.data.UmrahTravelAgentBySlugNameEntity
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelAgentGalleryFragment
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelAgentInfoFragment
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelAgentProductsFragment

class UmrahTravelAgentViewPagerAdapter(fm: FragmentManager, val slugName: String, private val umrahTravelAgentBySlugNameEntity: UmrahTravelAgentBySlugNameEntity): FragmentStatePagerAdapter(fm){

    override fun getCount(): Int  = SIZE_TAB

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> UmrahTravelAgentProductsFragment.createInstance(slugName)
            1 -> UmrahTravelAgentGalleryFragment.createInstance(slugName)
            2 -> UmrahTravelAgentInfoFragment.createInstance(slugName)
            else -> UmrahTravelAgentInfoFragment.createInstance(slugName)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> PRODUCTS_TITLE
            1 -> GALLERY_TITLE
            2 -> INFO_TITLE
            else -> PRODUCTS_TITLE
        }
    }

    companion object {
        const val SIZE_TAB = 3
        const val PRODUCTS_TITLE = "Paket Umroh"
        const val GALLERY_TITLE = "Galeri"
        const val INFO_TITLE = "Info"
    }
}