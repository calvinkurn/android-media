@file:SuppressLint("NotifyDataSetChanged")

package com.tokopedia.editor.ui.main.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.editor.base.BaseEditorFragment

class EditorPagerStateAdapter constructor(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    var fragments = mutableListOf<BaseEditorFragment>()
        private set

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): BaseEditorFragment {
        return fragments[position]
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.any {
            it.hashCode().toLong() == itemId
        }
    }

    fun addSingleFragment(fragment: BaseEditorFragment) {
        fragments.clear()
        addFragment(fragment)
    }

    fun addFragment(fragment: BaseEditorFragment) {
        fragments.add(fragment)
        notifyDataSetChanged()
    }

    fun addFragments(mFragments: List<BaseEditorFragment>) {
        fragments.clear()
        fragments.addAll(mFragments)
        notifyDataSetChanged()
    }

    fun removeFragment(position: Int) {
        fragments.removeAt(position)
        notifyDataSetChanged()
    }
}

