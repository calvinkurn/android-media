package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DigitalsViewHolder extends AbstractViewHolder<DigitalsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_digitals;
    @BindView(R.id.title)
    TextView titleTxt;
    private HomeCategoryListener listener;

    public DigitalsViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(DigitalsViewModel element) {
        titleTxt.setText(element.getTitle());
    }

    @OnClick(R.id.see_more)
    void onSeeMore(){
        listener.onDigitalMoreClicked(getAdapterPosition());
    }
}
