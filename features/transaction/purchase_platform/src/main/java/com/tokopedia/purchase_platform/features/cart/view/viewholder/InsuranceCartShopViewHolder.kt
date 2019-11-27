package com.tokopedia.purchase_platform.features.cart.view.viewholder

import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.date.util.SaldoDatePickerUtil
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartShops
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceProductApplicationDetails
import com.tokopedia.purchase_platform.features.cart.view.InsuranceItemActionListener
import com.tokopedia.transaction.insurance.utils.*
import java.util.*


class InsuranceCartShopViewHolder(itemView: View, private val insuranceItemActionlistener: InsuranceItemActionListener) : RecyclerView.ViewHolder(itemView) {
    private val tvInsuranceApplicationDetails: TextView

    private val cbSelectInsurance: CheckBox
    private val ivInsuranceIcon: ImageView
    private val tvInsuranceTitle: TextView
    private val tvProductTitle: TextView
    private val tvInsurancePrice: TextView
    private val tvInsuranceInfo: TextView
    private val tvInsuranceSubtitle: TextView
    private val tvInsuranceTickerText: TextView
    private val ivDeleteInsurance: ImageView
    private val tvChangeInsuranceApplicationDetails: TextView
    private var insuranceCartShops: InsuranceCartShops? = null
    private val typeValues = ArrayList<TextView>()
    private var btnValidate: Button? = null
    private var closeableBottomSheetDialog: CloseableBottomSheetDialog
    private var errorMessage: String? = null
    private var datePicker: SaldoDatePickerUtil
    private var onBind = true

    private val updateInsuranceProductApplicationDetailsArrayList = ArrayList<UpdateInsuranceProductApplicationDetails>()

    init {
        cbSelectInsurance = itemView.findViewById(R.id.insurance_checkbox)
        ivInsuranceIcon = itemView.findViewById(R.id.insurance_image_icon)
        tvInsuranceTitle = itemView.findViewById(R.id.insurance_tv_title)
        tvProductTitle = itemView.findViewById(R.id.tv_product_title)
        tvInsurancePrice = itemView.findViewById(R.id.insurance_tv_price)
        tvInsuranceInfo = itemView.findViewById(R.id.insurance_tv_info)
        tvInsuranceSubtitle = itemView.findViewById(R.id.insurance_tv_subtitle)
        ivDeleteInsurance = itemView.findViewById(R.id.insurance_delete_icon)
        tvInsuranceTickerText = itemView.findViewById(R.id.tv_ticker_text)
        tvChangeInsuranceApplicationDetails = itemView.findViewById(R.id.insurance_application_details_change)
        tvInsuranceApplicationDetails = itemView.findViewById(R.id.insurance_appliation_details)
        closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(itemView.context)
        datePicker = SaldoDatePickerUtil(itemView.context as Activity)

    }

