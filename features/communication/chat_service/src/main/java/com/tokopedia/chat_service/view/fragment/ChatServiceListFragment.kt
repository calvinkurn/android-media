package com.tokopedia.chat_service.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.chat_service.databinding.FragmentChatServiceListBinding
import com.tokopedia.chat_service.di.ChatServiceComponent
import com.tokopedia.chat_service.view.viewmodel.ChatServiceViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ChatServiceListFragment: BaseDaggerFragment() {

    private var binding: FragmentChatServiceListBinding? by autoClearedNullable()

    @Inject
    lateinit var viewModel: ChatServiceViewModel

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatServiceListBinding.inflate(
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
    }

    override fun initInjector() {
        getComponent(ChatServiceComponent::class.java).inject(this)
    }

    private fun initViews() {}

    private fun initObservers() {
        viewModel.getAllChannels().observe(viewLifecycleOwner) {
            binding?.mainTv?.text = ""
            var messageText = ""
            for (channel in it) {
                messageText += "${channel.name} - ${channel.id}\n ${channel.lastMessage?.message} \n ${channel.lastRead.toString()}\n\n"
            }
            binding?.mainTv?.text = messageText
        }
    }

    companion object {
        private const val TAG = "ChatServiceListFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): ChatServiceListFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ChatServiceListFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ChatServiceListFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ChatServiceListFragment
        }
    }
}
