package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.home.model.shopmodel.Stats;
import com.tokopedia.sellerapp.home.utils.ReputationLevelUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by normansyahputa on 8/29/16.
 */

public class ReputationView extends FrameLayout implements BaseView<ReputationView.ReputationViewModel> {

    @BindView(R.id.reputation_badge_listener)
    LinearLayout reputationBadgeListener;

    @BindView(R.id.reputation_points)
    TextView reputationPoints;

    public ReputationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.reputation_item_view, this);
        ButterKnife.bind(this);
    }

    @Override
    public void init(ReputationViewModel data) {

        ReputationLevelUtils.setReputationMedals(getContext(), reputationBadgeListener, data.typeMedal, data.levelMedal, data.reputationPoints);

        reputationPoints.setText("Reputasi : "+data.stats.shopReputationScore+" Poin");
    }

    public static class ReputationViewModel{
        public int typeMedal;
        public int levelMedal;
        public String reputationPoints;
        public Stats stats;
    }


}
