package com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet.bubbleawareness

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TokoChatBubblesAwarenessAdapter(
    fragmentActivity: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentList: List<Fragment>
): FragmentStateAdapter(fragmentActivity, lifecycle) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
