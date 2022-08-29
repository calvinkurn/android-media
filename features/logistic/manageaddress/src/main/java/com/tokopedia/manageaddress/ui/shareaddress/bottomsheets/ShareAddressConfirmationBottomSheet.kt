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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.logisticCommon.domain.model.ShareAddressBottomSheetState
import com.tokopedia.manageaddress.databinding.BottomsheetShareAddressConfirmationBinding
import com.tokopedia.manageaddress.di.DaggerShareAddressComponent
import com.tokopedia.manageaddress.di.ShareAddressComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject
import com.tokopedia.manageaddress.R
import com.tokopedia.logisticCommon.domain.request.ShareAddressToUserParam
import com.tokopedia.manageaddress.domain.model.shareaddress.SelectShareAddressParam
import com.tokopedia.media.loader.loadImage

class ShareAddressConfirmationBottomSheet : BottomSheetUnify(),
    HasComponent<ShareAddressComponent> {

    private var binding by autoCleared<BottomsheetShareAddressConfirmationBinding>()

    private var senderAddressId: String? = null
    private var receiverPhoneNumberOrEmail: String? = null
    private var receiverUserId: String? = null
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
        showCloseIcon = false
        showHeader = false
        setOnDismissListener { dismiss() }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initObserver() {
        viewModel.shareAddressResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShareAddressBottomSheetState.Success -> mListener?.onSuccessConfirmShareAddress(viewModel.isApprove)
                is ShareAddressBottomSheetState.Fail -> mListener?.onFailedShareAddress(it.errorMessage)
                is ShareAddressBottomSheetState.Loading -> onLoadingShareAddress(it.isShowLoading)
            }
        })
    }

    private fun onLoadingShareAddress(isShowLoading: Boolean) {
        if (viewModel.isApprove) {
            binding.btnShare.isLoading = isShowLoading
        } else {
            binding.btnCancel.isLoading = isShowLoading
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
            showCloseIcon = false
            showHeader = false
            setOnDismissListener { dismiss() }
            imgConfirmation.loadImage(IMAGE_SHARE_ADDRESS)
            txtTermsShareAddress.addLinkText(getString(R.string.share_address_confirmation_tnc_link)) {
                RouteManager.route(requireContext(), PRIVACY_POLICY_URL)
            }
            btnShare.setOnClickListener {
                onClickBtnShare()
            }
            btnCancel.setOnClickListener {
                onClickBtnCancel()
            }
        }
    }

    private fun onClickBtnShare() {
        viewModel.isApprove = true

        if (isFromNotif()) {
            viewModel.shareAddressFromNotif(
                SelectShareAddressParam(
                    receiverUserId = receiverUserId.orEmpty(),
                    senderAddressId = senderAddressId.orEmpty(),
                    approve = true,
                    source = source
                )
            )
        } else {
            viewModel.shareAddress(
                ShareAddressToUserParam(
                    senderAddressId = senderAddressId.orEmpty(),
                    receiverPhoneNumberOrEmail = receiverPhoneNumberOrEmail.orEmpty(),
                    initialCheck = false,
                    source = source
                )
            )
        }
    }

    private fun onClickBtnCancel() {
        viewModel.isApprove = false

        if (isFromNotif()) {
            viewModel.shareAddressFromNotif(
                SelectShareAddressParam(
                    receiverUserId = receiverUserId.orEmpty(),
                    senderAddressId = senderAddressId.orEmpty(),
                    approve = false
                )
            )
        } else {
            dismiss()
        }
    }

    private fun isFromNotif() : Boolean {
        return receiverUserId?.isNotBlank() == true
    }

    private fun TextView.addLinkText(
        linkText: String,
        onClickEvent: () -> Unit
    ) {
        val spannableString = SpannableString(linkText).apply {
            setSpan(object : ClickableSpan() {
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
                    this@addLinkText.invalidate()
                }
            }, 0, linkText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
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
        private const val IMAGE_SHARE_ADDRESS = "https://images.tokopedia.net/img/android/share_address/share_address_image.png"
        private const val PRIVACY_POLICY_URL = "https://www.tokopedia.com/privacy"

        fun newInstance(
            senderAddressId: String?,
            receiverPhoneNumberOrEmail: String?,
            receiverUserId: String?,
            source: String?,
            listener: Listener
        ): ShareAddressConfirmationBottomSheet {
            return ShareAddressConfirmationBottomSheet().apply {
                this.senderAddressId = senderAddressId ?: ""
                this.receiverPhoneNumberOrEmail = receiverPhoneNumberOrEmail ?: ""
                this.mListener = listener
                this.receiverUserId = receiverUserId
                this.source = source ?: ""
            }
        }
    }
}