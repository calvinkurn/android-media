package com.tokopedia.usercomponents.explicit.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitFailedBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitQuestionBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitShimmerBinding
import com.tokopedia.usercomponents.databinding.LayoutWidgetExplicitSuccessBinding
import com.tokopedia.usercomponents.explicit.di.DaggerExplicitComponent
import com.tokopedia.usercomponents.explicit.domain.model.Property
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewModel
import javax.inject.Inject

class ExplicitView(context: Context, attrs: AttributeSet?) :
    CardUnify2(context, attrs) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: ExplicitViewModel? = null

    private val bindingQuestion = LayoutWidgetExplicitQuestionBinding.inflate(LayoutInflater.from(context), this)
    private val bindingSuccess = LayoutWidgetExplicitSuccessBinding.inflate(LayoutInflater.from(context), this)
    private val bindingShimmer = LayoutWidgetExplicitShimmerBinding.inflate(LayoutInflater.from(context), this)
    private val bindingFailed = LayoutWidgetExplicitFailedBinding.inflate(LayoutInflater.from(context), this)

    val templateName = "food_preference"

    init {
        onLoading()
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
                viewModel =
                    ViewModelProvider(it, viewModelFactory).get(ExplicitViewModel::class.java)

                initObserver()
            }

        }
    }

    private fun initObserver() {
        viewModel?.getExplicitContent(templateName)
        viewModel?.explicitContent?.observe(context as LifecycleOwner) {
            when(it) {
                is Success -> {
                    setViewQuestion(it.data)
                    onQuestion()
                }
                is Fail -> {
                    setViewFailed()
                    onFailed()
                }
            }
        }
    }

    private fun setViewQuestion(data: Property) {
        bindingQuestion.apply {
            txtTitle.text = data.title
            txtDescription.text = data.subtitle
            imgIcon.urlSrc = data.image
            btnPositifAction.text = data.options[0].caption
            btnNegatifAction.text = data.options[1].caption
        }
    }

    private fun initListener() {
        bindingQuestion.imgDismiss.setOnClickListener {
            this.hide()
        }

        bindingSuccess.imgSuccessDismiss.setOnClickListener {
            this.hide()
        }

        bindingQuestion.btnPositifAction.setOnClickListener {
            bindingQuestion.apply {
                btnPositifAction.isLoading = true
                btnNegatifAction.isEnabled = false
            }
        }

        bindingQuestion.btnNegatifAction.setOnClickListener {
            bindingQuestion.apply {
                btnNegatifAction.isLoading = true
                btnPositifAction.isEnabled = false
            }
        }

        bindingFailed.containerLocalLoad.refreshBtn?.setOnClickListener {
            viewModel?.getExplicitContent(templateName)
        }
    }

    private fun onLoading() {
        showShadow(true)
        bindingSuccess.containerSuccess.hide()
        bindingQuestion.containerQuestion.hide()
        bindingShimmer.containerShimmer.show()
        bindingFailed.containerLocalLoad.hide()
    }

    private fun onQuestion() {
        showShadow(true)
        bindingSuccess.containerSuccess.hide()
        bindingQuestion.containerQuestion.show()
        bindingShimmer.containerShimmer.hide()
        bindingFailed.containerLocalLoad.hide()
    }

    private fun onSuccess() {
        showShadow(true)
        bindingSuccess.containerSuccess.show()
        bindingQuestion.containerQuestion.hide()
        bindingShimmer.containerShimmer.hide()
        bindingFailed.containerLocalLoad.hide()
    }

    private fun onFailed() {
        showShadow(false)
        bindingSuccess.containerSuccess.hide()
        bindingQuestion.containerQuestion.hide()
        bindingShimmer.containerShimmer.hide()
        bindingFailed.containerLocalLoad.show()
    }

    @SuppressLint("ResourcePackage")
    private fun setViewFailed() {
        bindingFailed.containerLocalLoad.apply {
            title?.text = context.getString(R.string.explicit_failed_title)
            description?.text = context.getString(R.string.explicit_failed_subtitle)
        }
    }

    private fun showShadow(show: Boolean) {
        this.cardType = if (show) TYPE_SHADOW else TYPE_CLEAR
    }

}