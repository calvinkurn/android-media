package com.tokopedia.common_electronic_money.compoundview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.common_electronic_money.R
import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.utils.currency.CurrencyFormatUtil
import org.jetbrains.annotations.NotNull
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Rizky on 15/05/18.
 */
class ETollCardInfoView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                  defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var attributesEmoneyInquiry: AttributesEmoneyInquiry

    private val textLabelBalance: TextView
    private val textRemainingBalance: TextView
    private val textDate: TextView
    private val textLabelCardNumber: TextView
    private val textCardNumber: TextView
    private val imageIssuer: ImageView
    private val textLabelBalanceLoader: LoaderTextView
    private val textRemainingBalanceLoader: LoaderTextView
    private val textDateLoader: LoaderTextView
    private val textLabelCardNumberLoader: LoaderTextView
    private val textCardNumberLoader: LoaderTextView
    private val imageIssuerLoader: LoaderTextView

    val cardNumber: String
        get() = attributesEmoneyInquiry.cardNumber

    val cardLastUpdatedDate: String
        get() = textDate.text.toString()

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

    }

    fun showCardInfo(attributesEmoneyInquiry: AttributesEmoneyInquiry) {
        this.attributesEmoneyInquiry = attributesEmoneyInquiry
        viewVisible()
        imageIssuer.visibility = View.VISIBLE
        ImageHandler.LoadImage(imageIssuer, attributesEmoneyInquiry.imageIssuer)

        textLabelCardNumber.text = resources.getString(R.string.emoney_card_info_label_card_number)
        textCardNumber.text = attributesEmoneyInquiry.formattedCardNumber
        textCardNumber.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700))
        textCardNumber.setTypeface(textCardNumber.typeface, Typeface.BOLD)

        textLabelBalance.text = resources.getString(R.string.emoney_card_info_label_card_balance)
        textRemainingBalance.text = CurrencyFormatUtil
                .convertPriceValueToIdrFormat(attributesEmoneyInquiry.lastBalance, true)
        textRemainingBalance.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96))

        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm",
                DateFormatUtils.DEFAULT_LOCALE)
        val date = Date()
        textDate.text = simpleDateFormat.format(date)
    }

    fun showLoading() {
        viewInvisible()
        resources.getString(R.string.emoney_card_info_label_card_balance)
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
        textDate.measure(0, 0)
        val paramsTextDate = textDate.layoutParams
        paramsTextDate.width = textDate.measuredWidth
        textDate.layoutParams = paramsTextDate
    }

    fun removeCardInfo() {
        viewVisible()
        textLabelCardNumber.text = resources.getString(R.string.emoney_card_info_label_card_number)
        textCardNumber.text = resources.getString(R.string.emoney_card_info_is_not_available_yet)
        textCardNumber.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700))
        textCardNumber.typeface = Typeface.DEFAULT
        textLabelBalance.text = resources.getString(R.string.emoney_card_info_label_card_balance)
        textRemainingBalance.text = resources.getString(R.string.emoney_card_info_is_not_available_yet)
        textRemainingBalance.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700))
        textDate.text = ""
        imageIssuer.setImageDrawable(null)
    }

    private fun viewVisible(){
        imageIssuerLoader.visibility = View.GONE
        imageIssuer.visibility = View.VISIBLE

        textLabelCardNumberLoader.visibility = View.GONE
        textLabelCardNumber.visibility = View.VISIBLE

        textCardNumberLoader.visibility = View.GONE
        textCardNumber.visibility = View.VISIBLE

        textLabelBalanceLoader.visibility = View.GONE
        textLabelBalance.visibility = View.VISIBLE

        textDateLoader.visibility = View.GONE
        textDate.visibility = View.VISIBLE

        textRemainingBalanceLoader.visibility = View.GONE
        textRemainingBalance.visibility = View.VISIBLE

    }

    private fun viewInvisible(){
        imageIssuerLoader.visibility = View.VISIBLE
        imageIssuer.visibility = View.GONE

        textLabelCardNumberLoader.visibility = View.VISIBLE
        textLabelCardNumber.visibility = View.GONE

        textCardNumberLoader.visibility = View.VISIBLE
        textCardNumber.visibility = View.GONE

        textLabelBalanceLoader.visibility = View.VISIBLE
        textLabelBalance.visibility = View.GONE

        textDateLoader.visibility = View.VISIBLE
        textDate.visibility = View.GONE

        textRemainingBalanceLoader.visibility = View.VISIBLE
        textRemainingBalance.visibility = View.GONE
    }

    fun getCardLastBalance(): String {
        if (::attributesEmoneyInquiry.isInitialized) {
            return CurrencyFormatUtil.convertPriceValueToIdrFormat(attributesEmoneyInquiry.lastBalance, true)
        }
        return ""
    }

}
