package com.tokopedia.flight.cancellation.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.R
import kotlinx.android.synthetic.main.fragment_flight_cancellation_terms_and_conditions.*

/**
 * @author by furqan on 26/04/2021
 */
class FlightCancellationTermsAndConditionsFragment : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_cancellation_terms_and_conditions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        flight_cancellation_terms_checkbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            flight_cancellation_terms_btn_next.isEnabled = isChecked
        }
        flight_cancellation_terms_btn_next.setOnClickListener {
            requestCancellation()
        }
        setTncText()
        setDescText()
    }

    private fun setTncText() {
        flight_cancellation_terms_tv_cancellation_tnc.text = MethodChecker.fromHtml(
                getString(R.string.flight_cancellation_terms_and_cancellation_text))
    }

    private fun setDescText() {
        flight_cancellation_terms_tv_description.text = MethodChecker.fromHtml(
                getString(R.string.flight_cancellation_review_description))
    }

    private fun requestCancellation() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    companion object {
        fun createInstance(): FlightCancellationTermsAndConditionsFragment =
                FlightCancellationTermsAndConditionsFragment()
    }

}