package com.tokopedia.privacycenter.ui.sharingwishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.FragmentSharingWishlistBinding
import com.tokopedia.privacycenter.databinding.SharingWishlistTabItemBinding
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.COLLECTION_PRIVATE
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.COLLECTION_PRIVATE_ID
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.COLLECTION_PUBLIC
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.COLLECTION_PUBLIC_ID
import com.tokopedia.privacycenter.ui.sharingwishlist.collection.SharingWishlistPageFragment
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SharingWishlistFragment : BaseDaggerFragment(), TabLayout.OnTabSelectedListener {

    private var viewBinding by autoClearedNullable<FragmentSharingWishlistBinding>()
    private var tabViewBinding by autoClearedNullable<SharingWishlistTabItemBinding>()

    private val pages: MutableList<SharingWishlistPagerUiModel> = mutableListOf()
    private var sharingWishlistPageAdapter: SharingWishlistPageAdapter? = null

    override fun getScreenName(): String = ""

    override fun initInjector() { }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSharingWishlistBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharingWishlistPageAdapter = SharingWishlistPageAdapter(
            pages,
            parentFragmentManager,
            lifecycle
        )

        viewBinding?.apply {
            toolbarSharingWishlist.apply {
                headerTitle = getString(R.string.sharing_wishlist_page_title)
                isShowShadow = false
                setNavigationOnClickListener {
                    activity?.onBackPressed()
                }
            }

            pagerWishlistCollection.apply {
                adapter = sharingWishlistPageAdapter
            }
        }

        setupTabCollection()
        renderPages()
    }

    private fun renderPages() {
        pages.clear()
        pages.addAll(
            listOf(
                SharingWishlistPagerUiModel(
                    COLLECTION_PRIVATE,
                    SharingWishlistPageFragment.createInstance(COLLECTION_PRIVATE_ID)
                ),
                SharingWishlistPagerUiModel(
                    COLLECTION_PUBLIC,
                    SharingWishlistPageFragment.createInstance(COLLECTION_PUBLIC_ID)
                )
            )
        )

        sharingWishlistPageAdapter?.notifyItemRangeInserted(0, pages.size)
    }

    private fun setupTabCollection() {
        viewBinding?.apply {
            TabLayoutMediator(tabCollection, pagerWishlistCollection) { tab, position ->
                tabViewBinding = SharingWishlistTabItemBinding.inflate(
                    LayoutInflater.from(context),
                    tabCollection,
                    false
                )

                tabViewBinding?.root?.let {
                    it.text = pages[position].name
                }

                tab.customView = tabViewBinding?.root
            }.attach()

            tabCollection.addOnTabSelectedListener(this@SharingWishlistFragment)
        }
    }

    private fun updateTabView(isSelected: Boolean, view: Typography) {
        view.setTextColor(
            if (isSelected) {
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            } else {
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
            }
        )
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        updateTabView(true, tab?.customView as Typography)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        updateTabView(false, tab?.customView as Typography)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        updateTabView(true, tab?.customView as Typography)
    }

    companion object {
        fun createInstance(tab: String) = SharingWishlistFragment().apply {
            arguments = Bundle().apply {
                putString(ApplinkConstInternalUserPlatform.PARAM_TAB, tab)
            }
        }
    }
}
