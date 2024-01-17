package com.tokopedia.recharge_credit_card.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imageassets.TokopediaImageUrl.CC_IMG_VERIFIED
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.model.client_number.InputFieldType
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberAutoCompleteModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel
import com.tokopedia.recharge_component.widget.setMainPadding
import com.tokopedia.recharge_credit_card.R
import com.tokopedia.recharge_credit_card.databinding.WidgetClientNumberWidgetCcBinding
import com.tokopedia.recharge_credit_card.pcidss.model.PcidssAutoCompleteNumberModel
import com.tokopedia.recharge_credit_card.pcidss.model.PcidssPrefixValidatorModel
import com.tokopedia.recharge_credit_card.pcidss.widgets.PcidssInputFieldWidget
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import org.jetbrains.annotations.NotNull
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by misael on 14/06/22
 * */
class RechargeCCClientNumberWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: WidgetClientNumberWidgetCcBinding = WidgetClientNumberWidgetCcBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private var mFilterChipListener: ClientNumberFilterChipListener? = null
    private var inputFieldType: InputFieldType? = null

    /* Credit Card */
    private var mCreditCardActionListener: CreditCardActionListener? = null

    init {
        initSortFilterChip()
        initPrimaryButton()
        initTickerView()
        initCCLogoVerified()
    }

    private fun initPrimaryButton() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetButton.run {
            visibility = VISIBLE
            setOnClickListener {
                mCreditCardActionListener?.onClickNextButton()
            }
        }
    }

    private fun initCCLogoVerified() {
        binding.clientNumberWidgetMainLayout.clientNumberCcLogoVerified.loadImage(CC_IMG_VERIFIED)
    }

    private fun initSortFilterChip() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetSortFilter.setMainPadding()
    }

    private fun setSortFilterChip(favnum: List<RechargeClientNumberChipModel>) {
        val sortFilter = arrayListOf<SortFilterItem>()

        binding.clientNumberWidgetMainLayout.clientNumberWidgetSeeAll.run {
            if (favnum.isNotEmpty()) {
                chip_text.hide()
                chipType = ChipsUnify.TYPE_ALTERNATE
                chipImageResource = getIconUnifyDrawable(
                    context,
                    IconUnify.VIEW_LIST,
                    ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
                )
                setOnClickListener {
                    mFilterChipListener?.onClickIcon(true)
                }
                show()
            } else {
                hide()
            }
        }

        // create each chip
        for (number in favnum) {
            val sortFilterItem = if (number.clientName.isEmpty()) {
                mFilterChipListener?.onShowFilterChip(false)
                SortFilterItem(number.clientNumber, type = ChipsUnify.TYPE_ALTERNATE)
            } else {
                mFilterChipListener?.onShowFilterChip(true)
                SortFilterItem(number.clientName, type = ChipsUnify.TYPE_ALTERNATE)
            }

            sortFilterItem.listener = {
                clearErrorState()
                hideSoftKeyboard()
                setContactName(number.clientName)

                if (number.clientName.isEmpty()) {
                    mFilterChipListener?.onClickFilterChip(false, number)
                } else {
                    mFilterChipListener?.onClickFilterChip(true, number)
                }
                clearFocusAutoComplete()
            }
            sortFilter.add(sortFilterItem)
        }

        binding.clientNumberWidgetMainLayout.clientNumberWidgetSortFilter.addItem(sortFilter)
    }

    fun submitInstantCheckout(
        headers: HashMap<String, String>,
        clientNumber: String,
        operatorId: String,
        productId: String,
        userId: String,
        signature: String,
        token: String
    ) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.pcidssInstantCheckout(
            headers,
            clientNumber,
            operatorId,
            productId,
            userId,
            signature,
            token
        )
    }

    fun showDialogConfirmation() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.pcidssDialogConfirmation()
    }

    fun submitCheckout(
        headers: HashMap<String, String>,
        operatorId: String,
        productId: String,
        signature: String,
        userId: String
    ) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.submitCreditCard(
            headers,
            signature,
            operatorId,
            productId,
            userId
        )
    }

    fun setFavoriteNumber(favoriteChips: List<RechargeClientNumberChipModel>) {
        setSortFilterChip(favoriteChips)
    }

    fun setAutoCompleteList(suggestions: List<RechargeClientNumberAutoCompleteModel>) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss
            .setInputFieldAutoCompleteList(
                suggestions.map {
                    PcidssAutoCompleteNumberModel(
                        it.clientName,
                        it.clientNumber,
                        it.operatorName,
                        it.token
                    )
                }
            )
    }

    fun setInputFieldType(type: InputFieldType, enableGoogleAutofill: Boolean = false) {
        with(binding) {
            inputFieldType = type
            clientNumberWidgetMainLayout.clientNumberWidgetPcidss.run {
                setInputType(type.inputType)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setInputAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_NUMBER, enableGoogleAutofill)
                }
            }
        }
    }

    fun setInputFieldStaticLabel(label: String) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss
            .setInputFieldStaticLabel(label)
    }

    fun setInputNumber(inputNumber: String, token: String = "") {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.setInputNumber(inputNumber, token)
    }

    fun isInputFieldEmpty(): Boolean {
        return binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.isInputFieldEmpty()
    }

    private fun initTickerView() {
        val messages = arrayListOf(
            TickerData(
                title = context.getString(R.string.cc_ticker_title),
                description = context.getString(R.string.cc_ticker_desc),
                type = Ticker.TYPE_ANNOUNCEMENT
            ),
            TickerData(
                title = "",
                description = context.getString(R.string.cc_ticker_2_desc),
                type = Ticker.TYPE_ANNOUNCEMENT
            )
        )

        val tickerAdapter = TickerPagerAdapter(context, messages).apply {
            setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$linkUrl")
                    mCreditCardActionListener?.onNavigateTokoCardWebView()
                }
            })
        }
        binding.clientNumberWidgetMainLayout.clientNumberWidgetTicker.run {
            autoSlideDelay = TICKER_AUTO_SLIDE_DELAY
            addPagerView(tickerAdapter, messages)
        }
    }

    fun setContactName(contactName: String, needValidation: Boolean = true) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.setInputFieldHint(contactName, needValidation)
    }

    fun resetContactName() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.resetInputFieldHint()
    }

    fun setLoading(isLoading: Boolean) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.setLoading(isLoading)
    }

    fun setPrefixValidator(data: PcidssPrefixValidatorModel) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.setCreditCardPrefixValidator(data)
    }

    fun setErrorInputField(errorMessage: String) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss
            .setErrorInputField(errorMessage)
    }

    fun setFilterChipShimmer(show: Boolean, shouldHideChip: Boolean = false) {
        binding.clientNumberWidgetMainLayout.run {
            if (show) {
                clientNumberWidgetSortFilter.hide()
                clientNumberWidgetSortFilterShimmer.show()
            } else {
                if (!shouldHideChip) clientNumberWidgetSortFilter.show()
                clientNumberWidgetSortFilterShimmer.hide()
            }
        }
    }

    fun clearErrorState() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.clearErrorState()
    }

    fun showOperatorIcon(url: String) {
        with(binding) {
            clientNumberWidgetOperatorGroup.show()
            clientNumberWidgetOperatorIcon.loadImage(url)
        }
    }

    fun hideOperatorIcon() {
        with(binding) {
            clientNumberWidgetOperatorGroup.invisible()
        }
    }

    fun clearFocusAutoComplete() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss.clearFocusAutoComplete()
    }

    fun hideSoftKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(this.windowToken, 0)
    }

    fun setInputFieldListener(pcidssInputFieldListener: PcidssInputFieldWidget.PcidssInputFieldWidgetListener) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetPcidss
            .setPcidssInputFieldWidgetListener(pcidssInputFieldListener)
    }

    fun setFilterChipListener(filterChipListener: ClientNumberFilterChipListener) {
        mFilterChipListener = filterChipListener
    }

    fun setCreditCardATCListener(creditCardActionListener: CreditCardActionListener) {
        mCreditCardActionListener = creditCardActionListener
    }

    fun enablePrimaryButton() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetButton.isEnabled = true
    }

    fun disablePrimaryButton() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetButton.isEnabled = false
    }

    interface CreditCardActionListener {
        fun onClickNextButton()
        fun onNavigateTokoCardWebView()
    }

    companion object {
        private val TICKER_AUTO_SLIDE_DELAY = 5000
    }
}
