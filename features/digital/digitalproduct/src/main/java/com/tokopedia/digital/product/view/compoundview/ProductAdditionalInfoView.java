package com.tokopedia.digital.product.view.compoundview;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class ProductAdditionalInfoView extends RelativeLayout {

    public static final String URL = "url";

    private ActionListener actionListener;

    private TextView tvInfo;
    private RelativeLayout containerAdditional;

    public ProductAdditionalInfoView(Context context) {
        super(context);
        initialView(context, null, 0);
    }

    public ProductAdditionalInfoView(Context context, ActionListener actionListener) {
        super(context);
        this.actionListener = actionListener;
        initialView(context, null, 0);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public ProductAdditionalInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialView(context, attrs, 0);
    }

    public ProductAdditionalInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialView(context, attrs, defStyleAttr);
    }

    private void initialView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(
                R.layout.view_holder_digital_product_additional_info, this, true
        );

        tvInfo = findViewById(R.id.tv_info);
        containerAdditional = findViewById(R.id.layout_container_additional);
    }

    public void renderData(Product product) {
        if (!product.getDetail().equals("")) {
            containerAdditional.setVisibility(VISIBLE);
            convertDetailWithHtml(product);
        } else {
            containerAdditional.setVisibility(GONE);
        }
    }

    @SuppressWarnings("deprecation")
    public void convertDetailWithHtml(final Product product) {
        if (TextUtils.isEmpty(product.getDetailUrl()) ||  TextUtils.isEmpty(product.getDetailUrlText())) {
            tvInfo.setText(MethodChecker.fromHtml(product.getDetail()));
        } else {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(MethodChecker.fromHtml(product.getDetail()));
            String detailUrl = "<a href=\"" + product.getDetailUrl() + "\"> " +
                    product.getDetailUrlText() + "</a>";
            stringBuilder.append(" ");
            stringBuilder.append(MethodChecker.fromHtml(detailUrl));
            tvInfo.setText(stringBuilder);
        }
        tvInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(product.getDetailUrl()) & !TextUtils.isEmpty(product.getDetailUrlText())) {
                    actionListener.onProductLinkClicked(product.getDetailUrl());
                }
            }
        });
    }

    public interface ActionListener {
        void onProductLinkClicked(String url);
    }

}