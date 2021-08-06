package com.tokopedia.hotel.cancellation.presentation.fragment

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitParam
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationActivity
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity
import com.tokopedia.hotel.cancellation.presentation.adapter.HotelCancellationReasonAdapter
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.util.HotelTextHyperlinkUtil
import com.tokopedia.hotel.databinding.FragmentHotelCancellationReasonBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by jessica on 30/04/20
 */

class HotelCancellationReasonFragment : HotelBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var cancellationViewModel: HotelCancellationViewModel
    private var binding by autoClearedNullable<FragmentHotelCancellationReasonBinding>()

    lateinit var reasonAdapter: HotelCancellationReasonAdapter

    private var invoiceId: String = ""

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            invoiceId = it.getString(EXTRA_INVOICE_ID) ?: ""
        }

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            cancellationViewModel = viewModelProvider.get(HotelCancellationViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupKeyboardBehaviour()
    }

    private fun setupKeyboardBehaviour() {
        view?.let {
            it.viewTreeObserver.addOnGlobalLayoutListener(keyboardAppearsListener)
        }
    }

    private var keyboardAppearsListener = ViewTreeObserver.OnGlobalLayoutListener {
        view?.apply {
            val r = Rect()
            getWindowVisibleDisplayFrame(r)
            if (rootView.height - (r.bottom - r.top) > SOFT_KEYBOARD_HEIGHT) {
                binding?.hotelCancellationPageFooter?.hide()
            } else {
                binding?.hotelCancellationPageFooter?.show()
            }
        }
    }

    override fun onDestroy() {
        view?.apply {
            viewTreeObserver.removeOnGlobalLayoutListener(keyboardAppearsListener)
        }
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        (activity as HotelCancellationActivity).updateSubtitle(getString(R.string.hotel_cancellation_page_2_subtitle))
    }

    override fun onErrorRetryClicked() {
        getCancellationData()
    }

    private fun getCancellationData() {
        cancellationViewModel.getCancellationData(invoiceId, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cancellationViewModel.cancellationData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    initView(it.data)
                }
                is Fail -> {

                }
            }
        })
    }

    fun initView(hotelCancellationModel: HotelCancellationModel) {
        //When page loaded, initially the button is disabled first until user choose reason
        binding?.hotelCancellationButtonNext?.isEnabled = false

        binding?.hotelCancellationPageFooter?.highlightColor = Color.TRANSPARENT
        binding?.hotelCancellationPageFooter?.movementMethod = LinkMovementMethod.getInstance()
        binding?.hotelCancellationPageFooter?.setText(HotelTextHyperlinkUtil.getSpannedFromHtmlString(requireContext(),
                hotelCancellationModel.footer.desc, hotelCancellationModel.footer.links), TextView.BufferType.SPANNABLE)

        binding?.hotelCancellationReasonRv?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.hotelCancellationReasonRv?.setHasFixedSize(true)
        binding?.hotelCancellationReasonRv?.isNestedScrollingEnabled = false
        reasonAdapter = HotelCancellationReasonAdapter()
        reasonAdapter.onClickItemListener = object : HotelCancellationReasonAdapter.OnClickItemListener {
            override fun onClick(selectedId: String, valid: Boolean) {
                binding?.hotelCancellationButtonNext?.isEnabled = valid
                reasonAdapter.onClickItem(selectedId)
            }

            override fun onTypeFreeTextAndMoreThan10Words(valid: Boolean, content: String) {
                reasonAdapter.freeText = content
                binding?.hotelCancellationButtonNext?.isEnabled = valid
            }
        }
        reasonAdapter.updateItems(hotelCancellationModel.reasons)
        binding?.hotelCancellationReasonRv?.adapter = reasonAdapter

        binding?.hotelCancellationButtonNext?.setOnClickListener {
            showConfirmationDialog(hotelCancellationModel.cancelCartId, hotelCancellationModel.confirmationButton, hotelCancellationModel)
        }
    }

    private fun showConfirmationDialog(cancelCartId: String, confirmationButton: HotelCancellationModel.ConfirmationButton,
                                       hotelCancellationModel: HotelCancellationModel) {
        val orderAmount = hotelCancellationModel.payment.detail.firstOrNull()?.amount ?: HOTEL_DEFAULT_AMOUNT_ZERO
        val cancellationFee = hotelCancellationModel.payment.detail.getOrNull(1)?.amount ?: HOTEL_DEFAULT_AMOUNT_ZERO
        val refundAmount = hotelCancellationModel.payment.summary.firstOrNull()?.amount ?: HOTEL_DEFAULT_AMOUNT_ZERO
        val dialog = DialogUnify(activity as AppCompatActivity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(confirmationButton.title)
        dialog.setDescription(confirmationButton.desc)
        dialog.setSecondaryCTAText(getString(R.string.hotel_cancellation_reason_submit))
        dialog.setPrimaryCTAText(getString(R.string.hotel_cancellation_reason_dismiss))
        dialog.setPrimaryCTAClickListener { dialog.dismiss() }
        dialog.dialogDesc.maxLines = DIALOG_MAX_LINES
        dialog.setSecondaryCTAClickListener {
            trackingHotelUtil.clickSubmitCancellation(requireContext(), invoiceId, hotelCancellationModel, HOTEL_CANCELLATION_REASON_SCREEN_NAME)
            startActivity(HotelCancellationConfirmationActivity.getCallingIntent(requireContext(), invoiceId, orderAmount, cancellationFee, refundAmount,
                    HotelCancellationSubmitParam(cancelCartId, HotelCancellationSubmitParam.SelectedReason(reasonAdapter.selectedId, reasonAdapter.freeText))))
            activity?.finish()
        }
        dialog.show()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelCancellationComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelCancellationReasonBinding.inflate(inflater,container,false)
        return binding?.root
    }

    companion object {
        const val HOTEL_DEFAULT_AMOUNT_ZERO = "0"
        const val HOTEL_CANCELLATION_REASON_SCREEN_NAME = "/hotel/ordercancelreason"
        const val SOFT_KEYBOARD_HEIGHT = 500
        const val DIALOG_MAX_LINES = 99
        private const val EXTRA_INVOICE_ID = "extra_invoice_id"
        fun getInstance(invoiceId: String): HotelCancellationReasonFragment =
                HotelCancellationReasonFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                    }
                }
    }
}