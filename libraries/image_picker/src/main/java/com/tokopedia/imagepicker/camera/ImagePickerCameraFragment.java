package com.tokopedia.imagepicker.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.gallery.ImagePickerGalleryFragment;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerCameraFragment extends Fragment{
    // TODO camera permission

    public static ImagePickerCameraFragment newInstance() {
        return new ImagePickerCameraFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_picker_camera, container, false);
        return view;
    }
}
