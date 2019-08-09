package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.InsuranceItemActionListener
import com.tokopedia.date.util.SaldoDatePickerUtil
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.transactiondata.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceApplicationValidation
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartDigitalProduct
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartShops
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceProductApplicationDetails

import java.util.ArrayList

import com.tokopedia.transaction.insurance.utils.PAGE_TYPE_CART
import com.tokopedia.transaction.insurance.utils.VALIDATION_TYPE_MAX_DATE
import com.tokopedia.transaction.insurance.utils.VALIDATION_TYPE_MAX_LENGTH
import com.tokopedia.transaction.insurance.utils.VALIDATION_TYPE_MIN_DATE
import com.tokopedia.transaction.insurance.utils.VALIDATION_TYPE_MIN_LENGTH
import com.tokopedia.transaction.insurance.utils.VALIDATION_TYPE_PATTERN
import com.tokopedia.transaction.insurance.utils.dateFormatter
import com.tokopedia.transaction.insurance.utils.getDate
import com.tokopedia.transaction.insurance.utils.getDateInServerFormat
import com.tokopedia.transaction.insurance.utils.getDateStringInUIFormat
import com.tokopedia.transaction.insurance.utils.getDay
import com.tokopedia.transaction.insurance.utils.getStartMonth
import com.tokopedia.transaction.insurance.utils.getStartYear
import com.tokopedia.transaction.insurance.utils.openBottomSheetWebView
import com.tokopedia.transaction.insurance.utils.updateEditTextBackground
import com.tokopedia.transaction.insurance.utils.validateMaxDate
import com.tokopedia.transaction.insurance.utils.validateMaxLength
import com.tokopedia.transaction.insurance.utils.validateMinDate
import com.tokopedia.transaction.insurance.utils.validateMinLength
import com.tokopedia.transaction.insurance.utils.validatePattern


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
    private var closeableBottomSheetDialog: CloseableBottomSheetDialog? = null
    private var errorMessage: String? = null
    private var datePicker: SaldoDatePickerUtil? = null

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
    }

    fun bindData(insuranceCartShops: InsuranceCartShops, position: Int, pageType: String) {

        this.insuranceCartShops = insuranceCartShops

        if (!insuranceCartShops.shopItemsList.isEmpty() && !insuranceCartShops.shopItemsList[0].digitalProductList.isEmpty()) {

            val insuranceCartDigitalProduct = insuranceCartShops.shopItemsList[0].digitalProductList[0]

            datePicker = SaldoDatePickerUtil(ivInsuranceIcon.context as Activity)

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
                tvInsuranceSubtitle.visibility = View.VISIBLE
            } else {
                tvInsuranceSubtitle.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(insuranceCartDigitalProduct.productInfo.tickerText)) {
                tvInsuranceTickerText.text = insuranceCartDigitalProduct.productInfo.tickerText
                tvInsuranceTickerText.visibility = View.VISIBLE
            } else {
                tvInsuranceTickerText.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(insuranceCartDigitalProduct.productInfo.iconUrl)) {
                ImageHandler.loadImage(ivInsuranceIcon.context, ivInsuranceIcon, insuranceCartDigitalProduct.productInfo.iconUrl, R.drawable.ic_modal_toko)
            }

            if (TextUtils.isEmpty(insuranceCartDigitalProduct.productInfo.linkName)) {
                tvInsuranceInfo.visibility = View.GONE
            } else {
                tvInsuranceInfo.visibility = View.VISIBLE
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
                    tvInsuranceApplicationDetails.visibility = View.VISIBLE
                    tvChangeInsuranceApplicationDetails.visibility = View.VISIBLE

                    tvChangeInsuranceApplicationDetails.setOnClickListener {
                        closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(tvChangeInsuranceApplicationDetails.context)
                        val rootView = LayoutInflater.from(tvChangeInsuranceApplicationDetails.context).inflate(R.layout.layout_insurance_bottom_sheet, null, false)

                        val applicationDetailsView = rootView.findViewById<LinearLayout>(R.id.ll_application_details)
                        btnValidate = rootView.findViewById(R.id.btn_validate)

                        typeValues.clear()
                        for (insuranceProductApplicationDetails in insuranceCartDigitalProduct.applicationDetails) {

                            if (insuranceProductApplicationDetails.type.equals("text", ignoreCase = true) || insuranceProductApplicationDetails.type.equals("number", ignoreCase = true)) {

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
                                            errorMessageView.visibility = View.GONE
                                        } else {
                                            errorMessageView.visibility = View.VISIBLE
                                            errorMessageView.text = errorMessage
                                        }
                                        updateEditTextBackground(subtitle, errorMessageView.currentTextColor, !TextUtils.isEmpty(errorMessage))
                                    }

                                    override fun afterTextChanged(s: Editable) {

                                    }
                                })

                                applicationDetailsView.addView(view)
                                addToValuesList(view, insuranceProductApplicationDetails)

                            } else if (insuranceProductApplicationDetails.type.equals("date", ignoreCase = true)) {

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
                                            errorMessageView.visibility = View.GONE
                                            insuranceProductApplicationDetails.value = getDateInServerFormat(s.toString())
                                            insuranceProductApplicationDetails.isError = false
                                        } else {
                                            errorMessageView.visibility = View.VISIBLE
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

                            } else if (insuranceProductApplicationDetails.type.equals("dropdown", ignoreCase = true)) {

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

                        closeableBottomSheetDialog!!.setContentView(rootView)
                        closeableBottomSheetDialog!!.show()
                    }
                } else {
                    tvInsuranceApplicationDetails.visibility = View.GONE
                    tvChangeInsuranceApplicationDetails.visibility = View.GONE
                }

            } else {
                tvInsuranceApplicationDetails.visibility = View.GONE
                tvChangeInsuranceApplicationDetails.visibility = View.GONE

            }

            tvInsurancePrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceCartDigitalProduct.pricePerProduct, false)

            if (pageType.equals(PAGE_TYPE_CART, ignoreCase = true)) {
                tvChangeInsuranceApplicationDetails.visibility = View.VISIBLE
                cbSelectInsurance.visibility = View.VISIBLE
                cbSelectInsurance.setOnCheckedChangeListener { buttonView, isChecked ->
                    insuranceCartShops.shopItemsList[0].digitalProductList[0].optIn = isChecked
                    insuranceItemActionlistener.onInsuranceSelectStateChanges()
                }

                /*
                 * By default need to keep this checked for cart page
                 * */

                cbSelectInsurance.isChecked = true
                insuranceCartDigitalProduct.optIn = true

                ivDeleteInsurance.visibility = View.VISIBLE
                ivDeleteInsurance.setOnClickListener {
                    val insuranceCartDigitalProductArrayList = ArrayList<InsuranceCartDigitalProduct>()
                    insuranceCartDigitalProductArrayList.add(insuranceCartDigitalProduct)
                    insuranceItemActionlistener.deleteMacroInsurance(insuranceCartDigitalProductArrayList, true)
                }
            } else {
                tvChangeInsuranceApplicationDetails.visibility = View.GONE
                cbSelectInsurance.visibility = View.GONE
                ivDeleteInsurance.visibility = View.GONE
            }
        } else {
            itemView.visibility = View.GONE
        }
    }

    private fun onDateViewClicked(view: TextView) {
        val date = dateFormatter(view.text.toString())
        datePicker!!.setDate(getDay(date), getStartMonth(date), getStartYear(date))
        datePicker!!.DatePickerCalendar { year, month, day -> view.text = getDate(year, month, day) }
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
                closeableBottomSheetDialog!!.dismiss()
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

    companion object {
        @JvmStatic
        val TYPE_VIEW_INSURANCE_CART_SHOP = R.layout.insurance_cart_item_shop
    }
}
