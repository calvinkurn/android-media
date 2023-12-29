package com.tokopedia.people.views.adapter

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.people.views.fragment.UserProfileFeedFragment
import com.tokopedia.people.views.fragment.UserProfileReviewFragment
import com.tokopedia.people.views.fragment.UserProfileVideoFragment
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomIcon
import com.tokopedia.unifycomponents.setNew

/**
 * created by fachrizalmrsln on 11/11/2022
 */
class UserProfilePagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
    private val tabLayout: TabsUnify,
    private val viewPager: ViewPager2,
    private val onOpenTab: (key: String) -> Unit,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val listFragment = mutableListOf<ProfileTabUiModel.Tab>()

    private var selectedPosition = 0

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (tab == null) return
            onOpenTab(listFragment[tab.position].key.value)
            setupTab(tab, isSelected = true)
            selectedPosition = tab.position
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            if (tab == null) return
            setupTab(tab, isSelected = true)
            selectedPosition = tab.position
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            if (tab == null) return
            setupTab(tab, isSelected = false)
        }
    }

    fun updateFragment(tabs: List<ProfileTabUiModel.Tab>) {
        if (isTabCountAndOrderStillSame(tabs)) {
            for (i in 0 until tabLayout.getUnifyTabLayout().tabCount) {
                tabLayout.getUnifyTabLayout().getTabAt(i)?.let { tab ->
                    setupTab(tab, tab.isSelected)
                }
            }
        } else {
            selectedPosition = selectedPosition.coerceAtMost(tabs.size - 1).coerceAtLeast(0)

            removeTabSelectedListener()

            val diffUtil = UserProfilePagerDiffUtil(listFragment, tabs)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            listFragment.clear()
            listFragment.addAll(tabs)
            diffResult.dispatchUpdatesTo(this)

            attachTab()
            viewPager.setCurrentItem(selectedPosition, true)
        }
    }

    private fun isTabCountAndOrderStillSame(newTabs: List<ProfileTabUiModel.Tab>): Boolean {
        return if (newTabs.size != listFragment.size) {
            false
        } else {
            var isSame = true
            newTabs.forEachIndexed { idx, e ->
                if (e.key != listFragment[idx].key) isSame = false
            }
            isSame
        }
    }

    fun getTabs() = listFragment

    fun getFeedsTabs() = listFragment.filter { it.key == ProfileTabUiModel.Key.Feeds }

    fun getVideoTabs() = listFragment.filter { it.key == ProfileTabUiModel.Key.Video }

    fun getReviewTabs() = listFragment.filter { it.key == ProfileTabUiModel.Key.Review }

    private fun attachTab() {
        TabsUnifyMediator(tabLayout, viewPager) { tab, position ->
            setupTab(tab, isSelected = false)
        }
        addTabSelectedListener()
    }

    private fun addTabSelectedListener() {
        tabLayout.getUnifyTabLayout().addOnTabSelectedListener(onTabSelectedListener)
    }

    private fun removeTabSelectedListener() {
        tabLayout.getUnifyTabLayout().removeOnTabSelectedListener(onTabSelectedListener)
    }

    private fun setupTab(tab: TabLayout.Tab, isSelected: Boolean) {
        val currentFragment = listFragment[tab.position]
        val key = currentFragment.key

        val color = when (isSelected) {
            true -> ContextCompat.getColor(fragmentActivity, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            false -> null
        }

        val icon = when (key) {
            ProfileTabUiModel.Key.Feeds -> IconUnify.IMAGE
            ProfileTabUiModel.Key.Video -> IconUnify.VIDEO
            ProfileTabUiModel.Key.Review -> IconUnify.STAR
            else -> 0
        }

        tab.setCustomIcon(getIconUnifyDrawable(fragmentActivity, icon, color))
            .setNew(currentFragment.isNew)
    }

    override fun getItemCount(): Int = listFragment.size

    override fun createFragment(position: Int): Fragment {
        val currentFragment = listFragment[position]
        return when (currentFragment.key) {
            ProfileTabUiModel.Key.Feeds -> UserProfileFeedFragment.getFragment(
                fragmentManager = fragmentManager,
                classLoader = fragmentActivity.classLoader,
                bundle = Bundle(),
            )
            ProfileTabUiModel.Key.Video -> UserProfileVideoFragment.getFragment(
                fragmentManager = fragmentManager,
                classLoader = fragmentActivity.classLoader,
                bundle = Bundle(),
            )
            ProfileTabUiModel.Key.Review -> UserProfileReviewFragment.getFragment(
                fragmentManager = fragmentManager,
                classLoader = fragmentActivity.classLoader,
                bundle = Bundle(),
            )
            else -> Fragment()
        }
    }
}