    fun bindData(insuranceCartShops: InsuranceCartShops, position: Int, pageType: String) {

        this.insuranceCartShops = insuranceCartShops

        if (!insuranceCartShops.shopItemsList.isEmpty() && !insuranceCartShops.shopItemsList[0].digitalProductList.isEmpty()) {

            val insuranceCartDigitalProduct = insuranceCartShops.shopItemsList[0].digitalProductList[0]

            if (!TextUtils.isEmpty(insuranceCartDigitalProduct.productInfo.sectionTitle)) {
                tvProductTitle.text = insuranceCartDigitalProduct.productInfo.sectionTitle
            } else {
                tvProductTitle.text = itemView.context.resources.getString(R.string.checkout_insurance_default_title)
            }

            if (!TextUtils.isEmpty(insuranceCartDigitalProduct.productInfo.title)) {
                tvInsuranceTitle.text = insuranceCartDigitalProduct.productInfo.title
            } else {
                tvProductTitle.text = itemView.context.resources.getString(R.string.checkout_insurance_default_title)
            }

            if (!TextUtils.isEmpty(insuranceCartDigitalProduct.productInfo.subTitle)) {
                tvInsuranceSubtitle.text = insuranceCartDigitalProduct.productInfo.subTitle
                tvInsuranceSubtitle.show()
            } else {
                tvInsuranceSubtitle.hide()
            }

            if (!TextUtils.isEmpty(insuranceCartDigitalProduct.productInfo.tickerText)) {
                tvInsuranceTickerText.text = insuranceCartDigitalProduct.productInfo.tickerText
                tvInsuranceTickerText.show()
            } else {
                tvInsuranceTickerText.hide()
            }

            if (!TextUtils.isEmpty(insuranceCartDigitalProduct.productInfo.iconUrl)) {
                ImageHandler.loadImageRounded2(ivInsuranceIcon.context, ivInsuranceIcon, insuranceCartDigitalProduct.productInfo.iconUrl)
            }

            if (TextUtils.isEmpty(insuranceCartDigitalProduct.productInfo.linkName)) {
                tvInsuranceInfo.hide()
            } else {
                tvInsuranceInfo.show()
                tvInsuranceInfo.text = insuranceCartDigitalProduct.productInfo.linkName
                tvInsuranceInfo.setOnClickListener { v ->

                    openBottomSheetWebView(tvInsuranceInfo.context,
                            insuranceCartDigitalProduct.productInfo.appLinkUrl,
                            insuranceCartDigitalProduct.productInfo.detailInfoTitle)
                }
            }

            if (!insuranceCartDigitalProduct.applicationDetails.isEmpty()) {

                val applicationDetails = StringBuilder()
                for (i in 0 until insuranceCartDigitalProduct.applicationDetails.size) {
                    if (!TextUtils.isEmpty(insuranceCartDigitalProduct.applicationDetails[i].value)) {
                        applicationDetails.append(insuranceCartDigitalProduct.applicationDetails[i].value)
                    }

                    if (i < insuranceCartDigitalProduct.applicationDetails.size - 1) {
                        applicationDetails.append(" | ")
                    }
                }

                if (!TextUtils.isEmpty(applicationDetails)) {
                    tvInsuranceApplicationDetails.text = applicationDetails
                    tvInsuranceApplicationDetails.show()
                    tvChangeInsuranceApplicationDetails.show()

                    tvChangeInsuranceApplicationDetails.setOnClickListener {
                        val rootView = LayoutInflater.from(tvChangeInsuranceApplicationDetails.context).inflate(R.layout.layout_insurance_bottom_sheet, null, false)

                        val applicationDetailsView = rootView.findViewById<LinearLayout>(R.id.ll_application_details)
                        applicationDetailsView.removeAllViews()
                        btnValidate = rootView.findViewById(R.id.btn_validate)

                        typeValues.clear()
                        for (insuranceProductApplicationDetails in insuranceCartDigitalProduct.applicationDetails) {

                            if (insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_TEXT, ignoreCase = true) ||
                                    insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_NUMBER, ignoreCase = true)) {

                                val view = LayoutInflater.from(tvChangeInsuranceApplicationDetails.context).inflate(R.layout.application_detail_text, null, false)

                                val subtitle = view.findViewById<TextView>(R.id.sub_title)
                                val errorMessageView = view.findViewById<TextView>(R.id.error_message)

                                (view.findViewById<View>(R.id.tv_title) as TextView).text = insuranceProductApplicationDetails.label
                                subtitle.text = insuranceProductApplicationDetails.value

                                subtitle.addTextChangedListener(object : TextWatcher {
                                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                                    }

                                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                                        if (validateView(subtitle, insuranceProductApplicationDetails)) {
                                            errorMessageView.hide()
                                        } else {
                                            errorMessageView.show()
                                            errorMessageView.text = errorMessage
                                        }
                                        updateEditTextBackground(subtitle, errorMessageView.currentTextColor, !TextUtils.isEmpty(errorMessage))
                                    }

                                    override fun afterTextChanged(s: Editable) {

                                    }
                                })

                                applicationDetailsView.addView(view)
                                addToValuesList(view, insuranceProductApplicationDetails)

                            } else if (insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_DATE, ignoreCase = true)) {

                                val view = LayoutInflater.from(tvChangeInsuranceApplicationDetails.context).inflate(R.layout.application_detail_date, null, false)

                                (view.findViewById<View>(R.id.title) as TextView).text = insuranceProductApplicationDetails.label
                                val subTitleTextView = view.findViewById<TextView>(R.id.sub_title)
                                val errorMessageView = view.findViewById<TextView>(R.id.error_message)

                                val dateText = insuranceProductApplicationDetails.value
                                subTitleTextView.text = getDateStringInUIFormat(dateText)
                                subTitleTextView.setOnClickListener { onDateViewClicked(subTitleTextView) }

                                subTitleTextView.addTextChangedListener(object : TextWatcher {
                                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                                    }

                                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                                        if (validateView(subTitleTextView, insuranceProductApplicationDetails)) {
                                            errorMessageView.hide()
                                            insuranceProductApplicationDetails.value = getDateInServerFormat(s.toString())
                                            insuranceProductApplicationDetails.isError = false
                                        } else {
                                            errorMessageView.show()
                                            errorMessageView.text = errorMessage
                                            insuranceProductApplicationDetails.isError = true
                                        }
                                        updateEditTextBackground(subTitleTextView, errorMessageView.currentTextColor, !TextUtils.isEmpty(errorMessage))
                                    }

                                    override fun afterTextChanged(s: Editable) {

                                    }
                                })

