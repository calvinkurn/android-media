package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachementAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.common.view.HorizontalProgressBar;

import java.io.File;

/**
 * @author by alvarisi on 3/26/18.
 */

public class FlightCancellationAttachmentViewHolder extends AbstractViewHolder<FlightCancellationAttachmentViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_cancellation_attachment;

    private AppCompatImageView ivAttachment;
    private AppCompatTextView tvFilename;
    private AppCompatTextView tvPassengerName;
    private HorizontalProgressBar progressBar;
    private AppCompatTextView tvPercentageUpload;
    private AppCompatTextView tvChangeImage;

    private boolean showChangeButton;
    private Context context;

    private FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener interactionListener;

    public FlightCancellationAttachmentViewHolder(View itemView, FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener interactionListener, boolean showChangeButton) {
        super(itemView);
        setupView(itemView);

        this.interactionListener = interactionListener;
        this.showChangeButton = showChangeButton;
    }

    private void setupView(View view) {
        context = view.getContext();
        ivAttachment = view.findViewById(R.id.iv_attachment);
        tvFilename = view.findViewById(R.id.tv_filename);
        tvPassengerName = view.findViewById(R.id.tv_passenger_name);
        progressBar = view.findViewById(R.id.progress_upload);
        tvPercentageUpload = view.findViewById(R.id.tv_percentage_upload);
        tvChangeImage = view.findViewById(R.id.tv_change_image);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void bind(FlightCancellationAttachmentViewModel element) {
        tvFilename.setText(element.getFilename());
        tvPassengerName.setText(element.getPassengerName());
        tvPercentageUpload.setText(String.format(getString(R.string
                        .flight_cancellation_upload_percentage_label),
                Long.toString(element.getPercentageUpload())));

        if (element.getFilepath() != null && element.getFilepath().length() > 0) {
            Glide.with(itemView.getContext())
                    .load(new File(element.getFilepath()))
                    .asBitmap()
                    .centerCrop()
                    .into(getRoundedImageViewTarget(ivAttachment, 5.0f));

            ivAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing
                }
            });

            if (showChangeButton) {
                tvChangeImage.setVisibility(View.VISIBLE);
            } else {
                tvChangeImage.setVisibility(View.GONE);
            }

            tvChangeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FlightCancellationAttachmentViewHolder.this.interactionListener != null) {
                        FlightCancellationAttachmentViewHolder.this.interactionListener.onUploadAttachmentButtonClicked(getAdapterPosition());
                    }
                }
            });
        } else {
            ivAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FlightCancellationAttachmentViewHolder.this.interactionListener != null) {
                        FlightCancellationAttachmentViewHolder.this.interactionListener.onUploadAttachmentButtonClicked(getAdapterPosition());
                    }
                }
            });

            tvChangeImage.setVisibility(View.GONE);
        }

        if (element.getFilename() != null && element.getFilename().length() > 0) {
            tvFilename.setVisibility(View.VISIBLE);
        } else {
            tvFilename.setVisibility(View.GONE);
        }

        if (element.getPercentageUpload() > 0) {
            progressBar.setVisibility(View.VISIBLE);
            tvPercentageUpload.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            tvPercentageUpload.setVisibility(View.GONE);
        }
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
