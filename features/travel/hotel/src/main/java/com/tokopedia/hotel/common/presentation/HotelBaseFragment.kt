package com.tokopedia.hotel.common.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.network.utils.ErrorHandler
import kotlinx.android.synthetic.main.item_hotel_fragment_loading_state.view.*
import kotlinx.android.synthetic.main.item_network_error_view.view.*

/**
 * @author by jessica on 12/06/19
 */

abstract class HotelBaseFragment: BaseDaggerFragment() {

    fun showErrorState(e: Throwable) {
        try {
            ((view?.parent) as ViewGroup).main_retry.visibility = View.VISIBLE
        } catch (exception: Exception) {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val retryLoad = inflater.inflate(R.layout.item_network_error_view, view?.parent as ViewGroup)

            retryLoad.iv_icon.setImageResource(ErrorHandlerHotel.getErrorImage(e))
            retryLoad.message_retry.text = ErrorHandlerHotel.getErrorTitle(context, e)
            retryLoad.sub_message_retry.text = ErrorHandler.getErrorMessage(context, e)

            retryLoad.button_retry.setOnClickListener {
                retryLoad.main_retry.visibility = View.GONE
                onErrorRetryClicked()
            }
        }
    }

    fun showLoadingState() {
        try {
            ((view?.parent) as ViewGroup).main_loading.visibility = View.VISIBLE
        } catch (exception: Exception) {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.item_hotel_fragment_loading_state, view?.parent as ViewGroup)
        }
    }

    fun hideLoadingState() {
        ((view?.parent) as ViewGroup).main_loading?.visibility = View.GONE
    }

    abstract fun onErrorRetryClicked()
}