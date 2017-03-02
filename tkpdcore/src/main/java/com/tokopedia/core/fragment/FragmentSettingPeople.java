package com.tokopedia.core.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.core.customadapter.SimpleListTabViewAdapter;
import com.tokopedia.core.manage.ManageConstant;
import com.tokopedia.core.manage.people.address.activity.ManagePeopleAddressActivity;
import com.tokopedia.core.manage.people.bank.activity.ManagePeopleBankActivity;
import com.tokopedia.core.manage.people.notification.activity.ManageNotificationActivity;
import com.tokopedia.core.manage.people.password.activity.ManagePasswordActivity;
import com.tokopedia.core.manage.people.profile.activity.ManagePeopleProfileActivity;
import com.tokopedia.core.network.NetworkErrorHelper;

import java.util.ArrayList;

public class FragmentSettingPeople extends TkpdFragment implements ManageConstant {

    private SimpleListTabViewAdapter lvAdapter;
    private ListView lvManage;
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayList<Integer> ResID = new ArrayList<Integer>();


    public static FragmentSettingPeople newInstance() {
        return new FragmentSettingPeople();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_SETTING_MANAGE_PROFILE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_manage_general, container, false);
        Name.clear();
        ResID.clear();
        Name.add(getString(R.string.title_personal_profile));
        Name.add(getString(R.string.title_address));
        Name.add(getString(R.string.title_bank));
        Name.add(getString(R.string.title_notification));
        Name.add(getString(R.string.title_password));
        ResID.add(R.drawable.ic_set_profile);
        ResID.add(R.drawable.ic_set_address);
        ResID.add(R.drawable.ic_set_bank);
        ResID.add(R.drawable.ic_set_notifications);
        ResID.add(R.drawable.ic_menu_general_setting);
        lvManage = (ListView) mainView.findViewById(R.id.list_manage);
        lvAdapter = new SimpleListTabViewAdapter(getActivity(), Name, ResID);
        lvManage.setAdapter(lvAdapter);
        lvManage.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                Intent intent = null;
                switch (pos) {
                    case 0:
                        intent = new Intent(getActivity(), ManagePeopleProfileActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), ManagePeopleAddressActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), ManagePeopleBankActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), ManageNotificationActivity.class);
                        startActivityForResult(intent, MANAGE_NOTIFICATION);
                        break;
                /*case 4:
                    intent = new Intent(getActivity(), ManagePrivacy.class);
					startActivityForResult(intent, 1);
					GAUtility.SendEvent(getActivity(), "Cat Manage People", "Act Click Btn", "Lbl Privacy");
					break;*/
                    case 4:
                        intent = new Intent(getActivity(), ManagePasswordActivity.class);
                        startActivity(intent);
                        break;
                }

            }

        });
        return mainView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() !=null) {
            ScreenTracking.screen(getScreenName());
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.message_success_change_profile));
        }
    }
}
