package com.tokopedia.tokochat.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.databinding.FragmentTokoChatBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.view.activity.TokoChatActivity
import com.tokopedia.tokochat.view.uimodel.TokoChatHeaderUiModel
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.tokochat_common.util.ValueUtil
import com.tokopedia.tokochat_common.view.fragment.BaseTokoChatFragment
import com.tokopedia.tokochat_common.view.adapter.BaseTokoChatAdapter
import com.tokopedia.tokochat_common.view.listener.TokoChatReplyTextListener
import com.tokopedia.tokochat_common.view.listener.TokoChatTypingListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.tokochat_common.view.uimodel.MessageBubbleUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoChatFragment: BaseTokoChatFragment<FragmentTokoChatBinding>(), TokoChatTypingListener, TokoChatReplyTextListener {

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
        setupBackground()
        setupToolbarData()
        setupReplySection(true)
        setupReceiverDummyMessages()
    }

    private fun renderBackground(url: String) {
        baseBinding?.tokochatIvBgChat?.let {
            Glide.with(it.context)
                .load(url)
                .centerInside()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(it)
        }
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
        observeTokoChatBackground()
    }

    override fun disableSendButton(isExceedLimit: Boolean) {

    }

    override fun enableSendButton() {

    }

    override fun onStartTyping() {

    }

    override fun onStopTyping() {

    }

    override fun getViewBindingInflate(container: ViewGroup?): FragmentTokoChatBinding {
        return FragmentTokoChatBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }

    private fun setupBackground() {
        viewModel.getTokoChatBackground()
    }

    private fun observeTokoChatBackground() {
        observe(viewModel.chatBackground) {
            when (it) {
                is Success -> renderBackground(it.data)
                is Fail -> {
                    //no op
                }
            }
        }
    }

    private fun setupToolbarData() {
        val uiModel = TokoChatHeaderUiModel(
            title = "Omar Maryadi",
            subTitle = "D7088FGX",
            imageUrl = "https://i-integration.gojekapi.com/darkroom/gomart-public-integration/v2/images/public/images/f9054d3d-7346-4b39-8385-61f6dfa81874_pertamax-icon.jpg",
            phoneNumber = "08123456789"
        )
        (activity as? TokoChatActivity)?.getToolbar()?.run {
            val userTitle = findViewById<Typography>(R.id.tokochat_text_user_title)
            val subTitle = findViewById<Typography>(R.id.tokochat_text_user_subtitle)
            val imageUrl = findViewById<ImageUnify>(R.id.tokochat_user_avatar)
            val callMenu = findViewById<IconUnify>(R.id.tokochat_icon_header_menu)

            userTitle.text = uiModel.title
            subTitle.text = uiModel.subTitle
            imageUrl.setImageUrl(uiModel.imageUrl)

            callMenu.run {
                setImage(IconUnify.CALL)
                setOnClickListener {

                }
            }
        }
    }

    private fun setupReplySection(isShowReplySection: Boolean) {
        baseBinding?.tokochatReplyBox?.run {
            shouldShowWithAction(isShowReplySection) {
                this.initLayout(this@TokoChatFragment, this@TokoChatFragment)
            }
        }
        baseBinding?.tokochatExpiredInfo?.showWithCondition(!isShowReplySection)
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
}
