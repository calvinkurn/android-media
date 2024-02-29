package com.tokopedia.home_account.explicitprofile.personalize

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.home_account.explicitprofile.di.component.ExplicitProfileComponents
import com.tokopedia.home_account.explicitprofile.personalize.ui.PersonalizeScreen
import com.tokopedia.nest.principles.ui.NestTheme
import javax.inject.Inject

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
                        uiState = viewModel.uiState,
                        counterState = viewModel.counterState,
                        onRetry = { viewModel.getQuestion() },
                        onSave = {  },
                        onSkip = { onSkip() },
                        onOptionSelected = { viewModel.itemSelected(it) }
                    )
                }
            }
        }
    }

    private fun onSkip() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }


    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ExplicitProfileComponents::class.java).inject(this)
    }

    companion object {
        fun createInstance(): Fragment = PersonalizeQuestionFragment()
    }
}
