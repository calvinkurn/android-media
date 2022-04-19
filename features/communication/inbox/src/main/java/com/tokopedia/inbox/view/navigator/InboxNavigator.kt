package com.tokopedia.inbox.view.navigator

import android.content.Context
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inboxcommon.InboxFragment
import com.tokopedia.inboxcommon.RoleType

class InboxNavigator constructor(
        private val context: Context,
        @IdRes
        private val containerId: Int,
        private val fm: FragmentManager,
        private val fragmentFactory: InboxFragmentFactory
) {
    private var notificationFragment: Fragment? = null
    private var chatFragment: Fragment? = null
    private var discussionFragment: Fragment? = null
    private var reviewFragment: Fragment? = null

    @InboxFragmentType
    var currentSelectedPage: Int = InboxFragmentType.NONE
        private set
    private val pages: MutableMap<Fragment?, String?> = mutableMapOf()

    init {
        initFragments()
    }

    private fun initFragments() {
        chatFragment = fragmentFactory.createChatListFragment()
        notificationFragment = fragmentFactory.createNotificationFragment()
        discussionFragment = fragmentFactory.createTalkInboxFragment()
        reviewFragment = fragmentFactory.createReviewInboxFragment()

        addPage(notificationFragment, context.getString(R.string.inbox_title_notification))
        addPage(chatFragment, context.getString(R.string.inbox_title_chat))
        addPage(discussionFragment, context.getString(R.string.inbox_title_discussion))
        addPage(reviewFragment, context.getString(R.string.inbox_title_review))
    }

    fun start(@InboxFragmentType page: Int) {
        val transaction = fm.beginTransaction()
        val fragment = getPageFragment(page)
        if (fm.fragments.isEmpty()) {
            addAllPages(fragment, transaction)
        }

        fragment?.let {
            showFragment(it, transaction)
            setSelectedPage(page)
        }
    }

    fun onPageSelected(@InboxFragmentType page: Int) {
        if (isActivityResumed() && !isCurrentlyOnThePage(page)) {
            showPage(page)
        } else {
            onPageClickedAgain(page)
        }
    }

    fun cleanupNavigator() {
        val transaction = fm.beginTransaction()
        fm.fragments.forEach {
            if (it.isAdded) {
                transaction.remove(it)
            }
        }
        transaction.commitAllowingStateLoss()
        pages.clear()
    }

    private fun showPage(page: Int) {
        val transaction = fm.beginTransaction()
        val fragment = getPageFragment(page)
        fragment?.let {
            showFragment(it, transaction)
            setSelectedPage(page)
        }
    }

    private fun onPageClickedAgain(page: Int) {
        val fragment = getFragmentOf(page) ?: return
        if (fragment is InboxFragment) {
            fragment.onPageClickedAgain()
        }
    }

    private fun isCurrentlyOnThePage(@InboxFragmentType page: Int): Boolean {
        val fragment = getFragmentOf(page) ?: return false
        val fragmentState = fragment.lifecycle.currentState
        return page == currentSelectedPage && fragmentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    fun notifyRoleChanged(@RoleType role: Int) {
        fm.fragments.forEach { fragment ->
            if (fragment is InboxFragment) {
                fragment.onRoleChanged(role)
            }
        }
    }

    private fun addAllPages(selectedPage: Fragment?, transaction: FragmentTransaction) {
        pages.keys.forEach {
            it?.let {
                val tag = it::class.java.canonicalName
                transaction.add(containerId, it, tag)

                if (it != selectedPage) {
                    transaction.setMaxLifecycle(it, Lifecycle.State.CREATED)
                }
            }
        }
    }

    private fun showFragment(fragment: Fragment, transaction: FragmentTransaction) {
        val tag = fragment::class.java.canonicalName
        val fragmentByTag = fm.findFragmentByTag(tag)
        val selectedFragment = fragmentByTag ?: fragment
        val currentState = selectedFragment.lifecycle.currentState
        val isFragmentNotResumed = !currentState.isAtLeast(Lifecycle.State.RESUMED)
        if (isFragmentNotResumed) {
            try {
                transaction.setMaxLifecycle(selectedFragment, Lifecycle.State.RESUMED)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        hideAllPages(transaction)
        transaction.show(selectedFragment)
                .commit()
    }

    private fun getFragmentOf(@InboxFragmentType type: Int): Fragment? {
        val fragment = getPageFragment(type) ?: return null
        val tag = fragment::class.java.canonicalName
        val fragmentByTag = fm.findFragmentByTag(tag)
        return fragmentByTag ?: fragment
    }

    private fun getPageFragment(@InboxFragmentType type: Int): Fragment? {
        return when (type) {
            InboxFragmentType.NOTIFICATION -> notificationFragment
            InboxFragmentType.CHAT -> chatFragment
            InboxFragmentType.DISCUSSION -> discussionFragment
            InboxFragmentType.REVIEW -> reviewFragment
            else -> null
        }
    }

    private fun addPage(fragment: Fragment?, title: String?) {
        fragment?.let { pages[it] = title }
    }

    private fun hideAllPages(transaction: FragmentTransaction) {
        fm.fragments.forEach { transaction.hide(it) }
    }

    private fun setSelectedPage(@InboxFragmentType page: Int) {
        currentSelectedPage = page
    }

    private fun isActivityResumed(): Boolean {
        val state = (context as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.RESUMED || state == Lifecycle.State.STARTED
    }
}