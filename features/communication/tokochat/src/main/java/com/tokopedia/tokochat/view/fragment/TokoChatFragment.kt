package com.tokopedia.tokochat.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.tokochat.databinding.FragmentTokoChatBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.view.mapper.TokoChatConversationMapper.mapToMessageBubbleUi
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.tokochat_common.view.fragment.TokoChatBaseFragment
import com.tokopedia.tokochat_common.view.adapter.TokoChatBaseAdapter
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class TokoChatFragment: TokoChatBaseFragment<FragmentTokoChatBinding>() {

    @Inject
    lateinit var viewModel: TokoChatViewModel

    private var channelUrl = ""

    override var adapter: TokoChatBaseAdapter = TokoChatBaseAdapter()

    override fun getScreenName(): String = TAG

    override fun initInjector() {
        getComponent(TokoChatComponent::class.java).inject(this)
    }

    override fun additionalSetup() {
        AndroidThreeTen.init(context?.applicationContext)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        initializeChatProfile()
        initializeChatProfile()
        initGroupBooking(savedInstanceState)
    }

    override fun initObservers() {

    }

    //TODO: Replace this with updated SDK
    private fun initializeChatProfile() {
        val userId = viewModel.getUserId()
        if (userId.isEmpty()) {
            viewModel.initializeProfile()
        }
    }

    private fun initGroupBooking(savedInstanceState: Bundle?) {
        val gojekOrderId = getParamString(
            ApplinkConst.TokoChat.ORDER_ID_GOJEK,
            arguments,
            savedInstanceState
        )
        viewModel.resetTypingStatus()
        viewModel.initGroupBooking(
            orderId = gojekOrderId,
            groupBookingListener = getGroupBookingListener()
        )
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
                this@TokoChatFragment.channelUrl = channelUrl
                viewModel.registerActiveChannel(channelUrl)
                observeChatHistory()
            }

        }
    }

    private fun observeChatHistory() {
        viewModel.getChatHistory(channelUrl).observe(viewLifecycleOwner) {
            val result = it.mapToMessageBubbleUi(viewModel.getUserId())
            adapter.addItems(result)
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val TAG = "TokoChatFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): TokoChatFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? TokoChatFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                TokoChatFragment::class.java.name
            ).apply {
                arguments = bundle
            } as TokoChatFragment
        }
    }

    override fun getViewBindingInflate(container: ViewGroup?): FragmentTokoChatBinding {
        return FragmentTokoChatBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }
}
