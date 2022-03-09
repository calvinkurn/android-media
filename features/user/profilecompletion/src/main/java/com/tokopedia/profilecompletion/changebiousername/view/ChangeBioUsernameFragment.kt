package com.tokopedia.profilecompletion.changebiousername.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.common.SubmitProfileError
import com.tokopedia.profilecompletion.databinding.FragmentChangeBioUsernameBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.changebiousername.viewmodel.ChangeBioUsernameViewModel
import com.tokopedia.profilecompletion.common.getErrorMessage
import com.tokopedia.profilecompletion.profileinfo.data.ProfileFeed
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profilecompletion.profileinfo.data.ProfileFeedData
import com.tokopedia.profilecompletion.profileinfo.tracker.ProfileInfoTracker
import com.tokopedia.unifycomponents.Toaster


class ChangeBioUsernameFragment : BaseDaggerFragment() {

    @Inject
    lateinit var tracker: ProfileInfoTracker

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ChangeBioUsernameViewModel::class.java)
    }

    private val binding: FragmentChangeBioUsernameBinding? by viewBinding()

    private var maxChar = 0
    private var minChar = 0
    private var page = ""

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
        initAction()
        initObserver()
    }

    override fun onFragmentBackPressed(): Boolean {
        when (page) {
            ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME -> {
                tracker.trackClickOnBtnBackUsername()
            }
            ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO -> {
                tracker.trackClickOnBtnBackChangeBio()
            }
        }
        return super.onFragmentBackPressed()
    }

    private fun initAction() {
        viewModel.getProfileFeed()
        showLoading()
    }

    private fun initObserver() {
        viewModel.profileFeed.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    hideLoading()
                    initData(it.data)
                }
                is Fail -> {
                    hideLoading()
                    view?.let { view ->
                        Toaster.make(view, ErrorHandler.getErrorMessage(context, it.throwable), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                    }
                }
            }
        }

        viewModel.resultValidationUsername.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Success -> {
                    if (!state.data.isValid) {
                        setInvalidInputUsername(state.data.errorMessage)
                    } else {
                        setValidInput()
                    }
                }

                is Fail -> {
                    view?.let { view ->
                        Toaster.make(view, ErrorHandler.getErrorMessage(context, state.throwable), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                    }
                }
            }
        }

        viewModel.resultSubmitUsername.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessSubmitUsername()
                }
                is Fail -> {
                    onErrorSubmitUsername(it.throwable)
                }
            }
        }

        viewModel.resultSubmitBio.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessSubmitBio()
                }
                is Fail -> {
                    onErrorSubmitBio(it.throwable)
                }
            }
        }

        viewModel.loadingState.observe(viewLifecycleOwner) {
            binding?.stubField?.etUsername?.isLoading = it
        }
    }

    private fun onSuccessSubmitUsername() {
        tracker.trackOnBtnSimpanUsernameSuccess()
        val resultIntent = Intent()
        resultIntent.putExtra(RESULT_KEY_MESSAGE_SUCCESS_USERNAME_BIO, getString(R.string.success_edit_profile_info))
        activity?.setResult(Activity.RESULT_OK, resultIntent)
        activity?.finish()
    }

    private fun onErrorSubmitUsername(error: Throwable) {
        hideLoading()
        if (error is SubmitProfileError) {
            try {
                val errorMsg = error.data
                    .getErrorMessage(getString(R.string.key_error_username))
                setInvalidInputUsername(errorMsg)
                tracker.trackOnBtnSimpanUsernameFailed(errorMsg)

            } catch (e: MessageErrorException) {
                val errorMsg = ErrorHandler.getErrorMessage(context, e)
                view?.let { view ->
                    Toaster.make(view, errorMsg,
                        Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                }
                tracker.trackOnBtnSimpanUsernameFailed(errorMsg)
            }
        } else {
            view?.let { view ->
                val errorMsg = ErrorHandler.getErrorMessage(context, error)
                Toaster.make(view, errorMsg,
                    Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                tracker.trackOnBtnSimpanUsernameFailed(errorMsg)
            }
        }
    }

    private fun onSuccessSubmitBio() {
        tracker.trackOnBtnLanjutChangeBioSuccess()
        hideLoading()
        val resultIntent = Intent()
        resultIntent.putExtra(RESULT_KEY_MESSAGE_SUCCESS_USERNAME_BIO, getString(R.string.success_edit_profile_info))
        activity?.setResult(Activity.RESULT_OK, resultIntent)
        activity?.finish()
    }

    private fun onErrorSubmitBio(error: Throwable) {
        hideLoading()
        if (error is SubmitProfileError) {
            try {
                val errorMsg = error.data
                    .getErrorMessage(getString(R.string.key_error_biography))
                setInvalidInputBio(errorMsg)
                tracker.trackOnBtnLanjutChangeBioFailed(errorMsg)

            } catch (e: MessageErrorException) {
                val errorMsg = ErrorHandler.getErrorMessage(context, e)
                view?.let { view ->
                    Toaster.make(view, errorMsg,
                        Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                }
                tracker.trackOnBtnLanjutChangeBioFailed(errorMsg)
            }
        } else {
            view?.let { view ->
                val errorMsg = ErrorHandler.getErrorMessage(context, error)
                Toaster.make(view, errorMsg,
                    Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                tracker.trackOnBtnLanjutChangeBioFailed(errorMsg)

            }
        }
    }

    private fun showLoading() {
        binding?.progressBar?.visible()
        binding?.btnSubmit?.gone()
        when (page) {
            ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO -> {
                binding?.stubField?.etUsername?.gone()
            }
            ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME -> {
                binding?.stubField?.etBio?.gone()
            }
        }
    }

    private fun hideLoading() {
        binding?.progressBar?.gone()
        binding?.btnSubmit?.show()
        when (page) {
            ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO -> {
                binding?.stubField?.etBio?.visible()
            }
            ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME -> {
                binding?.stubField?.etUsername?.visible()
            }
        }
    }

    private fun initData(profileFeedData: ProfileFeedData) {
        val data = activity?.intent?.extras
        data?.let { bundle ->
            if (bundle[ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PARAM]
                == ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME) {
                minChar = profileFeedData.userProfileConfiguration.usernameConfiguration.minimumChar
                maxChar = profileFeedData.userProfileConfiguration.usernameConfiguration.maximumChar
                page = ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME
                initViewsUsername(profileFeedData.profile)
            }
            else if (bundle[ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PARAM]
                == ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO) {
                minChar = 0
                maxChar = profileFeedData.userProfileConfiguration.biographyConfiguration.maximumChar
                page = ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO
                initViewsBio(profileFeedData.profile)
            }
        }
    }

    private fun initViewsUsername(data: ProfileFeed) {
        binding?.stubField?.etUsername?.editText?.setText(data.username)
        binding?.stubField?.etUsername?.setCounter(maxChar)
        binding?.stubField?.etUsername?.icon1?.hide()
        binding?.stubField?.etUsername?.visibility = View.VISIBLE
        setEditTextUsernameListener()
    }

    private fun initViewsBio(data: ProfileFeed) {
        binding?.stubField?.etBio?.editText?.setText(data.biography)
        binding?.stubField?.etUsername?.setCounter(maxChar)
        binding?.stubField?.etBio?.minLine = 5
        binding?.stubField?.etBio?.maxLine = 10
        binding?.stubField?.etBio?.visibility = View.VISIBLE
        setEditTextBioListener()
    }

    private fun setEditTextUsernameListener() {
        binding?.btnSubmit?.setOnClickListener {
            tracker.trackOnBtnSimpanUsernameClick()
            showLoading()
            viewModel.submitUsername(binding?.stubField?.etUsername?.editText?.text.toString())
        }

        binding?.stubField?.etUsername?.textInputLayout?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(editable: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length ?: 0 >= minChar) {
                    viewModel.validateUsername(text.toString())
                } else {
                    if (text?.length == 0) {
                        setInvalidInputUsername(getString(R.string.error_cant_empty))
                    } else {
                        setInvalidInputUsername(getString(R.string.error_min_char, minChar))
                    }
                    viewModel.cancelValidation()
                    binding?.stubField?.etUsername?.icon1?.hide()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
    }

    private fun setEditTextBioListener() {
        binding?.btnSubmit?.setOnClickListener {
            tracker.trackOnBtnLanjutChangeBioClick()
            showLoading()
            viewModel.submitBio(binding?.stubField?.etBio?.editText?.text.toString())
        }

        binding?.stubField?.etBio?.editText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding?.stubField?.etBio?.editText?.lineCount ?: 0 > 10) {
                    binding?.let{
                        it.stubField.etBio.editText.text?.delete(it.stubField.etBio.editText.text.length - 1, it.stubField.etBio.editText.text.length)

                    }
                }
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

    private fun setInvalidInputUsername(error: String) {
        binding?.btnSubmit?.isEnabled = false
        binding?.stubField?.etUsername?.icon1?.hide()
        binding?.stubField?.etUsername?.setMessage(error)
        binding?.stubField?.etUsername?.isInputError = true
    }

    private fun setInvalidInputBio(error: String) {
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

    companion object {
        const val RESULT_KEY_MESSAGE_SUCCESS_USERNAME_BIO = "result_bio_username"
    }
}