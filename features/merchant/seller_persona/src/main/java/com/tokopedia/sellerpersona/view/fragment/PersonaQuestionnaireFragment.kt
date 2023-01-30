package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.sellerpersona.databinding.FragmentPersonaQuestionnaireBinding
import com.tokopedia.sellerpersona.view.adapter.QuestionnairePagerAdapter
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.sellerpersona.view.viewmodel.QuestionnaireViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 24/01/23.
 */

class PersonaQuestionnaireFragment : BaseFragment<FragmentPersonaQuestionnaireBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: QuestionnaireViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(QuestionnaireViewModel::class.java)
    }

    private val pagerAdapter by lazy {
        QuestionnairePagerAdapter()
    }

    override fun bind(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonaQuestionnaireBinding {
        return FragmentPersonaQuestionnaireBinding.inflate(layoutInflater, container, false)
    }

    override fun inject() {
        daggerComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchQuestionnaire()
        setupViewPager()
        observeQuestionnaire()
    }

    private fun setupViewPager() {
        binding?.vpSpQuestionnaire?.run {
            adapter = pagerAdapter
            isUserInputEnabled = false

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                }
            })
        }
    }

    private fun observeQuestionnaire() {
        viewLifecycleOwner.observe(viewModel.questionnaire) {
            when (it) {
                is Success -> showQuestionnaire(it.data)
                is Fail -> showErrorState(it.throwable)
            }
        }
    }

    private fun showQuestionnaire(data: List<QuestionnairePagerUiModel>) {
        binding?.vpSpQuestionnaire?.post {
            pagerAdapter.setPages(data)
        }
    }

    private fun showErrorState(throwable: Throwable) {

    }

    private fun fetchQuestionnaire() {
        viewModel.fetchQuestionnaire()
    }
}