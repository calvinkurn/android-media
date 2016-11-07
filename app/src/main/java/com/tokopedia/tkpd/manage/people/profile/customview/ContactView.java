package com.tokopedia.tkpd.manage.people.profile.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.manage.people.profile.model.DataUser;
import com.tokopedia.tkpd.manage.people.profile.model.Profile;
import com.tokopedia.tkpd.manage.people.profile.presenter.ManagePeopleProfileFragmentPresenter;
import com.tokopedia.tkpd.util.SessionHandler;

import butterknife.Bind;

/**
 * Created on 6/9/16.
 */
public class ContactView extends BaseView<Profile, ManagePeopleProfileFragmentPresenter> {

    @Bind(R2.id.messenger)
    public EditText messenger;
    @Bind(R2.id.change_email_button)
    public View changeEmailBtn;
    @Bind(R2.id.email)
    public EditText email;
    @Bind(R2.id.phone_section)
    public View phoneSection;
    @Bind(R2.id.change_hp_button)
    public View changeHpBtn;
    @Bind(R2.id.phone)
    public EditText phone;
    @Bind(R2.id.phone_verification_section)
    public View phoneVerificationSection;
    @Bind(R2.id.verification)
    public EditText verification;
    @Bind(R2.id.verify_phone_button)
    public View verificationBtn;
    @Bind(R2.id.password)
    public EditText password;

    public ContactView(Context context) {
        super(context);
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_manage_people_profile_contact_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull Profile profile) {
        DataUser dataUser = profile.getDataUser();
        messenger.setText(dataUser.getUserMessenger());
        renderEmailView(dataUser.getUserEmail());
        renderPhoneView(dataUser.getUserPhone());
        renderPhoneVerificationView(dataUser.getUserPhone());
    }

    private void renderEmailView(String userEmail) {
        email.setText(userEmail);
        changeEmailBtn.setOnClickListener(new ChangeEmailButtonClick(userEmail));

    }

    private void renderPhoneView(String userPhone) {
        phone.setText(userPhone);
        if (SessionHandler.isMsisdnVerified()) {
            phoneSection.setVisibility(VISIBLE);
        } else {
            phoneSection.setVisibility(GONE);
        }
        changeHpBtn.setOnClickListener(new ChangePhoneButtonClick(userPhone));
    }

    private void renderPhoneVerificationView(String userPhone) {
        verification.setText(userPhone);
        if (SessionHandler.isMsisdnVerified()) {
            phoneVerificationSection.setVisibility(GONE);
        } else {
            phoneVerificationSection.setVisibility(VISIBLE);
        }
        verificationBtn.setOnClickListener(new VerificationButtonClick(userPhone));
    }

    @Override
    public void setPresenter(@NonNull ManagePeopleProfileFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    private class ChangeEmailButtonClick implements OnClickListener {

        private final String userEmail;

        public ChangeEmailButtonClick(String userEmail) {
            this.userEmail = userEmail;
        }

        @Override
        public void onClick(View view) {
            presenter.setOnChangeEmailButtonClick(getContext(), userEmail);
        }
    }

    private class ChangePhoneButtonClick implements OnClickListener {

        private final String userPhone;

        public ChangePhoneButtonClick(String userPhone) {
            this.userPhone = userPhone;
        }

        @Override
        public void onClick(View view) {
            presenter.setOnChangePhoneButtonClick(getContext(), userPhone);
        }
    }

    private class VerificationButtonClick implements OnClickListener {

        private final String userPhone;

        public VerificationButtonClick(String userPhone) {
            this.userPhone = userPhone;
        }

        @Override
        public void onClick(View view) {
            presenter.setOnVerificationButtonClick(getContext(), userPhone);
        }
    }
}
