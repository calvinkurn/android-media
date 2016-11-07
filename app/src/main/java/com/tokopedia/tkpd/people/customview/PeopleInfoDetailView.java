package com.tokopedia.tkpd.people.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.people.model.InputOutputData;
import com.tokopedia.tkpd.people.model.PeopleAddressData;
import com.tokopedia.tkpd.people.model.PeopleInfoData;
import com.tokopedia.tkpd.people.model.PeoplePrivacyData;
import com.tokopedia.tkpd.people.presenter.PeopleInfoFragmentPresenter;
import com.tokopedia.tkpd.util.SessionHandler;

import butterknife.Bind;

/**
 * Created on 6/2/16.
 */
public class PeopleInfoDetailView extends BaseView<InputOutputData, PeopleInfoFragmentPresenter> {

    @Bind(R2.id.user_email)
    TextView email;
    @Bind(R2.id.user_yahoo)
    TextView yahoo;
    @Bind(R2.id.user_phone)
    TextView phone;
    @Bind(R2.id.user_address)
    TextView address;
    @Bind(R2.id.user_hobby)
    TextView hobby;
    @Bind(R2.id.user_birth)
    TextView birth;

    public PeopleInfoDetailView(Context context) {
        super(context);
    }

    public PeopleInfoDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_people_info_detail;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
        email.setVisibility(GONE);
        yahoo.setVisibility(GONE);
        phone.setVisibility(GONE);
        address.setVisibility(GONE);
        hobby.setVisibility(GONE);
        birth.setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull InputOutputData data) {
        PeopleInfoData.UserInfo userInfo = data.getPeopleInfoData().getUserInfo();
        PeoplePrivacyData.Privacy privacy = data.getPeoplePrivacyData().getPrivacy();
        PeopleAddressData address = data.getPeopleAddressData();

        renderUserEmail(userInfo, privacy);
        renderUserYahoo(userInfo, privacy);
        renderUserPhone(userInfo, privacy);
        renderUserAddress(userInfo, privacy, address);
        renderUserHobby(userInfo);
        renderUserBirth(userInfo, privacy);

        setVisibility();
    }

    private void setVisibility() {
        if (email.getVisibility() == VISIBLE || yahoo.getVisibility() == VISIBLE
                || phone.getVisibility() == VISIBLE || birth.getVisibility() == VISIBLE) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    /**
     *
     * @param view view
     * @param privacyFlag flag to set visibility
     * @param value parameter to be checked as its view's value if null / empty set to gone
     */
    public void setVisibility(TextView view, String privacyFlag, String value) {
        if (privacyFlag.equals("2") &&
                !(value == null || value.equals("null") || value.isEmpty() || value.equals("0"))) {
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
        }
    }

    /**
     *
     * @param view view
     * @param privacyFlag flag to set visibility
     * @param value parameter to be checked as its view's value if null / empty set to gone
     * @param userID userID from webservice
     */
    public void setVisibility(TextView view, String privacyFlag, String value, String userID) {
        if (SessionHandler.isV4Login(getContext())) {
            if (SessionHandler.getLoginID(getContext()).equals(userID)) {
                view.setVisibility(!(value == null || value.equals("null") || value.isEmpty() || value.equals("0")) ? VISIBLE : GONE);
            } else {
                setVisibility(view, privacyFlag, value);
            }
        } else {
            setVisibility(view, privacyFlag, value);
        }
    }

    private void renderUserEmail(PeopleInfoData.UserInfo userInfo, PeoplePrivacyData.Privacy privacy) {
        email.setText(userInfo.getUserEmail());
        setVisibility(email,
                privacy.getFlagEmail(),
                userInfo.getUserEmail(),
                userInfo.getUserId()
        );
    }

    private void renderUserYahoo(PeopleInfoData.UserInfo userInfo, PeoplePrivacyData.Privacy privacy) {
        yahoo.setText(userInfo.getUserMessenger());
        setVisibility(yahoo,
                privacy.getFlagMessenger(),
                userInfo.getUserMessenger(),
                userInfo.getUserId()
        );
    }

    private void renderUserPhone(PeopleInfoData.UserInfo userInfo, PeoplePrivacyData.Privacy privacy) {
        phone.setText(userInfo.getUserPhone());
        setVisibility(phone,
                privacy.getFlagHp(),
                userInfo.getUserPhone(),
                userInfo.getUserId()
        );
    }

    private void renderUserAddress(PeopleInfoData.UserInfo userInfo,
                                   PeoplePrivacyData.Privacy privacy,
                                   PeopleAddressData addressData) {
        String value = null;
        for (PeopleAddressData.PeopleAddress data : addressData.getList()) {
            if (data.getAddressStatus() != 1) {
                value = data.getPeopleAddress();
            }
        }
        address.setText(value);
        setVisibility(address, privacy.getFlagAddress(), value, userInfo.getUserId());
    }

    private void renderUserHobby(PeopleInfoData.UserInfo userInfo) {
        hobby.setText(userInfo.getUserHobbies());
        setVisibility(hobby, "2", userInfo.getUserHobbies(), userInfo.getUserId());
    }

    private void renderUserBirth(PeopleInfoData.UserInfo userInfo, PeoplePrivacyData.Privacy privacy) {
        birth.setText(userInfo.getUserBirth());
        setVisibility(birth, privacy.getFlagBirthdate(), userInfo.getUserBirth(), userInfo.getUserId());
    }

    @Override
    public void setPresenter(@NonNull PeopleInfoFragmentPresenter presenter) {
        this.presenter = presenter;
    }
}
