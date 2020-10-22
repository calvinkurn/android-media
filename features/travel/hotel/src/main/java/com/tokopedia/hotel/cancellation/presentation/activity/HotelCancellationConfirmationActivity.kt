package com.tokopedia.hotel.cancellation.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitModel
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitParam
import com.tokopedia.hotel.cancellation.di.DaggerHotelCancellationComponent
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.fragment.HotelCancellationConfirmationFragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import kotlinx.android.synthetic.main.activity_hotel_cancellation_confirmation.*

/**
 * @author by jessica on 08/05/20
 */

class HotelCancellationConfirmationActivity : HotelBaseActivity(), HasComponent<HotelCancellationComponent> {

    private var cancellationSubmitParam: HotelCancellationSubmitParam? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        with(intent) {
            cancellationSubmitParam = getParcelableExtra(EXTRA_HOTEL_CANCELLATION_SUBMIT_PARAM)
        }
        super.onCreate(savedInstanceState)

        hotel_cancellation_confirmation_header.isShowBackButton = false
    }

    fun setPageTitle(title: String) {
        hotel_cancellation_confirmation_header.title = title
    }

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment {
        return if (cancellationSubmitParam != null) {
            HotelCancellationConfirmationFragment.getInstance(
                    intent.getStringExtra(EXTRA_HOTEL_CANCELLATION_INVOICE_ID) ?: "",
                    intent.getStringExtra(EXTRA_HOTEL_CANCELLATION_ORDER_AMOUNT) ?: "",
                    intent.getStringExtra(EXTRA_HOTEL_CANCELLATION_FEE) ?: "",
                    intent.getStringExtra(EXTRA_HOTEL_REFUND_AMOUNT) ?: "",
                    cancellationSubmitParam ?: HotelCancellationSubmitParam())

        } else {
            HotelCancellationConfirmationFragment.getInstance(intent.getParcelableExtra(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA),
            intent.getBooleanExtra(EXTRA_HOTEL_CANCELLATION_IS_ORDER_NOT_FOUND, false))
        }
    }

    override fun getComponent(): HotelCancellationComponent = DaggerHotelCancellationComponent.builder()
            .hotelComponent(HotelComponentInstance.getHotelComponent(application))
            .build()

    override fun getLayoutRes(): Int = R.layout.activity_hotel_cancellation_confirmation
    override fun getToolbarResourceID(): Int = R.id.hotel_cancellation_confirmation_header
    override fun getParentViewResourceID(): Int = R.id.hotel_cancellation_confirmation_parent_view

    companion object {
        fun getCallingIntent(context: Context, invoiceId: String, orderAmount: String, cancellationFee: String, refundAmount: String,
                             hotelCancellationSubmitParam: HotelCancellationSubmitParam): Intent =
                Intent(context, HotelCancellationConfirmationActivity::class.java)
                        .putExtra(EXTRA_HOTEL_CANCELLATION_SUBMIT_PARAM, hotelCancellationSubmitParam)
                        .putExtra(EXTRA_HOTEL_CANCELLATION_INVOICE_ID, invoiceId)
                        .putExtra(EXTRA_HOTEL_CANCELLATION_ORDER_AMOUNT, orderAmount)
                        .putExtra(EXTRA_HOTEL_CANCELLATION_FEE, cancellationFee)
                        .putExtra(EXTRA_HOTEL_REFUND_AMOUNT, refundAmount)

        fun getCallingIntent(context: Context, hotelCancellationSubmitModel: HotelCancellationSubmitModel, isOrderNotFound: Boolean = false): Intent =
                Intent(context, HotelCancellationConfirmationActivity::class.java)
                        .putExtra(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA, hotelCancellationSubmitModel)
                        .putExtra(EXTRA_HOTEL_CANCELLATION_IS_ORDER_NOT_FOUND, isOrderNotFound)

        const val EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA = "extra_cancellation_submit_data"
        const val EXTRA_HOTEL_CANCELLATION_IS_ORDER_NOT_FOUND = "extra_cancellation_is_order_not_found"

        const val EXTRA_HOTEL_CANCELLATION_SUBMIT_PARAM = "extra_cancellation_submit_param"
        const val EXTRA_HOTEL_CANCELLATION_INVOICE_ID = "cancellation_invoice_id"
        const val EXTRA_HOTEL_CANCELLATION_ORDER_AMOUNT = "cancellation_order_amount"
        const val EXTRA_HOTEL_CANCELLATION_FEE = "cancellation_fee"
        const val EXTRA_HOTEL_REFUND_AMOUNT = "cancellation_refund_amount"

    }
}