package com.tokopedia.core.myproduct.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core2.R;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.myproduct.adapter.WholesaleAdapter;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.model.TextDeleteModel;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;
import com.tokopedia.core.util.Pair;

import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;
import static com.tokopedia.core.myproduct.model.constant.ImageModelType.SELECTED;

/**
 * Created by m.normansyah on 15/12/2015.
 * this class is used for {@link com.tokopedia.core.myproduct.fragment.AddProductFragment} to verfy input
 */
public class VerificationUtils {
    public static final String TAG = VerificationUtils.class.getSimpleName();
    public static final String messageTAG = TAG+" : ";

    public static Pair<Boolean, String> validateAllInstoped(Context context, List<ImageModel> textDeleteModels){
        boolean isSelect = false;
        for(ImageModel textDeleteModel : textDeleteModels){
            boolean contains = textDeleteModel.getTypes().contains(SELECTED.getType());
            if(!contains){
                isSelect |= true;
            }else{
                isSelect |= false;
            }
        }
        if(isSelect)
            return new Pair<>(false, context.getString(R.string.error_not_all_instoped));

        return new Pair<>(true, null);
    }

    public static Pair<Boolean, String> validateEtalase(Context context, List<TextDeleteModel> textDeleteModels){
        boolean isSelect = false;
        for(TextDeleteModel textDeleteModel : textDeleteModels){
            if(textDeleteModel.isDefault()){
                isSelect |= true;
            }else{
                isSelect |= false;
            }
        }
        if(isSelect)
            return new Pair<>(false, context.getString(R.string.error_no_etalase_selected));

        return new Pair<>(true, null);
    }

    public static Pair<Boolean, String> validateNewEtalaseName(Context context, String etalaseName){

        if (etalaseName.trim().isEmpty()){
            return new Pair<>(false, context.getString(R.string.error_empty_new_etalase_name));
        }

        if (etalaseName.trim().length() < 3){
            return new Pair<>(false, context.getString(R.string.etalase_less_than_three_char));
        }

        if(!DbManagerImpl.getInstance().isEtalaseEmpty(etalaseName)){
            return new Pair<>(false, context.getString(R.string.error_etalase_exist));
        }

        return new Pair<>(true, null);
    }

    public static Pair<Boolean, String> validateCategories(Context context, List<TextDeleteModel> textDeleteModels){
        boolean isSelect = false;
        for(TextDeleteModel textDeleteModel : textDeleteModels){
            if(textDeleteModel.isDefault()){
                isSelect |= true;
            }else{
                isSelect |= false;
            }
        }
        if(isSelect)
            return new Pair<>(false, context.getString(R.string.error_no_category_selected));

        return new Pair<>(true, null);
    }

    public static Pair<Boolean, String> validateImages(Context context, List<ImageModel> imageModels){
        boolean isAllDefaultImage = true;
        for(ImageModel imageModel : imageModels){
            if(imageModel.getDbId()==0){
                isAllDefaultImage &= true;
            }else{
                isAllDefaultImage &= false;
            }
        }

        if(isAllDefaultImage)
            return new Pair<>(false, context.getString(R.string.error_no_picture_selected));

        return new Pair<>(true, null);
    }

