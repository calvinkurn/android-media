package com.tokopedia.checkout.view.view.shipmentform;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import com.tokopedia.checkout.R;


/**
 * @author Aghny A. Putra on 28/02/18.
 */

public class ResetShipmentFormDialog extends DialogFragment {
    public static final String DIALOG_FRAGMENT_TAG = ResetShipmentFormDialog.class.getSimpleName();
    private ResetShipmentFormCallbackAction mCallbackAction;

    public static ResetShipmentFormDialog newInstance(ResetShipmentFormCallbackAction callbackAction) {
        Bundle args = new Bundle();

        ResetShipmentFormDialog frag = new ResetShipmentFormDialog();
        frag.setArguments(args);
        frag.setCallbackAction(callbackAction);

        return frag;
    }

    private void setCallbackAction(ResetShipmentFormCallbackAction callbackAction) {
        mCallbackAction = callbackAction;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ForegroundColorSpan fgSpanBlack70 =
                new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.black_70));
        ForegroundColorSpan fgSpanBlack54 =
                new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.black_54));

        String textTitle = getString(R.string.dialog_title_back_to_cart);
        SpannableStringBuilder ssBuilderTitle = new SpannableStringBuilder(textTitle);
        ssBuilderTitle.setSpan(fgSpanBlack70, 0, textTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        String textMessage = getString(R.string.dialog_message_back_to_cart);
        SpannableStringBuilder ssBuilderMessage = new SpannableStringBuilder(textMessage);
        ssBuilderMessage.setSpan(fgSpanBlack54, 0, textMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        final AlertDialog dialog = dialogBuilder.setTitle(ssBuilderTitle)
                .setMessage(ssBuilderMessage)
                .setPositiveButton(R.string.label_dialog_back_to_cart_button_positive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCallbackAction.onResetCartShipmentForm();
                                dismiss();
                            }
                        })
                .setNegativeButton(R.string.label_dialog_back_to_cart_button_negative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCallbackAction.onCancelResetCartShipmentForm();
                                dismiss();
                            }
                        })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_54));
                btnPositive.setAllCaps(false);

                Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnNegative.setTextColor(ContextCompat.getColor(getActivity(), R.color.medium_green));
                btnNegative.setAllCaps(false);
            }
        });

        return dialog;
    }

    public interface ResetShipmentFormCallbackAction {

        void onResetCartShipmentForm();

        void onCancelResetCartShipmentForm();

    }

}
