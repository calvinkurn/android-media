package com.tokopedia.tkpd.manage.people.profile.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.manage.people.profile.model.DataUser;
import com.tokopedia.tkpd.manage.people.profile.model.Profile;
import com.tokopedia.tkpd.manage.people.profile.presenter.ManagePeopleProfileFragmentPresenter;
import com.tokopedia.tkpd.var.TkpdState;

import butterknife.Bind;

/**
 * Created on 6/9/16.
 */
public class DetailView extends BaseView<Profile, ManagePeopleProfileFragmentPresenter> {

    @Bind(R.id.user_name)
    public EditText userName;
    @Bind(R.id.birth_date)
    public EditText birthDate;
    @Bind(R.id.gender)
    public RadioGroup genderRadioGroup;
    @Bind(R.id.hobbies)
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
        return new StringBuilder().append(birthDay).append("/").append(birthMonth).append("/").append(birthYear);
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
                            Integer.parseInt(dataUser.getBirthDay()),
                            Integer.parseInt(dataUser.getBirthMonth()),
                            Integer.parseInt(dataUser.getBirthYear())
                    );
            datePicker.SetMaxYear(2000);
            datePicker.SetMinYear(1934);
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
