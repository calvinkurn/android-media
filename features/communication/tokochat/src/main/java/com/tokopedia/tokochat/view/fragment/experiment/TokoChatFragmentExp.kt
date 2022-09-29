package com.tokopedia.tokochat.view.fragment.experiment

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
import com.tokopedia.tokochat.databinding.FragmentTokoChatExpBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.view.activity.TokoChatListActivity
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.tokochat_common.view.adapter.TokoChatBaseAdapter
import com.tokopedia.tokochat_common.view.fragment.TokoChatBaseFragment
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

//TODO: Delete this after experiment
class TokoChatFragmentExp: TokoChatBaseFragment<FragmentTokoChatExpBinding>() {

    @Inject
    lateinit var viewModel: TokoChatViewModel

    override var adapter: TokoChatBaseAdapter = TokoChatBaseAdapter()

    private var channelUrl: String = ""

    override fun getScreenName(): String = TAG

    override fun getViewBindingInflate(container: ViewGroup?): FragmentTokoChatExpBinding {
        return FragmentTokoChatExpBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDummyChatService()
    }

    override fun initInjector() {
        getComponent(TokoChatComponent::class.java).inject(this)
    }

    override fun additionalSetup() {
        AndroidThreeTen.init(context?.applicationContext)
        val userId = viewModel.getUserId()
        if (userId.isEmpty()) {
            viewModel.initializeProfile()
        }
    }

    override fun initViews() {
        super.initViews()
        binding?.goBtn?.setOnClickListener {
            viewModel.getChatHistory(channelUrl).removeObservers(viewLifecycleOwner)
            viewModel.deRegisterActiveChannel(channelUrl)
            initObservers()
            setupDummyChatService()
        }

        binding?.mainBtn?.setOnClickListener {
            val message = binding?.mainEdt?.text.toString()
            binding?.mainEdt?.setText("")
            viewModel.sendMessage(channelUrl, message)
        }

        binding?.clearBtn?.setOnClickListener {
            binding?.mainTv?.text = ""
        }

        binding?.loadmoreBtn?.setOnClickListener {
            viewModel.loadPreviousMessages()
        }

        binding?.chatListBtn?.setOnClickListener {
            context?.let {
                startActivity(Intent(it, TokoChatListActivity::class.java))
            }
        }

        binding?.mainEdt?.addTextChangedListener(getTextWatcherListener())
    }

    override fun initObservers() {
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
            "RB-186701-4154028"
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
                this@TokoChatFragmentExp.channelUrl = channelUrl
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
//            viewModel.markChatAsRead(channelUrl)
        }
    }

    private fun getTextWatcherListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
//                    viewModel.setTypingStatus(true)
                } else {
//                    viewModel.setTypingStatus(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        }
    }

    companion object {
        private const val TAG = "TokoChatFragmentExp"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): TokoChatFragmentExp {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? TokoChatFragmentExp
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                TokoChatFragmentExp::class.java.name
            ).apply {
                arguments = bundle
            } as TokoChatFragmentExp
        }
    }
}
