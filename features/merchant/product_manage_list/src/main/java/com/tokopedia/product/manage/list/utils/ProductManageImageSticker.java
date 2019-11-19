package com.tokopedia.product.manage.list.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;

/**
 * Created by yoshua on 28/05/18.
 */

public class ProductManageImageSticker {
    private String name;
    private String price;
    private String shop_link;
    private String cashback;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getShop_link() {
        return shop_link;
    }
    public void setShop_link(String shop_link) {
        this.shop_link = shop_link;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public Bitmap processStickerToImage(Bitmap source, Context context){
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = new RelativeLayout(context);
        mInflater.inflate(com.tokopedia.seller.R.layout.partial_product_manage_image_sticker, view, true);

        ImageView imgSource = view.findViewById(com.tokopedia.seller.R.id.img_source);
        TextView tvPrice = view.findViewById(com.tokopedia.seller.R.id.tv_price);
        TextView tvName = view.findViewById(com.tokopedia.core2.R.id.tv_name);
        TextView tvShopLink = view.findViewById(com.tokopedia.seller.R.id.tv_shop_link);
        TextView tvCashback = view.findViewById(com.tokopedia.seller.R.id.tv_cashback);

        imgSource.setImageBitmap(source);
        tvPrice.setText(getPrice());
        tvName.setText(getName());
        tvShopLink.setText(getShop_link());
        tvCashback.setText(getCashback());

        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        view.draw(c);
        return bitmap;
    }

    public static class Builder {
        private String name;
        private String price;
        private String shop_link;
        private String cashback;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setPrice(String price) {
            this.price = price;
            return this;
        }
        public Builder setShop_link(String shop_link) {
            this.shop_link = shop_link;
            return this;
        }
        public Builder setCashback(String cashback) {
            this.cashback = cashback;
            return this;
        }
        public ProductManageImageSticker build() {
            ProductManageImageSticker productManageImageSticker = new ProductManageImageSticker();
            productManageImageSticker.setName(name);
            productManageImageSticker.setPrice(price);
            productManageImageSticker.setShop_link(shop_link);
            productManageImageSticker.setCashback(cashback);
            return productManageImageSticker;
        }
    }
}