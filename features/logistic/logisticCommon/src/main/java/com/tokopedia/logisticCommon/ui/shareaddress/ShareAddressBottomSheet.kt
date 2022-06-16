package com.tokopedia.logisticCommon.ui.shareaddress

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticCommon.util.ShareAddressUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.logisticCommon.R
import com.tokopedia.logisticCommon.databinding.BottomsheetShareAddressBinding
import com.tokopedia.logisticCommon.di.DaggerRequestShareAddressComponent
import com.tokopedia.logisticCommon.di.RequestShareAddressComponent
import com.tokopedia.logisticCommon.domain.request.RequestShareAddress
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShareAddressBottomSheet : BottomSheetUnify(),
    HasComponent<RequestShareAddressComponent> {

    private var binding by autoCleared<BottomsheetShareAddressBinding>()
    private var permissionCheckerHelper: PermissionCheckerHelper? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ShareAddressViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShareAddressViewModel::class.java]
    }

    private var isRequestAddress: Boolean = false
    private var mShareAddressListener: ShareAddressListener? = null
    private var mRequestAddressListener: RequestAddressListener? = null

    override fun getComponent(): RequestShareAddressComponent {
        return DaggerRequestShareAddressComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        showCloseIcon = false
        showHeader = false
        permissionCheckerHelper = PermissionCheckerHelper()
        setOnDismissListener { dismiss() }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initObserver() {
        viewModel.requestShareAddressResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val response = it.data.shareAddressResponse
                    if (response.isSuccess) {
                        mRequestAddressListener?.onSuccessRequestAddress()
                    } else {
                        binding.etInputEmailNoHp.setMessage(response.error)
                        binding.etInputEmailNoHp.setError(true)
                    }
                }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindView()
        initObserver()
    }

    private fun bindView() {
        setTitle()
        setInputField()
        setBtnShare()
    }

    private fun setTitle() {
        binding.txtTitleShareAddress.text = if (isRequestAddress) {
            getString(R.string.share_address_bottom_sheet_title_request)
        } else {
            getString(R.string.share_address_bottom_sheet_title_share)
        }
    }

    private fun setInputField() {
        binding.apply {
            etInputEmailNoHp.textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    setShareButtonEnabled(s?.isNotBlank() == true)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })

            etInputEmailNoHp.getSecondIcon().let { contactIcon ->
                contactIcon.setColorFilter(
                    ContextCompat.getColor(
                        etInputEmailNoHp.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN900
                    ), PorterDuff.Mode.SRC_IN
                )
                contactIcon.setOnClickListener {
                    onNavigateToContact()
                }
            }
        }
    }

    private fun setBtnShare() {
        binding.btnShare.apply {
            text = if (isRequestAddress) {
                getString(R.string.share_address_bottom_sheet_btn_send)
            } else {
                getString(R.string.share_address_bottom_sheet_btn_share)
            }

            setOnClickListener {
                onClickShareButton()
            }
        }
    }

    private fun onClickShareButton() {
        var email: String? = null
        var phone: String? = null
        val inputText = binding.etInputEmailNoHp.textFieldInput.text.toString()

        if (viewModel.isEmailValid(inputText)) {
            email = inputText
        } else {
            phone = inputText
        }

        if (isRequestAddress) {
            viewModel.requestShareAddress(
                RequestShareAddress(
                    userId = userSession.userId,
                    phone = phone ?: "",
                    email = email ?: ""
                )
            )
        } else {
            mShareAddressListener?.onClickShareAddress(email, phone)
        }
    }

    private fun setShareButtonEnabled(isEnabled: Boolean) {
        binding.btnShare.isEnabled = isEnabled
    }

    private fun onNavigateToContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper?.checkPermissions(
                this,
                getPermissions(),
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        //no-op
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        //no-op
                    }

                    override fun onPermissionGranted() {
                        openContactPicker()
                    }
                }, getString(R.string.rationale_need_contact)
            )
        }
    }

    private fun openContactPicker() {
        val contactPickerIntent = Intent(
            Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        )
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: Exception) {
            showToaster(getString(R.string.contact_not_found))
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
            PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper?.onRequestPermissionsResult(
                context,
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
            val contactURI = data?.data
            if (contactURI != null) {
                val contact = context?.let {
                    ShareAddressUtil.convertContactUriToData(
                        it.contentResolver,
                        contactURI
                    )
                }
                val contactNumber = contact?.contactNumber
                val phoneNumberOnly = removeSpecialChars(contactNumber.toString())
                binding.etInputEmailNoHp.textFieldInput.setText(phoneNumberOnly)
            }
        }
    }

    private fun removeSpecialChars(s: String): String {
        return s.replace("[^A-Za-z0-9 ]".toRegex(), "").replace(" ", "")
    }

    private fun initChildLayout() {
        binding = BottomsheetShareAddressBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    private fun showToaster(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage, Toaster.LENGTH_SHORT, type = Toaster.TYPE_NORMAL).show()
        }
    }

    interface ShareAddressListener {
        fun onClickShareAddress(email: String?, phone: String?)
    }

    interface RequestAddressListener {
        fun onSuccessRequestAddress()
    }

    companion object {
        const val TAG_SHARE_ADDRESS = "ShareAddressBottomSheet"
        const val REQUEST_CODE_CONTACT_PICKER = 99

        fun newInstance(
            isRequestAddress: Boolean, shareAddressListener: ShareAddressListener? = null,
            requestAddressListener: RequestAddressListener? = null
        ): ShareAddressBottomSheet {
            return ShareAddressBottomSheet().apply {
                this.isRequestAddress = isRequestAddress
                this.mShareAddressListener = shareAddressListener
                this.mRequestAddressListener = requestAddressListener
            }
        }
    }
}