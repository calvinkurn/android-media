package com.tokopedia.core.fragment.shopstatistic;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core2.R;

/**
 * Created by Tkpd_Eka on 7/9/2015.
 */
public class ShopStatisticResponse {

    public static class Model{
        public String mainResponse;
        public String responseSpeed;
        public int responseFast;
        public int responseMedium;
        public int responseSlow;
        public float barFast;
        public float barMedium;
        public float barSlow;
    }

    private class ViewHolder{
        ImageView mainIcon;
        TextView mainResponse;
        TextView totalFast;
        TextView totalMedium;
        TextView totalSlow;
        View barFast;
        View barMedium;
        View barSlow;
    }

    public static final int ICON_FAST = R.drawable.ic_icon_repsis_speed_cepat;
    public static final int ICON_MEDIUM = R.drawable.ic_icon_repsis_speed_sedang;
    public static final int ICON_SLOW = R.drawable.ic_icon_repsis_speed_lambat;

    public static final String BADGE_FAST = "badge-speed-good";
    public static final String BADGE_MEDIUM = "badge-speed-neutral";
    public static final String BADGE_SLOW = "badge-speed-bad";

    private View view;
    private ViewHolder holder;
    private Model model;

    public ShopStatisticResponse(View view){
        this.view = view;
        holder = new ViewHolder();
        initView();
    }

    public void setModelToView(Model model){
        this.model = model;
        switch(model.mainResponse){
            case BADGE_FAST:
                holder.mainIcon.setImageResource(ICON_FAST);
                break;
            case BADGE_MEDIUM:
                holder.mainIcon.setImageResource(ICON_MEDIUM);
                break;
            case BADGE_SLOW:
                holder.mainIcon.setImageResource(ICON_SLOW);
                break;
        }
        holder.totalFast.setText(model.responseFast + "");
        holder.totalMedium.setText(model.responseMedium + "");
        holder.totalSlow.setText(model.responseSlow + "");
        holder.barFast.getLayoutParams().width = Math.round((model.barFast / 100.0f) * holder.barFast.getLayoutParams().width);
        holder.barMedium.getLayoutParams().width = Math.round((model.barMedium / 100.0f) * holder.barMedium.getLayoutParams().width);
        holder.barSlow.getLayoutParams().width = Math.round((model.barSlow / 100.0f) * holder.barSlow.getLayoutParams().width);
        holder.mainResponse.setText(model.responseSpeed);
    }

    private void initView(){
        holder.mainIcon = (ImageView)findViewById(R.id.overall_icon);
        holder.mainResponse = (TextView)findViewById(R.id.overall_response);
        holder.totalFast = (TextView)findViewById(R.id.response_fast);
        holder.totalMedium = (TextView)findViewById(R.id.response_medium);
        holder.totalSlow = (TextView)findViewById(R.id.response_slow);
        holder.barFast = findViewById(R.id.fast_bar);
        holder.barMedium = findViewById(R.id.medium_bar);
        holder.barSlow = findViewById(R.id.slow_bar);

//        ViewGroup.LayoutParams param = new RelativeLayout.LayoutParams(50, 12);
//        holder.barSlow.setLayoutParams(param);

//        try { // TODO THIS IS WORKING
//            holder.barSlow.getLayoutParams().width = 50;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private View findViewById(int id){
        return view.findViewById(id);
    }

}
