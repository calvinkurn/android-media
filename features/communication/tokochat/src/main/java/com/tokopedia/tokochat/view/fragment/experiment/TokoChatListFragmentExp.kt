package com.tokopedia.tokochat.view.fragment.experiment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokochat.databinding.FragmentTokoChatListExpBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.tokochat_common.view.adapter.BaseTokoChatAdapter
import com.tokopedia.tokochat_common.view.fragment.BaseTokoChatFragment
import javax.inject.Inject

//TODO: Delete this after experiment
class TokoChatListFragmentExp: BaseTokoChatFragment<FragmentTokoChatListExpBinding>() {

    @Inject
    lateinit var viewModel: TokoChatViewModel

    override var adapter: BaseTokoChatAdapter = BaseTokoChatAdapter()

    override fun getScreenName(): String = TAG

    override fun getViewBindingInflate(container: ViewGroup?): FragmentTokoChatListExpBinding {
        return FragmentTokoChatListExpBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }

    override fun additionalSetup() {

    }

    override fun initObservers() {
        viewModel.getAllChannels().observe(viewLifecycleOwner) {
            binding?.mainTv?.text = ""
            var messageText = ""
            for (channel in it) {
                messageText += "${channel.name} - ${channel.id}\n ${channel.lastMessage?.message} \n ${channel.lastRead.toString()}\n\n"
            }
            binding?.mainTv?.text = messageText
        }
    }

    override fun initInjector() {
        getComponent(TokoChatComponent::class.java).inject(this)
    }

    companion object {
        private const val TAG = "TokoChatListFragmentExp"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): TokoChatListFragmentExp {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? TokoChatListFragmentExp
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                TokoChatListFragmentExp::class.java.name
            ).apply {
                arguments = bundle
            } as TokoChatListFragmentExp
        }
    }
}
