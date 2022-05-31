package com.tokopedia.usercomponents.explicit.view

import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitFailedBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitQuestionBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitSuccessBinding
import com.tokopedia.usercomponents.explicit.di.DaggerExplicitComponent
import com.tokopedia.usercomponents.explicit.domain.model.Property
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewModel
import javax.inject.Inject

class ExplicitView : CardUnify2 {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: ExplicitViewModel? = null

    private val bindingQuestion = LayoutWidgetExplicitQuestionBinding.inflate(LayoutInflater.from(context), this, false)
    private val bindingSuccess = LayoutWidgetExplicitSuccessBinding.inflate(LayoutInflater.from(context), this, false)
    private val bindingFailed = LayoutWidgetExplicitFailedBinding.inflate(LayoutInflater.from(context), this, false)

    private var templateName = ""

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        checkAttribute(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, templateName: String) : super(
        context,
        attrs
    ) {
        this.templateName = templateName
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

                if (GlobalConfig.DEBUG && templateName.isEmpty())
                    throw IllegalArgumentException(
                        """
                            Template name is NULL! You must declare template name!
                            if you use xml, please declare
                            example: app:template_name="PUT_YOUR_TEMPLATE_NAME"
                            if you create from programmatically please put your template name in AttributeSet or when create view
                            example: val explicitView = ExplicitView(context, attrs, "PUT_YOUR_TEMPLATE_NAME")
                        """.trimIndent()
                    )
            } finally {
                recycle()
            }
        }
    }

    private fun initView() {
        initInjector()
        initListener()
    }

    private fun initInjector() {
        context?.let {
            val component = DaggerExplicitComponent.builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
            component.inject(this)

            if (it is AppCompatActivity) {
                viewModel = ViewModelProvider(it, viewModelFactory).get(ExplicitViewModel::class.java)

                initObserver()
            }
        }
    }

    private fun initObserver() {
        viewModel?.getExplicitContent(templateName)

        val lifecycleOwner = context as LifecycleOwner

        viewModel?.explicitContent?.observe(lifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.first) {
                        setViewQuestion(it.data.second)
                        onQuestion()
                    } else {
                        dismiss()
                    }
                }
                is Fail -> {
                    onFailed()
                }
            }
        }

        viewModel?.statusSaveAnswer?.observe(lifecycleOwner) {
            when (it) {
                is Success -> onSuccess()
                is Fail -> onFailed()
            }
        }

        viewModel?.isQuestionLoading?.observe(lifecycleOwner) {
            if (it) {
                onLoading()
            }
        }
    }

    private fun setViewQuestion(data: Property?) {
        bindingQuestion.apply {
            txtTitle.text = data?.title
            txtDescription.text = data?.subtitle
            imgIcon.urlSrc = data?.image ?: ""
            btnPositifAction.text = data?.options?.get(0)?.caption
            btnNegatifAction.text = data?.options?.get(1)?.caption
        }
    }

    private fun initListener() {
        bindingQuestion.imgDismiss.setOnClickListener {
            viewModel?.updateState()
            dismiss()
        }

        bindingSuccess.imgSuccessDismiss.setOnClickListener { dismiss() }

        bindingQuestion.btnPositifAction.setOnClickListener {
            bindingQuestion.apply {
                btnPositifAction.isLoading = true
                btnNegatifAction.isEnabled = false
            }
            viewModel?.sendAnswer(bindingQuestion.btnPositifAction.text.toString())
        }

        bindingQuestion.btnNegatifAction.setOnClickListener {
            bindingQuestion.apply {
                btnNegatifAction.isLoading = true
                btnPositifAction.isEnabled = false
            }
            viewModel?.sendAnswer(bindingQuestion.btnNegatifAction.text.toString())
        }

        bindingFailed.containerLocalLoad.refreshBtn?.setOnClickListener {
            viewModel?.getExplicitContent(templateName)
        }
    }

    private fun dismiss() {
        this.hide()
    }

    private fun onLoading() {
        showShadow(true)
        bindingQuestion.apply {
            imgShimmer.visible()
            imgBackground.invisible()
            imgIcon.invisible()
            imgDismiss.invisible()
            txtTitle.invisible()
            txtDescription.invisible()
            btnPositifAction.invisible()
            btnNegatifAction.invisible()
        }
        replaceView(bindingQuestion.root)
    }

    private fun onQuestion() {
        showShadow(true)
        bindingQuestion.apply {
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
        replaceView(bindingQuestion.root)
    }

    private fun onSuccess() {
        showShadow(true)
        initSuccessMessageText()
        replaceView(bindingSuccess.root)
    }

    private fun onFailed() {
        showShadow(false)
        setViewFailed()
        replaceView(bindingFailed.containerLocalLoad)
    }

    private fun replaceView(view: View) {
        removeAllViews()
        addView(view)
    }

    @SuppressLint("ResourcePackage")
    private fun setViewFailed() {
        bindingFailed.containerLocalLoad.apply {
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
        bindingSuccess.txtSuccessTitle.movementMethod = LinkMovementMethod.getInstance()
        bindingSuccess.txtSuccessTitle.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun goToExplicitProfilePreference() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.EXPLICIT_PROFILE)
        context.startActivity(intent)
    }

    private fun showShadow(show: Boolean) {
        this.cardType = if (show) TYPE_SHADOW else TYPE_CLEAR
    }
}