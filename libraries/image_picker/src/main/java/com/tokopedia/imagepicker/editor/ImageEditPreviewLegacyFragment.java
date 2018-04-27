package com.tokopedia.imagepicker.editor;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.editor.presenter.ImageEditPreviewPresenter;
import com.tokopedia.imagepicker.editor.widgetcroplegacy.CropperView;

/**
 * Created by hendry on 25/04/18.
 */

public class ImageEditPreviewLegacyFragment extends Fragment implements ImageEditPreviewPresenter.ImageEditPreviewView {

    public static final String ARG_ORI_IMAGE_PATH = "arg_ori_img_path";
    public static final String ARG_EDITTED_IMAGE_PATH = "arg_edit_img_path";
    public static final String ARG_MIN_RESOLUTION = "arg_min_resolution";

    private CropperView cropperView;

    private String oriImagePath;
    private String edittedImagePath;
    private int rotationCount = 0;
    private int minResolution = 0;

    private ImageEditPreviewPresenter imageEditPreviewPresenter;
    private View progressBar;
    private View snapButton;

    public static ImageEditPreviewLegacyFragment newInstance(String oriImagePath, String edittedImagePath, int minResolution) {
        Bundle args = new Bundle();
        args.putString(ARG_ORI_IMAGE_PATH, oriImagePath);
        args.putString(ARG_EDITTED_IMAGE_PATH, edittedImagePath);
        args.putInt(ARG_MIN_RESOLUTION, minResolution);
        ImageEditPreviewLegacyFragment fragment = new ImageEditPreviewLegacyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_edit_preview, container, false);
        cropperView = view.findViewById(R.id.cropper_view);
        progressBar = view.findViewById(R.id.progressbar);
        snapButton = view.findViewById(R.id.snap_button);

        Bundle bundle = getArguments();
        oriImagePath = bundle.getString(ARG_ORI_IMAGE_PATH);
        minResolution = bundle.getInt(ARG_MIN_RESOLUTION);

        if (savedInstanceState == null) {
            edittedImagePath = getArguments().getString(ARG_EDITTED_IMAGE_PATH);
        } else {
            edittedImagePath = savedInstanceState.getString(ARG_EDITTED_IMAGE_PATH);
        }

        snapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropperView.toggleSnap();
            }
        });
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
    public void onSuccessConvertPathToPreviewBitmap(ImageEditPreviewPresenter.BitmapPreviewResult bitmapPreviewResult) {
        hidePreviewLoading();

        Bitmap previewBitmap = bitmapPreviewResult.previewBitmap;

//        float maxZoomResolution = -1;
//        if (minResolution > 0) {
//            int originalResolution = Math.min(bitmapPreviewResult.originalWidth, bitmapPreviewResult.originalHeight);
//            maxZoomResolution = (float) originalResolution / minResolution;
//        }
//        float defaultMaxZoom = cropperView.getWidth() * 2 / bitmapPreviewResult.previewWidth;
//
//        float maxZoom = Math.min(defaultMaxZoom, maxZoomResolution);
//        cropperView.setMaxZoom(maxZoom > 1 ? maxZoom : 1);
        //cropperView.setMaxZoom(1.6f);

//        Matrix matrix = cropperView.getMatrix();
//        float scaleX = getMatrixValue(matrix, Matrix.MSCALE_X);
//        cropperView.setMaxZoom(cropperView.getWidth()/bitmapPreviewResult.previewWidth);
        cropperView.setMaxZoom(5);

        cropperView.setImageBitmap(previewBitmap);

    }

    private float[] mMatrixValues = new float[9];

    private float getMatrixValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    private void showPreviewLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hidePreviewLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageEditPreviewPresenter != null) {
            imageEditPreviewPresenter.detachView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_EDITTED_IMAGE_PATH, edittedImagePath);
    }


}
