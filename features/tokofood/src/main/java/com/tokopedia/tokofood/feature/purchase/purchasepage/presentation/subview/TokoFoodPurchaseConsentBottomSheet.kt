package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.databinding.LayoutBottomSheetPurchaseConsentBinding
import com.tokopedia.tokofood.feature.purchase.purchasepage.di.DaggerTokoFoodPurchaseComponent
import com.tokopedia.tokofood.feature.purchase.purchasepage.di.TokoFoodPurchaseComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

class TokoFoodPurchaseConsentBottomSheet : BottomSheetUnify(),
    HasComponent<TokoFoodPurchaseComponent> {

    companion object {
        @JvmStatic
        fun createInstance(title: String,
                           desc: String,
                           tnc: String,
                           imageUrl: String,
                           listener: Listener): TokoFoodPurchaseConsentBottomSheet {
            return TokoFoodPurchaseConsentBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PARAM_TITLE, title)
                    putString(PARAM_DESC, desc)
                    putString(PARAM_TNC, tnc)
                    putString(PARAM_IMAGE_URL, imageUrl)
                }
                this.listener = listener
            }
        }

        private const val PARAM_TITLE = "title"
        private const val PARAM_DESC = "desc"
        private const val PARAM_TNC = "tnc"
        private const val PARAM_IMAGE_URL = "image_url"

        private const val NUNITO_TYPOGRAPHY_FONT = "NunitoSansExtraBold.ttf"

        private const val TAG = "TokoFoodPurchaseConsentBottomSheet"
    }

    @Inject
    lateinit var viewModel: TokoFoodPurchaseConsentViewModel

    private val title by lazy {
        arguments?.getString(PARAM_TITLE).orEmpty()
    }
    private val desc by lazy {
        arguments?.getString(PARAM_DESC).orEmpty()
    }
    private val imageUrl by lazy {
        arguments?.getString(PARAM_IMAGE_URL).orEmpty()
    }
    private val tnc by lazy {
        arguments?.getString(PARAM_TNC).orEmpty()
    }
    private val linkTextColor by lazy {
        MethodChecker.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )
    }

    private var viewBinding: LayoutBottomSheetPurchaseConsentBinding? = null

    private var listener: Listener? = null

    interface Listener {
        fun onSuccessAgreeConsent()
        fun onFailedAgreeConsent(throwable: Throwable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initializeView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            renderBottomSheet(it)
        }
        collectAgreeConsentData()
    }

    override fun getComponent(): TokoFoodPurchaseComponent {
        return DaggerTokoFoodPurchaseComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun collectAgreeConsentData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.agreeConsentData.collect { result ->
                when(result) {
                    is Result.Success -> {
                        listener?.onSuccessAgreeConsent()
                    }
                    is Result.Failure -> {
                        listener?.onFailedAgreeConsent(result.error)
                    }
                    is Result.Loading -> {
                        viewBinding?.buttonContinue?.isLoading = true
                    }
                }
            }
        }
    }

    private fun initializeView(): LayoutBottomSheetPurchaseConsentBinding {
        val viewBinding = LayoutBottomSheetPurchaseConsentBinding.inflate(LayoutInflater.from(context))
        this.viewBinding = viewBinding
        initializeBottomSheet(viewBinding)
        return viewBinding
    }

    private fun initializeBottomSheet(viewBinding: LayoutBottomSheetPurchaseConsentBinding) {
        showCloseIcon = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setChild(viewBinding.root)
    }

    private fun renderBottomSheet(viewBinding: LayoutBottomSheetPurchaseConsentBinding) {
        with(viewBinding) {
            imageConsent.setImageUrl(imageUrl)
            textConsentTitle.text = title
            textConsentDescription.text = desc
            checkboxConsentAgreement.setOnCheckedChangeListener { _, isChecked ->
                buttonContinue.isEnabled = isChecked
            }
            textConsentAgreement.setClickableUrlHtml(
                htmlText = tnc,
                applyCustomStyling = {
                    isUnderlineText = false
                    color = linkTextColor
                    context?.let {
                        applyTypographyFont(it)
                    }
                },
                onUrlClicked = ::onLinkClicked
            )

            checkboxConsentAgreement.isChecked = false
            buttonContinue.run {
                isEnabled = false
                setOnClickListener {
                    viewModel.agreeConsent()
                }
            }
        }
    }

    private fun onLinkClicked(url: String, text: String) {
        context?.let {
            RouteManager.route(it, ApplinkConstInternalGlobal.WEBVIEW, url)
        }
    }

    private fun TextPaint.applyTypographyFont(context: Context) {
        try {
            typeface = com.tokopedia.unifyprinciples.getTypeface(context, NUNITO_TYPOGRAPHY_FONT)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

}
