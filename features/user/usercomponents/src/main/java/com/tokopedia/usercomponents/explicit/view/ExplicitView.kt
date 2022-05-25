package com.tokopedia.usercomponents.explicit.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitFailedBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitQuestionBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitShimmerBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitSuccessBinding

class ExplicitView(context: Context, attrs: AttributeSet?) :
    CardUnify2(context, attrs) {

    private val bindingQuestion = LayoutWidgetExplicitQuestionBinding.inflate(LayoutInflater.from(context), this)
    private val bindingSuccess = LayoutWidgetExplicitSuccessBinding.inflate(LayoutInflater.from(context), this)
    private val bindingShimmer = LayoutWidgetExplicitShimmerBinding.inflate(LayoutInflater.from(context), this)
    private val bindingFailed = LayoutWidgetExplicitFailedBinding.inflate(LayoutInflater.from(context), this)

    init {
        onFailed()
        initListener()
    }

    private fun initListener() {
        bindingQuestion.imgDismiss.setOnClickListener {
            this.hide()
        }

        bindingSuccess.imgSuccessDismiss.setOnClickListener {
            this.hide()
        }

        bindingQuestion.btnPositifAction.setOnClickListener {
            setViewSuccess()
        }
    }

    private fun onLoading() {
        showShadow(true)
        bindingSuccess.containerSuccess.hide()
        bindingQuestion.containerQuestion.hide()
        bindingShimmer.containerShimmer.show()
        bindingFailed.root.hide()
    }

    private fun onQuestion() {
        showShadow(true)
        bindingSuccess.containerSuccess.hide()
        bindingQuestion.containerQuestion.show()
        bindingShimmer.containerShimmer.hide()
        bindingFailed.root.hide()
    }

    private fun onSuccess() {
        showShadow(true)
        bindingSuccess.containerSuccess.show()
        bindingQuestion.containerQuestion.hide()
        bindingShimmer.containerShimmer.hide()
        bindingFailed.root.hide()
    }

    private fun onFailed() {
        showShadow(false)
        bindingSuccess.containerSuccess.hide()
        bindingQuestion.containerQuestion.hide()
        bindingShimmer.containerShimmer.hide()
        bindingFailed.root.show()
    }

    private fun showShadow(show: Boolean) {
        this.cardType = if (show) TYPE_SHADOW else TYPE_CLEAR
    }

    private fun setViewSuccess() {
        bindingSuccess.containerSuccess.show()
        bindingQuestion.containerQuestion.hide()
    }
}