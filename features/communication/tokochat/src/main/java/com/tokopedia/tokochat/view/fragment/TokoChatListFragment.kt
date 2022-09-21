package com.tokopedia.tokochat.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokochat.databinding.FragmentTokoChatListBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoChatListFragment: BaseDaggerFragment() {

    private var binding: FragmentTokoChatListBinding? by autoClearedNullable()

    @Inject
    lateinit var viewModel: TokoChatViewModel

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokoChatListBinding.inflate(
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
        getComponent(TokoChatComponent::class.java).inject(this)
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
        private const val TAG = "TokoChatListFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): TokoChatListFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? TokoChatListFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                TokoChatListFragment::class.java.name
            ).apply {
                arguments = bundle
            } as TokoChatListFragment
        }
    }
}
