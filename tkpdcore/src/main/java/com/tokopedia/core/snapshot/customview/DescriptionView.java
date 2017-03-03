package com.tokopedia.core.snapshot.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.customview.DescriptionTextView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.snapshot.listener.SnapShotFragmentView;
import com.tokopedia.core.util.MethodChecker;

import butterknife.BindView;

/**
 * Created by hangnadi on 3/1/17.
 */
public class DescriptionView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    @BindView(R2.id.tv_desc)
    DescriptionTextView tvDesc;
    @BindView(R2.id.iv_toggle)
    ImageView ivToggle;

    private boolean isExpand = false;

    public DescriptionView(Context context) {
        super(context);
    }

    public DescriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_description_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        ivToggle.setImageResource(R.drawable.chevron_down);
        tvDesc.setMaxLines(5);
        isExpand = false;
        ivToggle.setOnClickListener(new DescriptionView.ClickToggle());
        setVisibility(GONE);
    }

    private void renderCollapse() {
        ivToggle.setImageResource(R.drawable.chevron_down);
        tvDesc.setMaxLines(5);
        tvDesc.requestFocus();
        isExpand = false;
    }

    private void renderExpand() {
        ivToggle.setImageResource(R.drawable.chevron_up);
        tvDesc.setMaxLines(Integer.MAX_VALUE);
        tvDesc.requestFocus();
        isExpand = true;
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvDesc.setText(data.getInfo().getProductDescription() == null
                || data.getInfo().getProductDescription().equals("")
                || data.getInfo().getProductDescription().equals("0")
                ? "Tidak ada deskripsi" : data.getInfo().getProductDescription());
        tvDesc.post(new Runnable() {
            @Override
            public void run() {
                int lineCnt = tvDesc.getLineCount();
                if (lineCnt > 5)
                    ivToggle.setVisibility(VISIBLE);
                else
                    ivToggle.setVisibility(INVISIBLE);
            }
        });
        setVisibility(VISIBLE);
        tvDesc.setAutoLinkMask(0);
        Linkify.addLinks(tvDesc, Linkify.WEB_URLS);
        tvDesc.setText(MethodChecker.fromHtml(tvDesc.getText().toString()));
    }

    private class ClickToggle implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isExpand) {
                renderExpand();
                UnifyTracking.eventPDPExpandDescription();
            } else {
                renderCollapse();
            }
        }
    }
}
