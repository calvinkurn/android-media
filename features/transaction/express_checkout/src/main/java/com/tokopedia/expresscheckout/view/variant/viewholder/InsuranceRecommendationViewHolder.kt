package com.tokopedia.expresscheckout.view.variant.viewholder

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatDrawableManager
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
import kotlinx.android.synthetic.main.item_insurance_recommendation_product_page.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class InsuranceRecommendationViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<InsuranceRecommendationViewModel>(view) {

    private var datePicker: SaldoDatePickerUtil? = null
    private var errorMessage: String = ""
    private var originalData = InsuranceRecommendationViewModel()
//    private var isInsuranceSelected = false

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

            itemView.insurance_checkbox.setChecked(insuranceCartDigitalProductViewModel.optIn)
            itemView.tv_product_title.text = insuranceCartDigitalProductViewModel.productInfo.title
            itemView.insurance_tv_title.text = insuranceCartDigitalProductViewModel.productInfo.sectionTitle

            if (!TextUtils.isEmpty(insuranceCartDigitalProductViewModel.productInfo.iconUrl)) {
                ImageHandler.loadImage(itemView.context, itemView.insurance_image_icon, insuranceCartDigitalProductViewModel.productInfo.iconUrl, R.drawable.ic_modal_toko)
            }
            itemView.insurance_tv_price.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceCartDigitalProductViewModel.pricePerProduct, false))

            itemView.tv_description.text = insuranceCartDigitalProductViewModel.productInfo.description
            if (TextUtils.isEmpty(insuranceCartDigitalProductViewModel.productInfo.infoText)) {
                itemView.insurance_tv_info.visibility = View.GONE
            } else {
                itemView.insurance_tv_info.visibility = View.VISIBLE

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
                            listener.setErrorInInsuranceSelection(!errorMessage.isBlank())
                            updateEditTextBackground(textView, errorMessageView.currentTextColor)
                        }
                    })


                } else if (insuranceProductApplicationDetails.type.equals("date", true) ||
                        insuranceProductApplicationDetails.type.equals("dropdown", true)) {

                    val view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.application_detail_date, null, false)

                    (view.findViewById(R.id.title) as TextView).text = insuranceProductApplicationDetails.label
                    val subTitleTextView = view.findViewById<TextView>(R.id.sub_title)
                    val errorMessageView = view.findViewById<TextView>(R.id.error_message)

                    if (insuranceProductApplicationDetails.type.equals("date", true)) {
                        subTitleTextView.text = ""
                        val dateText = insuranceProductApplicationDetails.value

                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        try {
                            val date = formatter.parse(dateText)
                            val newDate = SimpleDateFormat(DATE_FORMAT_VIEW).format(date)
                            subTitleTextView.text = newDate
                        } catch (e: ParseException) {
                            e.printStackTrace();
                        }

                        subTitleTextView.setOnClickListener {
                            onDateViewClicked(subTitleTextView)
                        }

                        subTitleTextView.addTextChangedListener(object : TextWatcher {

                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                                if (validateView(subTitleTextView, insuranceProductApplicationDetails)) {
                                    errorMessageView.visibility = View.GONE
                                    insuranceProductApplicationDetails.value = getDateStringInServerFormat(s.toString())
                                    insuranceProductApplicationDetails.isError = false
                                    listener.onInsuranceSelectedStateChanged(element, insuranceCartDigitalProductViewModel.optIn)
                                } else {
                                    errorMessageView.visibility = View.VISIBLE
                                    errorMessageView.text = errorMessage
                                    insuranceProductApplicationDetails.isError = true
                                    listener.onInsuranceSelectedStateChanged(null, insuranceCartDigitalProductViewModel.optIn)
                                }
                                listener.setErrorInInsuranceSelection(!errorMessage.isBlank())
                                updateEditTextBackground(subTitleTextView, errorMessageView.currentTextColor)
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
                            itemView.tv_description.visibility = View.GONE

                        } else {
                            applicationDetailsView.visibility = View.GONE
                            itemView.tv_description.visibility = View.VISIBLE
                        }

                    } else {
                        applicationDetailsView.visibility = View.GONE
                        itemView.tv_description.visibility = View.VISIBLE
                    }
                    listener.setErrorInInsuranceSelection(!errorMessage.isBlank())
                    listener.onInsuranceSelectedStateChanged(originalData, isChecked)
                }

            })

        }
    }

    private fun getDateStringInServerFormat(toString: String): String {

        var date = Date()
        val formatter = SimpleDateFormat(DATE_FORMAT_VIEW)
        try {
            date = formatter.parse(toString)

        } catch (e: ParseException) {
            e.printStackTrace();
        }
        val newDate = SimpleDateFormat("yyyy-MM-dd").format(date)
        return newDate
    }

    private val DATE_FORMAT_VIEW = "dd MMM yyyy"


    private fun onDateViewClicked(view: TextView) {

        val date = dateFormatter(view.text.toString())
        datePicker?.setDate(getDay(date), getStartMonth(date), getStartYear(date))
        datePicker?.DatePickerCalendar(object : SaldoDatePickerUtil.onDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int) {
                view.text = getDate(year, month, day)
            }
        })

    }

    private fun dateFormatter(date: String): String {

        val sdf = SimpleDateFormat(DATE_FORMAT_VIEW)
        val sdf_ws = SimpleDateFormat("dd/MM/yyyy")
        var formattedStart: Date? = null
        try {
            formattedStart = sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return sdf_ws.format(formattedStart)

    }

    private fun getDate(year: Int, month: Int, day: Int): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_VIEW)
        val date = Date()
        val cal = GregorianCalendar()
        cal.time = date
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.MONTH, month - 1)
        return dateFormat.format(cal.time)
    }

    private fun getStartYear(date: String): Int {
        val year = date.substring(6, 10)
        return Integer.parseInt(year)
    }

    private fun getStartMonth(date: String): Int {
        val month = date.substring(3, 5)
        return Integer.parseInt(month)
    }

    private fun getDay(date: String): Int {
        val day = date.substring(0, 2)
        return Integer.parseInt(day)
    }

    @SuppressLint("RestrictedApi")
    private fun updateEditTextBackground(mEditText: TextView, colorInt: Int) {
        if (mEditText == null) {
            return
        }

        var editTextBackground: Drawable? = mEditText.background ?: return

        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(editTextBackground!!)) {
            editTextBackground = editTextBackground.mutate()
        }

        val isErrorShowing = !errorMessage.isBlank()
        if (isErrorShowing) {
            // Set a color filter of the error color
            editTextBackground!!.colorFilter = AppCompatDrawableManager.getPorterDuffColorFilter(
                    colorInt, PorterDuff.Mode.SRC_IN)
        } else {
            // Else reset the color filter and refresh the drawable state so that the
            // normal tint is used
            DrawableCompat.clearColorFilter(editTextBackground!!)
            mEditText.refreshDrawableState()
        }
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
                if (data.type.equals("minLength", ignoreCase = true)) {
                    if (!validateMinLength(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }

                } else if (data.type.equals("maxLength", ignoreCase = true)) {

                    if (!validateMaxLength(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }

                } else if (data.type.equals("pattern", ignoreCase = true)) {

                    if (!validatePattern(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }
                } else if (data.type.equals("minDate", ignoreCase = true)) {

                    if (!validateMinDate(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }
                } else if (data.type.equals("maxDate", ignoreCase = true)) {

                    if (!validateMaxDate(view1.text, data.validationValue)) {
                        errorMessage = data.validationErrorMessage
                    }
                }
            }

            updateEditTextBackground(view1, errorView.currentTextColor)
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
            if (data.type.equals("minLength", ignoreCase = true)) {
                if (!validateMinLength(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage
                    return false
                }
            } else if (data.type.equals("maxLength", ignoreCase = true)) {
                if (!validateMaxLength(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage

                    return false
                }
            } else if (data.type.equals("pattern", ignoreCase = true)) {
                if (!validatePattern(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage

                    return false
                }
            } else if (data.type.equals("minDate", ignoreCase = true)) {
                if (!validateMinDate(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage
                    return false
                }
            } else if (data.type.equals("maxDate", ignoreCase = true)) {

                if (!validateMaxDate(valueView.text, data.validationValue)) {
                    errorMessage = data.validationErrorMessage
                    return false
                }
            }
        }
        return true
    }


    private fun validatePattern(value: CharSequence?, regExPattern: String): Boolean {
        if (value == null) {
            return false
        } else {
            val pattern = Pattern.compile(regExPattern)
            val matcher = pattern.matcher(value)
            return matcher.matches()
        }
    }

    private fun validateMinDate(value: CharSequence?, minValue: String): Boolean {

        val sdfo1 = SimpleDateFormat(DATE_FORMAT_VIEW)
        val incomingValue = sdfo1.parse(value.toString())

        val sdfo2 = SimpleDateFormat("yyyy-MM-dd")
        val minDate = sdfo2.parse(minValue)

        if (incomingValue.compareTo(minDate) >= 0) {
            System.out.println("incomingValue is after minDate")
            return true
        } else {
            System.out.println("incomingValue is before minDate")
            return false
        }
    }

    private fun validateMaxDate(value: CharSequence?, maxValue: String): Boolean {
        val sdfo1 = SimpleDateFormat(DATE_FORMAT_VIEW)
        val incomingValue = sdfo1.parse(value.toString())

        val sdfo2 = SimpleDateFormat("yyyy-MM-dd")
        val minDate = sdfo2.parse(maxValue)

        if (incomingValue.compareTo(minDate) > 0) {
            System.out.println("incomingValue is after minDate")
            return false
        } else {
            System.out.println("incomingValue is before minDate")
            return true
        }
    }

    private fun validateMaxLength(text: CharSequence, maxLength: String): Boolean {
        return TextUtils.isEmpty(text) || text.length <= Integer.valueOf(maxLength)
    }

    private fun validateMinLength(text: CharSequence, minLength: String): Boolean {
        return !TextUtils.isEmpty(text) && text.length >= Integer.valueOf(minLength)
    }

    private val typeValues = ArrayList<View>()

    private fun addToValuesList(view: View, data: InsuranceProductApplicationDetailsViewModel) {
        val view1 = view.findViewById<TextView>(R.id.sub_title)
        view1.tag = data
        typeValues.add(view)
    }

}