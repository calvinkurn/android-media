package com.tokopedia.privacycenter.ui.sharingwishlist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.privacycenter.ui.sharingwishlist.collection.SharingWishlistPageFragment

class SharingWishlistPageAdapter(
    private var pages: MutableList<SharingWishlistPagerUiModel>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position].fragment
    }
}

data class SharingWishlistPagerUiModel(
    val name: String,
    val fragment: SharingWishlistPageFragment
)
