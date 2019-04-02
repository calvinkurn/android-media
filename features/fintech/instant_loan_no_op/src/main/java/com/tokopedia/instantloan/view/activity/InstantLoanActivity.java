package com.tokopedia.instantloan.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import javax.annotation.Nullable;

public class InstantLoanActivity extends Activity {

    public static Intent createIntent(Context context) {
        return new Intent(context, InstantLoanActivity.class);
    }

    public static Intent getInstantLoanCallingIntent(Context context, Bundle bundle) {
        Toast.makeText(context, "N0-OP module found (uncomment 'instant_loan_no_op' from respective .gradle files)", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, InstantLoanActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

