package com.tokopedia.sellerorder.reschedule_pickup.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.OrderedListSpan
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.FragmentReschedulePickupBinding
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleDetailModel
import com.tokopedia.sellerorder.reschedule_pickup.di.DaggerReschedulePickupComponent
import com.tokopedia.sellerorder.reschedule_pickup.di.ReschedulePickupComponent
import com.tokopedia.sellerorder.reschedule_pickup.presentation.bottomsheet.RescheduleDayBottomSheet
import com.tokopedia.sellerorder.reschedule_pickup.presentation.bottomsheet.RescheduleReasonBottomSheet
import com.tokopedia.sellerorder.reschedule_pickup.presentation.bottomsheet.RescheduleTimeBottomSheet
import com.tokopedia.sellerorder.reschedule_pickup.presentation.dialog.ReschedulePickupLoadingDialog
import com.tokopedia.sellerorder.reschedule_pickup.presentation.viewmodel.ReschedulePickupViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ReschedulePickupFragment : BaseDaggerFragment(), RescheduleTimeBottomSheet.ChooseTimeListener,
    RescheduleDayBottomSheet.ChooseDayListener, RescheduleReasonBottomSheet.ChooseReasonListener {
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ReschedulePickupViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ReschedulePickupViewModel::class.java)
    }

    private var binding by autoClearedNullable<FragmentReschedulePickupBinding>()
    private var toaster: Snackbar? = null
    private var loadingDialog: ReschedulePickupLoadingDialog? = null
    private var orderId: String = ""
    private var courierName: String = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
        val component: ReschedulePickupComponent = DaggerReschedulePickupComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(SomConsts.PARAM_ORDER_ID, "")
            courierName = it.getString(SomConsts.PARAM_COURIER_NAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReschedulePickupBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        getInitialData()
    }

    private fun initObserver() {
        viewModel.reschedulePickupDetail.observe(viewLifecycleOwner, {
            binding?.loaderReschedulePickup?.visibility = View.GONE
            when (it) {
                is Success -> {
                    if (it.data.order.errorMessage.isNotEmpty()) {
                        showErrorToaster(it.data.order.errorMessage, this::getInitialData)
                    }
                    bindDataWithView(it.data)
                }
                is Fail -> {
                    NetworkErrorHelper.showEmptyState(
                        activity,
                        binding?.rootView,
                        this::getInitialData
                    )
                }
            }
        })

        viewModel.saveRescheduleDetail.observe(viewLifecycleOwner, {
            loadingDialog?.dismiss()
            when (it) {
                is Success -> {
                    if (it.data.status == STATUS_CODE_ALREADY_REQUEST_NEW_DRIVER) {
                        showErrorDialog(
                            getString(R.string.title_failed_reschedule_pickup_dialog),
                            it.data.message,
                            getString(R.string.title_cta_error_reschedule_pickup),
                            R.drawable.ic_illustration_gagal
                        )
                    } else {
                        activity?.setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra(SomConsts.RESULT_CONFIRM_SHIPPING, it.data.message)
                        })
                        activity?.finish()
                    }
                }
                is Fail -> {
                    showErrorToaster(
                        ErrorHandler.getErrorMessage(context, it.throwable),
                        this::saveRescheduleDetail
                    )
                }
            }
        })
    }

    private fun getInitialData() {
        showInitialLoading()
        viewModel.getReschedulePickupDetail(orderId)
    }

    private fun showInitialLoading() {
        binding?.let {
            it.loaderReschedulePickup.visibility = View.VISIBLE
            it.etDay.editText.isEnabled = false
            it.etTime.editText.isEnabled = false
            it.etReason.editText.isEnabled = false
        }
    }

    private fun bindDataWithView(data: RescheduleDetailModel) {
        binding?.let {
            it.invoiceOrderDetail.text = data.order.invoice
            it.courierOrderDetail.text = courierName
            val rescheduleGuide = resources.getStringArray(R.array.reschedule_guide)
            val rescheduleGuideBuilder = SpannableStringBuilder()
            rescheduleGuide.forEachIndexed { index, item ->
                rescheduleGuideBuilder.append(
                    if (index == rescheduleGuide.size - 1) item else item + "\n",
                    OrderedListSpan("${index + 1}."),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            it.tipsReschedulePickup.description = rescheduleGuideBuilder

            showSubtitle(requireContext(), it.subtitleReschedulePickup)

            it.etDay.editText.run {
                isEnabled = true
                inputType = InputType.TYPE_NULL
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        openDaySelectionBottomSheet(data.order.chooseDay)
                    }
                }
                setOnClickListener {
                    openDaySelectionBottomSheet(data.order.chooseDay)
                }
            }

            it.etTime.editText.isEnabled = false

            it.etReason.editText.run {
                isEnabled = true
                inputType = InputType.TYPE_NULL
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        openReasonSelectionBottomSheet(data.order.chooseReason)
                    }
                }
                setOnClickListener {
                    openReasonSelectionBottomSheet(data.order.chooseReason)
                }
            }

            it.btnReschedulePickup.setOnClickListener {
                saveRescheduleDetail()
            }
        }
    }

    private fun saveRescheduleDetail() {
        loadingDialog = loadingDialog ?: ReschedulePickupLoadingDialog(requireContext())
        loadingDialog?.show()
        binding?.let {
            viewModel.saveReschedule(
                orderId,
                it.etDay.editText.text.toString(),
                it.etTime.editText.text.toString(),
                it.etReason.editText.text.toString()
            )
        }
    }

    private fun openDaySelectionBottomSheet(daysOption: List<GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption>) {
        RescheduleDayBottomSheet(
            daysOption,
            this
        ).show(parentFragmentManager)
    }

    private fun openTimeSelectionBottomSheet(timeOption: List<GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption>) {
        RescheduleTimeBottomSheet(timeOption, this).show(
            parentFragmentManager
        )
    }

    private fun openReasonSelectionBottomSheet(reasonOption: List<GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.ReasonOption>) {
        RescheduleReasonBottomSheet(reasonOption, this).show(
            parentFragmentManager
        )
    }

    private fun showSubtitle(context: Context, textView: TextView?) {
        val onSubtitleClicked: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_G500
                )
            }
        }
        val boldSpan = StyleSpan(Typeface.BOLD)

        val subtitle = getString(R.string.label_subtitle_reschedule_pick_up)
        val clickableText = "di sini"
        val firstIndex = subtitle.indexOf(clickableText)
        val lastIndex = firstIndex.plus(clickableText.length)

        val subtitleText = SpannableString(subtitle).apply {
            setSpan(onSubtitleClicked, firstIndex, lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(boldSpan, firstIndex, lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        textView?.run {
            movementMethod = LinkMovementMethod.getInstance()
            isClickable = true
            setText(subtitleText, TextView.BufferType.SPANNABLE)
        }
    }

    private fun showErrorDialog(
        titleText: String,
        bodyText: String,
        positiveButton: String,
        image: Int,
    ) {
        // todo: still need to fix this
        val dialog = context?.run {
            DialogUnify(
                this,
                DialogUnify.SINGLE_ACTION,
                DialogUnify.WITH_ILLUSTRATION
            )
        }
        dialog?.let {
            it.setTitle(titleText)
            it.setDescription(bodyText)
            it.setImageDrawable(image)
            it.setPrimaryCTAText(positiveButton)
            it.setPrimaryCTAClickListener {
                it.dismiss()
            }
            it.show()
        }
    }

    private fun setWrapperWatcherOtherReason(wrapper: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val text = binding?.etReasonDetail?.editText?.text
                text?.let {
                    if (s.isNotEmpty() && it.length < OTHER_REASON_MIN_CHAR) {
                        setWrapperError(
                            wrapper,
                            getString(R.string.error_message_reason_below_min_char)
                        )
                    } else if (s.isNotEmpty() && it.length > OTHER_REASON_MAX_CHAR) {
                        setWrapperError(
                            wrapper,
                            getString(R.string.error_message_reason_over_max_char)
                        )
                    } else {
                        setWrapperError(wrapper, null)
                    }
                    validateInput()
                }
            }

            override fun afterTextChanged(text: Editable) {

            }
        }
    }

    private fun setWrapperError(wrapper: TextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.isErrorEnabled = false
        } else {
            wrapper.isErrorEnabled = true
            wrapper.error = s
        }
    }

    private fun setLoader(show: Boolean) {
        if (show) {
            binding?.loaderRescheduleDetail?.visibility = View.VISIBLE
        } else {
            binding?.loaderRescheduleDetail?.visibility = View.GONE
        }
    }

    private fun setRescheduleDetailSummary(courierName: String, day: String, time: String) {
        binding?.layoutRescheduleDetail?.visibility = View.VISIBLE
        binding?.layoutRescheduleDetail?.setHtmlDescription(getString(R.string.template_reschedule_detail_summary, courierName, day, time))
    }

    private fun validateInput() {
        val chosenDay = binding?.etDay?.editText?.text.toString()
        val chosenTime = binding?.etTime?.editText?.text.toString()
        val chosenReason = binding?.etReason?.editText?.text.toString()
        val otherReason = binding?.etReasonDetail?.editText?.text.toString()
        val isReasonValid = if (chosenReason == OTHER_REASON_RESCHEDULE) {
            otherReason.length in OTHER_REASON_MIN_CHAR..OTHER_REASON_MAX_CHAR
        } else chosenReason.isNotEmpty()

        binding?.btnReschedulePickup?.isEnabled =
            (chosenDay.isNotEmpty()) && (chosenTime.isNotEmpty()) && (chosenReason.isNotEmpty()) && (isReasonValid)
    }

    private fun showErrorToaster(
        message: String,
        onClick: () -> Unit
    ) {
        view?.let {
            toaster = Toaster.build(
                it,
                message,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR,
                getString(R.string.title_cta_error_reschedule_pickup)
            ) { onClick() }
        }
        if (toaster?.isShown == false) {
            toaster?.show()
        }
    }

    override fun onDayChosen(dayChosen: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption) {
        binding?.run {
            etDay.editText.setText(dayChosen.day)
            etTime.editText.isEnabled = true
            if (etTime.editText.text.toString().isNotEmpty()) {
                etTime.editText.setText("")
            }
            etTime.editText.run {
                inputType = InputType.TYPE_NULL
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        openTimeSelectionBottomSheet(dayChosen.chooseTime)
                    }
                }
                setOnClickListener {
                    openTimeSelectionBottomSheet(dayChosen.chooseTime)
                }
            }
            validateInput()
        }
    }

    override fun onReasonChosen(reasonChosen: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.ReasonOption) {
        binding?.etReason?.editText?.setText(reasonChosen.reason)
        if (reasonChosen.reason == OTHER_REASON_RESCHEDULE) {
            binding?.etReasonDetail?.run {
                visibility = View.VISIBLE
                editText.addTextChangedListener(setWrapperWatcherOtherReason(textInputLayout))
                requestFocus()
            }
        } else {
            binding?.etReasonDetail?.run {
                visibility = View.GONE
                editText.addTextChangedListener(null)
            }
        }
        validateInput()
    }

    override fun onTimeChosen(timeChosen: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption) {
        binding?.let {
            it.etTime.editText.setText(timeChosen.time)
            setRescheduleDetailSummary(
                courierName,
                it.etDay.editText.text.toString(),
                timeChosen.time
            )
            validateInput()
        }
    }

    companion object {
        private const val OTHER_REASON_RESCHEDULE = "Lainnya (Isi Sendiri)"
        private const val OTHER_REASON_MIN_CHAR = 15
        private const val OTHER_REASON_MAX_CHAR = 160

        // todo confirm this status code to BE
        private const val STATUS_CODE_ALREADY_REQUEST_NEW_DRIVER = "999"
        fun newInstance(bundle: Bundle): ReschedulePickupFragment {
            return ReschedulePickupFragment().apply {
                arguments = Bundle().apply {
                    putString(SomConsts.PARAM_ORDER_ID, bundle.getString(SomConsts.PARAM_ORDER_ID))
                    putString(
                        SomConsts.PARAM_COURIER_NAME,
                        bundle.getString(SomConsts.PARAM_COURIER_NAME)
                    )
                }
            }
        }
    }
}