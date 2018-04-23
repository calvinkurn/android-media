package com.tokopedia.imagepicker.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.internal.ui.BaseAnncaFragment;
import com.tokopedia.imagepicker.R;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerCameraFragment extends CameraFragment{
    // TODO camera permission

    @RequiresPermission("android.permission.CAMERA")
    public static ImagePickerCameraFragment newInstance(Configuration configuration) {
        return (ImagePickerCameraFragment) CameraFragment.newInstance(configuration);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
