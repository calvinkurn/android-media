package com.tokopedia.sellerapp.gmstat.views;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmstat.models.GetPopularProduct;
import com.tokopedia.sellerapp.gmstat.utils.KMNumbers2;
import com.tokopedia.sellerapp.home.utils.ImageHandler;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by normansyahputa on 11/9/16.
 */

public class PopularProductViewHelper {
    private final View itemView;
    @BindView(R.id.data_product_icon)
    ImageView dataProductIcon;

    @BindView(R.id.data_product_title)
    TextView dataProductTitle;

    @BindView(R.id.image_popular_product)
    ImageView imagePopularProduct;

    @BindView(R.id.text_popular_product)
    TextView textPopularProduct;

    @BindView(R.id.popular_product_description)
    TextView popularProductDescription;

    @BindView(R.id.number_of_selling)
    TextView numberOfSelling;

    @BindView(R.id.x_sold)
    TextView xSold;
    private GetPopularProduct getPopularProduct;

    @BindView(R.id.footer_popular_product)
    TextView footerPopularProduct;

    @BindView(R.id.popular_product_empty_state)
    LinearLayout popularProductEmptyState;

    @OnClick({R.id.image_popular_product, R.id.text_popular_product})
    public void gotoProductDetail(){
        if(getPopularProduct == null)
            return;

        ProductInfoActivity.createInstance(itemView.getContext(), getPopularProduct.getProductId()+"");
    }

    public PopularProductViewHelper(View itemView){
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;

        String categoryBold = String.format("\"<i><b>%s</b></i>\"", "Data dalam 30 hari terakhir");
        footerPopularProduct.setText(MethodChecker.fromHtml(categoryBold));
    }

    public void bindData(GetPopularProduct getPopularProduct, ImageHandler imageHandler){
        this.getPopularProduct = getPopularProduct;
        if(getPopularProduct == null || getPopularProduct.getProductId() == 0){
            popularProductEmptyState.setVisibility(View.VISIBLE);
            return;
        }

        dataProductTitle.setText("Data Produk");
        textPopularProduct.setText("Produk terlaris");
        imageHandler.loadImage(imagePopularProduct, getPopularProduct.getImageLink());
        popularProductDescription.setText(MethodChecker.fromHtml(getPopularProduct.getProductName()));
        long sold = getPopularProduct.getSold();
        String text = getFormattedString(sold);
//        numberOfSelling.setText(toKFormat(getPopularProduct.getSold()));
        numberOfSelling.setText(text);
        xSold.setText("x Terjual");
    }

    public static String getFormattedString(long value) {
        String text = "";
        if( value < 1_000_000){
            Locale locale = new Locale("in", "ID");
            NumberFormat currencyFormatter = NumberFormat.getNumberInstance(locale);
            System.out.println(text = (currencyFormatter.format(value)));
//                text = successTrans+"";
        }else if(value >= 1_000_000){
            text = KMNumbers2.formatNumbers(value);
        }
        return text;
    }
}
