package com.tokopedia.sellerorder.reschedule_pickup.presentation.fragment

import android.content.Context
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
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.OrderedListSpan
import com.tokopedia.sellerorder.databinding.FragmentReschedulePickupBinding
import com.tokopedia.sellerorder.reschedule_pickup.di.DaggerReschedulePickupComponent
import com.tokopedia.sellerorder.reschedule_pickup.di.ReschedulePickupComponent
import com.tokopedia.sellerorder.reschedule_pickup.presentation.bottomsheet.RescheduleOptionBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ReschedulePickupFragment : BaseDaggerFragment() {
    @Inject
    lateinit var userSession: UserSessionInterface
    private var binding by autoClearedNullable<FragmentReschedulePickupBinding>()
    private var courierName: String = ""
    private var invoice: String = ""

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
            courierName = it.getString(ARGUMENTS_COURIER_NAME, "")
            invoice = it.getString(ARGUMENTS_INVOICE, "")
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
        initViews()
    }

    private fun initViews() {
        binding?.let {
            it.courierOrderDetail.text = courierName
            it.invoiceOrderDetail.text = invoice
            val items = listOf(
                "Pastikan jadwal pick-up baru sesuai dengan kesepakatan pengiriman dengan pembeli",
                "Ubah jadwal pick-up hanya bisa dilakukan 1 (satu) kali",
                "Pastikan barang sudah siap"
            )
            val builder = SpannableStringBuilder()
            items.forEachIndexed { index, item ->
                builder.append(
                    if (index == items.size - 1) item else item + "\n",
                    OrderedListSpan("${index + 1}."),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            it.tipsReschedulePickup.description = builder

            R.drawable.ic_chevron_down.let { icon ->
                it.etDay.setFirstIcon(icon)
                it.etTime.setFirstIcon(icon)
                it.etReason.setFirstIcon(icon)
            }

            showSubtitle(requireContext(), it.subtitleReschedulePickup)
            it.etDay.editText.run {
                inputType = InputType.TYPE_NULL
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        openDaySelectionBottomSheet()
                    }
                }
                setOnClickListener {
                    openDaySelectionBottomSheet()
                }
            }

            it.etTime.editText.run {
                inputType = InputType.TYPE_NULL
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        openTimeSelectionBottomSheet()
                    }
                }
                setOnClickListener {
                    openTimeSelectionBottomSheet()
                }
            }

            it.etReason.editText.run {
                inputType = InputType.TYPE_NULL
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        openReasonSelectionBottomSheet()
                    }
                }
                setOnClickListener {
                    openReasonSelectionBottomSheet()
                }
            }
        }
    }

    private fun openDaySelectionBottomSheet() {
        val dummy = listOf<String>("Kamis, 9 Desember 2021", "Jumat, 10 Desember 2021")
        RescheduleOptionBottomSheet(dummy, setDayBottomSheetListener()).show(parentFragmentManager)
    }

    private fun openTimeSelectionBottomSheet() {
        val dummy = listOf<String>("08:00 WIB", "09:00 WIB", "10:00 WIB")
        RescheduleOptionBottomSheet(dummy, setTimeBottomSheetListener()).show(parentFragmentManager)
    }

    private fun openReasonSelectionBottomSheet() {
        val dummy = listOf<String>("Toko Tutup", "Pembeli Tidak Ditempat", "Lainnya (Isi Sendiri)")
        RescheduleOptionBottomSheet(dummy, setReasonBottomSheetListener()).show(
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

    private fun showDialogFragment(titleText: String?, bodyText: String?, positiveButton: String?, negativeButton: String?) {
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(titleText ?: "")
            setDescription(bodyText ?: "")
            setPrimaryCTAText(positiveButton ?: "")
            setPrimaryCTAClickListener {
                this.dismiss()
            }
            setSecondaryCTAClickListener {
                dismiss()
            }
            setSecondaryCTAText(negativeButton ?: "")
        }
        dialog.show()
    }


    private fun setDayBottomSheetListener(): RescheduleOptionBottomSheet.ChooseOptionListener {
        return object : RescheduleOptionBottomSheet.ChooseOptionListener {
            override fun onOptionChosen(option: String) {
                binding?.etDay?.editText?.setText(option)
                validateInput()
            }
        }
    }

    private fun setTimeBottomSheetListener(): RescheduleOptionBottomSheet.ChooseOptionListener {
        return object : RescheduleOptionBottomSheet.ChooseOptionListener {
            override fun onOptionChosen(option: String) {
                binding?.etTime?.editText?.setText(option)
                validateInput()
            }
        }
    }

    private fun setReasonBottomSheetListener(): RescheduleOptionBottomSheet.ChooseOptionListener {
        return object : RescheduleOptionBottomSheet.ChooseOptionListener {
            override fun onOptionChosen(option: String) {
                binding?.etReason?.editText?.setText(option)
                if (option == OTHER_REASON_RESCHEDULE) {
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
                        setWrapperError(wrapper, "Min. 15 karakter")
                    } else if (s.isNotEmpty() && it.length > OTHER_REASON_MAX_CHAR) {
                        setWrapperError(wrapper, "Sudah mencapai maks. char")
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

    private fun setRescheduleDetailSummary(summary: String) {
        binding?.layoutRescheduleDetail?.visibility = View.VISIBLE
        binding?.rescheduleDetail?.text = summary
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
        view: View?,
        message: String?,
    ) {
        message?.run {
            view?.let {
                Toaster.build(
                    it,
                    message,
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR,
                ).show()
            }
        }
    }

    companion object {
        private const val OTHER_REASON_RESCHEDULE = "Lainnya (Isi Sendiri)"
        private const val OTHER_REASON_MIN_CHAR = 15
        private const val OTHER_REASON_MAX_CHAR = 160
        private const val ARGUMENTS_COURIER_NAME = "ARGUMENTS_COURIER_NAME"
        private const val ARGUMENTS_INVOICE = "ARGUMENTS_INVOICE"
        fun newInstance(bundle: Bundle): ReschedulePickupFragment {
            return ReschedulePickupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENTS_COURIER_NAME, bundle.getString(ARGUMENTS_COURIER_NAME))
                    putString(ARGUMENTS_INVOICE, bundle.getString(ARGUMENTS_INVOICE))
                }
            }
        }
    }
}