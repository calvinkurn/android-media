package com.tokopedia.core.myproduct.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.util.Pair;
import com.tokopedia.core2.R;

/**
 * Created by m.normansyah on 15/12/2015.
 * this class is used for {@link com.tokopedia.core.myproduct.fragment.AddProductFragment} to verfy input
 */
public class VerificationUtils {
    public static final String TAG = VerificationUtils.class.getSimpleName();
    public static final String messageTAG = TAG+" : ";

    public static Pair<Boolean, String> validatePrice(Context context, String... text){
        boolean resBoolean = false;
        String resString = null;
        if(text==null||text.length<2) {
            resBoolean = false;
            resString = "supply text";
            return new Pair<>(resBoolean, resString);
        }

        try {
            String currency = text[0];
            if(text.length == 3 && text[2].equals("checkwholesale")){
                switch (currency){
                    case "Rp":
                        String priceText = text[1].replace(",","").replace(".", "");
                        if(priceText.equals("")){
                            resBoolean = false;
                            resString = context.getString(R.string.error_empty_price);
                            return new Pair<>(resBoolean, resString);
                        }
                        if(Double.parseDouble(priceText) == 100){
                            resBoolean = false;
                            resString = context.getString(R.string.error_minimum_price_wholesale);
                            return new Pair<>(resBoolean, resString);
                        }
                        break;
                    case "US$":
                        priceText = text[1].replace(",","");
                        if(priceText.equals("")){
                            resBoolean = false;
                            resString = context.getString(R.string.error_empty_price);
                            return new Pair<>(resBoolean, resString);
                        }
                        if(Double.parseDouble(priceText) == 1){
                            resBoolean = false;
                            resString = context.getString(R.string.error_minimum_price_wholesale);
                            return new Pair<>(resBoolean, resString);
                        }
                        break;
                }
            }
            if (validateCurrency(currency)) {
                switch (currency) {
                    case "Rp":
                        String priceText = text[1].replace(",","").replace(".", "");
                        if(priceText.equals("")){
                            resBoolean = false;
                            resString = context.getString(R.string.error_empty_price);
                            return new Pair<>(resBoolean, resString);
                        }
                        double price = Double.parseDouble(priceText);
                        if(price < 100){
                            resBoolean = false;
                            resString = context.getString(R.string.title_price_range)+"  100 - 100.000.000";
                            return new Pair<>(resBoolean, resString);}
                        if(price > 100_000_000){
                            resBoolean = false;
                            resString = context.getString(R.string.title_price_range)+"  100 - 100.000.000";
                            return new Pair<>(resBoolean, resString);}
                        if (price >= 100 && price <= 100_000_000){
                            resBoolean= true;
                            resString = null;
                            return new Pair<>(resBoolean, resString);}
                        break;
                    case "US$":
                        priceText = text[1].replace(",","");
                        if(priceText.equals("")){
                            resBoolean = false;
                            resString = context.getString(R.string.error_empty_price);
                            return new Pair<>(resBoolean, resString);
                        }
                        price = Double.parseDouble(priceText);
                        if(price < 1){
                            resBoolean = false;
                            resString = context.getString(R.string.error_price_higher_1);
                            return new Pair<>(resBoolean, resString);
                        }
                        if(price > 7500){
                            resBoolean = false;
                            resString = context.getString(R.string.error_price_lower4);
                            return new Pair<>(resBoolean, resString);
                        }
                        if(price >= 1 && price <= 7500){
                            resBoolean= true;
                            resString = null;
                            return new Pair<>(resBoolean, resString);}
                        break;
                    default:
                        resBoolean = false;
                        resString = context.getString(R.string.error_no_currency_selected);
                        return new Pair<>(resBoolean, resString);
                }
            } else {
                resBoolean = false;
                resString = context.getString(R.string.error_no_currency_selected);
                return new Pair<>(resBoolean, resString);
            }
        }catch(NumberFormatException nfe){
            Log.e(TAG, messageTAG+nfe.getLocalizedMessage());
            resBoolean = false;
            resString = context.getString(R.string.error_number);
            return new Pair<>(resBoolean, resString);
        }
        return null;
    }

    /**
     * validate price according to tokopedia policy
     * @param text need 2 String, currency text and value text
     * @return true if valid otherwise false if not valid
     */
    @Deprecated
    public static boolean validatePrice(String... text){
        boolean ret = false;
        if(text==null||text.length<=2)
            ret = false;
        try {
            String currency = text[0];
            if (validateCurrency(currency)) {
                switch (currency) {
                    case "Rp":
                        String priceText = text[1].replace(",","").replace(".", "");
                        if(priceText.equals(""))
                            priceText +="0";
                        int price = Integer.parseInt(priceText);
                        if (price >= 100 && price <= 100_000_000){
                            ret= true;
                        }
//                        else if(price==0){// if empty then no need to show error
//                            ret = true;
//                        }
                        break;
                    case "US$":
                        priceText = text[1].replace(",","");
                        double priceDouble = Double.parseDouble(priceText);
                        if(priceDouble >= 1 && priceDouble <= 7500){
                            ret= true;
                        }
//                        else if(price==0){// if empty then no need to show error
//                            ret = true;
//                        }
                        break;
                    default:
                        ret = false;
                }
            } else {
                ret = false;
            }
        }catch(NumberFormatException nfe){
            Log.e(TAG, messageTAG+nfe.getLocalizedMessage());
        }
        return ret;
    }

    public static Pair<Boolean, String> validateMinimumWeight(Context context, String... text){
        Pair<Boolean, String> res = new Pair<>();
        boolean resBoolean = false;
        String resString = null;
        String weightUnit = text[0];
        String weight = text[1];
        if (TextUtils.isEmpty(weight)) {
            resString = context.getString(R.string.error_empty_weight);
            resBoolean = false;
        } else if (weightUnit.equals(context.getResources().getStringArray(R.array.weight)[0])) {
            try {
                if (Integer.parseInt(weight) < 1
                        || Integer.parseInt(weight) > 100_000) {
                    resString = context.getString(R.string.error_weight_value_gr);
                    resBoolean = false;
                }else{
                    resBoolean = true;
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, messageTAG + e.getLocalizedMessage());
                resString = context.getString(R.string.error_weight_value_gr);
                resBoolean = false;
            }
        } else if (weightUnit.equals(context.getResources().getStringArray(R.array.weight)[1])) {
            try {
                if (Integer.parseInt(weight) < 1 || Integer.parseInt(weight) > 100) {
                    resString = context.getString(R.string.error_weight_value_kg);
                    resBoolean = false;
                }else{
                    resBoolean = true;
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, messageTAG+e.getLocalizedMessage());
                resString = context.getString(R.string.error_weight_value_kg);
                resBoolean = false;
            }
        }else{
            return new Pair<>(false, context.getString(R.string.error_no_weight_unit));
        }
        res.setModel1(resBoolean);
        res.setModel2(resString);
        return res;
    }

    public static boolean validateCurrency(String text){
        if(text != null && (text.contains("Rp")||text.contains("US$")))
            return true;
        return false;
    }
}

