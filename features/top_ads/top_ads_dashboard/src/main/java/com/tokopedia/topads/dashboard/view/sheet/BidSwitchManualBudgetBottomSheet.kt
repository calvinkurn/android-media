package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.topads.common.data.util.Utils.addBidValidationListener
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class BidSwitchManualBudgetBottomSheet(
    private val maxBid: String, private val minBid: String, private val suggestedBid: String,
    private val onSaveClicked: (String, String) -> Unit,
) : BottomSheetUnify() {

    private var tfPencarian: TextFieldUnify? = null
    private var tfRecommendasi: TextFieldUnify? = null
    private var btnSave: UnifyButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tfRecommendasi?.textFieldInput?.setText(suggestedBid)
        tfPencarian?.textFieldInput?.setText(suggestedBid)


        setUpListeners()
    }

    private fun setUpListeners() {
        btnSave?.setOnClickListener {
            onSaveClicked(
                tfPencarian?.textFieldInput?.text.toString().removeCommaRawString(),
                tfRecommendasi?.textFieldInput?.text.toString().removeCommaRawString()
            )
            dismiss()
        }

        tfPencarian?.addBidValidationListener(resources, minBid, maxBid, suggestedBid) { isError ->
            btnSave?.isEnabled = !isError
        }
        tfRecommendasi?.addBidValidationListener(resources, minBid, maxBid, suggestedBid) { isError ->
            btnSave?.isEnabled = !isError
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        setUpChildView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpChildView() {
        val view =
            View.inflate(context, R.layout.topads_dash_manual_bid_switch_budget_bottom_sheet, null)
        setChild(view)
        tfPencarian = view.findViewById(R.id.tf_pencarian_bid_switch_bs)
        tfRecommendasi = view.findViewById(R.id.tf_recommendasi_bid_switch_bs)
        btnSave = view.findViewById(R.id.btn_save_bid_switch_bs)

        setTitle(resources.getString(com.tokopedia.topads.common.R.string.topads_common_manual_dialog_title))

        val drawableRight = context?.let {
            ContextCompat.getDrawable(
                it, com.tokopedia.iconunify.R.drawable.iconunify_information)?.apply {
                setTint(
                    ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
                )
            }
        }
        setAction(drawableRight) {
            //to refactor InfoBS and show here
        }
    }

}