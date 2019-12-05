package com.tokopedia.atc_variant.view.viewholder

import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.atc_variant.R
import com.tokopedia.atc_variant.view.AddToCartVariantActionListener
import com.tokopedia.atc_variant.view.viewmodel.InsuranceRecommendationViewModel
import com.tokopedia.date.util.SaldoDatePickerUtil
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.purchase_platform.common.view.model.InsuranceApplicationValueViewModel
import com.tokopedia.purchase_platform.common.view.model.InsuranceProductApplicationDetailsViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.transaction.insurance.utils.*
import kotlinx.android.synthetic.main.item_insurance_recommendation_product_page.view.*
import java.util.*


class InsuranceRecommendationViewHolder(val view: View, val listenerNormal: AddToCartVariantActionListener) : AbstractViewHolder<InsuranceRecommendationViewModel>(view) {

    private var datePicker: SaldoDatePickerUtil? = null
    private var errorMessage: String = ""
    private var originalData = InsuranceRecommendationViewModel()

    companion object {
        val LAYOUT = R.layout.item_insurance_recommendation_product_page
    }

    override fun bind(element: InsuranceRecommendationViewModel?) {

        if (element != null &&
                !element.cartShopsList.isNullOrEmpty() &&
                !element.cartShopsList[0].shopItemsList.isNullOrEmpty() &&
                !element.cartShopsList[0].shopItemsList[0].digitalProductList.isNullOrEmpty()) {

            datePicker = SaldoDatePickerUtil(itemView.context as Activity?)
            this.originalData = element
            val insuranceCartShopItemsViewModel = element.cartShopsList[0].shopItemsList[0]
            val insuranceCartDigitalProductViewModel = insuranceCartShopItemsViewModel.digitalProductList[0]

            itemView.tv_product_title.text = insuranceCartDigitalProductViewModel.productInfo.sectionTitle
            itemView.insurance_tv_title.text = insuranceCartDigitalProductViewModel.productInfo.title

            if (insuranceCartDigitalProductViewModel.productInfo.subTitle.isNotBlank()) {
                itemView.insurance_tv_subtitle.text = insuranceCartDigitalProductViewModel.productInfo.subTitle
                itemView.insurance_tv_subtitle.show()
            } else {
                itemView.insurance_tv_subtitle.hide()
            }

            if (insuranceCartDigitalProductViewModel.productInfo.description.isNotBlank()) {
                itemView.tv_insurance_description.text = insuranceCartDigitalProductViewModel.productInfo.description
                itemView.tv_insurance_description.show()
            } else {
                itemView.tv_insurance_description.hide()
            }

            if (!TextUtils.isEmpty(insuranceCartDigitalProductViewModel.productInfo.iconUrl)) {
                ImageHandler.loadImage(itemView.context, itemView.insurance_image_icon, insuranceCartDigitalProductViewModel.productInfo.iconUrl, R.drawable.insurance_default_icon)
            }
            itemView.insurance_tv_price.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceCartDigitalProductViewModel.pricePerProduct, false))

            if (insuranceCartDigitalProductViewModel.productInfo.linkName.isBlank() ||
                    insuranceCartDigitalProductViewModel.productInfo.appLinkUrl.isBlank()) {
                itemView.insurance_tv_info.hide()
            } else {
                itemView.insurance_tv_info.show()
                itemView.insurance_tv_info.text = insuranceCartDigitalProductViewModel.productInfo.linkName
                itemView.insurance_tv_info.setOnClickListener {

                    openBottomSheetWebView(itemView.context,
                            insuranceCartDigitalProductViewModel.productInfo.appLinkUrl,
                            insuranceCartDigitalProductViewModel.productInfo.detailInfoTitle)

                }
            }

            val applicationDetailsView = itemView.application_detail_ll
            applicationDetailsView.removeAllViews()
            val insuranceProductApplicationDetailsArrayList = insuranceCartDigitalProductViewModel.applicationDetails

