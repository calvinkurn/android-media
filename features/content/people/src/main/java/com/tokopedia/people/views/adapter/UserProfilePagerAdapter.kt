package com.tokopedia.people.views.adapter

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
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

    fun insertFragment(tabs: List<ProfileTabUiModel.Tab>) {
        if (tabs.isEmpty()) return

        listFragment.clear()
        listFragment.addAll(tabs)

        attachTab()
    }

    fun getTabs() = listFragment

    fun getFeedsTabs() = listFragment.filter { it.key == ProfileTabUiModel.Key.Feeds }

    fun getVideoTabs() = listFragment.filter { it.key == ProfileTabUiModel.Key.Video }

    fun getReviewTabs() = listFragment.filter { it.key == ProfileTabUiModel.Key.Review }

    private fun attachTab() {
        TabsUnifyMediator(tabLayout, viewPager) { tab, position ->
            addNewTab(tab, position)
        }
        tabSelectedListener()
    }

    private fun tabSelectedListener() {
        tabLayout.getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) return
                addSelectedTab(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab == null) return
                addSelectedTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab == null) return
                addNewTab(tab, tab.position)
            }
        },)
    }

    private fun addNewTab(tab: TabLayout.Tab, position: Int) {
        val selectedFragment = listFragment[position]
        when (selectedFragment.key) {
            ProfileTabUiModel.Key.Feeds -> {
                tab.setCustomIcon(getIconUnifyDrawable(fragmentActivity, IconUnify.IMAGE))
                    .setNew(selectedFragment.isNew)
            }
            ProfileTabUiModel.Key.Video -> {
                tab.setCustomIcon(getIconUnifyDrawable(fragmentActivity, IconUnify.VIDEO))
                    .setNew(selectedFragment.isNew)
            }
            ProfileTabUiModel.Key.Review -> {
                tab.setCustomIcon(getIconUnifyDrawable(fragmentActivity, IconUnify.STAR))
                    .setNew(selectedFragment.isNew)
            }
            else -> {}
        }
    }

    private fun addSelectedTab(tab: TabLayout.Tab) {
        val selectedFragment = listFragment[tab.position]
        val key = selectedFragment.key
        onOpenTab(key.value)
        when (key) {
            ProfileTabUiModel.Key.Feeds -> {
                tab.setCustomIcon(getIconUnifyDrawable(fragmentActivity, IconUnify.IMAGE, ContextCompat.getColor(fragmentActivity, com.tokopedia.unifyprinciples.R.color.Unify_GN500)))
                    .setNew(selectedFragment.isNew)
            }
            ProfileTabUiModel.Key.Video -> {
                tab.setCustomIcon(getIconUnifyDrawable(fragmentActivity, IconUnify.VIDEO, ContextCompat.getColor(fragmentActivity, com.tokopedia.unifyprinciples.R.color.Unify_GN500)))
                    .setNew(selectedFragment.isNew)
            }
            ProfileTabUiModel.Key.Review -> {
                tab.setCustomIcon(getIconUnifyDrawable(fragmentActivity, IconUnify.STAR, ContextCompat.getColor(fragmentActivity, com.tokopedia.unifyprinciples.R.color.Unify_GN500)))
                    .setNew(selectedFragment.isNew)
            }
            else -> {}
        }
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
