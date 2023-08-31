package com.tokopedia.common_electronic_money.compoundview

import android.content.Context
import android.graphics.Typeface
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_electronic_money.R
import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import org.jetbrains.annotations.NotNull
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by Rizky on 15/05/18.
 */
class ETollCardInfoView @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var attributesEmoneyInquiry: AttributesEmoneyInquiry

    private val textLabelBalance: TextView
    private val textRemainingBalance: TextView
    private val textDate: Typography
    private val textLabelCardNumber: TextView
    private val textCardNumber: TextView
    private val imageIssuer: ImageView
    private val textLabelBalanceLoader: LoaderUnify
    private val textRemainingBalanceLoader: LoaderUnify
    private val textDateLoader: LoaderUnify
    private val textLabelCardNumberLoader: LoaderUnify
    private val textCardNumberLoader: LoaderUnify
    private val imageIssuerLoader: LoaderUnify
    private val tickerExtraPendingBalance: Ticker
    private val viewAdditionalBalance: LinearLayout
    private val tgAdditionalBalance: Typography
    private lateinit var listener: OnClickCardInfoListener

    val cardNumber: String
        get() = attributesEmoneyInquiry.cardNumber

    val cardLastUpdatedDate: String
        get() = textDate.text.toString()

    fun setListener(listener: OnClickCardInfoListener) {
        this.listener = listener
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_emoney_card_info, this, true)

        textLabelBalance = view.findViewById(R.id.text_label_balance)
        textRemainingBalance = view.findViewById(R.id.text_remaining_balance)
        textLabelCardNumber = view.findViewById(R.id.text_label_card_number)
        textCardNumber = view.findViewById(R.id.text_card_number)
        textDate = view.findViewById(R.id.text_date)
        imageIssuer = view.findViewById(R.id.image_issuer)

        textLabelBalanceLoader = view.findViewById(R.id.text_label_balance_loader)
        textRemainingBalanceLoader = view.findViewById(R.id.text_remaining_balance_loader)
        textDateLoader = view.findViewById(R.id.text_date_loader)
        imageIssuerLoader = view.findViewById(R.id.image_issuer_loader)
        textLabelCardNumberLoader = view.findViewById(R.id.text_label_card_number_loader)
        textCardNumberLoader = view.findViewById(R.id.text_card_number_loader)
        tickerExtraPendingBalance = view.findViewById(R.id.tickerExtraPendingBalance)
        viewAdditionalBalance = view.findViewById(R.id.view_additional_balance)
        tgAdditionalBalance = view.findViewById(R.id.tg_additional_balance)
    }

    fun showCardInfo(attributesEmoneyInquiry: AttributesEmoneyInquiry) {
        this.attributesEmoneyInquiry = attributesEmoneyInquiry
        viewVisible()
        imageIssuer.visibility = View.VISIBLE
        imageIssuer.loadImage(attributesEmoneyInquiry.imageIssuer)

        textLabelCardNumber.text = resources.getString(R.string.emoney_nfc_card_info_label_card_number)
        textCardNumber.text = attributesEmoneyInquiry.formattedCardNumber
        textCardNumber.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950))
        textCardNumber.setTypeface(textCardNumber.typeface, Typeface.BOLD)

        textLabelBalance.text = resources.getString(R.string.emoney_nfc_card_info_label_card_balance)
        textRemainingBalance.text = CurrencyFormatUtil
            .convertPriceValueToIdrFormat(attributesEmoneyInquiry.lastBalance, true)
        textRemainingBalance.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))

        val simpleDateFormat = SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            DateFormatUtils.DEFAULT_LOCALE
        )
        val date = Date()
        val result = String.format("(%s)", simpleDateFormat.format(date))
        textDate.text = result
        if (attributesEmoneyInquiry.extraPendingBalance) {
            showTickerInfo()
        } else {
            hideTickerInfo()
        }
        if (attributesEmoneyInquiry.showAdditionalBalance &&
            attributesEmoneyInquiry.pendingBalance.isMoreThanZero()) {
            showAdditionalBalanceInfo(attributesEmoneyInquiry.pendingBalance)
        } else {
            hideAdditionalBalanceInfo()
        }
    }

    private fun showTickerInfo() {
        tickerExtraPendingBalance.show()
        tickerExtraPendingBalance.setHtmlDescription(resources.getString(R.string.emoney_nfc_bca_stacking_layout))
        tickerExtraPendingBalance.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                listener.onClickExtraPendingBalance()
            }

            override fun onDismiss() {}
        })
    }

    private fun hideTickerInfo() {
        tickerExtraPendingBalance.hide()
    }

    private fun showAdditionalBalanceInfo(pendingBalance: Int) {
        viewAdditionalBalance.show()
        val pendingBalanceCurrency = CurrencyFormatUtil
            .convertPriceValueToIdrFormat(pendingBalance, true)
        tgAdditionalBalance.text = Html.fromHtml(
            context.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_bca_additional_balance_view,
            pendingBalanceCurrency))
    }

    private fun hideAdditionalBalanceInfo() {
        viewAdditionalBalance.hide()
    }

    fun showLoading() {
        viewInvisible()
        resources.getString(R.string.emoney_nfc_card_info_label_card_balance)
        textLabelBalance.measure(0, 0)
        val paramsTextLabelBalance = textLabelBalance.layoutParams
        paramsTextLabelBalance.width = textLabelBalance.measuredWidth
        textLabelBalance.layoutParams = paramsTextLabelBalance

        textRemainingBalance.text = "Rp. 1.000.000"
        textRemainingBalance.measure(0, 0)
        val paramsTextRemainingBalance = textRemainingBalance.layoutParams
        paramsTextRemainingBalance.width = textRemainingBalance.measuredWidth
        textRemainingBalance.layoutParams = paramsTextRemainingBalance

        textDate.text = " 26 Jun 2018, 13:47 "
        val paramsTextDate = textDate.layoutParams
        paramsTextDate.width = textDate.measuredWidth
        textDate.layoutParams = paramsTextDate

        hideTickerInfo()
        hideAdditionalBalanceInfo()
    }

    fun removeCardInfo() {
        viewVisible()
        textLabelCardNumber.text = resources.getString(R.string.emoney_nfc_card_info_label_card_number)
        textCardNumber.text = resources.getString(R.string.emoney_nfc_card_info_is_not_available_yet)
        textCardNumber.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950))
        textCardNumber.typeface = Typeface.DEFAULT
        textLabelBalance.text = resources.getString(R.string.emoney_nfc_card_info_label_card_balance)
        textRemainingBalance.text = resources.getString(R.string.emoney_nfc_card_info_is_not_available_yet)
        textRemainingBalance.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950))
        textDate.text = ""
        imageIssuer.setImageDrawable(null)
    }

    private fun viewVisible() {
        imageIssuerLoader.hide()
        imageIssuer.show()

        textLabelCardNumberLoader.hide()
        textLabelCardNumber.show()

        textCardNumberLoader.hide()
        textCardNumber.show()

        textLabelBalanceLoader.hide()
        textLabelBalance.show()

        textDateLoader.hide()
        textDate.show()

        textRemainingBalanceLoader.hide()
        textRemainingBalance.show()
    }

    private fun viewInvisible() {
        imageIssuerLoader.show()
        imageIssuer.hide()

        textLabelCardNumberLoader.show()
        textLabelCardNumber.hide()

        textCardNumberLoader.show()
        textCardNumber.hide()

        textLabelBalanceLoader.show()
        textLabelBalance.hide()

        textDateLoader.show()
        textDate.hide()

        textRemainingBalanceLoader.show()
        textRemainingBalance.hide()
    }

    fun getCardLastBalance(): String {
        if (::attributesEmoneyInquiry.isInitialized) {
            return CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(attributesEmoneyInquiry.lastBalance)
        }
        return ""
    }

    interface OnClickCardInfoListener {
        fun onClickExtraPendingBalance()
    }
}
