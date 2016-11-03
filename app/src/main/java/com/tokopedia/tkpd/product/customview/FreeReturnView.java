package com.tokopedia.tkpd.product.customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.BannerWebView;
import com.tokopedia.tkpd.product.listener.ProductDetailView;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpd.product.model.productdetail.ReturnInfo;
import com.tokopedia.tkpd.util.SelectableSpannedMovementMethod;

import butterknife.Bind;

/**
 * Created by stevenfredian on 7/18/16.
 */
public class FreeReturnView extends BaseView<ProductDetailData, ProductDetailView> {

    @Bind(R.id.layout_free_return)
    LinearLayout layoutFreeReturn;
    @Bind(R.id.image_free_return)
    ImageView imageFreeReturn;
    @Bind(R.id.text_free_return)
    TextView textFreeReturn;

    public FreeReturnView(Context context) {
        super(context);
    }

    public FreeReturnView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }


    @Override
    protected int getLayoutView() {
        return R.layout.view_free_return_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        ReturnInfo returnInfo = data.getInfo().getReturnInfo();
        if (returnInfo.getContent().equals("")) {
            setVisibility(GONE);
        } else {
            layoutFreeReturn.setBackgroundColor(Color.parseColor(returnInfo.getColorHex()));
            textFreeReturn.setText(Html.fromHtml(returnInfo.getContent()));
            textFreeReturn.setMovementMethod(new SelectableSpannedMovementMethod());

            Spannable sp = (Spannable)textFreeReturn.getText();
            URLSpan[] urls=sp.getSpans(0, textFreeReturn.getText().length(), URLSpan.class);
            SpannableStringBuilder style=new SpannableStringBuilder(textFreeReturn.getText());
            style.clearSpans();
            for(final URLSpan url : urls){
                style.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent(getContext(), BannerWebView.class);
                        intent.putExtra("url", url.getURL());
                        getContext().startActivity(intent);
                    }
                }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textFreeReturn.setText(style);

            ImageHandler.LoadImage(imageFreeReturn, returnInfo.getIcon());
            setVisibility(VISIBLE);
        }
    }
}
