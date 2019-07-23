package com.tokopedia.gm.featured.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.base.list.seller.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.base.list.seller.view.old.DataBindAdapter;
import com.tokopedia.gm.R;
import com.tokopedia.user.session.UserSession;

import org.jetbrains.annotations.NotNull;

import static com.tokopedia.gm.common.constant.GMCommonConstantKt.IMG_URL_ICON_LOCK_WHITE_GREEN;
import static com.tokopedia.gm.common.constant.GMCommonConstantKt.URL_FEATURED_PRODUCT;

@Deprecated
public class GMFeatureProductEmptyDataBinder extends BaseEmptyDataBinder {

    private String imageUrl;
    private TextView textViewOverlay;
    private ImageView imageViewOverlay;
    private Button buttonOverlay;
    private View viewOverlayRegularMerchant;
    private Context context;
    private GMFeaturedProductEmptyDataBinderListener gmFeaturedProductEmptyDataBinderListener;

    public interface GMFeaturedProductEmptyDataBinderListener {
        void buttonOverlayClicked();

        void spanReadMoreClicked();
    }

    public GMFeatureProductEmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public GMFeatureProductEmptyDataBinder(
            DataBindAdapter dataBindAdapter,
            String imageUrl,
            GMFeaturedProductEmptyDataBinderListener gmFeaturedProductEmptyDataBinderListener
    ) {
        super(dataBindAdapter, -1);
        this.imageUrl = imageUrl;
        this.gmFeaturedProductEmptyDataBinderListener = gmFeaturedProductEmptyDataBinderListener;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        context = parent.getContext();
        ImageHandler imageHandler = new ImageHandler(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gm_featured_empty_list, parent, false);
        ImageView imageViewEmptyResult = view.findViewById(R.id.no_result_image);
        viewOverlayRegularMerchant = view.findViewById(R.id.layout_overlay);
        textViewOverlay = view.findViewById(R.id.text_view_overlay_description);
        imageViewOverlay = view.findViewById(R.id.image_view_overlay);
        buttonOverlay = view.findViewById(R.id.button_redirect_to);
        imageHandler.loadImage(imageViewEmptyResult, imageUrl);
        if (!isPowerMerchant() || isIdlePowerMerchant()) {
            showUpgradeOverlay();
        }
        return new EmptyViewHolder(view);
    }

    private void showUpgradeOverlay() {
        textViewOverlay.setMovementMethod(LinkMovementMethod.getInstance());
        textViewOverlay.setText(createSpannableLink(
                context.getString(R.string.gm_featured_product_overlay_desc),
                context.getString(R.string.gm_featured_product_overlay_read_more)
        ));
        viewOverlayRegularMerchant.setVisibility(View.VISIBLE);
        buttonOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmFeaturedProductEmptyDataBinderListener.buttonOverlayClicked();
            }
        });
        buttonOverlay.setText(context.getString(R.string.gm_featured_product_overlay_upgrade_shop));
        ImageHandler imageHandler = new ImageHandler(context);
        imageHandler.loadImage(imageViewOverlay, IMG_URL_ICON_LOCK_WHITE_GREEN);

    }

    private boolean isPowerMerchant() {
        return new UserSession(context).isGoldMerchant();
    }

    private boolean isIdlePowerMerchant() {
        return new UserSession(context).isPowerMerchantIdle();
    }

    private SpannableStringBuilder createSpannableLink(
            String originalText,
            String readMoreText
    ) {
        SpannableString spannableText = new SpannableString(readMoreText);
        int startIndex = 0;
        int endIndex = spannableText.length();
        int color = context.getResources().getColor(R.color.merchant_green);
        spannableText.setSpan(color, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View view) {
                gmFeaturedProductEmptyDataBinderListener.spanReadMoreClicked();
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
            }
        };
        spannableText.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannableText.setSpan(
                new StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return new SpannableStringBuilder(originalText).append(" ").append(spannableText);
    }
}