package com.tokopedia.sellerpersona.view.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.data.remote.model.QuestionnaireAnswerParam
import com.tokopedia.sellerpersona.databinding.FragmentPersonaQuestionnaireBinding
import com.tokopedia.sellerpersona.view.adapter.QuestionnairePagerAdapter
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.sellerpersona.view.viewmodel.QuestionnaireViewModel
import com.tokopedia.unifycomponents.Toaster
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
    private val backPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@PersonaQuestionnaireFragment.handleOnBackPressed()
            }
        }
    }

    override fun bind(
        layoutInflater: LayoutInflater, container: ViewGroup?
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
        setupOnBackPressed()
    }

    private fun setupOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, backPressedCallback
        )
    }

    private fun handleOnBackPressed() {
        context?.let {
            val isAnyChanges = pagerAdapter.getPages().any { p ->
                p.options?.any { o -> o.isSelected }.orFalse()
            }
            if (!isAnyChanges) {
                findNavController().navigateUp()
                return
            }

            val dialog = DialogUnify(
                it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE
            )
            with(dialog) {
                setTitle(it.getString(R.string.sp_poup_exit_title))
                setDescription(it.getString(R.string.sp_popup_exit_description))
                setPrimaryCTAText(it.getString(R.string.sp_popup_exit_primary_cta))
                setPrimaryCTAClickListener {
                    dismiss()
                }
                setSecondaryCTAText(it.getString(R.string.sp_popup_exit_secondary_cta))
                setSecondaryCTAClickListener {
                    findNavController().navigateUp()
                    dismiss()
                }
                show()
            }
        }
    }

    private fun setupViewPager() {
        binding?.vpSpQuestionnaire?.run {
            currentItem
            adapter = pagerAdapter
            isUserInputEnabled = false

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setPreviousButtonVisibility(position)
                    updateProgressBar(position)
                    lastPosition = position
                    setOnOptionItemSelected()
                }
            })

            pagerAdapter.onOptionItemSelectedListener(::setOnOptionItemSelected)
        }
    }

    private fun setOnOptionItemSelected() {
        binding?.run {
            val isAlreadySelecting = pagerAdapter.getPages()
                .getOrNull(vpSpQuestionnaire.currentItem)?.options?.any { it.isSelected }.orFalse()
            btnSpNext.isEnabled = isAlreadySelecting
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
                ObjectAnimator.ofInt(this, PROGRESS_ATTR_TAG, start, end).apply {
                    duration = PROGRESS_DURATION
                    interpolator = progressBarInterpolator
                }
            } else {
                val start = getPercentValue(currentPosition, itemCount)
                val end = getPercentValue(currentPosition.plus(Int.ONE), itemCount)
                ObjectAnimator.ofInt(this, PROGRESS_ATTR_TAG, end, start).apply {
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
            btnSpNext.isEnabled = false
            btnSpNext.setOnClickListener {
                val currentPosition = binding?.vpSpQuestionnaire?.currentItem.orZero()
                val isAlreadySelecting = pagerAdapter.getPages()
                    .getOrNull(currentPosition)?.options?.any { it.isSelected }.orFalse()
                handleOnNextClicked(isAlreadySelecting)
            }
            btnSpPrev.setOnClickListener {
                moveToPreviousQuestion()
            }
        }
    }

    private fun handleOnNextClicked(isAlreadySelecting: Boolean) {
        if (isAlreadySelecting) {
            val lastSlideIndex = pagerAdapter.itemCount.minus(Int.ONE)
            val isLastSlide = binding?.vpSpQuestionnaire?.currentItem == lastSlideIndex
            if (isLastSlide) {
                submitAnswer()
            } else {
                moveToNextQuestion()
            }
        }
    }

    private fun moveToPreviousQuestion() {
        val currentPosition = binding?.vpSpQuestionnaire?.currentItem.orZero()
        binding?.vpSpQuestionnaire?.setCurrentItem(currentPosition.minus(Int.ONE), true)
    }

    private fun moveToNextQuestion() {
        val currentPosition = binding?.vpSpQuestionnaire?.currentItem.orZero()
        val isAlreadySelecting =
            pagerAdapter.getPages().getOrNull(currentPosition)?.options?.any { it.isSelected }
                .orFalse()
        if (isAlreadySelecting) {
            binding?.vpSpQuestionnaire?.setCurrentItem(currentPosition.plus(Int.ONE), true)
        }
    }

    private fun submitAnswer() {
        binding?.run {
            val answers = pagerAdapter.getPages().map { pager ->
                QuestionnaireAnswerParam(
                    id = pager.id.toLongOrZero(),
                    answers = pager.options.orEmpty().filter { it.isSelected }.map { it.value }
                )
            }
            showSubmitQuizLoadingState()
            viewModel.submitAnswer(answers)
            viewModel.setPersonaResult.observeOnce(viewLifecycleOwner) {
                when (it) {
                    is Success -> {
                        val action = PersonaQuestionnaireFragmentDirections
                            .actionQuestionnaireToResult(paramPersona = it.data)
                        findNavController().navigate(action)
                    }
                    is Fail -> {
                        dismissSubmitQuizLoadingState()
                        showErrorToaster()
                    }
                }
            }
        }
    }

    private fun showSubmitQuizLoadingState() {
        binding?.run {
            loaderSellerPersona.visible()
            tvSpQuizLoadingTitle.visible()
            tvSpQuizLoadingDesc.visible()
            btnSpNext.gone()
            btnSpPrev.gone()
            vpSpQuestionnaire.gone()
            progressBarFramePersona.gone()
        }
    }

    private fun dismissSubmitQuizLoadingState() {
        binding?.run {
            loaderSellerPersona.gone()
            tvSpQuizLoadingTitle.gone()
            tvSpQuizLoadingDesc.gone()
            btnSpNext.visible()
            btnSpPrev.visible()
            vpSpQuestionnaire.visible()
            progressBarFramePersona.visible()
        }
    }

    private fun showErrorToaster() {
        view?.run {
            val dp48 = context.resources.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl6
            )
            Toaster.toasterCustomBottomHeight = dp48
            Toaster.build(
                rootView,
                context.getString(R.string.sp_toaster_error_message),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                context.getString(R.string.sp_oke)
            ).show()
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
            binding?.loaderSellerPersona?.gone()
            when (it) {
                is Success -> showQuestionnaire(it.data)
                is Fail -> showErrorState()
            }
        }
    }

    private fun showQuestionnaire(data: List<QuestionnairePagerUiModel>) {
        binding?.run {
            pagerAdapter.setPages(data)
            errorViewSpQuestionnaire.gone()
            btnSpPrev.visible()
            progressBarFramePersona.visible()
            btnSpNext.visible()
            vpSpQuestionnaire.visible()
            progressBarPersonaQuestionnaire.max = MAX_PROGRESS
        }
    }

    private fun showErrorState() {
        binding?.run {
            errorViewSpQuestionnaire.visible()
            btnSpPrev.gone()
            btnSpNext.gone()
            errorViewSpQuestionnaire.setOnActionClicked {
                viewModel.fetchQuestionnaire()
            }
        }
    }

    private fun fetchQuestionnaire() {
        binding?.loaderSellerPersona?.visible()
        viewModel.fetchQuestionnaire()
    }
}