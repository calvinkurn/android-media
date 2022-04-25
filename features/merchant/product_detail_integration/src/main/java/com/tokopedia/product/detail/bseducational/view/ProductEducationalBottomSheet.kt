package com.tokopedia.product.detail.bseducational.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.bseducational.data.ProductEducationalResponse
import com.tokopedia.product.detail.bseducational.di.DaggerProductEducationalComponent
import com.tokopedia.product.detail.bseducational.di.ProductEducationalComponent
import com.tokopedia.product.detail.bseducational.di.ProductEducationalModule
import com.tokopedia.product.detail.common.generateTheme
import com.tokopedia.product.detail.common.goToWebView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ProductEducationalBottomSheet : BottomSheetUnify() {

    companion object {
        private const val EDUCATIONAL_SHEET_TAG = "product_educational_bs"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProductEducationalViewModel::class.java)
    }

    private var viewContent: View? = null
    private var contentContainer: ConstraintLayout? = null
    private var shimmerContainer: ConstraintLayout? = null
    private var infoImg: ImageUnify? = null
    private var infoTxt: Typography? = null
    private var infoBtn1: UnifyButton? = null
    private var infoBtn2: UnifyButton? = null
    private var typeParam: String = ""
    private var errorViewStub: ViewStub? = null
    private var btnError: UnifyButton? = null

    fun show(type: String, fragmentManager: FragmentManager) {
        typeParam = type
        show(fragmentManager, EDUCATIONAL_SHEET_TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent()?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    override fun onDismiss(dialog: DialogInterface) {
        (activity as? ProductEducationalActivity)?.finish()
        super.onDismiss(dialog)
    }

    private fun observeData() {
        viewModel.educationalData.observe(viewLifecycleOwner) {
            btnError?.isClickable = true
            shimmerContainer?.hide()

            when (it) {
                is Success -> {
                    errorViewStub?.hide()
                    contentContainer?.show()
                    shimmerContainer?.hide()
                    renderSuccessData(it.data.response)
                }
                is Fail -> {
                    onEducationalError()
                }
            }
        }
    }

    private fun onEducationalError() {
        setTitle("")

        val errorView: View? = if (errorViewStub?.parent != null) {
            errorViewStub?.inflate();
        } else {
            viewContent
        }

        btnError = errorView?.findViewById(R.id.product_educational_error_btn)
        btnError?.setOnClickListener {
            errorViewStub?.hide()
            shimmerContainer?.show()
            btnError?.isClickable = false
            viewModel.getEducationalData()
        }
        errorViewStub?.show()
        contentContainer?.hide()
    }

    private fun renderSuccessData(data: ProductEducationalResponse) {
        setTitle(data.title)
        infoImg?.loadImage(data.icon)
        context?.let {
            infoTxt?.text = HtmlLinkHelper(it, data.description).spannedString
        }

        infoBtn1?.hide()
        infoBtn2?.hide()

        data.educationalButtons.firstOrNull()?.let { value ->
            infoBtn1?.generateTheme(value.buttonColor)
            infoBtn1?.text = value.buttonTitle
            infoBtn1?.setOnClickListener {
                goToApplinkOrWebView(value.buttonApplink, value.buttonWebLink)
            }
            infoBtn1?.show()
        }

        data.educationalButtons.getOrNull(1)?.let { value ->
            infoBtn2?.generateTheme(value.buttonColor)
            infoBtn2?.text = value.buttonTitle
            infoBtn2?.setOnClickListener {
                goToApplinkOrWebView(value.buttonApplink, value.buttonWebLink)
            }
            infoBtn2?.show()
        }
    }

    private fun goToApplinkOrWebView(applink: String, webLink: String) {
        context?.let {
            if (applink.isEmpty() && webLink.isEmpty()) {
                dismiss()
                return@let
            }
            if (applink.isNotEmpty()) {
                route(applink)
            } else {
                route(webLink)
            }
        }
    }

    private fun route(applink: String) {
        context?.let {
            if (RouteManager.isSupportApplink(it, applink)) {
                RouteManager.route(it, applink)
            } else {
                applink.goToWebView(it)
            }
        }
    }

    private fun initBottomSheet() {
        isHideable = true
        clearContentPadding = true

        setShowListener {
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                    if (p1 == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetWrapper.invalidate()
                        bottomSheetWrapper.requestLayout()
                    }
                }
            })
        }

        viewContent = View.inflate(context, R.layout.product_educational_container, null)
        viewContent?.let {
            errorViewStub = it.findViewById(R.id.base_product_educational_error)
            infoImg = it.findViewById(R.id.info_bs_img)
            infoTxt = it.findViewById(R.id.info_bs_txt)
            infoBtn1 = it.findViewById(R.id.info_bs_btn_1)
            infoBtn2 = it.findViewById(R.id.info_bs_btn_2)
            contentContainer = it.findViewById(R.id.educational_content_container)
            shimmerContainer = it.findViewById(R.id.educational_shimmer_container)
        }
        setChild(viewContent)
    }

    private fun getComponent(): ProductEducationalComponent? {
        return DaggerProductEducationalComponent
                .builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .productEducationalModule(ProductEducationalModule(typeParam))
                .build()
    }
}