package com.tokopedia.tokochat.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.tokochat.databinding.FragmentTokoChatBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.tokochat_common.util.ValueUtil
import com.tokopedia.tokochat_common.view.fragment.BaseTokoChatFragment
import com.tokopedia.tokochat_common.view.adapter.BaseTokoChatAdapter
import com.tokopedia.tokochat_common.view.uimodel.MessageBubbleUiModel
import javax.inject.Inject

class TokoChatFragment: BaseTokoChatFragment<FragmentTokoChatBinding>() {

    @Inject
    lateinit var viewModel: TokoChatViewModel

    override var adapter: BaseTokoChatAdapter = BaseTokoChatAdapter()

    override fun getScreenName(): String = TAG

    override fun initInjector() {
        getComponent(TokoChatComponent::class.java).inject(this)
    }

    override fun additionalSetup() {
        AndroidThreeTen.init(context?.applicationContext)
    }

    override fun initViews() {
        super.initViews()
        setupReceiverDummyMessages()
        setupSenderDummyMessages()
    }

    private fun setupSenderDummyMessages() {
        val message = MessageBubbleUiModel.Builder()
            .withStartTime("")
            .withIsSender(true)
            .withIsRead(true)
            .withIsDummy(false)
            .withMsgId("123")
            .withBubbleStatus(ValueUtil.STATUS_NORMAL)
            .withFromUid("123")
            .withFromRole("User")
            .withReplyTime("123123123")
            .withMsg("Halooo")
            .withFraudStatus(0)
            .withLabel("Label")
            .build()
        adapter.addItem(message)
        adapter.notifyItemInserted(adapter.itemCount)

        val deletedMessage = MessageBubbleUiModel.Builder()
            .withStartTime("")
            .withIsSender(true)
            .withIsRead(true)
            .withIsDummy(false)
            .withMarkAsDeleted()
            .build()
        adapter.addItem(deletedMessage)
        adapter.notifyItemInserted(adapter.itemCount)

        val bannedMessage = MessageBubbleUiModel.Builder()
            .withStartTime("")
            .withIsSender(true)
            .withIsRead(true)
            .withIsDummy(false)
            .withFraudStatus(1)
            .build()
        adapter.addItem(bannedMessage)
        adapter.notifyItemInserted(adapter.itemCount)
    }

    private fun setupReceiverDummyMessages() {
        val message = MessageBubbleUiModel.Builder()
            .withStartTime("")
            .withIsSender(false)
            .withIsRead(false)
            .withIsDummy(false)
            .withMsgId("123")
            .withBubbleStatus(ValueUtil.STATUS_NORMAL)
            .withFromUid("123")
            .withFromRole("User")
            .withReplyTime("123123123")
            .withMsg("Halooo")
            .withFraudStatus(0)
            .withLabel("Label")
            .build()
        adapter.addItem(message)
        adapter.notifyItemInserted(adapter.itemCount)

        val deletedMessage = MessageBubbleUiModel.Builder()
            .withStartTime("")
            .withIsSender(false)
            .withIsRead(true)
            .withIsDummy(false)
            .withMarkAsDeleted()
            .build()
        adapter.addItem(deletedMessage)
        adapter.notifyItemInserted(adapter.itemCount)

        val bannedMessage = MessageBubbleUiModel.Builder()
            .withStartTime("")
            .withIsSender(false)
            .withIsRead(true)
            .withIsDummy(false)
            .withFraudStatus(1)
            .build()
        adapter.addItem(bannedMessage)
        adapter.notifyItemInserted(adapter.itemCount)
    }

    override fun initObservers() {

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
