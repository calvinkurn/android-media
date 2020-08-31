package com.tokopedia.topchat.chatroom.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.StickerViewHolder
import com.tokopedia.topchat.chatroom.view.custom.StickerRecyclerView
import com.tokopedia.topchat.chatroom.view.viewmodel.StickerViewModel
import javax.inject.Inject

class StickerFragment(
        contentLayoutId: Int,
        private val stickerListener: StickerViewHolder.Listener?
) : Fragment(contentLayoutId) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var stickerList: StickerRecyclerView? = null
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(StickerViewModel::class.java) }

    private var stickerGroupUID = ""
    private var stickerNeedUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArguments()
        initDagger()
        initObserver()
    }

    private fun initObserver() {
        viewModel.stickers.observe(this, Observer {
            it?.let {
                stickerList?.updateSticker(it)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewBind(view)
        initStickerListener()
        viewModel.loadStickers(stickerGroupUID, stickerNeedUpdate)
    }

    private fun initStickerListener() {
        stickerList?.stickerAdapter?.stickerListener = stickerListener
    }

    private fun initViewBind(view: View) {
        stickerList = view.findViewById(R.id.rv_sticker)
    }

    private fun initArguments() {
        stickerGroupUID = arguments?.getString(keyStickerGroupUID) ?: ""
        stickerNeedUpdate = arguments?.getBoolean(keyStickerNeedUpdate) ?: false
    }

    private fun initDagger() {
        if (activity != null && (activity as Activity).application != null) {
            context?.let {
                val chatComponent = DaggerChatComponent.builder()
                        .baseAppComponent(((activity as Activity).application as BaseMainApplication).baseAppComponent)
                        .chatRoomContextModule(ChatRoomContextModule(it))
                        .build()
                chatComponent.inject(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelAllJobs()
    }

    fun requestNewStickerList() {
        viewModel.loadStickers(stickerGroupUID, true)
    }

    companion object {
        private val LAYOUT = R.layout.fragment_sticker

        private const val keyStickerGroupUID = "stickerGroupUID"
        private const val keyStickerNeedUpdate = "stickerNeedUpdate"

        fun create(
                stickerGroup: StickerGroup,
                needUpdate: Boolean,
                stickerListener: StickerViewHolder.Listener?
        ): StickerFragment {
            val bundle = Bundle().apply {
                putString(keyStickerGroupUID, stickerGroup.groupUUID)
                putBoolean(keyStickerNeedUpdate, needUpdate)
            }
            return StickerFragment(LAYOUT, stickerListener).apply {
                arguments = bundle
            }
        }
    }
}