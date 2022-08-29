package com.tokopedia.loginregister.redefine_register_email.view.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.KeyboardHandler
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.common.view.emailextension.adapter.EmailExtensionAdapter
import com.tokopedia.loginregister.databinding.FragmentRedefineRegisterEmailBinding
import com.tokopedia.loginregister.redefine_register_email.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.view.viewmodel.RedefineRegisterEmailViewModel
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class RedefineRegisterEmailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            RedefineRegisterEmailViewModel::class.java
        )
    }

    private val binding: FragmentRedefineRegisterEmailBinding? by viewBinding()

    private var source: String = EMPTY_STRING
    private var isExtensionSelected = false
    private var emailExtensionList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        source = getParamString(ApplinkConstInternalGlobal.PARAM_SOURCE, arguments, savedInstanceState, EMPTY_STRING)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_redefine_register_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpField()
        initListener()
        initObserver()
        initEmailExtension(view)
        editorChangesListener()
    }

    private fun initEmailExtension(view: View) {
        setUpKeyboardListener(view)

        emailExtensionList.clear()
        emailExtensionList.addAll(requireContext().resources.getStringArray(R.array.email_extension))
        emailExtensionList.let { it ->
            binding?.emailExtension?.setExtensions(it, object: EmailExtensionAdapter.ClickListener {
                override fun onExtensionClick(extension: String, position: Int) {
                    val charEmail: Array<String> = binding?.fieldEmail?.editText?.text.toString().split(
                        DELIMITER_EMAIL).toTypedArray()
                    if (charEmail.isNotEmpty()) {
                        binding?.fieldEmail?.editText?.setText(String.format(STRING_FORMAT_EMAIL, charEmail[0], extension))
                    } else {
                        binding?.fieldEmail?.editText?.setText(String.format(
                            STRING_FORMAT_EMAIL,  binding?.fieldEmail?.editText?.text.toString().replace(
                            DELIMITER_EMAIL, EMPTY_STRING), extension))
                    }
                    binding?.fieldEmail?.editText?.setSelection( binding?.fieldEmail?.editText?.text.toString().trim { it <= CHAR_SPACE }.length)
                    isExtensionSelected = true
                    showEmailExtension(false)
                }
            })
        }
    }

    private fun setUpKeyboardListener(view: View) {
        KeyboardHandler(view, object : KeyboardHandler.OnKeyBoardVisibilityChangeListener {
            override fun onKeyboardShow() {
                if (binding?.fieldEmail?.editText?.text.toString().contains(DELIMITER_EMAIL) && !isExtensionSelected && binding?.fieldEmail?.editText?.isFocused == true) {
                    showEmailExtension(true)
                }
            }

            override fun onKeyboardHide() {
                showEmailExtension(false)
            }
        })
    }

    private fun showEmailExtension(isShow: Boolean) {
        binding?.emailExtension?.showWithCondition(isShow)
    }

    private fun editorChangesListener() {
        binding?.fieldEmail?.editText?.afterTextChanged {
            viewModel.validateEmail(it)

            setUpEmailExtension(it)
        }

        binding?.fieldPassword?.editText?.afterTextChanged {
            viewModel.validatePassword(it)
        }

        binding?.fieldName?.editText?.afterTextChanged {
            viewModel.validateName(it)
        }

        binding?.fieldName?.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitForm()
                true
            } else false
        }
    }

    private fun setUpEmailExtension(email: String) {
        if (binding?.fieldEmail?.editText?.isFocused == true && email.contains(DELIMITER_EMAIL)) {
            val charEmail = email.split(DELIMITER_EMAIL).toTypedArray()
            if (charEmail.size > 1) {
                binding?.emailExtension?.filterExtensions(charEmail[1])
            } else {
                binding?.emailExtension?.updateExtensions(emailExtensionList)
            }
            isExtensionSelected = false
            showEmailExtension(true)
        } else {
            showEmailExtension(false)
        }
    }

    private fun setUpField() {
        binding?.apply {
            fieldEmail.editText.imeOptions = EditorInfo.IME_ACTION_NEXT
            fieldPassword.editText.imeOptions = EditorInfo.IME_ACTION_NEXT
            fieldName.editText.imeOptions = EditorInfo.IME_ACTION_DONE
        }
    }

    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        viewModel.submitForm(
            binding?.fieldEmail?.editText?.text.toString(),
            binding?.fieldPassword?.editText?.text.toString(),
            binding?.fieldName?.editText?.text.toString()
        )
    }

    private fun initObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {
            binding?.btnSubmit?.isEnabled = true
            binding?.fieldEmail?.setMessageField(it.emailError)
            binding?.fieldPassword?.setMessageField(it.passwordError)
            binding?.fieldName?.setMessageField(it.nameError)
        }

        viewModel.isRegisteredEmail.observe(viewLifecycleOwner) {
            if (it) showLoginDialog()
        }
    }

    private fun TextFieldUnify2.setMessageField(stringResource: Int) {
        isInputError = if (stringResource != RedefineRegisterEmailViewModel.NOTHING_RESOURCE && stringResource != RedefineRegisterEmailViewModel.RESOURCE_NOT_CHANGED) {
            setMessage(getString(stringResource))
            true
        } else {
            setMessage(SPACE)
            false
        }
    }

    private fun showLoginDialog() {
        val email = binding?.fieldEmail?.editText?.text.toString()
        val offerToLoginDialog = RegisteredDialog.createRedefineRegisterEmailOfferLogin(requireActivity(), email)

        offerToLoginDialog.setPrimaryCTAClickListener {
            offerToLoginDialog.dismiss()
            gotoLoginEmailPage(email)
        }

        offerToLoginDialog.setSecondaryCTAClickListener {
            offerToLoginDialog.dismiss()
        }

        offerToLoginDialog.show()
    }

    private fun gotoLoginEmailPage(email: String) {

        val intent = RouteManager.getIntent(
            activity,
            ApplinkConstInternalUserPlatform.LOGIN_EMAIL,
            Uri.encode(email),
            source
        )
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, true)

        startActivity(intent)
        activity?.finish()
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(RedefineRegisterEmailComponent::class.java).inject(this)
    }

    companion object {
        private const val STRING_FORMAT_EMAIL = "%s@%s"
        private const val DELIMITER_EMAIL = "@"

        private const val EMPTY_STRING = ""
        private const val CHAR_SPACE = ' '
        private const val SPACE = " "
        private val SCREEN_NAME = RedefineRegisterEmailFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = RedefineRegisterEmailFragment()
    }
}