package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class ProductAdditionalInfoView extends RelativeLayout {

    public static final String PLAYSTORE_STRING = "play.google.com/store/apps";
    public static final String URL = "url";
    private ActionListener actionListener;

    @BindView(R2.id.tv_info)
    TextView tvInfo;
    @BindView(R2.id.layout_container_additional)
    LinearLayout containerAdditional;

    private Context context;

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
        this.context = context;
        LayoutInflater.from(context).inflate(
                R.layout.view_holder_product_additional_info_digital_module, this, true
        );
        ButterKnife.bind(this);
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
    public void convertDetailWithHtml(Product product) {
        final String detailUrl = "http://www.tokopedia.com";
        String detailUrlText = "Klik di sini";

        Spanned detail = Html.fromHtml(product.getDetail() + "<a href=\"" + detailUrl + "\"> " + detailUrlText + "</a>");
        tvInfo.setText(detail);
        tvInfo.setMovementMethod(LinkMovementMethod.getInstance());
        tvInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onProductLinkClicked(detailUrl);
            }
        });
    }

    public interface ActionListener {
        void onProductLinkClicked(String url);
    }
}
