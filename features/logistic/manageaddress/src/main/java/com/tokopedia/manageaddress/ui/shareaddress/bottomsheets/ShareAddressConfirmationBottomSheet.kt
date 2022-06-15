package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.manageaddress.databinding.BottomsheetShareAddressConfirmationBinding
import com.tokopedia.manageaddress.di.DaggerShareAddressComponent
import com.tokopedia.manageaddress.di.ShareAddressComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.domain.model.shareaddress.ShareAddressParam
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class ShareAddressConfirmationBottomSheet : BottomSheetUnify(),
    HasComponent<ShareAddressComponent> {

    private var binding by autoCleared<BottomsheetShareAddressConfirmationBinding>()

    private var email: String? = null
    private var phone: String? = null
    private var addressId: String? = null
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
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initObserver() {
        viewModel.shareAddressResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> mListener?.onSuccessShareAddress()
                is Fail -> showToaster(it.throwable.message.orEmpty())
            }
        })
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
            txtTermsShareAddress.addLinkText(getString(R.string.share_address_confirmation_tnc_link)) {
                // Need To Open Webview TNC Page
            }
            btnShare.setOnClickListener {
                viewModel.shareAddress(
                    ShareAddressParam(
                        email = email ?: "",
                        phone = phone ?: "",
                        addressId = addressId ?: ""
                    )
                )
            }
            setCloseClickListener {
                dismiss()
            }
        }
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

    private fun showToaster(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage, Toaster.LENGTH_SHORT, type = Toaster.TYPE_NORMAL).show()
        }
    }

    interface Listener {

        fun onSuccessShareAddress()
    }

    companion object {
        const val TAG_SHARE_ADDRESS_CONFIRMATION = "ShareAddressConfirmationBottomSheet"

        fun newInstance(
            email: String? = null,
            phone: String? = null,
            addressId: String? = null,
            listener: Listener
        ): ShareAddressConfirmationBottomSheet {
            return ShareAddressConfirmationBottomSheet().apply {
                this.email = email
                this.phone = phone
                this.addressId = addressId
                this.mListener = listener
            }
        }
    }
}