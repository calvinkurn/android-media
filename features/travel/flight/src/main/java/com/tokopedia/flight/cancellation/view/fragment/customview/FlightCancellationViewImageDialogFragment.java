package com.tokopedia.flight.cancellation.view.fragment.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.flight.R;

import java.io.File;

/**
 * @author by furqan on 03/12/18.
 */

public class FlightCancellationViewImageDialogFragment extends DialogFragment {

    public static final String EXTRA_FILE_PATH = "EXTRA_FILE_PATH";

    AppCompatImageView imageView;
    ImageView ivClose;
    Context context;

    String filepath;

    public static FlightCancellationViewImageDialogFragment newInstance(String filepath) {
        FlightCancellationViewImageDialogFragment fragment = new FlightCancellationViewImageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FILE_PATH, filepath);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FlightCancellationViewImageDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.flight.R.layout.dialog_fragment_flight_cancellation_view_image, container);
        context = view.getContext();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imageView = view.findViewById(R.id.flight_cancellation_image_view);
        ivClose = view.findViewById(R.id.flight_cancellation_image_iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filepath = getArguments().getString(EXTRA_FILE_PATH);

        showImage();
    }

    private void closeDialog() {
        getDialog().dismiss();
    }

    private void showImage() {
        Glide.with(context)
                .asBitmap()
                .load(new File(filepath))
                .centerCrop()
                .into(getRoundedImageViewTarget(imageView, 5.0f));
    }

    private static BitmapImageViewTarget getRoundedImageViewTarget(final ImageView imageView, final float radius) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCornerRadius(radius);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        };
    }
}
