package com.tokopedia.expresscheckout.view.variant.viewholder

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.InsuranceApplicationValueViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.InsuranceProductApplicationDetailsViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.InsuranceRecommendationViewModel
import kotlinx.android.synthetic.main.item_insurance_recommendation_product_page.view.*
import java.util.*
import java.util.regex.Pattern

class InsuranceRecommendationViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<InsuranceRecommendationViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_insurance_recommendation_product_page
    }

    override fun bind(element: InsuranceRecommendationViewModel?) {

        if (element != null &&
                !element.cartShopsList.isNullOrEmpty() &&
                !element.cartShopsList[0].shopItemsList.isNullOrEmpty() &&
                !element.cartShopsList[0].shopItemsList[0].digitalProductList.isNullOrEmpty()) {

            val insuranceCartShopItemsViewModel = element.cartShopsList[0].shopItemsList[0]
            val insuranceCartDigitalProductViewModel = insuranceCartShopItemsViewModel.digitalProductList[0]

            val isInsuranceSelected = insuranceCartDigitalProductViewModel.optIn

            itemView.insurance_checkbox.setChecked(isInsuranceSelected)

            itemView.insurance_tv_title.text = insuranceCartDigitalProductViewModel.productInfo.title

            if (!TextUtils.isEmpty(insuranceCartDigitalProductViewModel.productInfo.iconUrl)) {
                ImageHandler.loadImage(itemView.context, itemView.insurance_image_icon, insuranceCartDigitalProductViewModel.productInfo.iconUrl, R.drawable.ic_modal_toko)
            }
            itemView.insurance_tv_price.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceCartDigitalProductViewModel.pricePerProduct, false))

            itemView.tv_description.text = insuranceCartDigitalProductViewModel.productInfo.description
            /*if (TextUtils.isEmpty(insuranceCartDigitalProduct.getProductInfo().getLinkDetailInfoTitle())) {
            tvInsuranceInfo.setVisibility(View.GONE);
        } else {
            tvInsuranceInfo.setVisibility(View.VISIBLE);
            tvInsuranceInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 19/6/19 open bottom sheet with url
                    CloseableBottomSheetDialog dealsCategoryBottomSheet =
                            CloseableBottomSheetDialog.createInstanceRounded(tvInsuranceInfo.getContext());

                }
            });
        }*/


//            closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(tvChangeInsuranceApplicationDetails.getContext())


//            val rootView = LayoutInflater.from(tvChangeInsuranceApplicationDetails.getContext()).inflate(R.layout.layout_insurance_bottom_sheet, null, false)

            val applicationDetailsView = itemView.application_detail_ll
//            btnValidate = rootView.findViewById(R.id.btn_validate)


            val insuranceProductApplicationDetailsArrayList = insuranceCartDigitalProductViewModel.applicationDetails


            for (insuranceProductApplicationDetails in insuranceProductApplicationDetailsArrayList) {

                if (insuranceProductApplicationDetails.type.equals("text", true) || insuranceProductApplicationDetails.type.equals("number", true)) {

                    val view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.application_detail_text, null, false)

                    (view.findViewById(R.id.title) as TextView).setText(insuranceProductApplicationDetails.label)
                    (view.findViewById(R.id.sub_title) as TextView).setText(insuranceProductApplicationDetails.value)

                    applicationDetailsView.addView(view)
                    addToValuesList(view, insuranceProductApplicationDetails)

                } else if (insuranceProductApplicationDetails.type.equals("date", true) || insuranceProductApplicationDetails.type.equals("dropdown", true)) {

                    val view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.application_detail_date, null, false)

                    (view.findViewById(R.id.title) as TextView).setText(insuranceProductApplicationDetails.label)
                    (view.findViewById(R.id.sub_title) as TextView).setText(insuranceProductApplicationDetails.value)

                    applicationDetailsView.addView(view)
                    addToValuesList(view, insuranceProductApplicationDetails)

                    // TODO: 12/7/19 open new bottom sheet with  calender

                }
            }

            applicationDetailsView.visibility = View.GONE
//            setValidateListener()


            itemView.insurance_checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                insuranceCartDigitalProductViewModel.optIn = isChecked

                if (isChecked) {
                    applicationDetailsView.visibility = View.VISIBLE
                    itemView.tv_description.visibility = View.GONE

                } else {
                    applicationDetailsView.visibility = View.GONE
                    itemView.tv_description.visibility = View.VISIBLE
                }
//                insuranceItemActionlistener.onInsuranceSelectStateChanges()
            })

        }
    }


    private fun setValidateListener() {
        if (validateViews()) {
//            insuranceItemActionlistener.updateInsuranceProductData(insuranceCartShops, updateInsuranceProductApplicationDetailsArrayList)
//            closeableBottomSheetDialog.dismiss()
        }
    }

    internal var updateInsuranceProductApplicationDetailsArrayList = ArrayList<InsuranceApplicationValueViewModel>()

    private fun validateViews(): Boolean {
        for (valueView in typeValues) {
            updateInsuranceProductApplicationDetailsArrayList.clear()
            val insuranceProductApplicationDetailsViewModel = valueView.tag as InsuranceProductApplicationDetailsViewModel
            for (data in insuranceProductApplicationDetailsViewModel.validationsList) {
                if (data.type.equals("minLength", ignoreCase = true)) {
                    if (!validateMinLength(valueView.text, data.validationValue)) {
                        //show Some Error
                        Toast.makeText(itemView.getContext(), "Min length Validation fail", Toast.LENGTH_SHORT).show()
                        return false
                    }
                } else if (data.type.equals("maxLength", ignoreCase = true)) {
                    if (!validateMaxLength(valueView.text, data.validationValue)) {
                        //show Some Error
                        Toast.makeText(itemView.getContext(), "Max length Validation fail", Toast.LENGTH_SHORT).show()
                        return false
                    }
                } else if (data.type.equals("pattern", ignoreCase = true)) {
                    if (!validatePattern(valueView.text, data.validationValue)) {
                        //show Some Error
                        Toast.makeText(itemView.getContext(), "Pattern Validation fail", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
            }
            val updateInsuranceProductApplicationDetails = InsuranceApplicationValueViewModel(insuranceProductApplicationDetailsViewModel.id, valueView.text.toString())
            updateInsuranceProductApplicationDetailsArrayList.add(updateInsuranceProductApplicationDetails)
            return true
        }
        return true
    }

    fun validatePattern(value: CharSequence?, regExPattern: String): Boolean {
        if (value == null) {
            return false
        } else {
            val pattern = Pattern.compile(regExPattern)
            val matcher = pattern.matcher(value)
            return matcher.matches()
        }
    }

    private fun validateMaxLength(text: CharSequence, maxLength: String): Boolean {
        return TextUtils.isEmpty(text) || text.length <= Integer.valueOf(maxLength)
    }

    private fun validateMinLength(text: CharSequence, minLength: String): Boolean {
        return !TextUtils.isEmpty(text) && text.length >= Integer.valueOf(minLength)
    }

    private val typeValues = ArrayList<TextView>()

    private fun addToValuesList(view: View, data: InsuranceProductApplicationDetailsViewModel) {
        val view1 = view.findViewById<TextView>(R.id.sub_title)
        view1.tag = data
        typeValues.add(view1)
    }


}