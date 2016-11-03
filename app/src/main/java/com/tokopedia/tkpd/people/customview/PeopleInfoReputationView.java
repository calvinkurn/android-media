package com.tokopedia.tkpd.people.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.people.model.PeopleInfoData;
import com.tokopedia.tkpd.people.presenter.PeopleInfoFragmentPresenter;

import butterknife.Bind;

/**
 * Created on 6/2/16.
 */
public class PeopleInfoReputationView extends BaseView<PeopleInfoData, PeopleInfoFragmentPresenter> {

    @Bind(R.id.icon_reputation)
    ImageView mainReputationIcon;
    @Bind(R.id.user_reputation)
    TextView mainReputationText;
    @Bind(R.id.counter_positive)
    TextView counterPositive;
    @Bind(R.id.counter_neutral)
    TextView counterNeutral;
    @Bind(R.id.counter_negative)
    TextView counterNegative;

    public PeopleInfoReputationView(Context context) {
        super(context);
    }

    public PeopleInfoReputationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_people_info_reputation;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void renderData(@NonNull PeopleInfoData peopleInfoData) {
        PeopleInfoData.UserInfo.UserReputation reputation = peopleInfoData.getUserInfo().getUserReputation();

        if(reputation.getNoReputation() == 0) {
            mainReputationText.setVisibility(VISIBLE);
            ImageHandler.loadImageWithId(mainReputationIcon, R.drawable.ic_icon_repsis_smile_active);
        } else {
            mainReputationText.setVisibility(GONE);
            ImageHandler.loadImageWithId(mainReputationIcon, R.drawable.ic_icon_repsis_smile);
        }
        mainReputationText.setText(reputation.getPositivePercentage() + "%");
        counterPositive.setText(String.valueOf(reputation.getPositive()));
        counterNeutral.setText(String.valueOf(reputation.getNeutral()));
        counterNegative.setText(String.valueOf(reputation.getNegative()));
        setVisibility(VISIBLE);
    }

    @Override
    public void setPresenter(@NonNull PeopleInfoFragmentPresenter presenter) {
        this.presenter = presenter;
    }
}
