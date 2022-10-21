package com.tokopedia.loginregister.goto_seamless

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.FragmentGotoSeamlessBinding
import com.tokopedia.loginregister.goto_seamless.di.GotoSeamlessComponent
import com.tokopedia.loginregister.goto_seamless.model.GojekProfileData
import com.tokopedia.loginregister.goto_seamless.trackers.GotoSeamlessTracker
import com.tokopedia.remoteconfig.RemoteConfigInstance
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
        GotoSeamlessTracker.viewGotoSeamlessPage(getTrackerLabel())
        viewModel.gojekProfileData.observe(viewLifecycleOwner) {
            binding?.gotoSeamlessPrimaryBtn?.isLoading = false
            when(it) {
                is Success -> {
                    if(it.data.authCode.isNotEmpty()) {
                        renderPositiveButtonTxt(it.data)
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
                        val errorMsg = it.data.errors.first().message
                        GotoSeamlessTracker.clickOnSeamlessButton("${GotoSeamlessTracker.Label.FAILED} - $errorMsg", getTrackerLabel())
                        showGeneralErrorToaster()
                    } else {
                        successSeamlessLogin()
                    }
                }
                is Fail -> {
                    GotoSeamlessTracker.clickOnSeamlessButton("${GotoSeamlessTracker.Label.FAILED} - ${it.throwable.message}", getTrackerLabel())
                    showGeneralErrorToaster()
                }
            }
            binding?.gotoSeamlessPrimaryBtn?.isLoading = false
        }

        binding?.gotoSeamlessPrimaryBtn?.isLoading = true
        viewModel.getGojekData()
    }

    private fun renderPositiveButtonTxt(data: GojekProfileData) {
        context?.let {
            var description = it.getString(R.string.goto_seamless_description_text_2)
            val text = when(getVariant()) {
                TOKO_NAME_VARIANT -> {
                    "Masuk sebagai ${takeFirstName(data.tokopediaName)}"
                }
                GOJEK_NAME_VARIANT -> {
                    renderGojekIcon()
                    "Masuk sebagai ${takeFirstName(data.name)}"
                }
                else -> {
                    description = it.getString(R.string.goto_seamless_description_text)
                    "Masuk ke ${data.countryCode}${data.phone}"
                }
            }
            binding?.gotoSeamlessPrimaryBtn?.text = text
            binding?.gotoSeamlessDescriptionTxt?.text = description
        }
    }

    private fun renderGojekIcon() {
        context?.let {
            val icon = MethodChecker.getDrawable(it, R.drawable.ic_gojek_small)
            binding?.gotoSeamlessPrimaryBtn?.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        }
    }

    private fun takeFirstName(name: String): String {
        return try {
            name.trim().split(" ").first()
        } catch (e: Exception) {
            return name
        }
    }

    private fun showGeneralErrorToaster() {
        Toaster.build(requireView(), getString(R.string.goto_seamless_general_error), Toaster.TYPE_ERROR).show()
    }

    private fun successSeamlessLogin() {
        GotoSeamlessTracker.clickOnSeamlessButton(GotoSeamlessTracker.Label.SUCCESS, getTrackerLabel())
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
            GotoSeamlessTracker.clickOnSeamlessButton(GotoSeamlessTracker.Label.CLICK, getTrackerLabel())
            val gojekProfileData = viewModel.gojekProfileData.value
            if(gojekProfileData is Success) {
                binding?.gotoSeamlessPrimaryBtn?.isLoading = true
                viewModel.doSeamlessLogin(gojekProfileData.data.authCode)
            }
        }

        binding?.gotoSeamlessSecondaryBtn?.setOnClickListener {
            GotoSeamlessTracker.clickOnMasukAkunLain(getTrackerLabel())
            loginWithDifferentAccFlow()
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        GotoSeamlessTracker.clickOnBackBtn(getTrackerLabel())
        return super.onFragmentBackPressed()
    }

    private fun getVariant(): String {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(ROLLENCE_SEAMLESS_KEY)
    }

    private fun getTrackerLabel(): String {
        return try {
            getVariant().trim().split("_").first()
        } catch (e: Exception) {
            ""
        }
    }

    companion object {
        private const val GOTO_SEAMLESS_SCREEN_NAME = "gotoSeamlessLandingScreen"
        const val RESULT_OTHER_ACCS = 235

        private const val ROLLENCE_SEAMLESS_KEY = "seamless_experiment"
        private const val PHONE_VARIANT = "control_variant"
        private const val TOKO_NAME_VARIANT = "inapp_variant"
        private const val GOJEK_NAME_VARIANT = "other_variant"

        fun createInstance(): GotoSeamlessLoginFragment {
            return GotoSeamlessLoginFragment()
        }
    }
}
