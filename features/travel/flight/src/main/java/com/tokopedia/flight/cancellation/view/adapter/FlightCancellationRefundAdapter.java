package com.tokopedia.flight.cancellation.view.adapter;

import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationRefund;

import java.util.ArrayList;
import java.util.List;

public class FlightCancellationRefundAdapter extends RecyclerView.Adapter<FlightCancellationRefundAdapter.ViewHolder> {
    private List<FlightCancellationRefund> descriptions;

    public FlightCancellationRefundAdapter() {
        descriptions = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.flight.R.layout.item_flight_cancellation_refund, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(descriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    public void setDescriptions(List<FlightCancellationRefund> descriptions) {
        this.descriptions = descriptions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(com.tokopedia.flight.R.id.tv_description);
        }

        public void bind(FlightCancellationRefund description) {
            final int color = itemView.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68);
            String fullText = description.getTitle() + ", " + description.getSubtitle();
            int startIndex = fullText.indexOf(description.getSubtitle());
            int stopIndex = fullText.length();
            SpannableString descriptionStr = new SpannableString(fullText);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(color);
                    ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                }
            };
            descriptionStr.setSpan(clickableSpan, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            descriptionTextView.setText(descriptionStr);

        }
    }
}
