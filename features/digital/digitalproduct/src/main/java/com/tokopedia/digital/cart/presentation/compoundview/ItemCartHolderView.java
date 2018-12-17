package com.tokopedia.digital.cart.presentation.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.digital.R;

import java.util.List;

/**
 * @author by Nabilla Sabbaha on 2/28/2017.
 */

public class ItemCartHolderView extends RelativeLayout {

    private ImageView iconCategory;
    private TextView categoryName;
    private TextView operatorName;
    private LinearLayout layoutMainInfo;
    private LinearLayout layoutAdditionalInfo;
    private TextView buttonDetail;
    private View separator;

    private boolean additionalInfoShowed;
    private Context context;

    public ItemCartHolderView(Context context) {
        super(context);
        init(context);
    }

    public ItemCartHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ItemCartHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.view_holder_checkout_item_digital_module, this, true);

        iconCategory = findViewById(R.id.icon_category);
        categoryName = findViewById(R.id.category_name);
        operatorName = findViewById(R.id.operator_name);
        layoutMainInfo = findViewById(R.id.layout_main_info);
        layoutAdditionalInfo = findViewById(R.id.layout_additional_info);
        buttonDetail = findViewById(R.id.button_detail);
        separator = findViewById(R.id.separator);

        setView();
    }

    private void setView() {
        buttonDetail.setOnClickListener(getClickDetailListener());
    }

    public void setImageItemCart(String url) {
        ImageHandler.loadImageCircle2(context, iconCategory, url);
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.setText(categoryName);
    }

    public void setOperatorName(String operatorName) {
        this.operatorName.setText(operatorName);
    }

    public void renderDataMainInfo(List<CartItemDigital> cartItemDigitals) {
        layoutMainInfo.removeAllViews();
        for (CartItemDigital itemcart : cartItemDigitals) {
            addViewMainInfo(itemcart);
        }
    }

    public void renderAdditionalInfo(List<CartAdditionalInfo> additionalInfos) {
        layoutAdditionalInfo.removeAllViews();
        separator.setVisibility(additionalInfos.isEmpty() ? GONE : VISIBLE);
        buttonDetail.setVisibility(additionalInfos.isEmpty() ? GONE : VISIBLE);
        if (!additionalInfos.isEmpty()) {
            for (CartAdditionalInfo additionalInfo : additionalInfos) {
                addViewAdditionalInfo(additionalInfo);
            }
        }
    }

    private void addViewAdditionalInfo(CartAdditionalInfo additionalInfo) {
        addTitleAdditionalInfo(additionalInfo.getTitle());
        for (CartItemDigital cartItemDigital : additionalInfo.getCartItemDigitalList()) {
            addViewItemAdditionalInfo(cartItemDigital);
        }
    }

    private void addTitleAdditionalInfo(String titleAddInfo) {
        LinearLayout.LayoutParams layoutTitle = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutTitle.setMargins(0, 25, 0, 15);
        TextView title = new TextView(context);
        title.setLayoutParams(layoutTitle);
        title.setTextSize(context.getResources().getDimension(R.dimen.sp_14) /
                getResources().getDisplayMetrics().density);
        title.setText(titleAddInfo);
        layoutAdditionalInfo.addView(title);
    }

    private void addViewItemAdditionalInfo(CartItemDigital cartItemDigital) {
        ItemListCartView itemListCartView = new ItemListCartView(context);
        layoutAdditionalInfo.addView(itemListCartView);
        itemListCartView.bindView(cartItemDigital);
    }

    private void addViewMainInfo(CartItemDigital cartItemDigital) {
        ItemListCartView itemListCartView = new ItemListCartView(context);
        layoutMainInfo.addView(itemListCartView);
        itemListCartView.bindView(cartItemDigital);
    }

    private OnClickListener getClickDetailListener() {
        return v -> {
            if (additionalInfoShowed) {
                hideAdditionalInfo();
            } else {
                showAdditionalInfo();
            }
        };
    }

    private void showAdditionalInfo() {
        layoutAdditionalInfo.setVisibility(VISIBLE);
        buttonDetail.setText(context.getString(R.string.link_tutup));
        buttonDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.page_1, 0);
        additionalInfoShowed = true;
    }

    private void hideAdditionalInfo() {
        layoutAdditionalInfo.setVisibility(GONE);
        buttonDetail.setText(context.getString(R.string.link_lihat_detail));
        buttonDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.page_2, 0);
        additionalInfoShowed = false;
    }

}
