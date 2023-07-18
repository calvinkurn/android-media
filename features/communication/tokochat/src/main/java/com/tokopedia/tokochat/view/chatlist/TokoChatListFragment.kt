package com.tokopedia.tokochat.view.chatlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.tokochat.analytics.TokoChatAnalytics
import com.tokopedia.tokochat.common.view.chatlist.TokoChatListBaseFragment
import com.tokopedia.tokochat.common.view.chatlist.adapter.TokoChatListBaseAdapter
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel.Companion.getServiceTypeName
import com.tokopedia.tokochat.databinding.TokochatChatlistFragmentBinding
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoChatListFragment @Inject constructor(
    private val viewModel: TokoChatListViewModel
) :
    TokoChatListBaseFragment<TokochatChatlistFragmentBinding>(),
    TokoChatListItemListener {

    override var adapter: TokoChatListBaseAdapter = TokoChatListBaseAdapter(
        itemListener = this
    )

    private var tokoChatAnalytics = TokoChatAnalytics()

    override fun getScreenName(): String = TAG

    override fun initInjector() {}

    override fun getViewBindingInflate(container: ViewGroup?): TokochatChatlistFragmentBinding {
        return TokochatChatlistFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChatListData()
    }

    private fun initChatListData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                var isCompleted = false
                viewModel.getChatListFlow().collectLatest {
                    when (it) {
                        is Success -> {
                            setChatListData(it.data)
                            if (!isCompleted) {
                                viewModel.loadNextPageChatList(it.data.size)
                                isCompleted = true
                            }
                        }
                        is Fail -> {
                            Toaster.build(
                                requireView(),
                                it.throwable.toString(),
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun initObservers() {
        viewModel.chatListPair.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                tokoChatAnalytics.viewDriverChatList(it)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Log.d("TOKOCHAT-LIST", it.first.stackTraceToString())
        }
    }

    private fun setChatListData(newData: List<TokoChatListItemUiModel>) {
        adapter.setItemsAndAnimateChanges(newData)
    }

    override fun onLoadMore() {
        viewModel.loadNextPageChatList()
    }

    override fun onClickChatItem(element: TokoChatListItemUiModel) {
        val serviceName = getServiceTypeName(element.serviceType)
        tokoChatAnalytics.clickDriverChatList(
            gojekOrderId = element.orderId,
            serviceTypeName = serviceName,
            counter = element.counter,
            chatDuration = element.getRelativeTime()
        )
        context?.let {
            val source = "?${ApplinkConst.TokoChat.PARAM_SOURCE}=$serviceName"
            val paramOrderIdGojek = "&${ApplinkConst.TokoChat.ORDER_ID_GOJEK}=${element.orderId}"
            val applink = "${ApplinkConstInternalCommunication.TOKO_CHAT}$source$paramOrderIdGojek"
            val intent = RouteManager.getIntent(it, applink)
            startActivity(intent)
        }
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
