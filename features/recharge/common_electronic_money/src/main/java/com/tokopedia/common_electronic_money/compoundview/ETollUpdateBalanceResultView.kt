package com.tokopedia.common_electronic_money.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common_electronic_money.R
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import org.jetbrains.annotations.NotNull

/**
 * Created by Rizky on 16/05/18.
 */
class ETollUpdateBalanceResultView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                             defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val eTollCardInfoView: ETollCardInfoView
    private val buttonTopup: UnifyButton
    private val textLabelProgressTitle: Typography
    private val textLabelProgressMessage: Typography

    private lateinit var listener: OnTopupETollClickListener

    val cardNumber: String
        get() = eTollCardInfoView.cardNumber

    val cardLastBalance: String
        get() = eTollCardInfoView.getCardLastBalance()

    val cardLastUpdatedDate: String
        get() = eTollCardInfoView.cardLastUpdatedDate

    fun setListener(listener: OnTopupETollClickListener) {
        this.listener = listener
    }

    init {
        val view = View.inflate(context, R.layout.view_emoney_update_balance_result, this)

        eTollCardInfoView = view.findViewById(R.id.view_etoll_card_info)
        buttonTopup = view.findViewById(R.id.button_topup)
        textLabelProgressTitle = view.findViewById(R.id.text_label_progress_title)
        textLabelProgressMessage = view.findViewById(R.id.text_label_progress_message)
    }

    fun showCardInfoFromApi(inquiryBalanceModel: EmoneyInquiry) {
        textLabelProgressTitle.visibility = View.GONE
        textLabelProgressMessage.visibility = View.GONE
        inquiryBalanceModel.attributesEmoneyInquiry?.let {
            buttonTopup.text = it.buttonText
            eTollCardInfoView.visibility = View.VISIBLE
            eTollCardInfoView.showCardInfo(it)
            buttonTopup.visibility = View.VISIBLE

            if (::listener.isInitialized) {
                buttonTopup.setOnClickListener {
                    listener.onClick(inquiryBalanceModel.attributesEmoneyInquiry.operatorId,
                            inquiryBalanceModel.attributesEmoneyInquiry.issuer_id)
                }
            }
        }
    }

    fun showError(errorMessage: String) {
        textLabelProgressTitle.visibility = View.VISIBLE
        textLabelProgressTitle.text = resources.getString(R.string.emoney_update_card_balance_failed_title)
        textLabelProgressTitle.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_R600))
        textLabelProgressMessage.visibility = View.VISIBLE
        textLabelProgressMessage.text = errorMessage
        textLabelProgressMessage.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N300))
        eTollCardInfoView.visibility = View.VISIBLE
        eTollCardInfoView.removeCardInfo()
        buttonTopup.visibility = View.GONE
    }

    fun showLoading() {
        textLabelProgressTitle.visibility = View.VISIBLE
        textLabelProgressTitle.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700))
        textLabelProgressTitle.text = resources.getString(R.string.emoney_update_card_balance_progress_label_title)
        textLabelProgressMessage.visibility = View.VISIBLE
        textLabelProgressMessage.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N300))
        textLabelProgressMessage.text = resources.getString(R.string.emoney_update_card_balance_progress_label_message)
        eTollCardInfoView.visibility = View.VISIBLE
        eTollCardInfoView.showLoading()
        buttonTopup.visibility = View.GONE
    }

    override fun setVisibility(visibility: Int) {
        if (visibility == View.GONE) {
            textLabelProgressMessage.visibility = View.GONE
        }
        super.setVisibility(visibility)
    }

    interface OnTopupETollClickListener {
        fun onClick(operatorId: String, issuerId: Int)
    }
}
