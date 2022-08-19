package com.tokopedia.loginregister.registerinitial.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.view.LoginTextView
import com.tokopedia.loginregister.databinding.LayoutInitRegisterOtherMethodBottomsheetBinding
import com.tokopedia.loginregister.discover.pojo.DiscoverData
import com.tokopedia.loginregister.discover.pojo.ProviderData
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.Locale
import kotlinx.android.synthetic.main.layout_init_register_other_method_bottomsheet.loader_socmed
import kotlinx.android.synthetic.main.layout_init_register_other_method_bottomsheet.tv_message_socmed

class OtherMethodBottomSheet(val state: OtherMethodState<DiscoverData?>): BottomSheetUnify() {

    private var binding: LayoutInitRegisterOtherMethodBottomsheetBinding? = null
    private var onGoogleClickedListener: () -> Unit = {}
    private var onEmailClickedListener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutInitRegisterOtherMethodBottomsheetBinding.inflate(layoutInflater, container, false)
        setTitle(context?.getString(R.string.other_method_title) ?: "")
        setChild(binding?.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setState(state)
    }

    fun setState(state: OtherMethodState<DiscoverData?>) {
        when(state) {
            is OtherMethodState.Loading -> {
                showLoading()
            }
            is OtherMethodState.Success -> {
                showSuccess(state.data)
            }
            is OtherMethodState.Failed -> {
                showFailed(state.message)
            }
        }
    }

    private fun showLoading() {
        binding?.socmedContainer?.apply {
            tv_message_socmed.hide()
            loader_socmed.show()
        }
    }

    private fun showSuccess(discoverData: DiscoverData?) {
        discoverData?.let {
            binding?.socmedContainer?.removeAllViews()
            context?.let {

                val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        requireContext().resources.getDimensionPixelSize(R.dimen.dp_52)
                    )
                layoutParams.setMargins(SOCMED_BUTTON_MARGIN_0, SOCMED_BUTTON_MARGIN_0, SOCMED_BUTTON_MARGIN_0, SOCMED_BUTTON_MARGIN_10)

                //set button social media
                discoverData.providers.forEach { provider ->
                    val loginTextView = LoginTextView(it, MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                    loginTextView.setText(provider.name)
                    loginTextView.setRoundCorner(SOCMED_BUTTON_CORNER_SIZE)

                    when (provider.id) {
                        LoginConstants.DiscoverLoginId.EMAIL -> loginTextView.setImage(R.drawable.ic_email)
                        else -> loginTextView.setImage(provider.image)
                    }

                    setDiscoverOnClickListener(provider, loginTextView)
                    binding?.socmedContainer?.run {
                        addView(loginTextView, childCount, layoutParams)
                    }
                }
            }
        }
    }

    private fun showFailed(message: String?) {
        binding?.socmedContainer?.apply {
            tv_message_socmed.show()
            loader_socmed.hide()

            tv_message_socmed.text = message
        }
    }

    private fun setDiscoverOnClickListener(provider: ProviderData, loginTextView: LoginTextView) {
        when (provider.id.lowercase(Locale.getDefault())) {
            LoginConstants.DiscoverLoginId.GPLUS -> {
                loginTextView.setOnClickListener {
                    onGoogleClickedListener.invoke()
                }
            }
            LoginConstants.DiscoverLoginId.EMAIL -> {
                loginTextView.setOnClickListener {
                    onEmailClickedListener.invoke()
                }
            }
        }
    }

    fun setOnGoogleClickedListener(listener: () -> Unit) {
        onGoogleClickedListener = listener
    }

    fun setOnEmailClickedListener(listener: () -> Unit) {
        onEmailClickedListener = listener
    }

    companion object {
        private const val SOCMED_BUTTON_MARGIN_0 = 0
        private const val SOCMED_BUTTON_MARGIN_10 = 10
        private const val SOCMED_BUTTON_CORNER_SIZE = 10
    }
}