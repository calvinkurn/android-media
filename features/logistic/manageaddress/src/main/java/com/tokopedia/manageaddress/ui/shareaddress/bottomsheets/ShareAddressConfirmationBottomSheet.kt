package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.data.analytics.ShareAddressAnalytics
import com.tokopedia.manageaddress.databinding.BottomsheetShareAddressConfirmationBinding
import com.tokopedia.manageaddress.di.ActivityComponentFactory
import com.tokopedia.manageaddress.domain.request.shareaddress.SelectShareAddressParam
import com.tokopedia.manageaddress.domain.request.shareaddress.ShareAddressToUserParam
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class ShareAddressConfirmationBottomSheet : BottomSheetUnify() {

    private var binding by autoCleared<BottomsheetShareAddressConfirmationBinding>()

    private var senderAddressId: String? = null
    private var receiverPhoneNumberOrEmail: String? = null
    private var receiverUserId: String? = null
    private var receiverUserName: String? = null
    private var source: String = ""
    private var mListener: Listener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ShareAddressConfirmationViewModel by lazy {
        ViewModelProvider(
            requireParentFragment(),
            viewModelFactory
        )[ShareAddressConfirmationViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        hideHeader()
        setOnDismissListener { dismiss() }
    }

    private fun initInjector() {
        ActivityComponentFactory.instance.createComponent(requireActivity().application)
            .inject(this)
    }

    private fun hideHeader() {
        showCloseIcon = false
        showHeader = false
    }

    private fun initObserver() {
        viewModel.dismissEvent.observe(viewLifecycleOwner) {
            dismiss()
        }
        viewModel.toastEvent.observe(viewLifecycleOwner) {
            val isError = it !is ShareAddressConfirmationViewModel.Toast.Success
            val msg = if (isError) {
                (it as ShareAddressConfirmationViewModel.Toast.Error).msg
            } else {
                getString(R.string.success_share_address)
            }
            mListener?.showToast(isError, msg)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.btnAgree.isLoading =
                it is ShareAddressConfirmationViewModel.LoadingState.AgreeLoading
            binding.btnDisagree.isLoading =
                it is ShareAddressConfirmationViewModel.LoadingState.DisagreeLoading
        }
        viewModel.leavePageEvent.observe(viewLifecycleOwner) {
            mListener?.leavePage()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        binding = BottomsheetShareAddressConfirmationBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setLayout()
        initObserver()
    }

    private fun setLayout() {
        binding.apply {
            setOnDismissListener { dismiss() }
            txtDescShareAddress.setTextDescription()
            imgConfirmation.loadImage(IMAGE_SHARE_ADDRESS)
            txtTermsShareAddress.addLinkText(getString(R.string.share_address_confirmation_tnc_link)) {
                RouteManager.route(requireContext(), TERMS_AND_CONDITIONS)
            }
            btnAgree.setOnClickListener {
                onClickBtnShare()
            }
            btnDisagree.setOnClickListener {
                onClickBtnCancel()
            }
        }
    }

    private fun TextView.setTextDescription() {
        text = MethodChecker.fromHtml(
            String.format(
                getString(
                    R.string.share_address_confirmation_description,
                    receiverUserName?.takeIf { it.isNotBlank() }
                        ?: getString(R.string.desc_your_friend)
                )
            )
        )
    }

    private fun onClickBtnShare() {
        if (isFromNotif()) {
            viewModel.shareAddressFromNotif(
                SelectShareAddressParam.Param(
                    receiverUserId = receiverUserId.orEmpty(),
                    senderAddressId = senderAddressId.orEmpty(),
                    approve = true,
                    source = source
                )
            )
        } else {
            viewModel.shareAddress(
                ShareAddressToUserParam(
                    ShareAddressToUserParam.ShareAddressToUserData(
                        senderAddressId = senderAddressId.orEmpty(),
                        receiverPhoneNumberOrEmail = receiverPhoneNumberOrEmail.orEmpty(),
                        initialCheck = false,
                        source = source
                    )
                )
            )
        }
    }

    private fun onClickBtnCancel() {
        if (isFromNotif()) {
            viewModel.shareAddressFromNotif(
                SelectShareAddressParam.Param(
                    receiverUserId = receiverUserId.orEmpty(),
                    senderAddressId = senderAddressId.orEmpty(),
                    approve = false
                )
            )
        } else {
            ShareAddressAnalytics.directShareDisagreeSendAddress()
            dismiss()
        }
    }

    private fun isFromNotif(): Boolean {
        return receiverUserId?.isNotBlank() == true
    }

    private fun TextView.addLinkText(
        linkText: String,
        onClickEvent: () -> Unit
    ) {
        val spannableString = SpannableString(linkText).apply {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        onClickEvent()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.typeface = Typeface.DEFAULT_BOLD
                        ds.isUnderlineText = false
                        ds.color = ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                    }
                },
                0,
                linkText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod = LinkMovementMethod.getInstance()
        this.text = TextUtils.concat(this.text, " ", spannableString)
    }

    interface Listener {
        fun showToast(isError: Boolean, msg: String)
        fun leavePage()
    }

    companion object {
        const val TAG_SHARE_ADDRESS_CONFIRMATION = "ShareAddressConfirmationBottomSheet"
        private const val IMAGE_SHARE_ADDRESS =
            "https://images.tokopedia.net/img/android/share_address/share_address_image.png"
        private const val TERMS_AND_CONDITIONS =
            "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-bagikan-alamat"

        fun newInstance(
            senderAddressId: String?,
            receiverPhoneNumberOrEmail: String?,
            receiverUserId: String?,
            receiverUserName: String? = null,
            source: String?,
            listener: Listener
        ): ShareAddressConfirmationBottomSheet {
            return ShareAddressConfirmationBottomSheet().apply {
                this.senderAddressId = senderAddressId ?: ""
                this.receiverPhoneNumberOrEmail = receiverPhoneNumberOrEmail ?: ""
                this.mListener = listener
                this.receiverUserId = receiverUserId
                this.source = source ?: ""
                this.receiverUserName = receiverUserName
            }
        }
    }
}
