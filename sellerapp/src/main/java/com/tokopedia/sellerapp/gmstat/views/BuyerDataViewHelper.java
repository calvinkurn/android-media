package com.tokopedia.sellerapp.gmstat.views;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmstat.models.GetBuyerData;
import com.tokopedia.sellerapp.home.utils.ImageHandler;

import java.text.DecimalFormat;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.sellerapp.gmstat.views.GMStatActivityFragment.NoDataAvailable;
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

    @BindColor(R.color.grey_400)
    int gredyColor;

//    @BindDrawable(R.drawable.ic_rectangle_down)
    Drawable icRectagleDown;

//    @BindDrawable(R.drawable.ic_rectangle_up)
    Drawable icRectagleUp;

    private View itemView;

    @BindArray(R.array.gender)
    String[] gender;

    public BuyerDataViewHelper(View itemView){
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);

        icRectagleDown = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_down);
        icRectagleUp = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_up);
    }

    public void bindData(GetBuyerData getBuyerData, ImageHandler imageHandler) {
        if(getBuyerData.getTotalBuyer()==0 &&
                (getBuyerData.getMaleBuyer()==0 || getBuyerData.getFemaleBuyer()==0)){
            malePie.setText(String.format("%.2f%% Pria", "0"));
            femalePie.setText(String.format("%.2f%%", "0"));
        }else{
            double malePercentage = (double)getBuyerData.getMaleBuyer() / (double)getBuyerData.getTotalBuyer();
            double malePercent = Math.floor((malePercentage * 100) + 0.5);

            double femalePercentage = (double)getBuyerData.getFemaleBuyer() /  (double)getBuyerData.getTotalBuyer();
            double femalePercent = Math.floor((femalePercentage * 100) + 0.5);

            String biggerGender = "";
            if(malePercent >= femalePercent){
                biggerGender += gender[0];
                malePie.setText(String.format("%.2f%% %s", malePercent, biggerGender));
                femalePie.setText(String.format("%.2f%%", femalePercent));
                buyerDataPieChart.setProgress((float) femalePercentage);
            }else{
                biggerGender += gender[1];
                malePie.setText(String.format("%.2f%% %s", femalePercent, biggerGender));
                femalePie.setText(String.format("%.2f%%", malePercent));
                buyerDataPieChart.setProgress((float) malePercent);
            }
        }

        buyerCount.setText(getFormattedString(getBuyerData.getTotalBuyer())+"");

        double percentage = getBuyerData.getDiffTotal() * 100D;
        // image for arrow is here
        boolean isDefault = false;
        if(percentage == 0){
            buyerCountIcon.setVisibility(View.INVISIBLE);
            percentageBuyer.setTextColor(arrowUp);
            isDefault = true;
        }else if(percentage < 0){// down here
            if(percentage == NoDataAvailable*100){
                buyerCountIcon.setVisibility(View.INVISIBLE);
                percentageBuyer.setTextColor(gredyColor);
                isDefault = false;
            }else{
                buyerCountIcon.setImageDrawable(icRectagleDown);
//            imageHandler.loadImage(buyerCountIcon, R.mipmap.arrow_down_percentage);
                percentageBuyer.setTextColor(arrowDown);
                isDefault = true;
            }
        }else{// up here
            buyerCountIcon.setImageDrawable(icRectagleUp);
//            imageHandler.loadImage(buyerCountIcon, R.mipmap.arrow_up_percentage);
            percentageBuyer.setTextColor(arrowUp);
            isDefault = true;
        }

        if(isDefault){
            DecimalFormat formatter = new DecimalFormat("#0.00");
            double d = percentage;
            String text = "";
            System.out.println(text = formatter.format(d));
            percentageBuyer.setText(text.replace("-","")+"%");
        }else{
            percentageBuyer.setText("Tidak ada data");
        }

        // set icon up or down or netral
    }
}
