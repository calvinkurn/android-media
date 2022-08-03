package com.tokopedia.usercomponents.explicit.view

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitFailedBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitQuestionBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitSuccessBinding
import com.tokopedia.usercomponents.explicit.analytics.ExplicitAnalytics
import com.tokopedia.usercomponents.explicit.domain.model.Property
import javax.inject.Inject

class ExplicitView : CardUnify2, ExplicitAction {

    @Inject
    lateinit var explicitAnalytics: ExplicitAnalytics

    @Inject
    lateinit var viewModel: ExplicitViewContract

    private var bindingQuestion: LayoutWidgetExplicitQuestionBinding? = null
    private var bindingSuccess: LayoutWidgetExplicitSuccessBinding? = null
    private var bindingFailed: LayoutWidgetExplicitFailedBinding? = null

    private var templateName = ""
    private var pageName = ""
    private var pagePath = ""
    private var pageType = ""
    private var preferenceAnswer: Boolean? = null

    private var onWidgetDismissListener: (() -> Unit)? = null
    private var onWidgetFinishListener: (() -> Unit)? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        checkAttribute(attrs)
        initView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        templateName: String,
        pageName: String,
        pagePath: String,
        pageType: String
    ) : super(
        context,
        attrs
    ) {
        this.templateName = templateName
        this.pageName = pageName
        this.pagePath = pagePath
        this.pageType = pageType
        initView()
    }

    private fun checkAttribute(attrs: AttributeSet?) {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ExplicitView,
            0, 0
        ).apply {

            try {
                //you must set template name!
                templateName = getString(R.styleable.ExplicitView_template_name) ?: ""
                pageName = getString(R.styleable.ExplicitView_page_name) ?: ""
                pagePath = getString(R.styleable.ExplicitView_page_path) ?: ""
                pageType = getString(R.styleable.ExplicitView_page_type) ?: ""
            } finally {
                recycle()
            }

            val isEmptyRequireAttribute = templateName.isEmpty() || pageName.isEmpty() || pagePath.isEmpty() || pageType.isEmpty()
            if (GlobalConfig.DEBUG && isEmptyRequireAttribute)
                throw IllegalArgumentException(context.getString(R.string.explicit_error_attribute))
        }
    }

    private fun initView() {
        initBinding()
        initInjector()
        initListener()
    }

    private fun initBinding() {
        bindingQuestion = LayoutWidgetExplicitQuestionBinding.inflate(LayoutInflater.from(context), this, false)
        bindingSuccess = LayoutWidgetExplicitSuccessBinding.inflate(LayoutInflater.from(context), this, false)
        bindingFailed = LayoutWidgetExplicitFailedBinding.inflate(LayoutInflater.from(context), this, false)
    }

    private fun initInjector() {
        context?.let {
            initObserver()
        }
    }

    private fun initObserver() {
        viewModel.getExplicitContent(templateName)

        val lifecycleOwner = context as LifecycleOwner

        viewModel.explicitContent.observe(lifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.first) {
                        setViewQuestion(it.data.second)
                        onQuestionShow()
                    } else {
                        onDismiss()
                    }
                }
                is Fail -> {
                    onFailed()
                }
            }
        }

        viewModel.statusSaveAnswer.observe(lifecycleOwner) {
            when (it) {
                is Success -> onSubmitSuccessShow()
                is Fail -> onFailed()
            }
        }

        viewModel.isQuestionLoading.observe(lifecycleOwner) {
            if (it) {
                onLoading()
            }
        }

        viewModel.statusUpdateState.observeOnce(lifecycleOwner) {
            onWidgetFinishListener?.invoke()
        }
    }

    private fun setViewQuestion(data: Property?) {
        bindingQuestion?.apply {
            txtTitle.text = data?.title
            txtDescription.text = data?.subtitle
            imgIcon.urlSrc = data?.image ?: ""
            btnPositifAction.text = data?.options?.get(0)?.caption
            btnNegatifAction.text = data?.options?.get(1)?.caption
        }
    }

    private fun initListener() {

        bindingQuestion?.imgDismiss?.setOnClickListener {
            explicitAnalytics.trackClickDismissButton(pageName, templateName, pagePath, pageType)
            viewModel.updateState()
            onDismiss()
        }

        bindingSuccess?.imgSuccessDismiss?.setOnClickListener { onDismiss() }

        bindingQuestion?.btnPositifAction?.setOnClickListener {
            onButtonPositiveClicked()
        }

        bindingQuestion?.btnNegatifAction?.setOnClickListener {
            onButtonNegativeClicked()
        }

        bindingFailed?.containerLocalLoad?.refreshBtn?.setOnClickListener {
            if (preferenceAnswer == null)
                viewModel.getExplicitContent(templateName)
            else {
                onLoading()
                saveAnswer()
            }
        }
    }

    private fun saveAnswer() {
        if (preferenceAnswer != null) {
            viewModel.sendAnswer(preferenceAnswer)
        }
    }

    override fun onLoading() {
        showShadow(true)
        bindingQuestion?.apply {
            imgShimmer.visible()
            imgBackground.invisible()
            imgIcon.invisible()
            imgDismiss.invisible()
            txtTitle.invisible()
            txtDescription.invisible()
            btnPositifAction.invisible()
            btnNegatifAction.invisible()
        }
        replaceView(bindingQuestion?.root)
    }

    override fun onQuestionShow() {
        showShadow(true)
        bindingQuestion?.apply {
            imgShimmer.gone()
            imgBackground.visible()
            imgIcon.visible()
            imgDismiss.visible()
            txtTitle.visible()
            txtDescription.visible()
            btnPositifAction.apply {
                visible()
                isLoading = false
                isEnabled = true
            }
            btnNegatifAction.apply {
                visible()
                isLoading = false
                isEnabled = true
            }
        }
        replaceView(bindingQuestion?.root)
        explicitAnalytics.trackClickCard(pageName, templateName, pagePath, pageType)
    }

    override fun onButtonPositiveClicked() {
        explicitAnalytics.trackClickPositifButton(pageName, templateName, pagePath, pageType)
        bindingQuestion?.apply {
            btnPositifAction.isLoading = true
            btnNegatifAction.isEnabled = false
        }
        preferenceAnswer = true
        saveAnswer()
    }

    override fun onButtonNegativeClicked() {
        explicitAnalytics.trackClickNegatifButton(pageName, templateName, pagePath, pageType)
        bindingQuestion?.apply {
            btnNegatifAction.isLoading = true
            btnPositifAction.isEnabled = false
        }
        preferenceAnswer = false
        saveAnswer()
    }

    override fun onSubmitSuccessShow() {
        showShadow(true)
        initSuccessMessageText()
        replaceView(bindingSuccess?.root)
    }

    override fun onDismiss() {
        this.hide()
        onCleared()
        onWidgetDismissListener?.invoke()
    }

    override fun onFailed() {
        showShadow(false)
        setViewFailed()
        replaceView(bindingFailed?.containerLocalLoad)
    }

    private fun replaceView(view: View?) {
        removeAllViews()
        addView(view)
    }

    private fun setViewFailed() {
        bindingFailed?.containerLocalLoad?.apply {
            title?.text = context.getString(R.string.explicit_failed_title)
            description?.text = context.getString(R.string.explicit_failed_subtitle)
        }
    }

    private fun initSuccessMessageText() {
        val message = context.getString(R.string.explicit_succes_message)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    goToExplicitProfilePreference()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            },
            message.indexOf(context.getString(R.string.explicit_succes_message_action)),
            message.indexOf(context.getString(R.string.explicit_succes_message_action)) + context.getString(
                R.string.explicit_succes_message_action
            ).length,
            0
        )
        bindingSuccess?.txtSuccessTitle?.movementMethod = LinkMovementMethod.getInstance()
        bindingSuccess?.txtSuccessTitle?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun goToExplicitProfilePreference() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.EXPLICIT_PROFILE)
        context.startActivity(intent)
    }

    private fun showShadow(isShow: Boolean) {
        this.cardType = if (isShow) TYPE_SHADOW else TYPE_CLEAR
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onCleared()
    }

    override fun onCleared() {
        val lifecycleOwner = context as LifecycleOwner
        viewModel.explicitContent.removeObservers(lifecycleOwner)
        viewModel.statusSaveAnswer.removeObservers(lifecycleOwner)
        viewModel.isQuestionLoading.removeObservers(lifecycleOwner)
        removeAllViews()
        bindingFailed = null
        bindingQuestion = null
        bindingSuccess = null
    }

    fun setOnWidgetDismissListener(listener: () -> Unit) {
        onWidgetDismissListener = listener
    }

    fun setOnWidgetFinishListener(listener: () -> Unit) {
        onWidgetFinishListener = listener
    }

}