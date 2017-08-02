package com.tokopedia.core.manage.people.bank.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.R;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class BcaOneClickDeleteDialog extends DialogFragment {

    private static final String CARD_HOLDER = "CARD_HOLDER";
    private static final String CARD_NUMBER = "CARD_NUMBER";
    private TextView cancelButton;

    private TextView deleteButton;

    private TextView cardHolderName;

    private TextView cardNumber;

    public static BcaOneClickDeleteDialog createDialog(String cardHolder, String cardNumber) {
        BcaOneClickDeleteDialog bcaOneClickDeleteDialog = new BcaOneClickDeleteDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CARD_HOLDER, cardHolder);
        bundle.putString(CARD_NUMBER, cardNumber);
        bcaOneClickDeleteDialog.setArguments(bundle);
        return bcaOneClickDeleteDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bca_one_click_delete_dialog, container, false);
        cancelButton = (TextView) view.findViewById(R.id.cancel_button);
        deleteButton = (TextView) view.findViewById(R.id.delete_button);
        cardHolderName = (TextView) view.findViewById(R.id.card_holder_name);
        cardNumber = (TextView) view.findViewById(R.id.card_number);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
