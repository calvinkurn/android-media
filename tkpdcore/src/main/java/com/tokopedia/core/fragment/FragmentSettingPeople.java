package com.tokopedia.core.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.core.customadapter.SimpleListTabViewAdapter;
import com.tokopedia.core.manage.ManageConstant;
import com.tokopedia.core.manage.people.address.activity.ManagePeopleAddressActivity;
import com.tokopedia.core.manage.people.notification.activity.ManageNotificationActivity;
import com.tokopedia.core.manage.people.profile.activity.ManagePeopleProfileActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.common.TransactionRouter;

import java.util.ArrayList;

public class FragmentSettingPeople extends TkpdFragment implements ManageConstant {

    private static final int REQUEST_CHANGE_PASSWORD = 123;
    private static int REQUEST_ADD_PASSWORD = 1234;

    private SimpleListTabViewAdapter lvAdapter;
    private ListView lvManage;
    private ArrayList<String> Name = new ArrayList<String>();
    private ArrayList<Integer> ResID = new ArrayList<Integer>();

    private SessionHandler sessionHandler;


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
        sessionHandler = new SessionHandler(getActivity().getApplicationContext());
        View mainView = inflater.inflate(R.layout.fragment_manage_general, container, false);
        Name.clear();
        ResID.clear();
        lvManage = (ListView) mainView.findViewById(R.id.list_manage);
        lvAdapter = new SimpleListTabViewAdapter(getActivity(), Name, ResID);
        lvManage.setAdapter(lvAdapter);
        if (GlobalConfig.isSellerApp()) {
            Name.add(getString(R.string.title_personal_profile));
            Name.add(getString(R.string.title_address));
            Name.add(getString(R.string.title_bank));
            Name.add(getString(R.string.title_payment_menu));
            Name.add(getString(R.string.title_notification));
            Name.add(getString(R.string.title_password));
            ResID.add(R.drawable.ic_set_profile);
            ResID.add(R.drawable.ic_set_address);
            ResID.add(R.drawable.ic_set_bank);
            ResID.add(R.drawable.ic_set_payment);
            ResID.add(R.drawable.ic_set_notifications);
            ResID.add(R.drawable.ic_menu_general_setting);
            lvManage.setOnItemClickListener(onSellerSettingMenuClickedListener());
        } else {
            Name.add(getString(R.string.title_personal_profile));
            Name.add(getString(R.string.title_address));
            Name.add(getString(R.string.title_bank));
            Name.add(getString(R.string.title_payment_menu));
            Name.add(getString(R.string.title_notification));
            Name.add(getString(R.string.title_password));
            ResID.add(R.drawable.ic_set_profile);
            ResID.add(R.drawable.ic_set_address);
            ResID.add(R.drawable.ic_set_bank);
            ResID.add(R.drawable.ic_set_payment);
            ResID.add(R.drawable.ic_set_notifications);
            ResID.add(R.drawable.ic_menu_general_setting);
            lvManage.setOnItemClickListener(onSettingMenuClickedListener());
        }
        return mainView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() != null) {
            ScreenTracking.screen(MainApplication.getAppContext(), getScreenName());
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.message_success_change_profile));
        } else if (requestCode == REQUEST_CHANGE_PASSWORD && resultCode == Activity.RESULT_OK) {
            com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
                    .showGreenCloseSnackbar(getActivity(), getString(R.string
                            .message_success_change_password));
        }
    }

    private OnItemClickListener onSettingMenuClickedListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), ManagePeopleProfileActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), ManagePeopleAddressActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        if (sessionHandler.isHasPassword()) {
                            intent = ((TkpdCoreRouter) MainApplication.getAppContext())
                                    .getSettingBankIntent(getActivity());
                            startActivity(intent);
                        } else {
                            showNoPasswordDialog();
                        }
                        break;
                    case 3:
                        if ((getActivity().getApplication() instanceof TransactionRouter)) {
                            ((TransactionRouter) getActivity().getApplication())
                                    .goToUserPaymentList(getActivity());
                        }
                        break;
                    case 4:
                        intent = new Intent(getActivity(), ManageNotificationActivity.class);
                        startActivityForResult(intent, MANAGE_NOTIFICATION);
                        break;
                /*case 4:
                    intent = new Intent(getActivity(), ManagePrivacy.class);
					startActivityForResult(intent, 1);
					GAUtility.SendEvent(getActivity(), "Cat Manage People", "Act Click Btn", "Lbl Privacy");
					break;*/
                    case 5:
                        if (sessionHandler.isHasPassword()) {
                            intent = RouteManager.getIntent(getActivity(), ApplinkConst.CHANGE_PASSWORD);
                            startActivityForResult(intent, REQUEST_CHANGE_PASSWORD);
                        } else {
                            intentToAddPassword();
                        }
                        break;
                }
            }
        };
    }

    private OnItemClickListener onSellerSettingMenuClickedListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), ManagePeopleProfileActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), ManagePeopleAddressActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        if (sessionHandler.isHasPassword()) {
                            intent = ((TkpdCoreRouter) MainApplication.getAppContext())
                                    .getSettingBankIntent(getActivity());
                            startActivity(intent);
                        } else {
                            showNoPasswordDialog();
                        }
                        break;
                    case 3:
                        if ((getActivity().getApplication() instanceof TransactionRouter)) {
                            ((TransactionRouter) getActivity().getApplication())
                                    .goToUserPaymentList(getActivity());
                        }
                        break;
                    case 4:
                        intent = new Intent(getActivity(), ManageNotificationActivity.class);
                        startActivityForResult(intent, MANAGE_NOTIFICATION);
                        break;
                /*case 4:
                    intent = new Intent(getActivity(), ManagePrivacy.class);
					startActivityForResult(intent, 1);
					GAUtility.SendEvent(getActivity(), "Cat Manage People", "Act Click Btn", "Lbl Privacy");
					break;*/
                    case 5:
                        if (sessionHandler.isHasPassword()) {
                            intent = RouteManager.getIntent(getActivity(), ApplinkConst.CHANGE_PASSWORD);
                            startActivityForResult(intent, REQUEST_CHANGE_PASSWORD);
                        } else {
                            intentToAddPassword();
                        }
                        break;
                }
            }
        };
    }

    private void intentToAddPassword() {
        startActivityForResult(
                ((TkpdCoreRouter) getActivity().getApplicationContext())
                        .getAddPasswordIntent(getActivity()), REQUEST_ADD_PASSWORD);
    }

    private void showNoPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.error_bank_no_password_title));
        builder.setMessage(getResources().getString(R.string.error_bank_no_password_content));
        builder.setPositiveButton(getResources().getString(R.string.error_no_password_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intentToAddPassword();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.error_no_password_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }
}
