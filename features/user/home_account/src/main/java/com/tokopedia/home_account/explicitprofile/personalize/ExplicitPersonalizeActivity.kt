package com.tokopedia.home_account.explicitprofile.personalize

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.explicitprofile.di.component.ExplicitProfileComponents
import com.tokopedia.home_account.explicitprofile.personalize.ui.PersonalizeScreen
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExplicitPersonalizeActivity: BaseSimpleActivity(), HasComponent<ExplicitProfileComponents> {

    @Inject
    lateinit var viewModel: ExplicitPersonalizeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
        initInjector()
        initObserver()
        setView()
    }

    private fun setView() {
        setContent {
            PersonalizeScreen(
                uiState = viewModel.stateGetQuestion,
                counterState = viewModel.counterState,
                onSave = {
                    ExplicitPersonalizeAnalytics.sendClickOnSimpanEvent()
                    viewModel.saveAnswers()
                },
                onSkip = {
                    ExplicitPersonalizeAnalytics.sendClickOnNantiSajaEvent()
                    finishResultOk()
                },
                onOptionSelected = { viewModel.itemSelected(it) },
                saveAnswerState = viewModel.stateSaveAnswer,
                onSuccessSaveAnswer = { runTimer() }
            )
        }
    }

    private fun runTimer() {
        lifecycleScope.launch(Dispatchers.Default) {
            delay(THREE_SECOND)
            withContext(Dispatchers.Main) {
                finishResultOk()
            }
        }
    }

    private fun initObserver() {
        viewModel.stateSaveAnswer.observe(this) {
            when (it) {
                is PersonalizeSaveAnswerResult.Success -> {
                    ExplicitPersonalizeAnalytics.sendViewOnLoadingSimpanPilihanEvent()
                }
                is PersonalizeSaveAnswerResult.Failed -> {
                    showToasterError(it.throwable)
                }
                else -> {}
            }
        }

        viewModel.stateGetQuestion.observe(this) {
            when (it) {
                is ExplicitPersonalizeResult.Success -> {
                    ExplicitPersonalizeAnalytics.sendViewOnExplicitPageEvent()
                }
                is ExplicitPersonalizeResult.Failed -> {
                    finishResultOk()
                }
                else -> {}
            }
        }
    }

    private fun showToasterError(throwable: Throwable) {
        val root = this.findViewById<ViewGroup>(android.R.id.content)
        val message = ErrorHandler.getErrorMessage(this, throwable)
        Toaster.build(root, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun finishResultOk() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun getNewFragment(): Fragment? = null
    override fun getComponent(): ExplicitProfileComponents =
        ActivityComponentFactory.instance.createExplicitProfileComponent(application)

    companion object {
        private const val THREE_SECOND = 3000L
    }
}
