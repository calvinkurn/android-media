package com.tokopedia.hotel.common.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.hotel.R
import kotlinx.android.synthetic.main.item_hotel_fragment_loading_state.view.*
import kotlinx.android.synthetic.main.item_network_error_view.view.*

/**
 * @author by jessica on 12/06/19
 */

abstract class HotelBaseFragment: BaseDaggerFragment() {

    fun showErrorState(e: Throwable) {
        try {
            ((view?.parent) as ViewGroup).main_retry.visibility = View.VISIBLE
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val errorView = inflater.inflate(R.layout.item_network_error_view, view?.parent as ViewGroup)
            errorView.global_error.setActionClickListener {
                errorView.global_error.main_retry.visibility = View.GONE
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
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