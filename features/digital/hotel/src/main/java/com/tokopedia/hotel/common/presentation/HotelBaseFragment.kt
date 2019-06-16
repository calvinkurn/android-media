package com.tokopedia.hotel.common.presentation

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import kotlinx.android.synthetic.main.item_network_error_view.view.*

/**
 * @author by jessica on 12/06/19
 */

abstract class HotelBaseFragment: BaseDaggerFragment() {

    fun showErrorState(e: Throwable) {
        try {
            ((view!!.parent) as ViewGroup).main_retry!!.visibility = View.VISIBLE
        } catch (exception: Exception) {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.CENTER
            params.weight = 1.0f
            val retryLoad = inflater.inflate(R.layout.item_network_error_view, view?.parent as ViewGroup)

            retryLoad.iv_icon.setImageResource(ErrorHandlerHotel.getErrorImage(e))
            retryLoad.message_retry.text = ErrorHandlerHotel.getErrorTitle(context, e)
            retryLoad.sub_message_retry.text = ErrorHandlerHotel.getErrorMessage(context, e)

            retryLoad.button_retry.setOnClickListener {
                retryLoad.main_retry.visibility = View.GONE
                onErrorRetryClicked()
            }
        }
    }

    abstract fun onErrorRetryClicked()
}