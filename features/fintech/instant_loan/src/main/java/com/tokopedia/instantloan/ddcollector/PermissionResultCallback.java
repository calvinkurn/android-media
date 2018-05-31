package com.tokopedia.instantloan.ddcollector;

/**
 * Created by Jaison on 25/08/16.
 */


public interface PermissionResultCallback {
    void permissionGranted(int requestCode);

    void permissionDenied(int requestCode);

    void neverAskAgain(int requestCode);
}