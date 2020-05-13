package com.tokopedia.hotel.cancellation.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationActivity
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationConfirmationActivity
import com.tokopedia.hotel.cancellation.presentation.adapter.HotelCancellationReasonAdapter
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import com.tokopedia.hotel.common.util.HotelTextHyperlinkUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_cancellation_reason.*
import javax.inject.Inject

/**
 * @author by jessica on 30/04/20
 */

class HotelCancellationReasonFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var cancellationViewModel: HotelCancellationViewModel

    lateinit var reasonAdapter: HotelCancellationReasonAdapter

    private var invoiceId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            invoiceId = it.getString(EXTRA_INVOICE_ID) ?: ""
        }

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(activity as HotelCancellationActivity, viewModelFactory)
            cancellationViewModel = viewModelProvider.get(HotelCancellationViewModel::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as HotelCancellationActivity).updateSubtitle(getString(R.string.hotel_cancellation_page_2_subtitle))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancellationViewModel.getCancellationData(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_hotel_cancellation_data), invoiceId,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cancellationViewModel.cancellationData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    initView(it.data)
                }
                is Fail -> {
                }
            }
        })

        cancellationViewModel.cancellationSubmitData.observe(this, Observer {
            when (it) {
                is Success ->
                    startActivity(HotelCancellationConfirmationActivity.getCallingIntent(requireContext(), it.data))
                is Fail -> {}
            }
        })
    }

    fun initView(hotelCancellationModel: HotelCancellationModel) {
        //When page loaded, initially the button is disabled first until user choose reason
        hotel_cancellation_button_next.isEnabled = false

        hotel_cancellation_page_footer.highlightColor = Color.TRANSPARENT
        hotel_cancellation_page_footer.movementMethod = LinkMovementMethod.getInstance()
        hotel_cancellation_page_footer.setText(HotelTextHyperlinkUtil.getSpannedFromHtmlString(requireContext(),
                hotelCancellationModel.footer.desc, hotelCancellationModel.footer.links), TextView.BufferType.SPANNABLE)

        hotel_cancellation_reason_rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        hotel_cancellation_reason_rv.setHasFixedSize(true)
        hotel_cancellation_reason_rv.isNestedScrollingEnabled = false
        reasonAdapter = HotelCancellationReasonAdapter()
        reasonAdapter.onClickItemListener = object : HotelCancellationReasonAdapter.OnClickItemListener {
            override fun onClick(selectedId: String, valid: Boolean) {
                hotel_cancellation_button_next.isEnabled = valid
                reasonAdapter.onClickItem(selectedId)
            }

            override fun onTypeFreeTextAndMoreThan10Words(valid: Boolean, content: String) {
                reasonAdapter.freeText = content
                hotel_cancellation_button_next.isEnabled = valid
            }
        }
        reasonAdapter.updateItems(hotelCancellationModel.reasons)
        hotel_cancellation_reason_rv.adapter = reasonAdapter

        hotel_cancellation_button_next.setOnClickListener {
            showConfirmationDialog(hotelCancellationModel.cancelCartId, hotelCancellationModel.confirmationButton)
        }
    }

    private fun showConfirmationDialog(cancelCartId: String, confirmationButton: HotelCancellationModel.ConfirmationButton) {
        val dialog = DialogUnify(activity as AppCompatActivity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(confirmationButton.title)
        dialog.setDescription(confirmationButton.desc)
        dialog.setSecondaryCTAText(getString(R.string.hotel_cancellation_reason_submit))
        dialog.setPrimaryCTAText(getString(R.string.hotel_cancellation_reason_dismiss))
        dialog.setPrimaryCTAClickListener { dialog.dismiss() }
        dialog.setSecondaryCTAClickListener {
            Toast.makeText(requireContext(), "${reasonAdapter.selectedId} ${reasonAdapter.freeText}", Toast.LENGTH_SHORT).show()
//            cancellationViewModel.submitCancellationData(GraphqlHelper.loadRawString(resources, R.raw.gql_mutation_submit_hotel_cancellation),
//                    cancelCartId, reasonAdapter.selectedId, reasonAdapter.freeText)

            cancellationViewModel.submitCancellationData(GraphqlHelper.loadRawString(resources, R.raw.gql_mutation_submit_hotel_cancellation),
                    "123r5gkjwechfmr1cy2", reasonAdapter.selectedId, reasonAdapter.freeText)
        }
        dialog.show()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelCancellationComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_cancellation_reason, container, false)

    companion object {
        private const val EXTRA_INVOICE_ID = "extra_invoice_id"
        fun getInstance(invoiceId: String): HotelCancellationReasonFragment =
                HotelCancellationReasonFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                    }
                }
    }
}