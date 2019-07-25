package com.tokopedia.expresscheckout.view.variant.viewholder

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
import com.tokopedia.date.util.SaldoDatePickerUtil
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.InsuranceApplicationValueViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.InsuranceProductApplicationDetailsViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.InsuranceRecommendationViewModel
import com.tokopedia.transactiondata.utils.*
import kotlinx.android.synthetic.main.item_insurance_recommendation_product_page.view.*
import java.util.*

class InsuranceRecommendationViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<InsuranceRecommendationViewModel>(view) {

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
                itemView.insurance_tv_subtitle.visibility = View.VISIBLE
            } else {
                itemView.insurance_tv_subtitle.visibility = View.GONE
            }

            if (insuranceCartDigitalProductViewModel.productInfo.description.isNotBlank()) {
                itemView.tv_insurance_description.text = insuranceCartDigitalProductViewModel.productInfo.description
                itemView.tv_insurance_description.visibility = View.VISIBLE
            } else {
                itemView.tv_insurance_description.visibility = View.GONE
            }


            if (!TextUtils.isEmpty(insuranceCartDigitalProductViewModel.productInfo.iconUrl)) {
                ImageHandler.loadImage(itemView.context, itemView.insurance_image_icon, insuranceCartDigitalProductViewModel.productInfo.iconUrl, R.drawable.ic_modal_toko)
            }
            itemView.insurance_tv_price.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceCartDigitalProductViewModel.pricePerProduct, false))

            if (insuranceCartDigitalProductViewModel.productInfo.linkName.isBlank()) {
                itemView.insurance_tv_info.visibility = View.GONE
            } else {
                itemView.insurance_tv_info.visibility = View.VISIBLE
                itemView.insurance_tv_info.text = insuranceCartDigitalProductViewModel.productInfo.linkName
                itemView.insurance_tv_info.setOnClickListener({
                    // TODO: 19/6/19 open bottom sheet with url
                })
            }

            val applicationDetailsView = itemView.application_detail_ll
            val insuranceProductApplicationDetailsArrayList = insuranceCartDigitalProductViewModel.applicationDetails

            for (insuranceProductApplicationDetails in insuranceProductApplicationDetailsArrayList) {

                if (insuranceProductApplicationDetails.type.equals("text", true) ||
                        insuranceProductApplicationDetails.type.equals("number", true)) {

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
                                errorMessageView.visibility = View.GONE
                                insuranceProductApplicationDetails.value = s.toString()
                                insuranceProductApplicationDetails.isError = false
                                listener.onInsuranceSelectedStateChanged(element, insuranceCartDigitalProductViewModel.optIn)
                            } else {
                                errorMessageView.visibility = View.VISIBLE
                                errorMessageView.text = errorMessage
                                insuranceProductApplicationDetails.isError = true
                                listener.onInsuranceSelectedStateChanged(null, insuranceCartDigitalProductViewModel.optIn)
                            }
                            if (errorMessage.isBlank()) {
                                itemView.tv_info_text.visibility = View.VISIBLE
                            } else {
                                itemView.tv_info_text.visibility = View.GONE
                            }
                            listener.setErrorInInsuranceSelection(!errorMessage.isBlank())
                            updateEditTextBackground(textView, errorMessageView.currentTextColor, !errorMessage.isBlank())
                        }
                    })


                } else if (insuranceProductApplicationDetails.type.equals("date", true) ||
                        insuranceProductApplicationDetails.type.equals("dropdown", true)) {

                    val view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.application_detail_date, null, false)

                    (view.findViewById(R.id.title) as TextView).text = insuranceProductApplicationDetails.label
                    val subTitleTextView = view.findViewById<TextView>(R.id.sub_title)
                    val errorMessageView = view.findViewById<TextView>(R.id.error_message)

                    if (insuranceProductApplicationDetails.type.equals("date", true)) {
                        subTitleTextView.text = getDateStringInUIFormat(insuranceProductApplicationDetails.value)
                        subTitleTextView.setOnClickListener {
                            onDateViewClicked(subTitleTextView)
                        }

                        subTitleTextView.addTextChangedListener(object : TextWatcher {

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                                if (validateView(subTitleTextView, insuranceProductApplicationDetails)) {
                                    errorMessageView.visibility = View.GONE
                                    insuranceProductApplicationDetails.value = getDateInServerFormat(s.toString())
                                    insuranceProductApplicationDetails.isError = false
                                    listener.onInsuranceSelectedStateChanged(element, insuranceCartDigitalProductViewModel.optIn)
                                } else {
                                    errorMessageView.visibility = View.VISIBLE
                                    errorMessageView.text = errorMessage
                                    insuranceProductApplicationDetails.isError = true
                                    listener.onInsuranceSelectedStateChanged(null, insuranceCartDigitalProductViewModel.optIn)
                                }
                                if (errorMessage.isBlank()) {
                                    itemView.tv_info_text.visibility = View.VISIBLE
                                } else {
                                    itemView.tv_info_text.visibility = View.GONE
                                }
                                listener.setErrorInInsuranceSelection(!errorMessage.isBlank())
                                updateEditTextBackground(subTitleTextView, errorMessageView.currentTextColor, !errorMessage.isBlank())
                            }

                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


                            }

                            override fun afterTextChanged(s: Editable?) {


                            }
                        })

                    } else if (insuranceProductApplicationDetails.type.equals("dropdown", true)) {
                        subTitleTextView.text = insuranceProductApplicationDetails.value
                    }

                    applicationDetailsView.addView(view)
                    addToValuesList(view, insuranceProductApplicationDetails)

                }
            }

            applicationDetailsView.visibility = View.GONE

            itemView.insurance_checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

                run {
                    insuranceCartDigitalProductViewModel.optIn = isChecked

                    if (!insuranceCartDigitalProductViewModel.isProductLevel &&
                            insuranceCartDigitalProductViewModel.isApplicationNeeded) {
                        if (isChecked) {
                            validateViews()
                            applicationDetailsView.visibility = View.VISIBLE
                            itemView.tv_insurance_description.visibility = View.GONE
                        } else {
                            itemView.tv_insurance_description.visibility = View.VISIBLE
                            applicationDetailsView.visibility = View.GONE
                        }
                        if (errorMessage.isBlank() && isChecked) {
                            itemView.tv_info_text.visibility = View.VISIBLE
                        } else {
                            itemView.tv_info_text.visibility = View.GONE
                        }

                    } else {
                        applicationDetailsView.visibility = View.GONE
                        itemView.tv_info_text.visibility = View.GONE
                    }
                    listener.setErrorInInsuranceSelection(!errorMessage.isBlank())
                    listener.onInsuranceSelectedStateChanged(originalData, isChecked)
                }

            })
            itemView.tv_info_text.text = insuranceCartDigitalProductViewModel.productInfo.infoText
            itemView.insurance_checkbox.setChecked(insuranceCartDigitalProductViewModel.optIn)


        } else {
            itemView.visibility = View.GONE
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
                errorView.visibility = View.GONE
                insuranceProductApplicationDetailsViewModel.isError = false
                val updateInsuranceProductApplicationDetails =
                        InsuranceApplicationValueViewModel(insuranceProductApplicationDetailsViewModel.id, view1.text.toString())
                updateInsuranceProductApplicationDetailsArrayList.add(updateInsuranceProductApplicationDetails)
//                return true
            } else {
                errorView.text = errorMessage
                errorView.visibility = View.VISIBLE
                insuranceProductApplicationDetailsViewModel.isError = true
//                return false
            }
        }
//        return true
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