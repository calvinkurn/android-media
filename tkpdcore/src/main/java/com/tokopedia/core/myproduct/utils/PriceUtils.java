package com.tokopedia.core.myproduct.utils;

import android.util.Log;
import android.util.Pair;

/**
 * Created by sebastianuskh on 12/7/16.
 */

public class PriceUtils {

    public static final int CURRENCY_RUPIAH = 100;
    public static final int CURRENCY_DOLLAR = 200;
    private static final String TAG = "Price Utils";

    public static Pair<Boolean, String> validatePrice(int currency, double price){
        boolean resBoolean = false;
        String resString = null;

        try {
            switch (currency) {
                case CURRENCY_RUPIAH:
                    if(price < 100){
                        resBoolean = false;
                        resString = "Harga harus lebih besar dari Rp 100";
                        return new Pair<>(resBoolean, resString);}
                    if(price > 100_000_000){
                        resBoolean = false;
                        resString = "Harga harus lebih kecil dari Rp 100.000.000";
                        return new Pair<>(resBoolean, resString);}
                    if (price >= 100 && price <= 100_000_000){
                        resBoolean= true;
                        resString = null;
                        return new Pair<>(resBoolean, resString);}
                    break;
                case CURRENCY_DOLLAR:
                    if(price < 1){
                        resBoolean = false;
                        resString = "Harga harus lebih besar dari $1";
                        return new Pair<>(resBoolean, resString);
                    }
                    if(price > 7500){
                        resBoolean = false;
                        resString = "Harga harus lebih kecil dari $7500";
                        return new Pair<>(resBoolean, resString);
                    }
                    if(price >= 1 && price <= 7500){
                        resBoolean= true;
                        resString = null;
                        return new Pair<>(resBoolean, resString);}
                    break;
                default:
                    resBoolean = false;
                    resString = "Mata uang harus dipilih";
                    return new Pair<>(resBoolean, resString);
            }
        }catch(NumberFormatException nfe){
            Log.e(TAG, nfe.getLocalizedMessage());
            resBoolean = false;
            resString = "Terjadi kesalahan";
            return new Pair<>(resBoolean, resString);
        }
        return null;
    }

}