    /**
     * validate whole price item
     * @param context if description string need to get from context
     * @param currencyUnit currently support currency were rupiah and dollar
     * @param priceRef reference price ( single price )
     * @param wholeSalePrice whole sale price
     * @return true with null string if no error, false with string description if not valid
     */
    public static Pair<Boolean, String> validateWholeSalePrice(Context context, String currencyUnit, String priceRef, String wholeSalePrice, WholeSaleAdapterModel dataBefore) {
        Pair<Boolean, String> res = new Pair<>();
        boolean resBoolean = false;
        String resString = null;
        if (wholeSalePrice.length() <= 0 || wholeSalePrice.equals("")) {
            resString = context.getString(R.string.error_no_product_name);
            res.setModel1(false);
            res.setModel2(resString);
            return res;
        }
        try {
            switch (currencyUnit) {
                case "Rp":
                    String priceText = priceRef.replace(",", "").replace(".", "");
                    if (priceText.equals(""))
                        priceText += "0";
                    int refConv = Integer.parseInt(priceText);
                    int wholeSalePrc = Integer.parseInt(wholeSalePrice.replace(",", "").replace(".", ""));

                    if (wholeSalePrc >= refConv) {
                        resString = context.getString(R.string.error_price_lower);
                        res.setModel1(false);
                        res.setModel2(resString);
                        return res;
                    }

                    if (dataBefore != null) {
                        String priceBefore = String.format("%.0f", dataBefore.getWholeSalePrice()).replace(",", "");
                        priceBefore = priceBefore.replace(".", "");
                        int wholeSalePrcBefore = Integer.parseInt(priceBefore.replace(",", "").replace(".", ""));

                        if(wholeSalePrc >= wholeSalePrcBefore) {
                            resString = context.getString(R.string.error_price_lower_prev);
                            res.setModel1(false);
                            res.setModel2(resString);
                            return res;
                        }
                    }

                    if (validatePrice(currencyUnit, String.valueOf(wholeSalePrc))) {
                        res.setModel1(true);
                        res.setModel2(null);
                        return res;
                    } else {
                        res.setModel1(false);
                        res.setModel2(context.getString(R.string.error_price_less_than_min));
                        return res;
                    }
                case "US$":
                    priceText = priceRef.replace(",", "");
                    double refConvDollar = Double.parseDouble(priceText);
                    double wholeSalePrcDollar = Double.parseDouble(wholeSalePrice.replace(",", ""));

                    if (wholeSalePrcDollar >= refConvDollar) {
                        resString = context.getString(R.string.error_price_lower);
                        res.setModel1(false);
                        res.setModel2(resString);
                        return res;
                    }

                    if (dataBefore != null) {
                        String priceBefore = String.format("%.0f", dataBefore.getWholeSalePrice()).replace(",", "");
                        double wholeSalePrcBefore = Double.parseDouble(priceBefore.replace(",", ""));

                        if(wholeSalePrcDollar >= wholeSalePrcBefore) {
                            resString = context.getString(R.string.error_price_lower_prev);
                            res.setModel1(false);
                            res.setModel2(resString);
                            return res;
                        }
                    }

                    if (validatePrice(currencyUnit, String.valueOf(wholeSalePrcDollar))) {
                        res.setModel1(true);
                        res.setModel2(null);
                        return res;
                    } else {
                        res.setModel1(false);
                        res.setModel2(context.getString(R.string.error_price_higher_1));
                        return res;
                    }
            }
        } catch (NumberFormatException e) {
            res.setModel1(false);
            res.setModel2(context.getString(R.string.error_number));
            return res;
        }
        return res;
    }

    /**
     * validate whole price range
     * @param index use this only for {@code WholesaleAdapter.QTY1} and {@code WholesaleAdapter.QTY2}
     * @param context {@link Context} object
     * @param ref string from quantity one or quantity two
     * @param data string from quantity one or quantity two
     * @return true if valid and false if not valid
     */
    public static Pair<Boolean, String> validateWholeSaleItemQuantity(int index, Context context, String ref, String data, WholeSaleAdapterModel dataBefore){
        Pair<Boolean, String> res = new Pair<>();
        boolean resBoolean = false;
        String resString = null;
        switch (index){
            case WholesaleAdapter.QTY1:
                String quantityOne = data;
                String quantityTwo = ref;
                if(quantityOne.length()<=0||quantityOne.equals("")
                        ||quantityTwo.length()<=0||quantityTwo.equals("")){
                    resString = context.getString(R.string.error_field_required);
                    res.setModel1(false);
                    res.setModel2(resString);
                    return res;
                }
                double quantity1 = Double.parseDouble(quantityOne);
                double quantity2 = Double.parseDouble(quantityTwo);
                if((int) quantity1 <1 || (int) quantity1 >10000){
                    res.setModel1(false);
                    res.setModel2(context.getString(R.string.error_minimumorder_value));
                    return res;
                }

                if(quantity1<=1||quantity1>quantity2 ){
                    res.setModel1(false);
                    res.setModel2(context.getString(R.string.error_wholesale_quantity));
                    return res;
                }/*else if(quantity2<=0||quantity2<quantity1){
                    res.setModel1(false);
                    res.setModel2(context.getString(R.string.error_wholesale_quantity));
                    return res;
                }*/
                if(dataBefore != null && dataBefore.getQuantityTwo() >= quantity1){
                    res.setModel1(false);
                    res.setModel2(context.getString(R.string.error_wholesale_quantity));
                    return res;
                }
                res.setModel1(true);
                res.setModel2(null);
                return res;
            case WholesaleAdapter.QTY2:
                quantityOne = ref;
                quantityTwo = data;
                if(quantityOne.length()<=0||quantityOne.equals("")
                        ||quantityTwo.length()<=0||quantityTwo.equals("")){
                    resString = context.getString(R.string.error_field_required);
                    res.setModel1(false);
                    res.setModel2(resString);
                    return res;
                }
                quantity1 = Double.parseDouble(quantityOne);
                quantity2 = Double.parseDouble(quantityTwo);

                if((int) quantity2 <1 || (int) quantity2 >10000){
                    res.setModel1(false);
                    res.setModel2(context.getString(R.string.error_minimumorder_value));
                    return res;
                }
                /*if(quantity1<=1||quantity1>quantity2){
                    res.setModel1(false);
                    res.setModel2(context.getString(R.string.error_wholesale_quantity));
                    return res;
                }else */if(quantity2<=1||quantity2<quantity1){
                res.setModel1(false);
                res.setModel2(context.getString(R.string.error_wholesale_quantity));
                return res;
            }
                res.setModel1(true);
                res.setModel2(null);
                return res;
        }

        return res;
    }


