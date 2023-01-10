package com.tokopedia.tokochat.stub.view

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokochat.view.chatroom.TokoChatFragment

class TokoChatFragmentStub: TokoChatFragment() {

    companion object {
        private const val TAG = "TokoChatFragmentStub"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
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
}
