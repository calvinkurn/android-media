package com.tokopedia.imagepicker.gallery;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.imagepicker.R;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerGalleryFragment extends Fragment {
    // TODO read external directory permission
    public static ImagePickerGalleryFragment newInstance() {
        return new ImagePickerGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_picker_gallery, container, false);
        return view;
    }
}
