package com.tokopedia.topchat.chatroom.view.adapter

import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.view.adapter.util.StickerGroupDiffUtil
import com.tokopedia.topchat.chatroom.view.fragment.StickerFragment

class StickerFragmentStateAdapter constructor(
        private val fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private var stickers: List<StickerGroup> = emptyList()
    private var stickerFragment: ArrayMap<String, StickerFragment> = ArrayMap()
    private var needToUpdateSticker: HashSet<String> = HashSet()

    override fun getItemCount(): Int {
        return if (stickers.isNotEmpty()) 1 else 0
    }

    override fun createFragment(position: Int): Fragment {
        val sticker = stickers[position]
        val fragmentId = sticker.groupUUID
        val needUpdate = needToUpdateSticker.contains(fragmentId)
        val fragment = StickerFragment.create(sticker, needUpdate)
        stickerFragment[fragmentId] = fragment
        return fragment
    }

    fun updateStickers(stickers: List<StickerGroup>, needToUpdate: List<StickerGroup>) {
        val diffUtil = StickerGroupDiffUtil(this.stickers, stickers)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.stickers = stickers
        diffResult.dispatchUpdatesTo(this)
        if (needToUpdate.isNotEmpty()) {
            dispatchReUpdateStickerEvent(needToUpdate)
        }
    }

    private fun dispatchReUpdateStickerEvent(needToUpdate: List<StickerGroup>) {
        for (latestSticker in needToUpdate) {
            needToUpdateSticker.add(latestSticker.groupUUID)
            val fragment = stickerFragment[latestSticker.groupUUID] ?: continue
            if (!fragment.isDetached or !fragment.isRemoving) {
                fragment.requestNewStickerList()
            }
        }
    }
}