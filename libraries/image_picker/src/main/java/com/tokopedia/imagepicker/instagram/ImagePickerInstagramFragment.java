package com.tokopedia.imagepicker.instagram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.imagepicker.R;

/**
 * Created by hendry on 19/04/18.
 */

public class ImagePickerInstagramFragment extends Fragment {
    // TODO write external storage permission

    public static ImagePickerInstagramFragment newInstance() {
        return new ImagePickerInstagramFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_picker_instagram, container, false);
        return view;
    }

}
