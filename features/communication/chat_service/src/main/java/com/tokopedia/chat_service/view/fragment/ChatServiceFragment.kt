package com.tokopedia.chat_service.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.channel.GetChannelRequest
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.chat_service.databinding.FragmentChatServiceBinding
import com.tokopedia.chat_service.di.ChatServiceComponent
import com.tokopedia.chat_service.view.viewmodel.ChatServiceViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ChatServiceFragment: BaseDaggerFragment() {

    private var binding: FragmentChatServiceBinding? by autoClearedNullable()

    @Inject
    lateinit var viewModel: ChatServiceViewModel

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatServiceBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setupDummyChatService()
    }

    override fun initInjector() {
        getComponent(ChatServiceComponent::class.java).inject(this)
    }

    private fun setupDummyChatService() {
        AndroidThreeTen.init(context?.applicationContext)
        viewModel.createChannel(
            name = "Omar Maryadi",
            memberIds = listOf("942317400"),
            type = "REGULAR",
            source = ""
        )
//        viewModel.initGroupBooking(
//            "RB-133978-9720561",
//            1,
//            OrderChatType.Driver
//        )

//        viewModel.getAllChannels(
//            GetChannelRequest(
//                types = listOf(ChannelType.GroupBooking),
//                batchSize = 10,
//                timestamp = System.currentTimeMillis(),
//                shouldRetry = false
//            )
//        )

//        viewModel.loadPreviousMessages()
    }

    private fun initObservers() {
        viewModel.conversationsMessage.observe(viewLifecycleOwner) {
            Log.d(TAG, it.toString())
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Log.d(TAG, it.message.toString())
            it.printStackTrace()
        }

//        viewModel.getAllChannels().observe(viewLifecycleOwner) {
//            Log.d(TAG, it.toString())
//        }
    }

    override fun onStop() {
        super.onStop()
//        viewModel.deRegisterActiveChannel()
    }

    companion object {
        private const val TAG = "ChatServiceFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): ChatServiceFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ChatServiceFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ChatServiceFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ChatServiceFragment
        }
    }
}