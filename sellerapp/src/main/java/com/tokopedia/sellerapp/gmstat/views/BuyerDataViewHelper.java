package com.tokopedia.sellerapp.gmstat.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmstat.models.GetBuyerData;
import com.tokopedia.sellerapp.home.utils.ImageHandler;

import java.text.DecimalFormat;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.sellerapp.gmstat.views.PopularProductViewHelper.getFormattedString;

/**
 * Created by normansyahputa on 11/11/16.
 */

public class BuyerDataViewHelper {

    @BindView(R.id.buyer_data_pie_chart)
    DonutProgress buyerDataPieChart;

    @BindView(R.id.buyer_count)
    TextView buyerCount;

    @BindView(R.id.buyer_count_icon)
    ImageView buyerCountIcon;

    @BindView(R.id.percentage_buyer)
    TextView percentageBuyer;

    @BindView(R.id.female_pie)
    TextView femalePie;

    @BindView(R.id.male_pie)
    TextView malePie;

    @BindColor(R.color.arrow_down)
    int arrowDown;

    @BindColor(R.color.arrow_up)
    int arrowUp;

    private View itemView;

    public BuyerDataViewHelper(View itemView){
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void bindData(GetBuyerData getBuyerData, ImageHandler imageHandler) {
        double malePercentage = (double)getBuyerData.getMaleBuyer() / (double)getBuyerData.getTotalBuyer();
        double malePercent = Math.floor((malePercentage * 100) + 0.5);
        malePie.setText(String.format("%.2f%% Pria", malePercent));

        double femalePercentage = (double)getBuyerData.getFemaleBuyer() /  (double)getBuyerData.getTotalBuyer();
        double femalePercent = Math.floor((femalePercentage * 100) + 0.5);
        femalePie.setText(String.format("%.2f%%", femalePercent));
        buyerDataPieChart.setProgress((float) femalePercentage);


        buyerCount.setText(getFormattedString(getBuyerData.getTotalBuyer())+"");

        double percentage = getBuyerData.getDiffTotal() * 100D;
        // image for arrow is here
        if(percentage < 0){// down here
            imageHandler.loadImage(buyerCountIcon, R.mipmap.arrow_down_percentage);
            percentageBuyer.setTextColor(arrowDown);
        }else{// up here
            imageHandler.loadImage(buyerCountIcon, R.mipmap.arrow_up_percentage);
            percentageBuyer.setTextColor(arrowUp);
        }

        DecimalFormat formatter = new DecimalFormat("#0.00");
        double d = percentage;
        String text = "";
        System.out.println(text = formatter.format(d));
        percentageBuyer.setText(text+"%");

        // set icon up or down or netral
    }
}
