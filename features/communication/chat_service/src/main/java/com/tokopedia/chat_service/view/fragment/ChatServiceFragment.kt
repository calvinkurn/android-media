package com.tokopedia.chat_service.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.chat_service.databinding.FragmentChatServiceBinding
import com.tokopedia.chat_service.di.ChatServiceComponent
import com.tokopedia.chat_service.view.activity.ChatServiceListActivity
import com.tokopedia.chat_service.view.viewmodel.ChatServiceViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ChatServiceFragment: BaseDaggerFragment() {

    private var binding: FragmentChatServiceBinding? by autoClearedNullable()

    @Inject
    lateinit var viewModel: ChatServiceViewModel

    private var channelUrl: String = ""

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
        initViews()
        initObservers()
        setupDummyChatService()
    }

    override fun initInjector() {
        getComponent(ChatServiceComponent::class.java).inject(this)
    }

    private fun initViews() {
        AndroidThreeTen.init(context?.applicationContext)

        binding?.goBtn?.setOnClickListener {
            viewModel.getChatHistory(channelUrl).removeObservers(viewLifecycleOwner)
            viewModel.deRegisterActiveChannel(channelUrl)
            initObservers()
            setupDummyChatService()
        }

        binding?.mainBtn?.setOnClickListener {
            val message = binding?.mainEdt?.text.toString()
            binding?.mainEdt?.setText("")
            viewModel.sendMessage(message, channelUrl)
        }

        binding?.clearBtn?.setOnClickListener {
            binding?.mainTv?.text = ""
        }

        binding?.loadmoreBtn?.setOnClickListener {
            viewModel.loadPreviousMessages()
        }

        binding?.chatListBtn?.setOnClickListener {
            context?.let {
                startActivity(Intent(it, ChatServiceListActivity::class.java))
            }
        }

        binding?.mainEdt?.addTextChangedListener(getTextWatcherListener())
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            it.printStackTrace()
        }

        viewModel.getTypingStatus().observe(viewLifecycleOwner) {
            binding?.typingStatusTv?.text = it.toString()
        }

        viewModel.isChatConnected.observe(viewLifecycleOwner) {
            val connectionString = if (it) {
                "Chat connected"
            } else {
                "Chat disconnected"
            }
            binding?.connectionStatusTv?.text = connectionString
        }

        viewModel.getTotalUnreadCount().observe(viewLifecycleOwner) {
            binding?.counterTv?.text = it.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.deRegisterActiveChannel(channelUrl)
    }

    private fun setupDummyChatService() {
        val orderId = getOrderIdOrDefault()
        if (orderId.isNotEmpty()) {
            viewModel.resetTypingStatus()
            viewModel.initGroupBooking(
                orderId,
                2,
                getGroupBookingListener(),
                OrderChatType.Driver
            )
        }
    }

    private fun getOrderIdOrDefault(): String {
        val text = binding?.orderIdEdt?.text
        return if (text.isNullOrEmpty()) {
            "F-176219770"
        } else {
            text.toString()
        }
    }

    private fun getGroupBookingListener(): ConversationsGroupBookingListener {
        return object : ConversationsGroupBookingListener {
            override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
                view?.let {
                    var errorMessage = error.getErrorMessage()
                    if (errorMessage.isEmpty()) {
                        errorMessage = error.toString()
                    }
                    Toaster.build(
                        it, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR
                    ).show()
                }
            }

            override fun onGroupBookingChannelCreationStarted() {}

            override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
                this@ChatServiceFragment.channelUrl = channelUrl
                viewModel.registerActiveChannel(channelUrl)
                viewModel.doCheckChatConnection()
                observeChatHistory(channelUrl)
            }

        }
    }

    private fun observeChatHistory(channelUrl: String) {
        viewModel.getChatHistory(channelUrl).observe(viewLifecycleOwner) {
            activity?.runOnUiThread {
                binding?.mainTv?.text = ""
                var messageText = ""
                for (message in it) {
                    messageText += "${message.messageSender?.userName}:\n ${message.messageText} - ${message.readReceipt}\n\n"
                }
                binding?.mainTv?.text = messageText
            }
            viewModel.markChatAsRead(channelUrl)
        }
    }

    private fun getTextWatcherListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    viewModel.setTypingStatus(true)
                } else {
                    viewModel.setTypingStatus(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        }
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
