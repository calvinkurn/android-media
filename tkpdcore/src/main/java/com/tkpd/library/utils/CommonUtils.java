package com.tkpd.library.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tokopedia.core.util.MethodChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public class CommonUtils {

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

    ///////////////////////////////////////////////////////////////////////////////

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

    /**
     * @param reference
     * @param <T>
     * @return false if empty otherwise true if not empty
     */
    public static <T extends Collection> boolean checkCollectionNotNull(T reference) {
        if (!com.tokopedia.abstraction.common.utils.view.CommonUtils.checkNotNull(reference) || reference.isEmpty())
            return false;

        return true;
    }
}
