package com.tokopedia.tokochat.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.databinding.FragmentTokoChatBinding
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.view.activity.TokoChatActivity
import com.tokopedia.tokochat.view.bottomsheet.MaskingPhoneNumberBottomSheet
import com.tokopedia.tokochat.view.uimodel.TokoChatHeaderUiModel
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.tokochat_common.util.TokoChatImageUrl
import com.tokopedia.tokochat_common.util.TokoChatSource
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.tokochat_common.view.fragment.TokoChatBaseFragment
import com.tokopedia.tokochat_common.view.adapter.TokoChatBaseAdapter
import com.tokopedia.tokochat_common.view.customview.TokoChatReplyMessageView
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleBaseUiModel
import com.tokopedia.tokochat_common.view.listener.TokoChatReplyTextListener
import com.tokopedia.tokochat_common.view.listener.TokoChatTypingListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoChatFragment: TokoChatBaseFragment<FragmentTokoChatBinding>(), TokoChatTypingListener, TokoChatReplyTextListener {

    @Inject
    lateinit var viewModel: TokoChatViewModel

    private var source: String? = null

    override var adapter: TokoChatBaseAdapter = TokoChatBaseAdapter()

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
        setupToolbarData("")
        setupReplySection(true, getString(com.tokopedia.tokochat_common.R.string.tokochat_message_closed_chat))
        setupReceiverDummyMessages()
    }

    private fun setDataFromArguments() {
        source = arguments?.getString(ApplinkConst.TokoChat.PARAM_SOURCE)
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

    private fun setupSenderDummyMessages() {
        val message = TokoChatMessageBubbleBaseUiModel.Builder()
            .withStartTime("")
            .withIsSender(true)
            .withIsRead(true)
            .withIsDummy(false)
            .withMsgId("123")
            .withBubbleStatus(TokoChatValueUtil.STATUS_NORMAL)
            .withFromUid("123")
            .withFromRole("User")
            .withReplyTime("123123123")
            .withMsg("Halooo")
            .withFraudStatus(0)
            .withLabel("Label")
            .build()
        adapter.addItem(message)
        adapter.notifyItemInserted(adapter.itemCount)

        val deletedMessage = TokoChatMessageBubbleBaseUiModel.Builder()
            .withStartTime("")
            .withIsSender(true)
            .withIsRead(true)
            .withIsDummy(false)
            .withMarkAsDeleted()
            .build()
        adapter.addItem(deletedMessage)
        adapter.notifyItemInserted(adapter.itemCount)

        val bannedMessage = TokoChatMessageBubbleBaseUiModel.Builder()
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
        val message = TokoChatMessageBubbleBaseUiModel.Builder()
            .withStartTime("")
            .withIsSender(false)
            .withIsRead(false)
            .withIsDummy(false)
            .withMsgId("123")
            .withBubbleStatus(TokoChatValueUtil.STATUS_NORMAL)
            .withFromUid("123")
            .withFromRole("User")
            .withReplyTime("123123123")
            .withMsg("Halooo")
            .withFraudStatus(0)
            .withLabel("Label")
            .build()
        adapter.addItem(message)
        adapter.notifyItemInserted(adapter.itemCount)

        val deletedMessage = TokoChatMessageBubbleBaseUiModel.Builder()
            .withStartTime("")
            .withIsSender(false)
            .withIsRead(true)
            .withIsDummy(false)
            .withMarkAsDeleted()
            .build()
        adapter.addItem(deletedMessage)
        adapter.notifyItemInserted(adapter.itemCount)

        val bannedMessage = TokoChatMessageBubbleBaseUiModel.Builder()
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
        observeChatRoomTicker()
    }

    override fun disableSendButton(isExceedLimit: Boolean) {
        val chatSendButton = getChatSendButton()
        chatSendButton?.background =
            context?.let { ContextCompat.getDrawable(it, com.tokopedia.tokochat_common.R.drawable.bg_tokochat_send_btn_disabled) }
        chatSendButton?.setOnClickListener {
            if (!isExceedLimit) {
                showSnackbarError(getString(com.tokopedia.tokochat_common.R.string.tokochat_desc_empty_text_box))
            }
        }
    }

    override fun enableSendButton() {
        val chatSendButton = getChatSendButton()
        chatSendButton?.background = context?.let { ContextCompat.getDrawable(it, com.tokopedia.tokochat_common.R.drawable.bg_tokochat_send_btn) }
        chatSendButton?.setOnClickListener {
            onSendButtonClicked()
        }
    }

    private fun getChatSendButton(): IconUnify? {
        return baseBinding?.tokochatReplyBox?.findViewById(com.tokopedia.tokochat_common.R.id.tokochat_ic_send_btn)
    }

    fun onSendButtonClicked() {
        //todo sent chat
    }

    fun showSnackbarError(message: String) {
        view?.let {
            Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    override fun onStartTyping() {
        //todo call start typing by viewmodel
    }

    override fun onStopTyping() {
        //todo call stop typing by viewmodel
    }

    private fun isValidComposedMessage(): Boolean {
        val message = getComposedMessage()
        return message.isNotBlank()
    }

    private fun getComposedMessage(message: String? = null): String {
        return if (message != null && message.isNotEmpty()) {
            message
        } else {
            getComposeMessageArea()?.getComposedText() ?: ""
        }
    }

    private fun getComposeMessageArea(): TokoChatReplyMessageView? {
        return baseBinding?.tokochatReplyBox
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

    private fun observeChatRoomTicker() {
        observe(viewModel.chatRoomTicker) {
            when (it) {
                is Success -> {
                    //TODO will adjust by kelvin
                }
                is Fail -> {
                    //no op
                }
            }
        }
    }

    private fun setupToolbarData(driverPhoneNumber: String) {
        val uiModel = TokoChatHeaderUiModel(
            title = "Omar Maryadi",
            subTitle = "D7088FGX",
            imageUrl = "https://i-integration.gojekapi.com/darkroom/gomart-public-integration/v2/images/public/images/f9054d3d-7346-4b39-8385-61f6dfa81874_pertamax-icon.jpg",
            phoneNumber = "08123456789"
        )
        (activity as? TokoChatActivity)?.getHeaderUnify()?.run {
            val userTitle = findViewById<Typography>(R.id.tokochat_text_user_title)
            val subTitle = findViewById<Typography>(R.id.tokochat_text_user_subtitle)
            val imageUrl = findViewById<ImageUnify>(R.id.tokochat_user_avatar)
            val callMenu = findViewById<IconUnify>(R.id.tokochat_icon_header_menu)

            userTitle.text = uiModel.title
            subTitle.text = uiModel.subTitle
            imageUrl.setImageUrl(uiModel.imageUrl)

            val sourceLogoUrl = getSourceLogoUrl(source)

            if (sourceLogoUrl.isNotBlank()) {
                val sourceLogo = findViewById<ImageUnify>(R.id.tokochat_iv_source_logo)
                sourceLogo.setImageUrl(sourceLogoUrl)
            }

            callMenu.run {
                setImage(IconUnify.CALL)

                setOnClickListener {
                    showMaskingPhoneNumberBottomSheet(driverPhoneNumber)
                }
            }
        }
    }

    private fun showMaskingPhoneNumberBottomSheet(driverPhoneNumber: String) {
        val bottomSheetMaskingPhoneNumber = MaskingPhoneNumberBottomSheet.newInstance(driverPhoneNumber)
        bottomSheetMaskingPhoneNumber.show(childFragmentManager)
    }

    private fun setupReplySection(isShowReplySection: Boolean, expiredMessage: String) {
        baseBinding?.tokochatReplyBox?.run {
            shouldShowWithAction(isShowReplySection) {
                this.initLayout(this@TokoChatFragment, this@TokoChatFragment)
            }
        }
        baseBinding?.tokochatExpiredInfo?.shouldShowWithAction(!isShowReplySection) {
            baseBinding?.tokochatExpiredInfo?.setExpiredInfoDesc(expiredMessage)
        }
    }

    private fun getSourceLogoUrl(source: String?): String {
        return when (source) {
            TokoChatSource.TokoFood -> TokoChatImageUrl.IC_TOKOFOOD_SOURCE
            else -> ""
        }
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
