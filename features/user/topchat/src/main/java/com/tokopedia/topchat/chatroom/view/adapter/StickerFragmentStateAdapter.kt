package com.tokopedia.topchat.chatroom.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.view.adapter.util.StickerGroupDiffUtil
import com.tokopedia.topchat.chatroom.view.fragment.StickerFragment

class StickerFragmentStateAdapter(
        private val fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private var stickers: List<StickerGroup> = emptyList()

    override fun getItemCount(): Int {
        return if (stickers.isNotEmpty()) 1 else 0
    }

    override fun createFragment(position: Int): Fragment {
        return StickerFragment.create(stickers[position])
    }

    fun updateStickers(stickers: List<StickerGroup>, isExpired: Boolean) {
        val diffUtil = StickerGroupDiffUtil(this.stickers, stickers)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.stickers = stickers
        diffResult.dispatchUpdatesTo(this)
    }
}