            for (insuranceProductApplicationDetails in insuranceProductApplicationDetailsArrayList) {

                if (insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_TEXT, true) ||
                        insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_NUMBER, true)) {

                    val view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.application_detail_text, null, false)

                    view.findViewById<TextView>(R.id.tv_title).text = insuranceProductApplicationDetails.label

                    val textView = view.findViewById<TextView>(R.id.sub_title)
                    val errorMessageView = view.findViewById<TextView>(R.id.error_message)
                    textView.text = insuranceProductApplicationDetails.value

                    applicationDetailsView.addView(view)
                    addToValuesList(view, insuranceProductApplicationDetails)

                    textView.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {

                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                            if (validateView(textView, insuranceProductApplicationDetails)) {
                                errorMessageView.hide()
                                insuranceProductApplicationDetails.value = s.toString()
                                insuranceProductApplicationDetails.isError = false
                                listenerNormal.onInsuranceSelectedStateChanged(element, insuranceCartDigitalProductViewModel.optIn)
                            } else {
                                errorMessageView.show()
                                errorMessageView.text = errorMessage
                                insuranceProductApplicationDetails.isError = true
                                listenerNormal.onInsuranceSelectedStateChanged(null, insuranceCartDigitalProductViewModel.optIn)
                            }
                            if (errorMessage.isBlank()) {
                                itemView.tv_info_text.show()
                            } else {
                                itemView.tv_info_text.hide()
                            }
                            updateEditTextBackground(textView, errorMessageView.currentTextColor, !errorMessage.isBlank())
                        }
                    })


                } else if (insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_DATE, true) ||
                        insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_DROPDOWN, true)) {

                    val view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.application_detail_date, null, false)

                    (view.findViewById(R.id.title) as TextView).text = insuranceProductApplicationDetails.label
                    val subTitleTextView = view.findViewById<TextView>(R.id.sub_title)
                    val errorMessageView = view.findViewById<TextView>(R.id.error_message)

                    if (insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_DATE, true)) {
                        subTitleTextView.text = getDateStringInUIFormat(insuranceProductApplicationDetails.value)
                        subTitleTextView.setOnClickListener {
                            onDateViewClicked(subTitleTextView)
                        }

                        subTitleTextView.addTextChangedListener(object : TextWatcher {

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                                if (validateView(subTitleTextView, insuranceProductApplicationDetails)) {
                                    errorMessageView.hide()
                                    insuranceProductApplicationDetails.value = getDateInServerFormat(s.toString())
                                    insuranceProductApplicationDetails.isError = false
                                    listenerNormal.onInsuranceSelectedStateChanged(element, insuranceCartDigitalProductViewModel.optIn)
                                } else {
                                    errorMessageView.show()
                                    errorMessageView.text = errorMessage
                                    insuranceProductApplicationDetails.isError = true
                                    listenerNormal.onInsuranceSelectedStateChanged(null, insuranceCartDigitalProductViewModel.optIn)
                                }
                                if (errorMessage.isBlank()) {
                                    itemView.tv_info_text.show()
                                } else {
                                    itemView.tv_info_text.hide()
                                }

                                updateEditTextBackground(subTitleTextView, errorMessageView.currentTextColor, !errorMessage.isBlank())
                            }

                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


                            }

                            override fun afterTextChanged(s: Editable?) {


                            }
                        })

                    } else if (insuranceProductApplicationDetails.type.equals(INSURANCE_APPLICATION_TYPE_DROPDOWN, true)) {
                        subTitleTextView.text = insuranceProductApplicationDetails.value
                    }

                    applicationDetailsView.addView(view)
                    addToValuesList(view, insuranceProductApplicationDetails)

                }
            }

            applicationDetailsView.hide()

            itemView.insurance_checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

                run {
                    insuranceCartDigitalProductViewModel.optIn = isChecked

                    if (!insuranceCartDigitalProductViewModel.isProductLevel &&
                            insuranceCartDigitalProductViewModel.isApplicationNeeded) {
                        if (isChecked) {
                            validateViews()
                            applicationDetailsView.show()
                            itemView.tv_insurance_description.hide()
                        } else {
                            itemView.tv_insurance_description.show()
                            applicationDetailsView.hide()
                        }
                        if (errorMessage.isBlank() && isChecked) {
                            itemView.tv_info_text.show()
                        } else {
                            itemView.tv_info_text.hide()
                        }

                    } else {
                        applicationDetailsView.hide()
                        itemView.tv_info_text.hide()
                    }
                    listenerNormal.onInsuranceSelectedStateChanged(originalData, isChecked)
                }

            })
            itemView.tv_info_text.text = insuranceCartDigitalProductViewModel.productInfo.infoText
            itemView.insurance_checkbox.setChecked(insuranceCartDigitalProductViewModel.optIn)


        } else {
            itemView.hide()
        }
    }

    private fun onDateViewClicked(view: TextView) {

        val date = dateFormatter(view.text.toString())
        datePicker?.setDate(getDay(date), getStartMonth(date), getStartYear(date))
        datePicker?.DatePickerCalendar(object : SaldoDatePickerUtil.onDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int) {
                view.text = getDate(year, month, day)
            }
        })

    }

    internal var updateInsuranceProductApplicationDetailsArrayList = ArrayList<InsuranceApplicationValueViewModel>()

    private fun validateViews() {

        for (view in typeValues) {

            val view1 = view.findViewById<TextView>(R.id.sub_title)
            val errorView = view.findViewById<TextView>(R.id.error_message)

            errorMessage = ""
            updateInsuranceProductApplicationDetailsArrayList.clear()
            val insuranceProductApplicationDetailsViewModel = view1.tag as InsuranceProductApplicationDetailsViewModel
            for (data in insuranceProductApplicationDetailsViewModel.validationsList) {
                if (data.type.equals(VALIDATION_TYPE_MIN_LENGTH, ignoreCase = true)) {
                    if (!validateMinLength(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }

                } else if (data.type.equals(VALIDATION_TYPE_MAX_LENGTH, ignoreCase = true)) {

                    if (!validateMaxLength(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }

                } else if (data.type.equals(VALIDATION_TYPE_PATTERN, ignoreCase = true)) {

                    if (!validatePattern(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }
                } else if (data.type.equals(VALIDATION_TYPE_MIN_DATE, ignoreCase = true)) {

                    if (!validateMinDate(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }
                } else if (data.type.equals(VALIDATION_TYPE_MAX_DATE, ignoreCase = true)) {

                    if (!validateMaxDate(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }
                }
            }

            updateEditTextBackground(view1, errorView.currentTextColor, !errorMessage.isBlank())
            if (errorMessage.isEmpty()) {
                errorView.hide()
                insuranceProductApplicationDetailsViewModel.isError = false
                val updateInsuranceProductApplicationDetails =
                        InsuranceApplicationValueViewModel(insuranceProductApplicationDetailsViewModel.id, view1.text.toString())
                updateInsuranceProductApplicationDetailsArrayList.add(updateInsuranceProductApplicationDetails)
            } else {
                errorView.text = errorMessage
                errorView.show()
                insuranceProductApplicationDetailsViewModel.isError = true
            }
        }
    }


    private fun validateView(valueView: TextView, insuranceApplicationDetail: InsuranceProductApplicationDetailsViewModel): Boolean {
        errorMessage = ""
        for (data in insuranceApplicationDetail.validationsList) {
            if (data.type.equals(VALIDATION_TYPE_MIN_LENGTH, ignoreCase = true)) {
                if (!validateMinLength(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage
                    return false
                }
            } else if (data.type.equals(VALIDATION_TYPE_MAX_LENGTH, ignoreCase = true)) {
                if (!validateMaxLength(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage

                    return false
                }
            } else if (data.type.equals(VALIDATION_TYPE_PATTERN, ignoreCase = true)) {
                if (!validatePattern(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage

                    return false
                }
            } else if (data.type.equals(VALIDATION_TYPE_MIN_DATE, ignoreCase = true)) {
                if (!validateMinDate(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage
                    return false
                }
            } else if (data.type.equals(VALIDATION_TYPE_MAX_DATE, ignoreCase = true)) {

                if (!validateMaxDate(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage
                    return false
                }
            }
        }
        return true
    }

    private val typeValues = ArrayList<View>()

    private fun addToValuesList(view: View, data: InsuranceProductApplicationDetailsViewModel) {
        val view1 = view.findViewById<TextView>(R.id.sub_title)
        view1.tag = data
        typeValues.add(view)
    }

}