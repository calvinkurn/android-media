package com.tokopedia.tokocash.accountsetting.presentation.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tokocash.R;

/**
 * Created by nabillasabbaha on 11/6/17.
 */

public class DeleteTokoCashAccountDialog extends DialogFragment {

    public final static String REVOKE_TOKEN = "revoke_token";
    public final static String IDENTIFIER = "identifier";
    public final static String IDENTIFIER_TYPE = "identifier_type";

    private DeleteAccessAccountListener listener;

    public void setListener(DeleteAccessAccountListener listener) {
        this.listener = listener;
    }

    public static DeleteTokoCashAccountDialog createDialog(String revokeToken, String identifier,
                                                           String identifierType) {
        DeleteTokoCashAccountDialog cardDialog = new DeleteTokoCashAccountDialog();
        Bundle bundle = new Bundle();
        bundle.putString(REVOKE_TOKEN, revokeToken);
        bundle.putString(IDENTIFIER, identifier);
        bundle.putString(IDENTIFIER_TYPE, identifierType);
        cardDialog.setArguments(bundle);
        return cardDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_delete_tokocash_account, container, false);
        TextView detailMessage = (TextView) view.findViewById(R.id.detail_message);
        ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
        TextView cancelButton = (TextView) view.findViewById(R.id.cancel_button);
        TextView deleteButton = (TextView) view.findViewById(R.id.delete_button);

        String email = getArguments().getString(IDENTIFIER);
        detailMessage.setText(String.format(getString(R.string.tokocash_dialog_delete_access), email, email));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteAccess(getArguments().getString(REVOKE_TOKEN),
                        getArguments().getString(IDENTIFIER),
                        getArguments().getString(IDENTIFIER_TYPE));
                dismiss();
            }
        });
        return view;
    }

    public interface DeleteAccessAccountListener {
        void onDeleteAccess(String revokeToken, String identifier, String identifierType);
    }
}
