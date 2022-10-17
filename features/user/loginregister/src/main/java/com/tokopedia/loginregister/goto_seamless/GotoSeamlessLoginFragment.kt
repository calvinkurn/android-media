package com.tokopedia.loginregister.goto_seamless

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.FragmentGotoSeamlessBinding
import com.tokopedia.loginregister.goto_seamless.di.GotoSeamlessComponent
import com.tokopedia.loginregister.goto_seamless.trackers.GotoSeamlessTracker
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

class GotoSeamlessLoginFragment: BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(GotoSeamlessLoginViewModel::class.java)
    }

    private val binding by viewBinding(FragmentGotoSeamlessBinding::bind)

    override fun getScreenName(): String = GOTO_SEAMLESS_SCREEN_NAME

    override fun initInjector() {
        getComponent(GotoSeamlessComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_goto_seamless, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        GotoSeamlessTracker.viewGotoSeamlessPage()
        viewModel.gojekProfileData.observe(viewLifecycleOwner) {
            binding?.gotoSeamlessPrimaryBtn?.isLoading = false
            when(it) {
                is Success -> {
                    if(it.data.authCode.isNotEmpty()) {
                        binding?.gotoSeamlessPrimaryBtn?.text = "Masuk ke ${it.data.countryCode}${it.data.phone}"
                    } else {
                        cancelSeamlessLoginFlow()
                    }
                }
                is Fail -> cancelSeamlessLoginFlow()
            }
        }

        viewModel.loginResponse.observe(viewLifecycleOwner) {
            when(it) {
                is Success -> {
                    if(it.data.errors.isNotEmpty()) {
                        val errorMsg = it.data.errors[0].message
                        GotoSeamlessTracker.clickOnSeamlessButton("${GotoSeamlessTracker.Label.FAILED} - $errorMsg")
                        showGeneralErrorToaster()
                    } else {
                        successSeamlessLogin()
                    }
                }
                is Fail -> {
                    GotoSeamlessTracker.clickOnSeamlessButton("${GotoSeamlessTracker.Label.FAILED} - ${it.throwable.message}")
                    showGeneralErrorToaster()
                }
            }
            binding?.gotoSeamlessPrimaryBtn?.isLoading = false
        }

        binding?.gotoSeamlessPrimaryBtn?.isLoading = true
        viewModel.getGojekData()
    }

    private fun showGeneralErrorToaster() {
        Toaster.build(requireView(), getString(R.string.goto_seamless_general_error), Toaster.TYPE_ERROR).show()
    }

    private fun successSeamlessLogin() {
        GotoSeamlessTracker.clickOnSeamlessButton(GotoSeamlessTracker.Label.SUCCESS)
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun loginWithDifferentAccFlow() {
        activity?.setResult(RESULT_OTHER_ACCS)
        activity?.finish()
    }

    private fun cancelSeamlessLoginFlow() {
        activity?.setResult(Activity.RESULT_CANCELED)
        activity?.finish()
    }

    private fun setupViews() {
        binding?.gotoSeamlessPrimaryBtn?.setOnClickListener {
            GotoSeamlessTracker.clickOnSeamlessButton(GotoSeamlessTracker.Label.CLICK)
            val gojekProfileData = viewModel.gojekProfileData.value
            if(gojekProfileData is Success) {
                binding?.gotoSeamlessPrimaryBtn?.isLoading = true
                viewModel.doSeamlessLogin(gojekProfileData.data.authCode)
            }
        }

        binding?.gotoSeamlessSecondaryBtn?.setOnClickListener {
            GotoSeamlessTracker.clickOnMasukAkunLain()
            loginWithDifferentAccFlow()
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        GotoSeamlessTracker.clickOnBackBtn()
        return super.onFragmentBackPressed()
    }

    companion object {
        private const val GOTO_SEAMLESS_SCREEN_NAME = "gotoSeamlessLandingScreen"
        const val RESULT_OTHER_ACCS = 235

        fun createInstance(): GotoSeamlessLoginFragment {
            return GotoSeamlessLoginFragment()
        }
    }
}