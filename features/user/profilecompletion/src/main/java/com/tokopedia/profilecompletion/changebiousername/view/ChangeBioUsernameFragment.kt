package com.tokopedia.profilecompletion.changebiousername.view

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.common.SubmitProfileError
import com.tokopedia.profilecompletion.databinding.FragmentChangeBioUsernameBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.changebiousername.viewmodel.ChangeBioUsernameViewModel
import com.tokopedia.profilecompletion.common.getErrorMessage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class ChangeBioUsernameFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ChangeBioUsernameViewModel::class.java)
    }

    private val binding: FragmentChangeBioUsernameBinding? by viewBinding()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_bio_username, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = activity?.intent?.data
        if (data?.getQueryParameter("page") == "username") {
            initViewsUsername()
        } else if (data?.getQueryParameter("page") == "bio"){
            initViewsBio()
        }
        initObserver()
    }

    private fun initObserver() {
        viewModel.resultValidationUsername.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Success -> {
                    if (!state.data.isValid) {
                        setInvalidInput(state.data.errorMessage)
                    } else {
                        setValidInput()
                    }
                }

                is Fail -> {

                }
            }
        }

        viewModel.resultSubmitUsername.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {

                }
                is Fail -> {

                }
            }
        }

        viewModel.resultSubmitBio.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {

                }
                is Fail -> {
                    if (it.throwable is SubmitProfileError) {
                        submitBioError((it.throwable as SubmitProfileError).data
                            .getErrorMessage(getString(R.string.key_error_biography)))
                    }
                }
            }
        }

        viewModel.loadingState.observe(viewLifecycleOwner) {
            binding?.stubField?.etUsername?.isLoading = it
        }
    }

    private fun initViewsUsername() {
        binding?.stubField?.etUsername?.icon1?.hide()
        binding?.stubField?.etUsername?.visibility = View.VISIBLE
        setEditTextUsernameListener()
    }

    private fun initViewsBio() {
        binding?.stubField?.etBio?.maxLine = 10
        binding?.stubField?.etBio?.visibility = View.VISIBLE
        setEditTextBioListener()
    }

    private fun setEditTextUsernameListener() {
        binding?.btnSubmit?.setOnClickListener {
            viewModel.submitUsername(binding?.stubField?.etUsername?.editText?.text.toString())
        }

        binding?.stubField?.etUsername?.textInputLayout?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(editable: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                refreshStateTextField()
                viewModel.validateUsername(text.toString())
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
    }

    private fun setEditTextBioListener() {
        binding?.btnSubmit?.setOnClickListener {
            viewModel.submitBio(binding?.stubField?.etBio?.editText?.text.toString())
        }

        binding?.stubField?.etBio?.editText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding?.stubField?.etBio?.isInputError == true) {
                    binding?.stubField?.etBio?.isInputError = false
                    binding?.stubField?.etBio?.setMessage("")
                }
                binding?.btnSubmit?.isEnabled = true
            }

        })
        binding?.stubField?.etBio?.editText?.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                binding?.stubField?.etBio?.editText?.lineCount ?: 0 >= 10
            else false
        }
    }

    private fun refreshStateTextField() {
        binding?.stubField?.etUsername?.isInputError = false
    }

    private fun setInvalidInput(error: String) {
        binding?.btnSubmit?.isEnabled = false
        binding?.stubField?.etUsername?.icon1?.hide()
        binding?.stubField?.etUsername?.setMessage(error)
        binding?.stubField?.etUsername?.isInputError = true
    }

    private fun submitBioError(error: String) {
        binding?.btnSubmit?.isEnabled = false
        binding?.stubField?.etBio?.setMessage(error)
        binding?.stubField?.etBio?.isInputError = true
    }

    private fun setValidInput() {
        binding?.btnSubmit?.isEnabled = true
        binding?.stubField?.etUsername?.icon1?.show()
        binding?.stubField?.etUsername?.setMessage(getString(R.string.description_textfield_username))
        binding?.stubField?.etUsername?.isInputError = false
    }
}