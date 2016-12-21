package com.tokopedia.core;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManagePeoplePassword extends TActivity {
    private EditText OldPassword;
    private EditText NewPassword;
    private EditText NewPasswordConfirmation;
    private TextView SaveButton;
    private TkpdProgressDialog mProgressDialog;
    private TextView ErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_manage_people_password);


        mProgressDialog = new TkpdProgressDialog(ManagePeoplePassword.this, TkpdProgressDialog.NORMAL_PROGRESS);
        OldPassword = (EditText) findViewById(R.id.old_password);
        NewPassword = (EditText) findViewById(R.id.new_password);
        NewPasswordConfirmation = (EditText) findViewById(R.id.new_password_confirmation);
        SaveButton = (TextView) findViewById(R.id.save_button);

        SaveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Validation()) {
                    SavePassword();
                }
            }
        });
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONFIG_P_PASSWORD;
    }

    private void SavePassword() {
        mProgressDialog.showDialog();
        NetworkHandler network = new NetworkHandler(ManagePeoplePassword.this, TkpdUrl.GET_PEOPLE);
        network.AddParam("act", "edit_password");
        network.AddParam("oldpassword", OldPassword.getText().toString());
        network.AddParam("newpassword", NewPassword.getText().toString());
        network.AddParam("confpassword", NewPasswordConfirmation.getText().toString());
        network.Commit(new NetworkHandlerListener() {

            @Override
            public void onSuccess(Boolean status) {
                mProgressDialog.dismiss();
            }

            @Override
            public void getResponse(JSONObject Result) {
                try {
                    if (!Result.getString("success").equals("0")) {
                        CommonUtils.UniversalToast(getBaseContext(), getString(R.string.message_success_change_pass));
                        logout();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                CommonUtils.UniversalToast(getBaseContext(), MessageError.get(0));
            }
        });
    }

    private void logout() {

        new GlobalCacheManager().deleteAll();

        SessionHandler.clearUserData(ManagePeoplePassword.this);
        NotificationModHandler notif = new NotificationModHandler(ManagePeoplePassword.this);
        notif.cancelNotif();
        SessionHandler.onLogoutListener logout = (SessionHandler.onLogoutListener) ManagePeoplePassword.this;
        if (logout != null)
            logout.onLogout(true);

    }

    private boolean Validation() {
        boolean Valid = true;
        OldPassword.setError(null);
        NewPassword.setError(null);
        NewPasswordConfirmation.setError(null);
        if (OldPassword.getText().toString().length() == 0) {
            OldPassword.setError(getString(R.string.error_field_required));
            Valid = false;
        }
        if (NewPassword.getText().toString().length() == 0) {
            NewPassword.setError(getString(R.string.error_field_required));
            Valid = false;
        }
        if (NewPasswordConfirmation.getText().toString().length() == 0) {
            NewPasswordConfirmation.setError(getString(R.string.error_field_required));
            Valid = false;
        }
        if (!NewPassword.getText().toString().equals(NewPasswordConfirmation.getText().toString())) {
            NewPasswordConfirmation.setError(getString(R.string.error_password_not_same));
            Valid = false;
        }
        return Valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
