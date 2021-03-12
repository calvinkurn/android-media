package com.tokopedia.topads.edit.view.fragment.edit

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.SingleAd
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.groupId
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.topads_edit_without_group_layout.*
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 1/6/20.
 */

class EditFormWithoutGroupFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(EditFormDefaultViewModel::class.java)
    }

    private var adId = "0"
    private var minBid = "0"
    private var maxBid = "0"
    private var suggestBidPerClick = "0"
    private var validation1 = true
    private var validation2 = true
    private var currentBudget = 0

    companion object {
        fun newInstance(bundle: Bundle?): EditFormWithoutGroupFragment {
            val fragment = EditFormWithoutGroupFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return EditFormWithoutGroupFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_edit_without_group_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adId = arguments?.getString(groupId) ?: "0"
        viewModel.getSingleAdInfo(adId, ::onSuccessAdInfo)
        radio_group.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == radio1.id) {
                daily_budget.visibility = View.GONE
            } else {
                daily_budget.visibility = View.VISIBLE
            }
        }

        budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                currentBudget = abs(number.toInt()).toString().removeCommaRawString().toInt()
                val result = number.toInt()
                daily_budget.textFieldInput.setText((Constants.MULTIPLIER * result).toString())
                when {
                    minBid == "0" || maxBid == "0" -> {
                        return
                    }
                    result % (Constants.MULTIPLY_CONST.toInt()) != 0 -> {
                        validation2 = false
                        setMessageErrorField(getString(R.string.topads_common_50_multiply_error), Constants.MULTIPLY_CONST, true)
                    }
                    result < minBid.toFloat() -> {
                        setMessageErrorField(getString(R.string.min_bid_error), minBid, true)
                        validation2 = false
                    }
                    result > maxBid.toFloat() -> {
                        validation2 = false
                        setMessageErrorField(getString(R.string.max_bid_error), maxBid, true)
                    }
                    else -> {
                        validation2 = true
                        setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)
                    }
                }
                actionEnable()
            }
        })

        daily_budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(daily_budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                if (number < Constants.MULTIPLIER * currentBudget) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(String.format(getString(R.string.min_bid_error), Constants.MULTIPLIER * currentBudget))
                    validation1 = false

                } else {
                    validation1 = true
                    daily_budget.setError(false)
                    daily_budget.setMessage("")
                }
                actionEnable()
            }
        })
        save_butt.setOnClickListener {
            var priceDaily = 0.0F
            if (radio2.isChecked) {
                priceDaily = daily_budget.textFieldInput.text.toString().removeCommaRawString().toFloat()
            }
            viewModel.editSingleAd(adId, budget.textFieldInput.text.toString().removeCommaRawString().toFloat(),
                    priceDaily)
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
    }

    private fun onSuccessAdInfo(data: List<SingleAd>) {
        data.firstOrNull().let {
            budget.textFieldInput.setText(it?.priceBid.toString())
            if (it?.priceDaily != 0) {
                radio2.isChecked = true
                daily_budget.textFieldInput.setText(it?.priceDaily.toString())

            } else {
                daily_budget.textFieldInput.setText((Constants.MULTIPLIER * (it.priceBid)).toString())
            }
            val suggestionsDefault = ArrayList<DataSuggestions>()
            val dummyId: MutableList<Long> = mutableListOf(it?.itemID?.toLong() ?: 0)
            suggestionsDefault.add(DataSuggestions(Constants.PRODUCT, dummyId))
            viewModel.getBidInfoDefault(suggestionsDefault, this::onBidSuccessSuggestion)
        }
    }

    private fun actionEnable() {
        save_butt.isEnabled = validation1 == true && validation2 == true
    }

    private fun onBidSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        suggestBidPerClick = data[0].suggestionBid
        minBid = data[0].minBid
        maxBid = data[0].maxBid
        setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)
    }

    private fun setMessageErrorField(error: String, bid: String, isError: Boolean) {
        budget.setError(isError)
        budget.setMessage(String.format(error, bid))
    }
}