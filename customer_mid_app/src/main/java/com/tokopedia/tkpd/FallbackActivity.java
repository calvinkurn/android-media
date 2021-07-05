package com.tokopedia.tkpd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.customer_mid_app.R;

/**
 * Created by ricoharisin on 11/22/16.
 */

public class FallbackActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_dialog_tokopedia_app_not_valid));
        builder.setMessage(getString(R.string.title_message_install_from_playstore));
        builder.setPositiveButton("OK", (DialogInterface dialogInterface, int i) -> {
            startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="
                            + getApplication().getPackageName()))
            );
            dialogInterface.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
