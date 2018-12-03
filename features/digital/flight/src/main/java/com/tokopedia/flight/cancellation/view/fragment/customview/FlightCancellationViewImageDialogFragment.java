package com.tokopedia.flight.cancellation.view.fragment.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.flight.R;

import java.io.File;

/**
 * @author by furqan on 03/12/18.
 */

public class FlightCancellationViewImageDialogFragment extends DialogFragment {

    AppCompatImageView imageView;
    Context context;

    public FlightCancellationViewImageDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_flight_cancellation_view_image, container);
        context = view.getContext();

        imageView = view.findViewById(R.id.image_view);

        return view;
    }

    public void showImage(String filepath) {
        Glide.with(context)
                .load(new File(filepath))
                .asBitmap()
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
