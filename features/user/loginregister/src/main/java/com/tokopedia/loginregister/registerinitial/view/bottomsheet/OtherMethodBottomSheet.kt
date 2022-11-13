package com.tokopedia.loginregister.registerinitial.view.bottomsheet

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.LayoutInitRegisterOtherMethodBottomsheetBinding
import com.tokopedia.loginregister.discover.pojo.DiscoverData
import com.tokopedia.loginregister.discover.pojo.ProviderData
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import java.util.Locale

class OtherMethodBottomSheet(val state: OtherMethodState<DiscoverData?>): BottomSheetUnify() {

    private var viewBinding: LayoutInitRegisterOtherMethodBottomsheetBinding? = null
    private var onGoogleClickedListener: () -> Unit = {}
    private var onEmailClickedListener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = LayoutInitRegisterOtherMethodBottomsheetBinding.inflate(layoutInflater, container, false)
        setTitle(context?.getString(R.string.other_method_title) ?: "")
        setChild(viewBinding?.root)

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
        viewBinding?.socmedContainer?.apply {
            viewBinding?.socmedContainer?.hide()
            viewBinding?.loaderSocmed?.show()
        }
    }

    private fun showSuccess(discoverData: DiscoverData?) {
        discoverData?.let {
            viewBinding?.socmedContainer?.removeAllViews()
            context?.let {

                val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                )
                layoutParams.setMargins(SOCMED_BUTTON_MARGIN_0, SOCMED_BUTTON_MARGIN_10, SOCMED_BUTTON_MARGIN_0, SOCMED_BUTTON_MARGIN_10)

                //set button social media
                discoverData.providers.forEach { provider ->

                    val buttonSocmed = UnifyButton(it)
                    buttonSocmed.layoutParams = layoutParams
                    buttonSocmed.buttonType = UnifyButton.Type.ALTERNATE
                    buttonSocmed.buttonVariant = UnifyButton.Variant.GHOST
                    buttonSocmed.text = provider.name

                    //set icon
                    when (provider.id) {
                        LoginConstants.DiscoverLoginId.EMAIL -> {
                            val icon = ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.ic_email
                            )
                            buttonSocmed.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
                        }
                        else -> {
                            loadImageWithEmptyTarget(it, provider.image, {
                                fitCenter()
                            }, MediaBitmapEmptyTarget(
                                onReady = { bitmap ->
                                    val icon = BitmapDrawable(it.resources, bitmap)
                                    buttonSocmed.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
                                }
                            ))
                        }
                    }

                    //set action
                    setDiscoverOnClickListener(provider, buttonSocmed)
                    viewBinding?.socmedContainer?.run {
                        addView(buttonSocmed, childCount, layoutParams)
                    }
                }
            }
        }
    }

    private fun showFailed(message: String?) {
        viewBinding?.socmedContainer?.apply {
            viewBinding?.tvMessageSocmed?.show()
            viewBinding?.loaderSocmed?.hide()

            viewBinding?.tvMessageSocmed?.text = message
        }
    }

    private fun setDiscoverOnClickListener(provider: ProviderData, button: UnifyButton) {
        when (provider.id.lowercase(Locale.getDefault())) {
            LoginConstants.DiscoverLoginId.GPLUS -> {
                button.setOnClickListener {
                    onGoogleClickedListener.invoke()
                }
            }
            LoginConstants.DiscoverLoginId.EMAIL -> {
                button.setOnClickListener {
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
    }
}