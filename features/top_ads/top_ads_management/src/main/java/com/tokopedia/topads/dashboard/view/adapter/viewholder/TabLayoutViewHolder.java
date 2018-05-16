package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.topads.R;

/**
 * Created by hadi.putra on 15/05/18.
 */

public class TabLayoutViewHolder extends RecyclerView.ViewHolder {
    private TextView mainTextView;
    private TextView subTextView;
    private View baseView;

    public TabLayoutViewHolder(View itemView) {
        super(itemView);
        baseView = itemView.findViewById(R.id.base_layout);
        mainTextView = itemView.findViewById(R.id.title);
        subTextView = itemView.findViewById(R.id.subtitle);
    }

    public void bind(String title, String subtitle){
        mainTextView.setText(title);
        subTextView.setText(subtitle);
    }

    public void toggleActivate(boolean isActive){
        int white = ContextCompat.getColor(itemView.getContext(), R.color.white);
        int black = ContextCompat.getColor(itemView.getContext(), R.color.black_70);
        if (isActive){
            baseView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.gradient_background));
            mainTextView.setTextColor(white);
            subTextView.setTextColor(white);
        } else {
            baseView.setBackgroundColor(white);
            mainTextView.setTextColor(black);
            subTextView.setTextColor(black);
        }
    }
}
