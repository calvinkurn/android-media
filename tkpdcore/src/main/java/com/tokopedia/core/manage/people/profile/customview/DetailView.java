package com.tokopedia.core.manage.people.profile.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.core2.R;
import com.tokopedia.core.manage.people.profile.model.DataUser;
import com.tokopedia.core.manage.people.profile.model.Profile;
import com.tokopedia.core.manage.people.profile.presenter.ManagePeopleProfileFragmentPresenter;
import com.tokopedia.core.var.TkpdState;

/**
 * Created on 6/9/16.
 */
public class DetailView extends BaseView<Profile, ManagePeopleProfileFragmentPresenter> {

    public TextView userName;
    public TextView btnChangeName;
    public EditText birthDate;
    public RadioGroup genderRadioGroup;
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
    protected void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(getLayoutView(),this, true);
        userName = (TextView) view.findViewById(R.id.user_name);
        btnChangeName = (TextView) view.findViewById(R.id.change_name_button);
        birthDate = (EditText) view.findViewById(R.id.birth_date);
        genderRadioGroup = (RadioGroup) view.findViewById(R.id.gender);
        hobby = (EditText) view.findViewById(R.id.hobbies);
    }

    @Override
    public void renderData(@NonNull Profile profile) {
        DataUser dataUser = profile.getDataUser();
        renderUserName(dataUser.getFullName());
        renderChangeNameButton(dataUser.isUserGeneratedName());
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

    private void renderChangeNameButton(boolean isUserGeneratedName) {
        btnChangeName.setVisibility(isUserGeneratedName ? VISIBLE : GONE);
        btnChangeName.setOnClickListener(new ChangeNameListener());
    }

    private void renderUserName(String fullName) {
        userName.setText(fullName);
    }

    private void renderBirthDate(DataUser dataUser) {
        birthDate.setText(generateDate(dataUser.getBirthDay(), dataUser.getBirthMonth(), dataUser.getBirthYear()));
        birthDate.setOnClickListener(new BirthDateClick(getContext(), dataUser, birthDate));
    }

    private static StringBuilder generateDate(String birthDay,
                                              String birthMonth,
                                              String birthYear) {
        return new StringBuilder().append(getBirthDay(birthDay))
                .append("/")
                .append(getBirthMonth(birthMonth))
                .append("/")
                .append(getBirthYear(birthYear));
    }

    private static boolean isValid(String param) {
        return param != null && !param.isEmpty();
    }

    private static int getBirthDay(String birthDay) {
        return isValid(birthDay) ? Integer.parseInt(birthDay) : 1;
    }

    private static int getBirthMonth(String birthMonth) {
        return isValid(birthMonth) ? Integer.parseInt(birthMonth) : 1;
    }

    private static int getBirthYear(String birthYear) {
        return isValid(birthYear) ? Integer.parseInt(birthYear) : 1989;
    }

    @Override
    public void setPresenter(@NonNull ManagePeopleProfileFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    private static class BirthDateClick implements OnClickListener {

        private final Context context;
        private final DataUser dataUser;
        private final EditText birthDate;

        public BirthDateClick(Context context, DataUser dataUser, EditText birthDate) {
            this.context = context;
            this.dataUser = dataUser;
            this.birthDate = birthDate;
        }

        @Override
        public void onClick(View view) {
            DatePickerUtil datePicker =
                    new DatePickerUtil(
                            CommonUtils.getActivity(context),
                            getBirthDay(dataUser.getBirthDay()),
                            getBirthMonth(dataUser.getBirthMonth()),
                            getBirthYear(dataUser.getBirthYear())
                    );
            datePicker.SetMaxYear(2002);
            datePicker.SetMinYear(1936);
            datePicker.SetShowToday(false);
            datePicker.DatePickerCalendar((year, month, dayOfMonth) -> birthDate.setText(
                    generateDate(
                            checkNumber(dayOfMonth),
                            checkNumber(month),
                            checkNumber(year)
                    )
            ));
        }

        public String checkNumber(int number) {
            return number <= 9 ? "0" + number : String.valueOf(number);
        }
    }

    private class ChangeNameListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            presenter.setOnChangeNameClick(getContext());
        }
    }
}
