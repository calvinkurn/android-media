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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitFailedBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitQuestionBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitSuccessBinding
import com.tokopedia.usercomponents.explicit.analytics.ExplicitAnalytics
import com.tokopedia.usercomponents.explicit.domain.model.OptionsItem
import com.tokopedia.usercomponents.explicit.domain.model.Property
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewContract

class ExplicitView constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardUnify(context, attrs), ExplicitAction {

    private var explicitViewContract: ExplicitViewContract? = null

    private var bindingQuestion: LayoutWidgetExplicitQuestionBinding? = null
    private var bindingSuccess: LayoutWidgetExplicitSuccessBinding? = null
    private var bindingFailed: LayoutWidgetExplicitFailedBinding? = null

    private var explicitData = ExplicitData()
    private var preferenceAnswer: Boolean? = null

    private var onWidgetDismissListener: () -> Unit = {}
    private var onWidgetFinishListener: () -> Unit = {}

    override fun setupView(explicitViewContract: ExplicitViewContract, data: ExplicitData) {
        this.explicitViewContract = explicitViewContract

        explicitData = data

        initView()
    }

    override fun isViewAttached() = explicitViewContract != null

    private fun initView() {
        initBinding()
        if (explicitViewContract?.isLoggedIn() == true) {
            initObserver()
            initListener()
        } else {
            onDismiss()
        }
    }

    private fun initBinding() {
        bindingQuestion = LayoutWidgetExplicitQuestionBinding.inflate(LayoutInflater.from(context), this, false)
        bindingSuccess = LayoutWidgetExplicitSuccessBinding.inflate(LayoutInflater.from(context), this, false)
        bindingFailed = LayoutWidgetExplicitFailedBinding.inflate(LayoutInflater.from(context), this, false)
    }

    private fun initObserver() {
        onLoading()
        explicitViewContract?.getExplicitContent(explicitData.templateName)

        val lifecycleOwner = context as LifecycleOwner

        explicitViewContract?.explicitContent?.observe(lifecycleOwner) {
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

        explicitViewContract?.statusSaveAnswer?.observe(lifecycleOwner) {
            when (it) {
                is Success -> onSubmitSuccessShow(it.data.first)
                is Fail -> onFailed()
            }
        }

        explicitViewContract?.statusUpdateState?.observeOnce(lifecycleOwner) {
            onWidgetFinishListener.invoke()
        }
    }

    private fun setViewQuestion(data: Property?) {
        bindingQuestion?.apply {
            txtTitle.text = data?.title
            txtDescription.text = data?.subtitle
            imgIcon.urlSrc = data?.image ?: ""
            btnPositiveAction.text = data?.options?.get(0)?.caption
            btnNegativeAction.text = data?.options?.get(1)?.caption
        }
    }

    private fun initListener() {

        bindingQuestion?.imgDismiss?.setOnClickListener {
            ExplicitAnalytics.trackClickDismissButton(explicitData)
            explicitViewContract?.updateState()
            onDismiss()
        }

        bindingSuccess?.imgSuccessDismiss?.setOnClickListener {
            onDismiss()
            onWidgetFinishListener.invoke()
        }

        bindingQuestion?.btnPositiveAction?.setOnClickListener {
            onButtonPositiveClicked()
        }

        bindingQuestion?.btnNegativeAction?.setOnClickListener {
            onButtonNegativeClicked()
        }

        bindingFailed?.containerLocalLoad?.refreshBtn?.setOnClickListener {
            onLoading()
            if (preferenceAnswer == null) {
                explicitViewContract?.getExplicitContent(explicitData.templateName)
            } else {
                saveAnswer()
            }
        }
    }

    private fun saveAnswer() {
        if (preferenceAnswer != null) {
            explicitViewContract?.sendAnswer(preferenceAnswer)
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
            btnPositiveAction.invisible()
            btnNegativeAction.invisible()
        }
        replaceView(bindingQuestion?.root)
    }

    override fun onQuestionShow() {
        showShadow(true)
        bindingQuestion?.apply {
            imgShimmer.hide()
            imgBackground.visible()
            imgIcon.visible()
            imgDismiss.visible()
            txtTitle.visible()
            txtDescription.visible()
            btnPositiveAction.apply {
                visible()
                isLoading = false
                isEnabled = true
            }
            btnNegativeAction.apply {
                visible()
                isLoading = false
                isEnabled = true
            }
        }
        replaceView(bindingQuestion?.root)
        ExplicitAnalytics.trackQuestionShow(explicitData)
    }

    override fun onButtonPositiveClicked() {
        ExplicitAnalytics.trackClickPositiveButton(explicitData)
        bindingQuestion?.apply {
            btnPositiveAction.isLoading = true
            btnNegativeAction.isEnabled = false
        }
        preferenceAnswer = true
        saveAnswer()
    }

    override fun onButtonNegativeClicked() {
        ExplicitAnalytics.trackClickNegativeButton(explicitData)
        bindingQuestion?.apply {
            btnNegativeAction.isLoading = true
            btnPositiveAction.isEnabled = false
        }
        preferenceAnswer = false
        saveAnswer()
    }

    override fun onSubmitSuccessShow(data: OptionsItem?) {
        showShadow(true)
        initSuccessMessageText(
            data?.message.orEmpty(),
            data?.textApplink.orEmpty(),
            data?.applink.orEmpty()
        )
        replaceView(bindingSuccess?.root)
    }

    override fun onDismiss() {
        this.hide()
        onCleared()
        onWidgetDismissListener.invoke()
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

    private fun initSuccessMessageText(
        messageSuccess: String,
        textApplink: String,
        applink: String
    ) {
        val message = "$messageSuccess $textApplink"
        val indexStar = messageSuccess.length + 1
        val indexEnd = indexStar + textApplink.length

        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    goToExplicitProfilePreference(applink)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            },
            indexStar,
            indexEnd,
            0
        )
        bindingSuccess?.txtSuccessTitle?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    private fun goToExplicitProfilePreference(applink: String) {
        val intent = RouteManager.getIntent(context, applink)
        context.startActivity(intent)
    }

    private fun showShadow(isShow: Boolean) {
        this.cardType = if (isShow) TYPE_SHADOW else TYPE_CLEAR
    }

    override fun onCleared() {
        val lifecycleOwner = context as LifecycleOwner
        explicitViewContract?.explicitContent?.removeObservers(lifecycleOwner)
        explicitViewContract?.statusSaveAnswer?.removeObservers(lifecycleOwner)
        explicitViewContract?.statusUpdateState?.removeObservers(lifecycleOwner)
        removeAllViews()
        bindingFailed = null
        bindingQuestion = null
        bindingSuccess = null
        explicitViewContract = null
    }

    fun setOnWidgetDismissListener(listener: () -> Unit) {
        onWidgetDismissListener = listener
    }

    fun setOnWidgetFinishListener(listener: () -> Unit) {
        onWidgetFinishListener = listener
    }

}
