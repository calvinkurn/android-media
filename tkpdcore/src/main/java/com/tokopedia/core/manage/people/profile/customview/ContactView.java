package com.tokopedia.core.manage.people.profile.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.manage.people.profile.model.DataUser;
import com.tokopedia.core.manage.people.profile.model.Profile;
import com.tokopedia.core.manage.people.profile.presenter.ManagePeopleProfileFragmentPresenter;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created on 6/9/16.
 */
public class ContactView extends BaseView<Profile, ManagePeopleProfileFragmentPresenter> {


    public TextView messenger;
    public View changeEmailBtn;
    public EditText email;
    public View phoneSection;
    public View changeHpBtn;
    public EditText phone;
    public TextView tvPhone;
    public View verificationBtn;
    public TextView checkEmailInfo;
    public TextView tvEmail, tvEmailHint;
    public View tvVerifiedPhoneNumber;


    //case 0, phone not verified and email is empty, thus show edit text email (email) and hint
    //case 1, phone not verified and email available, this not showing any button, but show email
    //case 2, phone verified and email is empty, thus show tv email
    //case 3, phone verified and email is available, thus show change button and text view email (tvEmail)
    public static final int CASE_DEFAULT = 0;
    public static final int CASE_FIRST = 1;
    public static final int CASE_SECOND = 2;
    public static final int CASE_THIRD = 3;

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
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        tvEmail = (TextView) view.findViewById(R.id.tv_email);
        tvEmailHint = (TextView) view.findViewById(R.id.tv_email_hint);
        changeEmailBtn = view.findViewById(R.id.change_email_button);
        email = view.findViewById(R.id.email);
        messenger = view.findViewById(R.id.messenger);
        phoneSection = view.findViewById(R.id.phone_section);
        changeHpBtn = view.findViewById(R.id.change_hp_button);
        phone = view.findViewById(R.id.phone);
        tvPhone = (TextView) view.findViewById(R.id.tv_phone);
        verificationBtn = view.findViewById(R.id.verify_phone_button);
        checkEmailInfo = view.findViewById(R.id.check_email_info);
        tvVerifiedPhoneNumber = view.findViewById(R.id.tv_verified_phone_number);
    }

    @Override
    public void renderData(@NonNull Profile profile) {
        DataUser dataUser = profile.getDataUser();
        renderEmailView(dataUser.getUserEmail());
        renderPhoneView(dataUser.getUserPhone());
    }

    private void renderEmailView(String userEmail) {
        email.setVisibility(GONE);
        email.setClickable(false);
        email.setEnabled(false);
        tvEmailHint.setVisibility(GONE);
        tvEmail.setVisibility(GONE);
        changeEmailBtn.setVisibility(GONE);
        int emailCase  = 0;
        if (!SessionHandler.isMsisdnVerified() && TextUtils.isEmpty(userEmail)) emailCase = CASE_DEFAULT;
        else if (!SessionHandler.isMsisdnVerified() && !TextUtils.isEmpty(userEmail)) emailCase = CASE_FIRST;
        else if (SessionHandler.isMsisdnVerified() && TextUtils.isEmpty(userEmail)) emailCase = CASE_SECOND;
        else if (SessionHandler.isMsisdnVerified() && !TextUtils.isEmpty(userEmail)) emailCase = CASE_THIRD;
        switch (emailCase) {
            case CASE_DEFAULT:
            case CASE_SECOND:
                email.setVisibility(VISIBLE);
                email.setClickable(true);
                email.setEnabled(true);
                email.setOnClickListener(new AddEmailClick());
                tvEmailHint.setVisibility(VISIBLE);
                break;
            case CASE_FIRST:
                tvEmail.setText(userEmail);
                tvEmail.setVisibility(VISIBLE);
                break;
            case CASE_THIRD:
                tvEmail.setText(userEmail);
                tvEmail.setVisibility(VISIBLE);
                changeEmailBtn.setVisibility(VISIBLE);
                changeEmailBtn.setOnClickListener(new ChangeEmailButtonClick(userEmail));
                break;
            default:
                break;
        }
    }

    private void renderPhoneView(String userPhone) {
        tvPhone.setText(userPhone);
        if (SessionHandler.isMsisdnVerified()) {
            changeHpBtn.setVisibility(VISIBLE);
            verificationBtn.setVisibility(GONE);
            tvVerifiedPhoneNumber.setVisibility(VISIBLE);
        } else {
            changeHpBtn.setVisibility(GONE);
            verificationBtn.setVisibility(VISIBLE);
            tvVerifiedPhoneNumber.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(userPhone)) {
            tvPhone.setVisibility(VISIBLE);
            phone.setVisibility(GONE);
        }
        changeHpBtn.setOnClickListener(new ChangePhoneButtonClick(userPhone));
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

    private class AddEmailClick implements OnClickListener {
        @Override
        public void onClick(View view) {
            presenter.setOnAddEmailClick(getContext());
        }
    }
}
