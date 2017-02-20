package com.tokopedia.core.manage.people.profile.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.manage.people.profile.model.DataUser;
import com.tokopedia.core.manage.people.profile.model.Profile;
import com.tokopedia.core.manage.people.profile.presenter.ManagePeopleProfileFragmentPresenter;
import com.tokopedia.core.var.TkpdState;

import butterknife.BindView;

/**
 * Created on 6/9/16.
 */
public class DetailView extends BaseView<Profile, ManagePeopleProfileFragmentPresenter> {

    @BindView(R2.id.user_name)
    public EditText userName;
    @BindView(R2.id.birth_date)
    public EditText birthDate;
    @BindView(R2.id.gender)
    public RadioGroup genderRadioGroup;
    @BindView(R2.id.hobbies)
    public EditText hobby;

    public DetailView(Context context) {
        super(context);
    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_manage_people_profile_detail_view;
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
        renderUserName(dataUser.getFullName());
        renderBirthDate(dataUser);
        renderGender(dataUser.getGender());
        renderHobby(dataUser.getHobby());
    }

    private void renderHobby(String mHobby) {
        hobby.setText(mHobby);
    }

    private void renderGender(String gender) {
        if (gender.equals(TkpdState.Gender.MALE)) {
            genderRadioGroup.check(R.id.male);
        } else if (gender.equals(TkpdState.Gender.FEMALE)) {
            genderRadioGroup.check(R.id.female);
        }
    }

    private void renderUserName(String fullName) {
        userName.setText(fullName);
    }

    private void renderBirthDate(DataUser dataUser) {
        birthDate.setText(generateDate(dataUser.getBirthDay(), dataUser.getBirthMonth(), dataUser.getBirthYear()));
        birthDate.setOnClickListener(new BirthDateClick(dataUser));
    }

    private StringBuilder generateDate(String birthDay, String birthMonth, String birthYear) {
        return new StringBuilder().append(getBirthDay(birthDay)).append("/").append(getBirthMonth(birthMonth)).append("/").append(getBirthYear(birthYear));
    }

    private boolean isValid(String param) {
        return param != null && !param.isEmpty();
    }

    private int getBirthDay(String birthDay) {
        return isValid(birthDay) ? Integer.parseInt(birthDay) : 1;
    }

    private int getBirthMonth(String birthMonth) {
        return isValid(birthMonth) ? Integer.parseInt(birthMonth) : 1;
    }

    private int getBirthYear(String birthYear) {
        return isValid(birthYear) ? Integer.parseInt(birthYear) : 1989;
    }

    @Override
    public void setPresenter(@NonNull ManagePeopleProfileFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    private class BirthDateClick implements OnClickListener {

        private final DataUser dataUser;

        public BirthDateClick(DataUser dataUser) {
            this.dataUser = dataUser;
        }

        @Override
        public void onClick(View view) {
            DatePickerUtil datePicker =
                    new DatePickerUtil(
                            (Activity) getContext(),
                            getBirthDay(dataUser.getBirthDay()),
                            getBirthMonth(dataUser.getBirthMonth()),
                            getBirthYear(dataUser.getBirthYear())
                    );
            datePicker.SetMaxYear(2002);
            datePicker.SetMinYear(1936);
            datePicker.SetShowToday(false);
            datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
                @Override
                public void onDateSelected(int year, int month, int dayOfMonth) {
                    birthDate.setText(
                            generateDate(
                                    checkNumber(dayOfMonth),
                                    checkNumber(month),
                                    checkNumber(year)
                            )
                    );
                }
            });
        }

        public String checkNumber(int number) {
            return number <= 9 ? "0" + number : String.valueOf(number);
        }
    }
}
