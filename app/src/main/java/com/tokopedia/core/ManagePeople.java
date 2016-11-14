package com.tokopedia.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.customadapter.SimpleListTabViewAdapter;
import com.tokopedia.core.manage.people.notification.activity.ManageNotificationActivity;
import com.tokopedia.core.manage.people.address.activity.ManagePeopleAddressActivity;
import com.tokopedia.core.manage.people.profile.activity.ManagePeopleProfileActivity;
import com.tokopedia.core.network.NetworkErrorHelper;

import java.util.ArrayList;

public class ManagePeople extends TkpdActivity {
    private TextView ManageProfile;
    private TextView ManageAddress;
    private TextView ManageBank;
    private TextView ManageNotification;
    private TextView ManagePrivacy;


    private SimpleListTabViewAdapter lvAdapter;
    private ListView lvManage;
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayList<Integer> ResID = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_manage_shop);


        Name.add(getString(R.string.title_personal_profile));
        Name.add(getString(R.string.title_address));
        Name.add(getString(R.string.title_bank));
        Name.add(getString(R.string.title_notification));
//		Name.add(getString(R.string.title_privacy));
        Name.add(getString(R.string.title_password));
        ResID.add(R.drawable.ic_set_profile);
        ResID.add(R.drawable.ic_set_address);
        ResID.add(R.drawable.ic_set_bank);
        ResID.add(R.drawable.ic_set_notifications);
        ResID.add(R.drawable.ic_set_privacy);
        ResID.add(R.drawable.ic_menu_general_setting);
        lvManage = (ListView) findViewById(R.id.list_manage);
        lvAdapter = new SimpleListTabViewAdapter(ManagePeople.this, Name, ResID);
        lvManage.setAdapter(lvAdapter);
        lvManage.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                Intent intent = null;
                switch (pos) {
                    case 0:
                        intent = new Intent(ManagePeople.this, ManagePeopleProfileActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    case 1:
                        intent = new Intent(ManagePeople.this, ManagePeopleAddressActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(ManagePeople.this, ManagePeopleBank.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(ManagePeople.this, ManageNotificationActivity.class);
                        startActivity(intent);
                        break;
                /*case 4:
					intent = new Intent(ManagePeople.this, ManagePrivacy.class);
					startActivityForResult(intent, 1);
					GAUtility.SendEvent(ManagePeople.this, "Cat Manage People", "Act Click Btn", "Lbl Privacy");
					break;*/
                    case 4:
                        intent = new Intent(ManagePeople.this, ManagePeoplePassword.class);
                        startActivity(intent);
                        break;
                }

            }

        });
    }

    Intent datas = new Intent();
    Bundle bundles = new Bundle();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    NetworkErrorHelper.showSnackbar(this, getString(R.string.message_success_change_profile));
                    super.RefreshDrawer();
                    setResult(resultCode, datas);
                    break;
                case 1:
                    bundles.putBundle("privacy", data.getExtras());
                    datas.putExtras(bundles);
                    setResult(resultCode, datas);
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
