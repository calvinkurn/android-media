package com.tokopedia.tokochat.view.chatlist

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokochat.databinding.TokochatChatlistFragmentBinding
import com.tokopedia.tokochat_common.view.chatlist.TokoChatListBaseFragment
import javax.inject.Inject

class TokoChatListFragment @Inject constructor(

): TokoChatListBaseFragment<TokochatChatlistFragmentBinding>() {
    override fun getScreenName(): String = TAG

    override fun initInjector() {}

    companion object {
        private const val TAG = "TokoChatListFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
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