    /**
     * validate resolution using path
     * @param path valid path
     * @return true if resolution is comply with tokopedia rules
     */
    public static Pair<Boolean, String> checkImageResolution(Context context, String path) throws MetadataUtil.UnSupportedimageFormatException{
        Pair<Integer, Integer> resolution = MetadataUtil.getWidthFromImage(path);
        if(checkNotNull(resolution))
            return VerificationUtils.checkImageResolution(context, resolution.getModel1(), resolution.getModel2());
        else{
            return new Pair<Boolean, String>(false, context.getString(R.string.error_picture_not_valid));
        }
    }

    /**
     * validate resolution using width and height
     * @param dimen width and height of image
     * @return true if width > 299 and height > 99
     */
    @Deprecated
    private static boolean checkImageResolution(int... dimen){
        int width = dimen[0];
        int height = dimen[1];
        if(width > 299 && height > 99)
            return true;
        return false;
    }

    private static Pair<Boolean, String> checkImageResolution(Context context, int... dimen){
        Pair<Boolean, String> result = new Pair<>();

        if(context == null) {
            return new Pair<>(false, context.getString(R.string.error_picture_not_valid));
        }


        int width = dimen[0];
        int height = dimen[1];
        if(width > 299 && height > 299){
            result.setModel1(true);
            return result;
        }else{
            result.setModel1(false);
            result.setModel2(context.getString(R.string.error_image_resolution));
            return result;
        }
    }

    /**
     * validate user's product name while trying to add product
     * @param text product name when user try to add product
     * @return false if product name is invalid, true if valid
     */
    public static Pair<Boolean, String> validateProductName(Context context, String... text){
        Pair<Boolean, String> res = new Pair<>();
        boolean resBoolean = false;
        String resString = null;
        String productName = text[0];
        if (TextUtils.isEmpty(productName) || productName.equals("0")) {
            resString = context.getString(R.string.error_empty_product_name);
            resBoolean = false;
        }else if(productName.length()>70){
            resString = context.getString(R.string.add_product_name_alert);
            resBoolean = false;
        }else{
            resBoolean = true;
        }
        res.setModel2(resString);
        res.setModel1(resBoolean);
        return res;
    }

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

    public static Pair<Boolean, String> validatePreOrder(Context context, String text){
        if(TextUtils.isEmpty(text)){
            return new Pair<>(false, context.getString(R.string.error_empty_preorder));
        }
        try{
            int value = Integer.parseInt(text);
            if(value <= 0){
                return new Pair<>(false, context.getString(R.string.error_pre_order));
            }

//            if(value > 30){
//                return new Pair<>(false, "lebih kecil dari 30 hari");
//            }

            return new Pair<>(true, null);
        }catch(NumberFormatException nfe){
            return new Pair<>(false, nfe.getLocalizedMessage());
        }
    }

    public static Pair<Boolean, String> validateDescription(Context context, String... text){
        Pair<Boolean, String> res = new Pair<>();
        boolean resBoolean = false;
        String resString = null;
        String description = text[0];
        if (TextUtils.isEmpty(description)) {
            resString = context.getString(R.string.error_field_required);
            resBoolean = false;
        }  else{
            resBoolean = true;
        }
        res.setModel1(resBoolean);
        res.setModel2(resString);
        return res;
    }

    public static Pair<Boolean, String> validateMininumOrder(Context context, String... text){
        Pair<Boolean, String> res = new Pair<>();
        boolean resBoolean = false;
        String resString = null;
        String minimumOrder = text[0];
        try {
            if (TextUtils.isEmpty(minimumOrder)) {
                resString = context.getString(R.string.error_field_required);
                resBoolean = false;
            } else if (Integer.parseInt(minimumOrder) < 1
                    || Integer.parseInt(minimumOrder) > 10000) {
                resString = context.getString(R.string.error_minimumorder_value);
                resBoolean = false;
            } else {
                resBoolean = true;
            }
        } catch (NumberFormatException e){
            resBoolean = false;
            resString = context.getString(R.string.error_number);
        }
        res.setModel1(resBoolean);
        res.setModel2(resString);
        return res;
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

    public static boolean validateMinimumOrder(String text){
        return false;
    }

    public static boolean validateCurrency(String text){
        if(text != null && (text.contains("Rp")||text.contains("US$")))
            return true;
        return false;
    }
}

