package com.tokopedia.usercomponents.explicit.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitQuestionBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitShimmerBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitSuccessBinding

class ExplicitView(context: Context, attrs: AttributeSet?) :
    CardUnify2(context, attrs) {

    private val bindingQuestion = LayoutWidgetExplicitQuestionBinding.inflate(LayoutInflater.from(context), this)
    private val bindingSuccess = LayoutWidgetExplicitSuccessBinding.inflate(LayoutInflater.from(context), this)
    private val bindingShimmer = LayoutWidgetExplicitShimmerBinding.inflate(LayoutInflater.from(context), this)

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