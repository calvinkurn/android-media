package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreEmptySearchViewModel;

/**
 * @author by yfsx on 12/10/18.
 */
public class ExploreEmptySearchViewHolder extends AbstractViewHolder<ExploreEmptySearchViewModel> {

    private static final String EMPTY_IMG_FORMAT = "https://ecs7.tokopedia.net/img/android/toped_logo_crying/%s/toped_logo_crying.png";

    @LayoutRes
    public static final int LAYOUT = R.layout.item_af_explore_empty_search;

    private ExploreContract.View mainView;

    private AppCompatImageView image;
    private Button searchButton;

    public ExploreEmptySearchViewHolder(View itemView, ExploreContract.View mainView) {
        super(itemView);
        this.mainView = mainView;
        image = itemView.findViewById(R.id.image);
        searchButton = itemView.findViewById(R.id.btn_search);
    }

    @Override
    public void bind(ExploreEmptySearchViewModel element) {
        initView();
        initViewListener();
    }

    private void initView() {
        String screenDensity = DisplayMetricUtils.getScreenDensity(itemView.getContext());
        ImageHandler.loadImage2(
                image,
                String.format(EMPTY_IMG_FORMAT, screenDensity),
                R.drawable.ic_loading_image
        );
    }

    private void initViewListener() {
        searchButton.setOnClickListener(view -> mainView.onButtonEmptySearchClicked());
    }
}
