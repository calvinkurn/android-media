package com.tokopedia.topchat.chatroom.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.StickerViewHolder
import com.tokopedia.topchat.chatroom.view.custom.StickerRecyclerView
import com.tokopedia.topchat.chatroom.view.viewmodel.StickerViewModel
import javax.inject.Inject

class StickerFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var stickerListener: StickerViewHolder.Listener? = null
    private var stickerList: StickerRecyclerView? = null
    private val viewModelFragmentProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelFragmentProvider.get(StickerViewModel::class.java)
    }

    private var stickerGroupUID = ""
    private var stickerNeedUpdate = false

    interface Listener {
        fun getStickerViewHolderListener(): StickerViewHolder.Listener?
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            stickerListener = context.getStickerViewHolderListener()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                LAYOUT, container, false
        ).also {
            initArguments()
            initObserver()
        }
    }

    override fun getScreenName(): String {
        return "sticker-chatroom"
    }

    override fun initInjector() {
        getComponent(ChatComponent::class.java).inject(this)
    }

    private fun initObserver() {
        viewModel.stickers.observe(viewLifecycleOwner, Observer {
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
        ): StickerFragment {
            val bundle = Bundle().apply {
                putString(keyStickerGroupUID, stickerGroup.groupUUID)
                putBoolean(keyStickerNeedUpdate, needUpdate)
            }
            return StickerFragment().apply {
                arguments = bundle
            }
        }
    }
}