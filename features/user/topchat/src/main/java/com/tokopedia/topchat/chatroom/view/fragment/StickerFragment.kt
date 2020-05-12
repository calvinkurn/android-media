package com.tokopedia.topchat.chatroom.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import kotlinx.android.synthetic.main.fragment_sticker.view.*

class StickerFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.tp_sticker_uid?.text = arguments?.getString(keyStickerGroupUID)
    }

    companion object {
        private val LAYOUT = R.layout.fragment_sticker

        private const val keyStickerGroupUID = "stickerGroupUID"

        fun create(stickerGroup: StickerGroup): StickerFragment {
            val bundle = Bundle().apply {
                putString(keyStickerGroupUID, stickerGroup.groupUUID)
            }
            return StickerFragment(LAYOUT).apply {
                arguments = bundle
            }
        }
    }
}