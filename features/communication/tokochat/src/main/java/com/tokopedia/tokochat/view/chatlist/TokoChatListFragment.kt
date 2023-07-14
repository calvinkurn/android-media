package com.tokopedia.tokochat.view.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokochat.databinding.TokochatChatlistFragmentBinding
import com.tokopedia.tokochat_common.view.chatlist.TokoChatListBaseFragment
import com.tokopedia.tokochat_common.view.chatlist.adapter.TokoChatListBaseAdapter
import com.tokopedia.tokochat_common.view.chatlist.listener.TokoChatListItemListener
import timber.log.Timber
import javax.inject.Inject

class TokoChatListFragment @Inject constructor(

):
    TokoChatListBaseFragment<TokochatChatlistFragmentBinding>(),
    TokoChatListItemListener {

    override var adapter: TokoChatListBaseAdapter = TokoChatListBaseAdapter(
        itemListener = this
    )

    override fun getScreenName(): String = TAG

    override fun initInjector() {}

    override fun getViewBindingInflate(container: ViewGroup?): TokochatChatlistFragmentBinding {
        return TokochatChatlistFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }

    override fun initObservers() {
        adapter.addItem("1")
        adapter.addItem("1")
        adapter.addItem("1")
        adapter.addItem("1")
        adapter.notifyDataSetChanged()
    }

    override fun onLoadMore() {
        adapter.addItem("1")
    }

    private fun notifyWhenAllowed(position: Int) {
        try {
            baseBinding?.tokochatListRv?.post {
                adapter.notifyItemChanged(position)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    override fun onClickChatItem(position: Int) {

    }

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
