package com.tokopedia.kyc_centralized.ui.gotoKyc.main.challenge

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentGotoKycDobChallengeBinding
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.analytics.GotoKycAnalytics
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.DobChallengeExhaustedBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.GetChallengeResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.submit.FinalLoaderParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.getGotoKycErrorMessage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class DobChallengeFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentGotoKycDobChallengeBinding>()

    private var selectedDate = ""
    private var pickedDate = 0
    private var pickedMonth = 0
    private var pickedYear = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DobChallengeViewModel::class.java]
    }

    private val args: DobChallengeFragmentArgs by navArgs()

    private var datePicker : DateTimePickerUnify? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGotoKycDobChallengeBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GotoKycAnalytics.sendViewDobPage(projectId = args.parameter.projectId)

        viewModel.getChallenge(args.parameter.challengeId)

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        binding?.ivDobChallenge?.loadImageWithoutPlaceholder(
            getString(R.string.img_url_goto_kyc_dob_challenge_main)
        )

        binding?.fieldDob?.apply {
            editText.isFocusable = false
            icon1.setImageDrawable(getIconUnifyDrawable(requireContext(), IconUnify.CHEVRON_DOWN))
        }
    }

    private fun initListener() {
        binding?.unifyToolbar?.setNavigationOnClickListener {
            GotoKycAnalytics.sendClickOnButtonBackFromDobPage(args.parameter.projectId)
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding?.fieldDob?.editText?.setOnClickListener {
            showDatePicker()
        }

        binding?.fieldDob?.icon1?.setOnClickListener {
            showDatePicker()
        }

        binding?.btnConfirmation?.setOnClickListener {
            GotoKycAnalytics.sendClickButtonConfirmationDobPage(args.parameter.projectId)
            viewModel.submitChallenge(
                challengeId = args.parameter.challengeId,
                questionId = viewModel.questionId,
                selectedDate = selectedDate
            )
        }
    }

    private fun initObserver() {
        viewModel.getChallenge.observe(viewLifecycleOwner) {
            when (it) {
                is GetChallengeResult.Loading -> {
                    showLoadingScreen()
                }
                is GetChallengeResult.Success -> {
                    showChallengeQuestion()
                }
                is GetChallengeResult.Failed -> {
                    showToaster(it.throwable)
                    activity?.setResult(Activity.RESULT_CANCELED)
                    activity?.finish()
                }
            }
        }

        viewModel.submitChallenge.observe(viewLifecycleOwner) {
            when (it) {
                is SubmitChallengeResult.Loading -> {
                    setButtonLoading(true)
                    binding?.fieldDob?.apply {
                        isInputError = false
                        setMessage(" ")
                    }
                }
                is SubmitChallengeResult.Success -> {
                    setButtonLoading(false)
                    gotoFinalLoader()
                }
                is SubmitChallengeResult.WrongAnswer -> {
                    setButtonLoading(false)
                    binding?.fieldDob?.apply {
                        isInputError = true
                        setMessage(
                            getString(
                                R.string.goto_kyc_dob_challenge_wrong_answer_message,
                                it.attemptsRemaining
                            )
                        )
                    }
                }
                is SubmitChallengeResult.Exhausted -> {
                    setButtonLoading(false)
                    showDobChallengeFailedBottomSheet(
                        cooldownTimeInSeconds = it.cooldownTimeInSeconds,
                        maximumAttemptsAllowed = it.maximumAttemptsAllowed
                    )
                }
                is SubmitChallengeResult.Failed -> {
                    setButtonLoading(false)
                    binding?.fieldDob?.apply {
                        isInputError = true
                        val message = it.throwable.getGotoKycErrorMessage(requireContext())
                        setMessage(message)
                    }
                }
            }
        }
    }

    private fun showLoadingScreen() {
        binding?.apply {
            loader.show()
            unifyToolbar.hide()
            ivDobChallenge.hide()
            tvHeader.hide()
            tvDescription.hide()
            btnConfirmation.hide()
            fieldDob.hide()
        }
    }

    private fun showChallengeQuestion() {
        binding?.apply {
            loader.hide()
            unifyToolbar.show()
            ivDobChallenge.show()
            tvHeader.show()
            tvDescription.show()
            btnConfirmation.show()
            btnConfirmation.isEnabled = false
            fieldDob.show()
        }
    }

    private fun showDatePicker() {
        val calMax = Calendar.getInstance()
        calMax.add(Calendar.YEAR, MAX_YEAR)
        val yearMax = calMax.get(Calendar.YEAR)
        val monthMax = calMax.get(Calendar.MONTH)
        val dayMax = calMax.get(Calendar.DAY_OF_MONTH)
        val maxDate = GregorianCalendar(yearMax, monthMax, dayMax)

        val calMin = Calendar.getInstance()
        calMin.add(Calendar.YEAR, MIN_YEAR)
        val yearMin = calMin.get(Calendar.YEAR)
        val monthMin = calMin.get(Calendar.MONTH)
        val dayMin = calMin.get(Calendar.DAY_OF_MONTH)
        val minDate = GregorianCalendar(yearMin, monthMin, dayMin)

        val defaultDate = if (pickedDate != 0 && pickedMonth != 0 && pickedYear != 0) {
            GregorianCalendar(pickedYear, pickedMonth, pickedDate)
        } else {
            maxDate
        }

        if (datePicker == null) {
            datePicker = DateTimePickerUnify(
                context = requireContext(), minDate = minDate, defaultDate = defaultDate, maxDate = maxDate
            )

            datePicker?.let { datePickerUnify ->
                datePickerUnify.setTitle(getString(R.string.goto_kyc_dob_challenge_choose_dob))

                datePickerUnify.datePickerButton.setOnClickListener {
                    val selectedDatePicker = datePickerUnify.getDate()
                    pickedDate = selectedDatePicker.get(Calendar.DAY_OF_MONTH)
                    pickedMonth = selectedDatePicker.get(Calendar.MONTH)
                    pickedYear = selectedDatePicker.get(Calendar.YEAR)
                    val selectedDate = formatDateParam(
                        dayOfMonth = pickedDate,
                        month = pickedMonth + 1,
                        year = pickedYear
                    )
                    this.selectedDate = selectedDate
                    val date = DateFormatUtils.formatDate(
                        DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MMMM_YYYY, selectedDate
                    )
                    binding?.fieldDob?.editText?.setText(date)
                    binding?.btnConfirmation?.isEnabled = true
                    binding?.fieldDob?.apply {
                        isInputError = false
                        setMessage(" ")
                    }
                    datePickerUnify.dismiss()
                }

                datePickerUnify.setOnDismissListener {
                    datePicker = null
                }

                datePickerUnify.show(childFragmentManager, TAG_BOTTOM_SHEET_DATE_PICKER)
            }
        }
    }

    private fun showDobChallengeFailedBottomSheet(cooldownTimeInSeconds: String, maximumAttemptsAllowed: String) {
        val dobChallengeExhaustedBottomSheet = DobChallengeExhaustedBottomSheet.newInstance(
            projectId = args.parameter.projectId,
            source = args.parameter.pageSource,
            cooldownTimeInSeconds = cooldownTimeInSeconds,
            maximumAttemptsAllowed = maximumAttemptsAllowed
        )

        dobChallengeExhaustedBottomSheet.show(
            childFragmentManager,
            TAG_BOTTOM_SHEET_DOB_CHALLENGE_FAILED
        )

        dobChallengeExhaustedBottomSheet.setOnDismissListener {
            activity?.setResult(KYCConstant.ActivityResult.RESULT_FINISH)
            activity?.finish()
        }
    }

    private fun gotoFinalLoader() {
        val parameter = FinalLoaderParam(
            source = args.parameter.pageSource,
            projectId = args.parameter.projectId,
            challengeId = args.parameter.challengeId,
            gotoKycType = KYCConstant.GotoKycFlow.PROGRESSIVE
        )
        val toFinalLoaderPage = DobChallengeFragmentDirections.actionDobChallengeFragmentToFinalLoaderFragment(parameter)
        view?.findNavController()?.navigate(toFinalLoaderPage)
    }

    private fun setButtonLoading(isLoading: Boolean) {
        binding?.btnConfirmation?.isLoading = isLoading
    }

    private fun showToaster(throwable: Throwable?) {
        val message = throwable.getGotoKycErrorMessage(requireContext())
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun formatDateParam(dayOfMonth: Int, month: Int, year: Int): String {
        return String.format(
            null,
            "%s-%s-%s",
            year.toString(),
            String.format(null,"%02d", month),
            String.format(null,"%02d", dayOfMonth)
        )
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val MAX_YEAR = -17
        private const val MIN_YEAR = -100
        private const val TAG_BOTTOM_SHEET_DATE_PICKER = "bottom sheet date picker"
        private const val TAG_BOTTOM_SHEET_DOB_CHALLENGE_FAILED = "bottom_sheet_dob_challenge_failed"
    }
}
