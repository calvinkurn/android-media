package com.tokopedia.flight.bookingV2.presentation.adapter;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.flight.bookingV2.presentation.viewmodel.SimpleViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/21/17.
 */

public class FlightSimpleAdapter extends RecyclerView.Adapter<FlightSimpleAdapter.ViewHolder> {
    private static final int PARAM_EMPTY_MARGIN = 0;
    private List<SimpleViewModel> viewModels;
    private float fontSize;
    private float marginTopDp;
    private float marginBottomDp;
    private float marginLeftDp;
    private float marginRightDp;
    private boolean isArrowVisible;
    private boolean isClickable;
    private boolean isTitleBold;
    private boolean isTitleOnly;
    private boolean isTitleHalfView;
    private boolean isContentAllignmentLeft;
    private int titleMaxLines;
    private OnAdapterInteractionListener interactionListener;

    @ColorInt
    private int contentColorValue;

    public FlightSimpleAdapter() {
        viewModels = new ArrayList<>();
        isArrowVisible = false;
        isClickable = false;
        isTitleBold = false;
        isTitleOnly = false;
        isContentAllignmentLeft = false;
        isTitleHalfView = true;
        titleMaxLines = 1;
        marginTopDp = 0f;
        marginBottomDp = 0f;
        marginLeftDp = 0f;
        marginRightDp = 0f;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.flight.orderlist.R.layout.item_simple_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(viewModels.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public void setViewModel(SimpleViewModel simpleViewModel) {
        this.viewModels.add(simpleViewModel);
    }

    public void setViewModels(List<SimpleViewModel> viewModels) {
        this.viewModels = viewModels;
    }

    public void setDescriptionTextColor(@ColorInt int colorInt) {
        contentColorValue = colorInt;
    }

    public void setArrowVisible(boolean isArrowVisible) {
        this.isArrowVisible = isArrowVisible;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public void setTitleBold(boolean isTitleBold) {
        this.isTitleBold = isTitleBold;
    }

    public void setTitleOnly(boolean isTitleOnly) {
        this.isTitleOnly = isTitleOnly;
    }

    public void setTitleHalfView(boolean titleHalfView) {
        isTitleHalfView = titleHalfView;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public void setInteractionListener(OnAdapterInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public void setContentAllignmentLeft(boolean contentAllignmentLeft) {
        isContentAllignmentLeft = contentAllignmentLeft;
    }

    public void setMarginTopDp(float marginTopDp) {
        this.marginTopDp = marginTopDp;
    }

    public void setMarginBottomDp(float marginBottomDp) {
        this.marginBottomDp = marginBottomDp;
    }

    public void setTitleMaxLines(int maxLines) {
        this.titleMaxLines = maxLines;
    }

    public void setMarginLeftDp(float marginLeftDp) {
        this.marginLeftDp = marginLeftDp;
    }

    public void setMarginRightDp(float marginRightDp) {
        this.marginRightDp = marginRightDp;
    }

    public interface OnAdapterInteractionListener {
        void onItemClick(int adapterPosition, SimpleViewModel viewModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView contentTextView;
        private ImageView arrowImageView;
        private LinearLayout containerLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(com.tokopedia.design.R.id.tv_title);
            contentTextView = (TextView) itemView.findViewById(com.tokopedia.flight.orderlist.R.id.tv_content);
            arrowImageView = (ImageView) itemView.findViewById(com.tokopedia.flight.orderlist.R.id.iv_arrow);
            containerLinearLayout = (LinearLayout) itemView.findViewById(com.tokopedia.design.R.id.container);
        }

        public void bind(final SimpleViewModel viewModel) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titleTextView.getLayoutParams();

            titleTextView.setText(viewModel.getLabel());
            contentTextView.setText(viewModel.getDescription());
            contentTextView.setVisibility(isTitleOnly ? View.GONE : View.VISIBLE);
            arrowImageView.setVisibility(isArrowVisible ? View.VISIBLE : View.GONE);
            if (contentColorValue != 0) {
                contentTextView.setTextColor(contentColorValue);
            }

            if (isTitleBold) {
                titleTextView.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                titleTextView.setTypeface(Typeface.DEFAULT);
            }

            if (fontSize != 0f) {
                titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
            }

            titleTextView.setMaxLines(titleMaxLines);

            if (isTitleHalfView) {
                layoutParams.width = 0;
                layoutParams.weight = 1;
                titleTextView.setLayoutParams(layoutParams);
            } else {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.weight = 0;
                layoutParams.setMargins(
                        PARAM_EMPTY_MARGIN,
                        PARAM_EMPTY_MARGIN,
                        10,
                        PARAM_EMPTY_MARGIN
                );
                titleTextView.setLayoutParams(layoutParams);
                titleTextView.setMinWidth(150);
            }

            if (isContentAllignmentLeft) {
                contentTextView.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
            }

            containerLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (interactionListener != null) {
                        interactionListener.onItemClick(getAdapterPosition(), viewModel);
                    }
                }
            });
            if (isClickable) {
                containerLinearLayout.setBackground(itemView.getContext().getResources().getDrawable(com.tokopedia.abstraction.R.drawable.selectable_background_tokopedia));
            } else {
                containerLinearLayout.setBackground(null);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            Resources resources = itemView.getContext().getResources();
            params.setMargins(
                    convertToPixel(resources, marginLeftDp),
                    convertToPixel(resources, marginTopDp),
                    convertToPixel(resources, marginRightDp),
                    convertToPixel(resources, marginBottomDp)
            );
            containerLinearLayout.setLayoutParams(params);


        }
    }

    private int convertToPixel(Resources resources, float dp){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                dp,
                resources.getDisplayMetrics()
        );
    }
}
