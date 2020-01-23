package com.tokopedia.salam.umrah.travel.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelAgentGalleryFragment
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelAgentInfoFragment
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelAgentProductsFragment

class UmrahTravelAgentViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){

    override fun getCount(): Int  = SIZE_TAB

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> UmrahTravelAgentProductsFragment()
            1 -> UmrahTravelAgentGalleryFragment()
            2 -> UmrahTravelAgentInfoFragment()
            else -> UmrahTravelAgentProductsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Paket Umroh"
            1 -> "Galeri"
            2 -> "Info"
            else -> "Paket Umroh"
        }
    }

    companion object {
        const val SIZE_TAB = 3
    }
}