package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.ContentProductViewModel;

/**
 * Created by yfsx on 02/01/18.
 */

public class ContentProductViewHolder extends AbstractViewHolder<ContentProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_content_product;

    private FeedPlus.View mainView;
    private TextView tvHeader, tvDescription;
    private ImageView ivHeader;
    private Button btnContent;

    public ContentProductViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        mainView = viewListener;
        tvHeader = itemView.findViewById(R.id.tv_header);
        tvDescription = itemView.findViewById(R.id.tv_description);
        btnContent = itemView.findViewById(R.id.btn_content);
        ivHeader = itemView.findViewById(R.id.iv_content_product);
    }

    @Override
    public void bind(ContentProductViewModel element) {
        initView();
        populateView(element);
        initViewListener(element);
    }

    private void initView() {
        tvHeader.setVisibility(View.GONE);
        tvDescription.setVisibility(View.GONE);
        btnContent.setVisibility(View.GONE);
        ivHeader.setVisibility(View.GONE);
    }

    private void populateView(ContentProductViewModel model) {
        if (!TextUtils.isEmpty(model.getTextHeader())) {
            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText(model.getTextHeader());
        }
        if (!TextUtils.isEmpty(model.getTextDescription())) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(model.getTextDescription());
        }
        if (!TextUtils.isEmpty(model.getImageUrl())) {
            ivHeader.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(ivHeader, model.getImageUrl());
        }
        if (!TextUtils.isEmpty(model.getApplink())) {
            btnContent.setVisibility(View.VISIBLE);
            btnContent.setText(model.getButtonTitle());
        }
    }

    private void initViewListener(final ContentProductViewModel model) {
        btnContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.onContentProductLinkClicked(model.getApplink());
            }
        });
    }
}
