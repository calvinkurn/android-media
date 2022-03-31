package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.topads.common.data.util.Utils.addBidValidationListener
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.view.sheet.InfoBottomSheet
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class BidSwitchManualBudgetBottomSheet(
    private val maxBid: String, private val minBid: String, private val suggestedBid: String,
    private val isWhiteListUser: Boolean, private val onSaveClicked: (String, String) -> Unit,
    private val trackerListener: TrackerListener? = null,
) : BottomSheetUnify() {

    private val infoBottomSheet by lazy(LazyThreadSafetyMode.NONE) {
        InfoBottomSheet(InfoBottomSheet.TYPE_DASAR, true)
    }
    private var txtRecommendasiTitle: Typography? = null
    private var tfPencarian: TextFieldUnify? = null
    private var tfRecommendasi: TextFieldUnify? = null
    private var btnSave: UnifyButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isWhiteListUser) {
            tfRecommendasi?.invisible()
            txtRecommendasiTitle?.invisible()
        }
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
            trackerListener?.manualBidBottomSheetLanjuktanClicked()
            dismiss()
        }

        setOnDismissListener { trackerListener?.manualBidBottomSheetDismissed() }

        tfPencarian?.addBidValidationListener(minBid, maxBid, suggestedBid) { isError ->
            btnSave?.isEnabled = !isError
        }
        tfRecommendasi?.addBidValidationListener(minBid, maxBid, suggestedBid) { isError ->
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
        txtRecommendasiTitle = view.findViewById(R.id.txtRecommendasiTitle)

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
            infoBottomSheet.show(childFragmentManager)
        }
    }

    interface TrackerListener {
        fun manualBidBottomSheetLanjuktanClicked()
        fun manualBidBottomSheetDismissed()
    }

}