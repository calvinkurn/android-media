package com.tkpd.library.utils;

import android.app.Activity;
import android.app.Fragment;
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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core2.R;
import com.tokopedia.network.constant.ResponseStatus;

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

public class CommonUtils {

    public CommonUtils() {

    }

    public static boolean EmailValidation(String email) {
        /*String EmailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern;
		pattern = Pattern.compile(EmailPattern);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();*/
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String SaveImageFromBitmap(Activity context, Bitmap bitmap, String PicName) {
        File pictureFile = getOutputMediaFile(context, PicName);
        String path = "";
        if (pictureFile == null) {
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            addImageToGallery(pictureFile.getPath(), context);
            path = pictureFile.getPath();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
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

    public static void UniversalToast(Context context, String text) {
        Toast.makeText(context, MethodChecker.fromHtml(text), Toast.LENGTH_LONG).show();
    }

    public static float DptoPx(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
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

    public static void requestBarcodeScanner(Fragment fragment, Class customClass) {
        IntentIntegrator.forFragment(fragment).setCaptureActivity(customClass).initiateScan();
    }

    public static void requestBarcodeScanner(androidx.fragment.app.Fragment fragment, Class customClass) {
        IntentIntegrator.forSupportFragment(fragment).setCaptureActivity(customClass).initiateScan();
    }

    public static String getBarcode(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null && scanResult.getContents() != null) {
            return scanResult.getContents();
        }
        return "";
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

    public static void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) MainApplication.getInstance().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
