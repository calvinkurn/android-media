package com.tokopedia.topchat.chatlist.view.adapter

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import java.util.*

/**
 * @author : Steven 2019-08-06
 */
class ChatListPagerAdapter(fm: FragmentManager)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var itemList: MutableList<ChatListTab> = ArrayList()

    override fun getItem(position: Int): Fragment {
        if(position >= itemList.size) {
            return Fragment()
        }
        return itemList[position].fragment
    }

    override fun getCount() = itemList.size

    fun setItemList(tabList: ArrayList<ChatListTab>) {
        itemList = tabList
        notifyDataSetChanged()
    }

    data class ChatListTab(
        val title: String,
        val fragment: Fragment,
        val icon: Drawable?,
        var counter: String = "0"
    ) {
        fun increaseTabCounter() {
            var count = counter.toIntOrNull()
            count?.let {
                count += 1
                counter = count.toString()
            }
        }

        fun decreaseTabCounter() {
            var count = counter.toIntOrZero()
            count -= 1
            if (count < 0) {
                count = 0
            }
            counter = count.toString()
        }
    }
}