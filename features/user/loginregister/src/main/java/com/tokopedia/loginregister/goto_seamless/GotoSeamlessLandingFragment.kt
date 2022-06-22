package com.tokopedia.loginregister.goto_seamless

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gojek.icp.identity.loginsso.Environment
import com.gojek.icp.identity.loginsso.SSOHostBridge
import com.gojek.icp.identity.loginsso.data.SSOHostData
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.FragmentGotoSeamlessBinding
import com.tokopedia.loginregister.goto_seamless.di.GotoSeamlessComponent
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class GotoSeamlessLandingFragment: BaseDaggerFragment() {

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycleScope.launch {
            initSDK()
        }
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
        binding?.gotoSeamlessPrimaryBtn?.text = "Masuk pakai ${formatPhoneNumber(userSession.phoneNumber)}"
    }

    private suspend fun initSDK() {
        val ssoHostData = SSOHostData(
            "tokopedia:consumer:app",
            "qmcpRpZPBC7DTRNQiI7dIkuGoxrqsu",
            Environment.Integration
        )

        val ssoDataBridge = SSOHostBridge.getSsoHostBridge()
        ssoDataBridge.initBridge(requireContext(), ssoHostData)
    }

    private fun formatPhoneNumber(phoneNum: String): String? {
        if (phoneNum.isNotEmpty()) {
            return when {
                phoneNum.startsWith("62") -> phoneNum.replaceFirst("62", "0")
                phoneNum.startsWith("+62") -> phoneNum.replaceFirst("+62", "0")
                else -> phoneNum
            }
        }
        return phoneNum
    }

    companion object {
        private const val GOTO_SEAMLESS_SCREEN_NAME = "gotoSeamlessLandingScreen"

        fun createInstance(): GotoSeamlessLandingFragment {
            return GotoSeamlessLandingFragment()
        }
    }
}