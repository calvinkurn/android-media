package com.tokopedia.affiliate.feature.createpost.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.design.component.ButtonCompat;

import java.util.ArrayList;

import static com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_PREVIOUS_IMAGE;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST;

public class CreatePostFragment extends BaseDaggerFragment implements CreatePostContract.View {

    private static final int REQUEST_IMAGE_PICKER = 1234;
    private ArrayList<String> selectedImage = new ArrayList<>();

    private ButtonCompat addImageBtn;

    public static CreatePostFragment createInstance(@NonNull Bundle bundle) {
        CreatePostFragment fragment = new CreatePostFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_create_post, container, false);
        addImageBtn = view.findViewById(R.id.addImageBtn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addImageBtn.setOnClickListener(view1 -> {
            startActivityForResult(
                    CreatePostImagePickerActivity.getInstance(
                            getActivity(),
                            selectedImage),
                    REQUEST_IMAGE_PICKER);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICKER) {
                ArrayList<String> imageListResult = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                ArrayList<String> imageListOriginal = data.getStringArrayListExtra(RESULT_PREVIOUS_IMAGE);
                ArrayList<String> imageListDescription = data.getStringArrayListExtra(RESULT_IMAGE_DESCRIPTION_LIST);
                selectedImage = imageListResult;
            }
        }
    }
}
