package com.tokopedia.sellerpersona.view.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.data.remote.model.QuestionnaireAnswerParam
import com.tokopedia.sellerpersona.databinding.FragmentPersonaQuestionnaireBinding
import com.tokopedia.sellerpersona.view.adapter.QuestionnairePagerAdapter
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.sellerpersona.view.viewmodel.QuestionnaireViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 24/01/23.
 */

class PersonaQuestionnaireFragment : BaseFragment<FragmentPersonaQuestionnaireBinding>() {

    companion object {
        private const val SLIDER_FIRST_INDEX = 0
        private const val PROGRESS_DURATION = 200L
        private const val MAX_PROGRESS = 100
        private const val PROGRESS_ATTR_TAG = "progress"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: QuestionnaireViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(QuestionnaireViewModel::class.java)
    }
    private val pagerAdapter by lazy { QuestionnairePagerAdapter() }
    private val progressBarInterpolator by lazy { AccelerateDecelerateInterpolator() }
    private var lastPosition = Int.ZERO

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
        setupView()
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
                    setPreviousButtonVisibility(position)
                    updateProgressBar(position)
                    lastPosition = position
                }
            })
        }
    }

    private fun updateProgressBar(position: Int) {
        binding?.progressBarPersonaQuestionnaire.run {
            val itemCount = pagerAdapter.itemCount
            val currentPosition = position.plus(Int.ONE)
            val isGoToNext = lastPosition < position
            val isFirstPage = position == Int.ZERO && position == lastPosition
            val animator = if (isGoToNext || isFirstPage) {
                val start = getPercentValue(currentPosition.minus(Int.ONE), itemCount)
                val end = getPercentValue(currentPosition, itemCount)
                ObjectAnimator.ofInt(this, PROGRESS_ATTR_TAG, start, end)
                    .apply {
                        duration = PROGRESS_DURATION
                        interpolator = progressBarInterpolator
                    }
            } else {
                val start = getPercentValue(currentPosition, itemCount)
                val end = getPercentValue(currentPosition.plus(Int.ONE), itemCount)
                ObjectAnimator.ofInt(this, PROGRESS_ATTR_TAG, end, start)
                    .apply {
                        duration = PROGRESS_DURATION
                        interpolator = progressBarInterpolator
                    }
            }
            animator.start()
        }
    }

    private fun getPercentValue(value: Int, maxValue: Int): Int {
        return try {
            value.times(MAX_PROGRESS).div(maxValue)
        } catch (e: ArithmeticException) {
            Timber.e(e)
            Int.ZERO
        }
    }

    private fun setupView() {
        binding?.run {
            btnSpNext.setOnClickListener {
                val lastSlideIndex = pagerAdapter.itemCount.minus(Int.ONE)
                val isLastSlide = vpSpQuestionnaire.currentItem == lastSlideIndex
                if (isLastSlide) {
                    submitAnswer(it)
                } else {
                    moveToNextQuestion()
                }
            }
            btnSpPrev.setOnClickListener {
                moveToPreviousQuestion()
            }
        }
    }

    private fun moveToPreviousQuestion() {
        val currentPosition = binding?.vpSpQuestionnaire?.currentItem.orZero()
        binding?.vpSpQuestionnaire?.setCurrentItem(currentPosition.minus(Int.ONE), true)
    }

    private fun moveToNextQuestion() {
        val currentPosition = binding?.vpSpQuestionnaire?.currentItem.orZero()
        binding?.vpSpQuestionnaire?.setCurrentItem(currentPosition.plus(Int.ONE), true)
    }

    private fun submitAnswer(view: View) {
        binding?.run {
            btnSpPrev.isEnabled = false
            btnSpNext.isLoading = true
            val answers = pagerAdapter.getPages().map { pager ->
                QuestionnaireAnswerParam(
                    id = pager.id.toLongOrZero(),
                    answers = pager.options.orEmpty().filter { it.isSelected }.map { it.value }
                )
            }
            viewModel.submitAnswer(answers)
            viewModel.setPersonaResult.observeOnce(viewLifecycleOwner) {
                btnSpPrev.isEnabled = true
                btnSpNext.isLoading = false
                when (it) {
                    is Success -> {
                        view.findNavController().navigate(R.id.actionQuestionnaireToResult)
                    }
                    is Fail -> {

                    }
                }
            }
        }
    }

    private fun setPreviousButtonVisibility(position: Int) {
        binding?.run {
            val shouldShowButton = position != SLIDER_FIRST_INDEX
            btnSpPrev.isVisible = shouldShowButton
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
        binding?.run {
            progressBarPersonaQuestionnaire.max = MAX_PROGRESS
            vpSpQuestionnaire.post {
                pagerAdapter.setPages(data)
            }
        }
    }

    private fun showErrorState(throwable: Throwable) {
        throwable.printStackTrace()
    }

    private fun fetchQuestionnaire() {
        viewModel.fetchQuestionnaire()
    }
}