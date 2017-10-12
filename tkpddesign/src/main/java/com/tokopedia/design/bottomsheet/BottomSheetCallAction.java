package com.tokopedia.design.bottomsheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;

/**
 * @author anggaprasetiyo on 10/12/17.
 */

public class BottomSheetCallAction extends BottomSheetDialog {
    public static final String DEFAULT_LABEL_ACTION_1 = "Call";
    public static final String DEFAULT_LABEL_ACTION_2 = "Message";
    public static final String DEFAULT_LABEL_TITLE = "Hubungi";
    public static final int DEFAULT_ICON_ACTION_1 = R.drawable.ic_phone_black_36dp;
    public static final int DEFAULT_ICON_ACTION_2 = R.drawable.ic_textsms_action;

    private View btnAction1;
    private View btnAction2;
    private ImageView ivAction1;
    private ImageView ivAction2;
    private TextView tvLabel;
    private TextView tvLabelAction1;
    private TextView tvLabelAction2;

    private CallActionData callActionData;
    private ActionListener actionListener;

    public BottomSheetCallAction(@NonNull Context context) {
        super(context);
        initialView(context);
    }

    public BottomSheetCallAction(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        initialView(context);
    }

    protected BottomSheetCallAction(@NonNull Context context, boolean cancelable,
                                    OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialView(context);
    }

    public void setCallActionData(CallActionData callActionData) {
        this.callActionData = callActionData;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private BottomSheetCallAction(Builder builder, Context context) {
        super(context);
        callActionData = builder.callActionData;
        actionListener = builder.actionListener;
        initialView(context);
    }

    private void initialView(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.widget_bottom_sheet_dialog_logistic_caller, null
        );
        setContentView(view);
        btnAction1 = view.findViewById(R.id.btn_action_1);
        btnAction2 = view.findViewById(R.id.btn_action_2);

        tvLabel = (TextView) view.findViewById(R.id.tv_title);
        tvLabelAction1 = (TextView) view.findViewById(R.id.tv_label_action_1);
        tvLabelAction2 = (TextView) view.findViewById(R.id.tv_label_action_2);
        ivAction1 = (ImageView) view.findViewById(R.id.iv_icon_action_1);
        ivAction2 = (ImageView) view.findViewById(R.id.iv_icon_action_2);

        tvLabel.setText(DEFAULT_LABEL_TITLE);
        tvLabelAction1.setText(DEFAULT_LABEL_ACTION_1);
        tvLabelAction2.setText(DEFAULT_LABEL_ACTION_2);

        ivAction1.setImageResource(DEFAULT_ICON_ACTION_1);
        ivAction2.setImageResource(DEFAULT_ICON_ACTION_2);

        btnAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onPhoneCallerClicked(callActionData);
            }
        });
        btnAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onPhoneMessageClicked(callActionData);
            }
        });
    }

    public static class CallActionData {
        private String phoneNumber;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public CallActionData setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }


    }

    public interface ActionListener {
        void onPhoneCallerClicked(CallActionData callActionData);

        void onPhoneMessageClicked(CallActionData callActionData);
    }


    public static final class Builder {
        private Context context;
        private CallActionData callActionData;
        private ActionListener actionListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder callActionData(CallActionData val) {
            callActionData = val;
            return this;
        }

        public Builder actionListener(ActionListener val) {
            actionListener = val;
            return this;
        }

        public BottomSheetCallAction build() {
            return new BottomSheetCallAction(this, context);
        }
    }
}
