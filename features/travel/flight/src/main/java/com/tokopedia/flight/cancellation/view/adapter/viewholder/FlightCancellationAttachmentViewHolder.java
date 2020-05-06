package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachementAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;

import java.io.File;

/**
 * @author by alvarisi on 3/26/18.
 */

public class FlightCancellationAttachmentViewHolder extends AbstractViewHolder<FlightCancellationAttachmentViewModel> {

    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_cancellation_attachment;

    private AppCompatImageView ivAttachment;
    private AppCompatTextView tvFilename;
    private AppCompatTextView tvPassengerName;
    private AppCompatTextView tvViewImage;
    private RelativeLayout imageContainer;
    private LinearLayout tvChangeImage;

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
        imageContainer = view.findViewById(com.tokopedia.flight.R.id.image_container);
        ivAttachment = view.findViewById(com.tokopedia.flight.R.id.iv_attachment);
        tvFilename = view.findViewById(com.tokopedia.flight.R.id.tv_filename);
        tvPassengerName = view.findViewById(com.tokopedia.flight.R.id.tv_passenger_name);
        tvChangeImage = view.findViewById(com.tokopedia.flight.R.id.tv_change_image);
        tvViewImage = view.findViewById(com.tokopedia.flight.R.id.tv_view_image);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void bind(FlightCancellationAttachmentViewModel element) {
        tvFilename.setText(element.getFilename());
        tvPassengerName.setText(element.getPassengerName());

        if (element.getFilepath() != null && element.getFilepath().length() > 0) {
            Glide.with(itemView.getContext())
                    .asBitmap()
                    .load(new File(element.getFilepath()))
                    .centerCrop()
                    .into(getRoundedImageViewTarget(ivAttachment, 5.0f));

            ivAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interactionListener.viewImage(element.getFilepath());
                }
            });

            resizeAttachmentTo60x60();

            if (showChangeButton) {
                tvChangeImage.setVisibility(View.VISIBLE);
                tvViewImage.setVisibility(View.VISIBLE);
            } else {
                tvChangeImage.setVisibility(View.GONE);
                tvViewImage.setVisibility(View.GONE);
            }

            tvChangeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FlightCancellationAttachmentViewHolder.this.interactionListener != null) {
                        FlightCancellationAttachmentViewHolder.this.interactionListener.onUploadAttachmentButtonClicked(getAdapterPosition());
                    }
                }
            });

            imageContainer.setBackgroundResource(com.tokopedia.flight.R.drawable.bg_flight_gray_rounded_stroke);

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
            imageContainer.setBackgroundResource(com.tokopedia.flight.R.drawable.bg_flight_gray_rounded_dashed);

            resizeAttachmentTo40x40();
        }

        renderFileName(element);

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

    private void resizeAttachmentTo60x60() {
        ivAttachment.getLayoutParams().height = context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_60);
        ivAttachment.getLayoutParams().width = context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_60);
        ivAttachment.requestLayout();
    }

    private void resizeAttachmentTo40x40() {
        ivAttachment.getLayoutParams().height = context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_40);
        ivAttachment.getLayoutParams().width = context.getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_40);
        ivAttachment.requestLayout();
    }

    private void renderFileName(FlightCancellationAttachmentViewModel element) {
        if (element.getFilename() != null && element.getFilename().length() > 0) {
            tvFilename.setVisibility(View.VISIBLE);
        } else {
            tvFilename.setVisibility(View.GONE);
        }
    }
}
