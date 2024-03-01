package com.tokopedia.home_account.explicitprofile.personalize

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.home_account.explicitprofile.di.component.ExplicitProfileComponents
import com.tokopedia.home_account.explicitprofile.personalize.ui.PersonalizeScreen
import com.tokopedia.home_account.R
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR

class PersonalizeQuestionFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModel: ExplicitPersonalizeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NestTheme {
                    PersonalizeScreen(
                        uiState = viewModel.stateGetQuestion,
                        counterState = viewModel.counterState,
                        onRetry = { viewModel.getQuestion() },
                        onSave = { viewModel.saveAnswers() },
                        onSkip = { finishResultOk() },
                        onOptionSelected = { viewModel.itemSelected(it) },
                        saveAnswerState = viewModel.stateSaveAnswer
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        viewModel.stateSaveAnswer.observe(viewLifecycleOwner) {
            when (it) {
                is PersonalizeSaveAnswerResult.Success -> {
                    showSuccessPage()
                }
                is PersonalizeSaveAnswerResult.Failed -> {
                    showToasterError()
                }
                else -> {}
            }
        }
    }

    private fun showToasterError() {
        Toaster.build(requireView(), "message", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun finishResultOk() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun showSuccessPage() {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .replace(abstractionR.id.parent_view, PersonalizeSuccessFragment.createInstance(), PersonalizeSuccessFragment.TAG)
                .commit()
        }
    }


    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ExplicitProfileComponents::class.java).inject(this)
    }

    companion object {
        fun createInstance(): Fragment = PersonalizeQuestionFragment()
    }
}
