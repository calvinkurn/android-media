package com.tkpd.library.utils.legacy;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tokopedia.core.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

@Deprecated
public class CommonUtils {

    public CommonUtils() {

    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
    }

    public static String getUniqueDeviceID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DeviceID = tm.getDeviceId();
        String Brand = Build.BRAND;
        String Model = Build.MODEL;
        String UniqueDeviceID = Brand + "~" + Model + "~" + DeviceID;
        Timber.d("Device ID" + UniqueDeviceID);
        return UniqueDeviceID;
    }

    public static void addImageToGallery(final String filePath, final Context context) {
        try {
            ContentValues values = new ContentValues();

            values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, filePath);

            context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception ex) {
        }
    }

    private static File getOutputMediaFile(Activity context, String PicName) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Tokopedia");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName = PicName + ".jpg";
        mediaFile = new File(mediaStorageDir, mImageName);
        return mediaFile;
    }

    public static float DptoPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float DimentoPx(Context context, int dimen) {
        return context.getResources().getDimension(dimen);
        //int dp = (int) context.getResources().getDimension(dimen);
        //return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static Boolean isUrl(String url) {
        return url.contains("http");
    }


    ///////////////////////////////////////////////////////////////////////////////

    public static String getDate(long time) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currenTimeZone = new Date(time);
        return sdf.format(currenTimeZone);
    }

    public static String replaceCreditCardNumber(String text) {
        final String MASKCARD = "$1<XTKPDX>$2";
        final Pattern PATTERNCARD =
                Pattern.compile("\\b([0-9]{4})[0-9]{0,9}([0-9]{4})\\b");
        Matcher matcher = PATTERNCARD.matcher(text);
        if (matcher.find()) {
            return matcher.replaceAll(MASKCARD);
        }
        return text;

    }

    /**
     * chek nullability at json parsing
     *
     * @param input "0" or null as null AND other as not null
     * @return
     */
    public static boolean checkNullForZeroJson(String input) {
        if (input == null || input.equals("0") || input.equals(""))
            return false;

        return true;
    }

    public static String parseNullForZeroJson(String input) {
        if (checkNullForZeroJson(input)) {
            return input;
        } else {
            return null;
        }
    }

    public static List<String> checkNullMessageError(List<String> messageError) {
        boolean isNull = false;
        if (messageError == null && messageError.size() <= 0)
            return new ArrayList<String>();

        for (String msg : messageError) {
            if (!checkNullForZeroJson(msg))
                return new ArrayList<String>();
        }
        return messageError;
    }


    public static void hideKeyboard(Activity activity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static <T> boolean checkNotNull(T reference) {
        if (reference == null) {
            return false;
        } else {
            return true;
        }
    }

    public static <T> boolean checkStringNotNull(T reference) {
        if (checkNotNull(reference) && (!reference.equals("")) && (!reference.equals("0"))) {
            return true;
        } else {
            return false;
        }
    }

    public static Double parsePriceToDouble(String price, String currency) {
        price = price.replace(",", "");
        if (!currency.equals("US$")) {
            // remove cent
            price = price.replace(".", "");
        }
        return Double.parseDouble(price);
    }

    /**
     * @param reference
     * @param <T>
     * @return false if empty otherwise true if not empty
     */
    public static <T extends Collection> boolean checkCollectionNotNull(T reference) {
        if (!checkNotNull(reference) || reference.isEmpty())
            return false;

        return true;
    }

    /**
     * DON't USE THIS, IT HAS INVALID LOGIC.
     * check whether messageerror empty or not.
     *
     * @param errorMessages
     * @return true if error message WAS NOT empty and false if empty
     */
    @Deprecated
    public static boolean checkErrorMessageEmpty(List<String> errorMessages) {
        boolean result = false;

        // dont set is error message if just size one and contain ""
        for (String error : errorMessages) {
            if (error.equals(""))
                result = false;
        }

        if (!result && !checkCollectionNotNull(errorMessages))
            result = true;


        return result;
    }

    /**
     * @param errorMessages
     * @return true if empty, false is not empty
     */
    public static boolean checkErrorMessageEmpty(String errorMessages) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("");

        if (checkNotNull(errorMessages)) {
            if (strings.toString().equals(errorMessages)) {
                return true;
            }
        }

        return false;
    }

    public static void forceShowKeyboard(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static boolean checkStringNotEmpty(String message) {
        if (message != null && message.equals("") && message.equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    public static String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }


}
