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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.manageaddress.data.analytics.ShareAddressAnalytics
import com.tokopedia.manageaddress.ui.uimodel.ShareAddressBottomSheetState
import com.tokopedia.manageaddress.databinding.BottomsheetShareAddressConfirmationBinding
import com.tokopedia.manageaddress.di.DaggerShareAddressComponent
import com.tokopedia.manageaddress.di.ShareAddressComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.domain.request.shareaddress.ShareAddressToUserParam
import com.tokopedia.manageaddress.domain.request.shareaddress.SelectShareAddressParam
import com.tokopedia.media.loader.loadImage

class ShareAddressConfirmationBottomSheet :
    BottomSheetUnify(),
    HasComponent<ShareAddressComponent> {

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
        ViewModelProvider(this, viewModelFactory)[ShareAddressConfirmationViewModel::class.java]
    }

    override fun getComponent(): ShareAddressComponent {
        return DaggerShareAddressComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        hideHeader()
        setOnDismissListener { dismiss() }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun hideHeader() {
        showCloseIcon = false
        showHeader = false
    }

    private fun initObserver() {
        viewModel.shareAddressResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ShareAddressBottomSheetState.Success -> onSuccessShareAddress()
                is ShareAddressBottomSheetState.Fail -> onFailedShareAddress(it.errorMessage)
                is ShareAddressBottomSheetState.Loading -> onLoadingShareAddress(it.isShowLoading)
            }
        }
    }

    private fun onSuccessShareAddress() {
        if (viewModel.isApprove) {
            trackOnAgreeShareAddress(isSuccess = true)
        } else {
            trackOnDisagreeShareAddress(isSuccess = true)
        }
        mListener?.onSuccessConfirmShareAddress(viewModel.isApprove)
    }

    private fun onFailedShareAddress(errorMessage: String) {
        if (viewModel.isApprove) {
            trackOnAgreeShareAddress(isSuccess = false)
        } else {
            trackOnDisagreeShareAddress(isSuccess = false)
        }
        mListener?.onFailedShareAddress(errorMessage)
    }

    private fun trackOnAgreeShareAddress(isSuccess: Boolean) {
        ShareAddressAnalytics.onAgreeSendAddress(
            isDirectShare = isFromNotif().not(),
            isSuccess = isSuccess
        )
    }

    private fun trackOnDisagreeShareAddress(isSuccess: Boolean) {
        ShareAddressAnalytics.onDisagreeSendAddress(
            isDirectShare = isFromNotif().not(),
            isSuccess = isSuccess
        )
    }

    private fun onLoadingShareAddress(isShowLoading: Boolean) {
        if (viewModel.isApprove) {
            binding.btnAgree.isLoading = isShowLoading
        } else {
            binding.btnDisagree.isLoading = isShowLoading
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
        viewModel.isApprove = true

        if (isFromNotif()) {
            viewModel.shareAddressFromNotif(
                SelectShareAddressParam(
                    SelectShareAddressParam.SelectShareAddressData(
                        receiverUserId = receiverUserId.orEmpty(),
                        senderAddressId = senderAddressId.orEmpty(),
                        approve = true,
                        source = source
                    )
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
        viewModel.isApprove = false

        if (isFromNotif()) {
            viewModel.shareAddressFromNotif(
                SelectShareAddressParam(
                    SelectShareAddressParam.SelectShareAddressData(
                        receiverUserId = receiverUserId.orEmpty(),
                        senderAddressId = senderAddressId.orEmpty(),
                        approve = false
                    )
                )
            )
        } else {
            trackOnDisagreeShareAddress(isSuccess = true)
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
                            com.tokopedia.unifyprinciples.R.color.Unify_G500
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
        fun onSuccessConfirmShareAddress(isApproved: Boolean)

        fun onFailedShareAddress(errorMessage: String)
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
