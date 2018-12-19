package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.digital.widget.view.fragment.DigitalChannelFragment;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class DigitalsViewHolder extends AbstractViewHolder<DigitalsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_digitals;

    private FrameLayout container;

    private FragmentManager fragmentManager;

    public DigitalsViewHolder(FragmentManager fragmentManager, View itemView) {
        super(itemView);

        container = itemView.findViewById(R.id.container_layout_digital_widget);

        this.fragmentManager = fragmentManager;
    }

    @Override
    public void bind(DigitalsViewModel element) {
        container.postDelayed(() -> {
            try {
                Fragment oldFragment = fragmentManager.findFragmentById(container.getId());
                if (oldFragment == null) {
                    fragmentManager.beginTransaction().add(container.getId(), new DigitalChannelFragment()).commit();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }, 500);
    }

}