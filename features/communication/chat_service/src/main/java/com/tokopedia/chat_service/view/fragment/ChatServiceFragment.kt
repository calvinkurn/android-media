package com.tokopedia.chat_service.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.chat_service.databinding.FragmentChatServiceBinding
import com.tokopedia.chat_service.di.ChatServiceComponent
import com.tokopedia.chat_service.view.viewmodel.ChatServiceViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
        viewModel.createChannel(
            name = "Kelvin",
            memberIds = listOf("1", "2"),
            type = "personal",
            source = "tokopedia-chatservice"
        )
    }

    private fun initObservers() {
        viewModel.conversationsChannel.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding?.mainTv?.text = it.data.toString()
                    viewModel.getChatHistory(it.data.url)
                }
                is Fail -> {
                    it.throwable.printStackTrace()
                }
            }
        }

        viewModel.conversationsMessage.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    Log.d(TAG, it.data.toString())
                }
                is Fail -> {
                    it.throwable.printStackTrace()
                }
            }
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