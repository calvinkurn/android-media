package com.tokopedia.usercomponents.explicitglobalcomponent.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitGlobalComponentQuestionBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitGlobalComponentShimmerBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitGlobalComponentSuccessBinding

class ExplicitGlobalComponentView(context: Context, attrs: AttributeSet?) :
    CardUnify2(context, attrs) {

    private val bindingQuestion = LayoutWidgetExplicitGlobalComponentQuestionBinding.inflate(LayoutInflater.from(context), this)
    private val bindingSuccess = LayoutWidgetExplicitGlobalComponentSuccessBinding.inflate(LayoutInflater.from(context), this)
    private val bindingShimmer = LayoutWidgetExplicitGlobalComponentShimmerBinding.inflate(LayoutInflater.from(context), this)

    init {
        onLoading()
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
        bindingSuccess.containerSuccess.hide()
        bindingQuestion.containerQuestion.hide()
        bindingShimmer.containerShimmer.show()
    }

    private fun onQuestion() {
        bindingSuccess.containerSuccess.hide()
        bindingQuestion.containerQuestion.show()
        bindingShimmer.containerShimmer.hide()
    }

    private fun onSuccess() {
        bindingSuccess.containerSuccess.show()
        bindingQuestion.containerQuestion.hide()
        bindingShimmer.containerShimmer.hide()
    }

    private fun setViewSuccess() {
        bindingSuccess.containerSuccess.show()
        bindingQuestion.containerQuestion.hide()
    }
}