package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreViewHolder extends AbstractViewHolder<ExploreViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_af_explore;

    private ExploreContract.View mainView;

    private ImageView ivImage;
    private ImageView btnByme;
    private TextView tvTitle, tvCommission;
    private View layout;

    public ExploreViewHolder(View itemView, ExploreContract.View mainView) {
        super(itemView);
        this.mainView = mainView;
        ivImage = itemView.findViewById(R.id.iv_image);
        btnByme = itemView.findViewById(R.id.iv_byme);
        tvTitle = itemView.findViewById(R.id.tv_title);
        layout = itemView.findViewById(R.id.main_view);
        tvCommission = itemView.findViewById(R.id.tv_commission);
    }

    @Override
    public void bind(ExploreViewModel element) {
        initView(element);
        initViewListener(element);
    }

    private void initView(ExploreViewModel element) {
        ImageHandler.clearImage(ivImage);
        ImageHandler.loadImageRounded2(mainView.getContext(), ivImage, element.getImageUrl());

        tvTitle.setText(MethodChecker.fromHtml(element.getTitle()));
        tvCommission.setText(MethodChecker.fromHtml(element.getCommissionString()));
    }

    private void initViewListener(ExploreViewModel element) {
        btnByme.setOnClickListener(view -> {
            mainView.onBymeClicked(element);
        });

        layout.setOnClickListener(view -> {
            mainView.onProductClicked(element);
        });
    }

}