                                applicationDetailsView.addView(view)
                                addToValuesList(view, insuranceProductApplicationDetails)

                            } else if (insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_DROPDOWN, ignoreCase = true)) {

                                val view = LayoutInflater.from(tvChangeInsuranceApplicationDetails.context).inflate(R.layout.application_detail_date, null, false)

                                (view.findViewById<View>(R.id.title) as TextView).text = insuranceProductApplicationDetails.label
                                val subTitleTextView = view.findViewById<TextView>(R.id.sub_title)
                                if (!TextUtils.isEmpty(insuranceProductApplicationDetails.value)) {
                                    subTitleTextView.text = insuranceProductApplicationDetails.value
                                } else {
                                    subTitleTextView.text = insuranceProductApplicationDetails.placeHolder
                                }

                                val errorMessageView = view.findViewById<TextView>(R.id.error_message)

                                applicationDetailsView.addView(view)
                                addToValuesList(view, insuranceProductApplicationDetails)
                            }
                        }

                        setValidateListener()

                        closeableBottomSheetDialog.setContentView(rootView)
                        closeableBottomSheetDialog.show()
                    }
                } else {
                    tvInsuranceApplicationDetails.hide()
                    tvChangeInsuranceApplicationDetails.hide()
                }

            } else {
                tvInsuranceApplicationDetails.hide()
                tvChangeInsuranceApplicationDetails.hide()

            }

            tvInsurancePrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceCartDigitalProduct.pricePerProduct, false)

            if (pageType.equals(PAGE_TYPE_CART, ignoreCase = true)) {
                tvChangeInsuranceApplicationDetails.show()
                cbSelectInsurance.show()
                cbSelectInsurance.setOnCheckedChangeListener { buttonView, isChecked ->
                    insuranceItemActionlistener.sendEventChangeInsuranceState(isChecked, insuranceCartDigitalProduct.productInfo.title)
                    if (!onBind) {
                        insuranceCartShops.shopItemsList[0].digitalProductList[0].optIn = isChecked
                        insuranceItemActionlistener.onInsuranceSelectStateChanges()
                    }
                }

                onBind = true
                cbSelectInsurance.isChecked = insuranceCartDigitalProduct.optIn
                onBind = false

                ivDeleteInsurance.show()
                ivDeleteInsurance.setOnClickListener {
                    val insuranceCartDigitalProductArrayList = ArrayList<InsuranceCartDigitalProduct>()
                    insuranceCartDigitalProductArrayList.add(insuranceCartDigitalProduct)
                    insuranceItemActionlistener.sendEventDeleteInsurance(insuranceCartDigitalProduct.productInfo.title)
                    insuranceItemActionlistener.deleteMacroInsurance(insuranceCartDigitalProductArrayList, true)
                }
            } else {
                tvChangeInsuranceApplicationDetails.hide()
                cbSelectInsurance.hide()
                ivDeleteInsurance.hide()
            }
        } else {
            itemView.hide()
        }
    }

    private fun onDateViewClicked(view: TextView) {
        val date = dateFormatter(view.text.toString())
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date))
        datePicker.DatePickerCalendar { year, month, day -> view.text = getDate(year, month, day) }
    }

    private fun validateView(valueView: TextView, insuranceProductApplicationDetails: InsuranceProductApplicationDetails): Boolean {
        errorMessage = ""

        for ((_, validationValue, type, validationErrorMessage) in insuranceProductApplicationDetails.validationsList) {
            if (type.equals(VALIDATION_TYPE_MIN_LENGTH, ignoreCase = true)) {
                if (!validateMinLength(valueView.text, validationValue)) {
                    errorMessage = validationErrorMessage
                    return false
                }
            } else if (type.equals(VALIDATION_TYPE_MAX_LENGTH, ignoreCase = true)) {
                if (!validateMaxLength(valueView.text, validationValue)) {
                    errorMessage = validationErrorMessage

                    return false
                }
            } else if (type.equals(VALIDATION_TYPE_PATTERN, ignoreCase = true)) {
                if (!validatePattern(valueView.text, validationValue)) {
                    errorMessage = validationErrorMessage

                    return false
                }
            } else if (type.equals(VALIDATION_TYPE_MIN_DATE, ignoreCase = true)) {
                if (!validateMinDate(valueView.text.toString(), validationValue)) {
                    errorMessage = validationErrorMessage
                    return false
                }
            } else if (type.equals(VALIDATION_TYPE_MAX_DATE, ignoreCase = true)) {

                if (!validateMaxDate(valueView.text.toString(), validationValue)) {
                    errorMessage = validationErrorMessage
                    return false
                }
            }
        }
        return true
    }

    private fun setValidateListener() {
        btnValidate!!.setOnClickListener { v ->
            if (validateViews()) {
                insuranceItemActionlistener.updateInsuranceProductData(insuranceCartShops!!, updateInsuranceProductApplicationDetailsArrayList)
                closeableBottomSheetDialog.dismiss()
            }
        }
    }

    private fun validateViews(): Boolean {
        var updatedValue = ""
        updateInsuranceProductApplicationDetailsArrayList.clear()
        for (valueView in typeValues) {
            val (id, _, _, _, _, _, _, validationsList) = valueView.tag as InsuranceProductApplicationDetails
            for ((_, validationValue, type, validationErrorMessage) in validationsList) {
                if (type.equals(VALIDATION_TYPE_MIN_LENGTH, ignoreCase = true)) {
                    if (!validateMinLength(valueView.text, validationValue)) {
                        //show Some Error
                        errorMessage = validationErrorMessage
                        Toast.makeText(ivInsuranceIcon.context, errorMessage, Toast.LENGTH_SHORT).show()
                        return false
                    } else {
                        updatedValue = valueView.text.toString()
                    }
                } else if (type.equals(VALIDATION_TYPE_MAX_LENGTH, ignoreCase = true)) {
                    if (!validateMaxLength(valueView.text, validationValue)) {
                        //show Some Error
                        errorMessage = validationErrorMessage
                        Toast.makeText(ivInsuranceIcon.context, errorMessage, Toast.LENGTH_SHORT).show()
                        return false
                    } else {
                        updatedValue = valueView.text.toString()
                    }
                } else if (type.equals(VALIDATION_TYPE_PATTERN, ignoreCase = true)) {
                    if (!validatePattern(valueView.text, validationValue)) {
                        //show Some Error
                        errorMessage = validationErrorMessage
                        Toast.makeText(ivInsuranceIcon.context, errorMessage, Toast.LENGTH_SHORT).show()
                        return false
                    } else {
                        updatedValue = valueView.text.toString()
                    }
                } else if (type.equals(VALIDATION_TYPE_MIN_DATE, ignoreCase = true)) {
                    if (!validateMinDate(valueView.text.toString(), validationValue)) {
                        errorMessage = validationErrorMessage
                        Toast.makeText(ivInsuranceIcon.context, errorMessage, Toast.LENGTH_SHORT).show()
                        return false
                    } else {
                        updatedValue = getDateInServerFormat(valueView.text.toString())
                    }
                } else if (type.equals(VALIDATION_TYPE_MAX_DATE, ignoreCase = true)) {

                    if (!validateMaxDate(valueView.text.toString(), validationValue)) {
                        errorMessage = validationErrorMessage
                        Toast.makeText(ivInsuranceIcon.context, errorMessage, Toast.LENGTH_SHORT).show()
                        return false
                    } else {
                        updatedValue = getDateInServerFormat(valueView.text.toString())
                    }
                }
            }

            val updateInsuranceProductApplicationDetails = UpdateInsuranceProductApplicationDetails(id, updatedValue)
            updateInsuranceProductApplicationDetailsArrayList.add(updateInsuranceProductApplicationDetails)
        }
        return true
    }

    private fun addToValuesList(view: View, data: InsuranceProductApplicationDetails) {
        val view1 = view.findViewById<TextView>(R.id.sub_title)
        view1.tag = data
        typeValues.add(view1)

    }

    public fun getProductTitle(): String {
        return tvProductTitle.text.toString()
    }

    companion object {
        @JvmField
        val TYPE_VIEW_INSURANCE_CART_SHOP = R.layout.insurance_cart_item_shop
    }
}
