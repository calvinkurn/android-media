package com.tokopedia.imagepicker.editor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.editor.presenter.ImageEditPreviewPresenter;
import com.tokopedia.imagepicker.editor.widget.CropperView;

/**
 * Created by hendry on 25/04/18.
 */

public class ImageEditPreviewFragment extends Fragment implements ImageEditPreviewPresenter.ImageEditPreviewView {

    public static final String ARG_ORI_IMAGE_PATH = "arg_ori_img_path";
    public static final String ARG_EDITTED_IMAGE_PATH = "arg_edit_img_path";

    private CropperView cropperView;

    private String oriImagePath;
    private String edittedImagePath;
    private int rotationCount = 0;

    private ImageEditPreviewPresenter imageEditPreviewPresenter;
    private View progressBar;

    public static ImageEditPreviewFragment newInstance(String oriImagePath, String edittedImagePath) {
        Bundle args = new Bundle();
        args.putString(ARG_ORI_IMAGE_PATH, oriImagePath);
        args.putString(ARG_EDITTED_IMAGE_PATH, edittedImagePath);
        ImageEditPreviewFragment fragment = new ImageEditPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_edit_preview, container, false);
        cropperView = view.findViewById(R.id.cropper_view);
        progressBar = view.findViewById(R.id.progressbar);

        Bundle bundle = getArguments();
        oriImagePath = bundle.getString(ARG_ORI_IMAGE_PATH);

        if (savedInstanceState == null) {
            edittedImagePath = getArguments().getString(ARG_EDITTED_IMAGE_PATH);
        } else {
            edittedImagePath = savedInstanceState.getString(ARG_EDITTED_IMAGE_PATH);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadNewImage();
    }

    private void loadNewImage() {
        rotationCount = 0;
        imageEditPreviewPresenter = new ImageEditPreviewPresenter();
        imageEditPreviewPresenter.attachView(this);
        showPreviewLoading();
        imageEditPreviewPresenter.convertImagePathToPreviewBitmap(edittedImagePath);
    }

    @Override
    public void onErrorConvertPathToPreviewBitmap(Throwable e) {
        hidePreviewLoading();
        NetworkErrorHelper.showRedCloseSnackbar(getView(), ErrorHandler.getErrorMessage(getContext(), e));
    }

    @Override
    public void onSuccessConvertPathToPreviewBitmap(Bitmap bitmap, float expectedPreviewWidth) {
        hidePreviewLoading();

        cropperView.setImageBitmap(bitmap);
        if (cropperView.getWidth() != 0) {
            cropperView.setMaxZoom(cropperView.getWidth() * 2 / expectedPreviewWidth);
        } else {
            ViewTreeObserver vto = cropperView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cropperView.getViewTreeObserver().removeOnPreDrawListener(this);
                    cropperView.setMaxZoom(cropperView.getWidth() * 2 / 1280f);
                    return true;
                }
            });
        }
    }

    private void showPreviewLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hidePreviewLoading(){
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageEditPreviewPresenter!= null) {
            imageEditPreviewPresenter.detachView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_EDITTED_IMAGE_PATH, edittedImagePath);
    }


}
